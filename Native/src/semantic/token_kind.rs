pub(crate) enum TokenKind {
    MiscEndOfFile,
    MiscBadToken(String),

    Identifier(String),

    KeywordStruct,
    KeywordArray,
    KeywordBool,
    KeywordI8,
    KeywordI16,
    KeywordI32,
    KeywordI64,
    KeywordF32,
    KeywordF64,
    KeywordStr,

    TokenBraceOpen,
    TokenBraceClose,
    TokenBracketOpen,
    TokenBracketClose,
    TokenSet,
    TokenComma,
    TokenColon,
    TokenSemicolon,

    LiteralBoolean(bool),
    LiteralNumber(i64),
    LiteralFloat(f64),
    LiteralString(String),
}