use std::cell::Cell;
use std::collections::HashMap;
use std::fmt::Error;

use crate::data::{Datapoint, Dataset};
use crate::semantic::TokenKind;
use crate::semantic::TokenKind::*;
use crate::Walkable;

macro_rules! match_token {
    ($self:ident $(,)? $token:pat) => {
        match $self.current() {
            $token => (),
            _ => return Err(Error::default()),
        }
    };
}

pub(crate) struct Parser {
    tokens: Vec<TokenKind>,
    index: Cell<usize>,
}

impl Parser {
    pub(crate) fn parse(tokens: Vec<TokenKind>) -> Result<Dataset, Error> {
        let parser = Parser {
            tokens,
            index: Cell::new(0),
        };

        let mut root = Dataset::new();

        while !matches!(parser.current(), MiscEndOfFile) {
            let (name, data) = parser.parse_element()?;
            root.add(name, data);
        }

        Ok(root)
    }

    fn parse_element(&self) -> Result<(String, Datapoint), Error> {
        // Sort out structs first

        // struct MyStruct {
        if matches!(self.current(), KeywordStruct) {
            self.skip();
            return match self.consume() {
                Identifier(name) => Ok((name.clone(), self.parse_struct()?)),
                _ => Err(Error::default()),
            }
        }

        let identifier = match self.consume() {
            Identifier(name) => name.clone(),
            _ => return Err(Error::default()),
        };

        // MyStruct {
        if matches!(self.current(), TokenBraceOpen) {
            return Ok((identifier, self.parse_struct()?));
        }

        // Structs are sorted out, now let's deal with types

        // myNumber: u32 = 0
        // MyArray: u32[][] [ [
        if matches!(self.current(), TokenColon) {
            self.skip();

            let cur = self.current();
            let _type = match cur {
                KeywordI8 | KeywordI16 | KeywordI32 | KeywordI64 |
                KeywordBool | KeywordF32 | KeywordF64 | KeywordStr => cur,
                _ => return Err(Error::default())
            };

            let mut depth: usize = 0;
            while matches!(self.current(), KeywordArray) {
                self.skip();
                depth += 1;
            }

            return match depth {
                0 => Ok((identifier, self.parse_literal(Some(_type))?)),
                _ => Ok((identifier, self.parse_array(Some(_type), depth as i32)?))
            }
        }

        // Type annotations have been dealt with, no parse inferred values

        // myNumber 0
        // MyArray [ [
        let mut depth: usize = 0;
        while matches!(self.peek(depth), TokenBracketOpen) {
            depth += 1;
        }

        return match depth {
            0 => Ok((identifier, self.parse_literal(None)?)),
            _ => Ok((identifier, self.parse_array(None, depth as i32)?))
        }
    }

    fn parse_struct(&self) -> Result<Datapoint, Error> {
        match_token!(self, TokenBraceOpen);

        let mut map: HashMap<String, Datapoint> = HashMap::new();
        while !matches!(self.current(), TokenBraceClose) {
            let (name, data) = self.parse_element()?;
            map.insert(name, data);
        }

        match_token!(self, TokenBraceClose);
        Ok(Datapoint::Struct(map))
    }

    fn parse_array(&self, expected_type: Option<&TokenKind>, depth: i32) -> Result<Datapoint, Error> {
        let (actual_type, elements): (&TokenKind, _) = self.parse_array_elements();

        if let Some(expected) = actual_type.castable_from(expected_type) {
            self.map_array(elements, expected, -(1 - depth), 0)
        } else {
            Err(Error::default())
        }
    }

    fn parse_literal(&self, expected_type: Option<&TokenKind>) -> Result<Datapoint, Error> {
        todo!()
    }
}

impl Walkable<TokenKind> for Parser {
    #[inline]
    fn size(&self) -> usize {
        self.tokens.len()
    }

    #[inline]
    fn peek(&self, offset: usize) -> &TokenKind {
        self.tokens.get(self.index.get() + offset).expect("Index out of bounds!")
    }

    #[inline]
    fn skip(&self) {
        self.index.set(self.index.get() + 1);
    }
}