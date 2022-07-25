namespace io.Alliance.ADN.Semantic; 

internal abstract record Token(TokenType Type) {
    internal abstract bool IsLiteral { get; }
    internal virtual bool IsIdentifier => false;
}