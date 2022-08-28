use std::cell::Cell;

use crate::data::Dataset;
use crate::semantic::TokenKind;
use crate::Walkable;

pub(crate) struct Parser {
    tokens: Vec<TokenKind>,
    index: Cell<usize>,
}

impl Parser {
    pub(crate) fn new(tokens: Vec<TokenKind>) -> Parser {
        Parser { tokens, index: Cell::new(0) }
    }

    pub(crate) fn parse_all(&self) -> Dataset {
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