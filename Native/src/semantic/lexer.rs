use std::cell::Cell;

use crate::semantic::TokenKind;
use crate::semantic::TokenKind::{Identifier, KeywordBool, KeywordF32, KeywordF64, KeywordI16, KeywordI32, KeywordI64, KeywordI8, KeywordStr, KeywordStruct, LiteralBoolean, LiteralFloat, LiteralNumber, LiteralString, MiscBadToken, TokenBraceClose, TokenBraceOpen, TokenBracketClose, TokenBracketOpen, TokenColon, TokenComma, TokenSemicolon, TokenSet};
use crate::Walkable;

pub(crate) struct Lexer {
    list: Vec<char>,
    index: Cell<usize>,
}

impl Lexer {
    pub(crate) fn new(file: String) -> Lexer {
        Lexer {
            list: file.chars().collect(),
            index: Cell::new(0),
        }
    }

    pub(crate) fn lex_all(&self) -> Vec<TokenKind> {
        let mut tokens: Vec<TokenKind> = Vec::new();
        let max_index = self.list.len();

        while self.index.get() < max_index {
            tokens.push(match self.consume() {
                '{' => TokenBraceOpen,
                '[' => TokenBracketOpen,
                ']' => TokenBracketClose,
                '}' => TokenBraceClose,
                '=' => TokenSet,
                ',' => TokenComma,
                ';' => TokenSemicolon,
                ':' => TokenColon,
                '.' => self.lex_numeric(true, false),
                '+' => self.lex_numeric(false, false),
                '-' => self.lex_numeric(false, true),
                '"' => self.lex_string(),
                first => self.lex_textual(*first)
            })
        }

        tokens
    }

    fn lex_numeric(&self, dot: bool, neg: bool) -> TokenKind {
        let mut num: Vec<char> = Vec::new();

        if neg {
            num.push('-');
        }

        let mut dot = dot;
        let mut bad = false;

        loop {
            let cur = self.consume();

            if !cur.is_numeric() && cur != &'.' {
                break;
            }

            if cur == &'.' {
                if dot {
                    bad = true;
                }

                dot = true;
            }

            num.push(*cur);
        }

        let string = String::from_iter(num.iter());

        if bad {
            return MiscBadToken(string);
        }

        if dot {
            return LiteralFloat(string.parse::<f64>().expect("Error parsing float!"));
        }

        LiteralNumber(string.parse::<i64>().expect("Error parsing integer!"))
    }

    /// - Opening and closing quotation marks are not included in the string.
    /// - Tab, carriage return, line feed, quotation mark and backslash may be escaped (with a backslash).
    /// - A backslash that does not escape one of the five characters will be ignored and NOT included in the string.
    fn lex_string(&self) -> TokenKind {
        let mut text: Vec<char> = Vec::new();

        loop {
            let current = self.consume();

            if current == &'\"' {
                break;
            }

            if current == &'\\' {
                match self.consume() {
                    '"' => text.push('"'),
                    't' => text.push('\t'),
                    'r' => text.push('\r'),
                    'n' => text.push('\n'),
                    '\\' => text.push('\\'),
                    char => text.push(*char)
                }
            }

            text.push(*current);
        }

        return LiteralString(String::from_iter(text.iter()));
    }

    fn lex_textual(&self, first: char) -> TokenKind {
        if !first.is_alphabetic() {
            return MiscBadToken(String::from(first));
        }

        let mut text: Vec<char> = vec![first];

        loop {
            let current = self.current();

            if !current.is_alphanumeric() && current != &'_' {
                break;
            }

            text.push(*current);
            self.skip();
        }

        let text = String::from_iter(text.iter());

        match &*text {
            "struct" => KeywordStruct,
            "bool" => KeywordBool,
            "i8" => KeywordI8,
            "i16" => KeywordI16,
            "i32" => KeywordI32,
            "i64" => KeywordI64,
            "f32" => KeywordF32,
            "f64" => KeywordF64,
            "str" => KeywordStr,

            "true" => LiteralBoolean(true),
            "false" => LiteralBoolean(false),

            _ => Identifier(text),
        }
    }
}

impl Walkable<char> for Lexer {
    #[inline]
    fn size(&self) -> usize {
        self.list.len()
    }

    #[inline]
    fn peek(&self, offset: usize) -> &char {
        self.list.get(self.index.get() + offset).expect("Index out of bounds!")
    }

    #[inline]
    fn skip(&self) {
        self.index.set(self.index.get() + 1);
    }
}