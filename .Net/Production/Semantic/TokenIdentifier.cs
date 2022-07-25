namespace io.Alliance.ADN.Semantic;

internal sealed record TokenIdentifier(string Name) : Token(TokenType.TOKEN_IDENTIFIER) {
    internal override bool IsLiteral => false;
    internal override bool IsIdentifier => true;
}