namespace io.Alliance.ADN.Semantic;

internal sealed record TokenLiteral(TokenType Type, dynamic Value) : Token(Type) {
    internal override bool IsLiteral => true;
}