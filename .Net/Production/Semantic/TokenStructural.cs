namespace io.Alliance.ADN.Semantic; 

internal sealed record TokenStructural(TokenType Type) : Token(Type) {
    internal override bool IsLiteral => false;
}