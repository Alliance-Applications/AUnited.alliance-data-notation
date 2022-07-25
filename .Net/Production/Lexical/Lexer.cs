using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;
using io.Alliance.ADN.Semantic;

namespace io.alliance.adn.lexical; 

internal sealed class Lexer {
   private readonly DiagnosticStack _diagnostics;
    private readonly SourceText _input;

    private int _index;

    private SyntaxNodeKind _nodeKind;
    private int _start;
    private string _text;
    private object? _value;

    internal Lexer(SourceText text) {
        _diagnostics = new DiagnosticStack();
        _input = text;
        _text = "";
    }

    private char Current => Peek(0);
    private char Next => Peek(1);
    private char NextNext => Peek(2);

    internal static IEnumerable<SyntaxToken> LexAll(SourceText source, out IEnumerable<Diagnostic> diagnostics) {
        var lexer = new Lexer(source);
        var tokens = new List<SyntaxToken>();

        while (true) {
            var token = lexer.Lex();
            tokens.Add(token);

            if (token.Kind == SyntaxNodeKind.TOKEN_EOF) {
                break;
            }
        }

        diagnostics = lexer._diagnostics;
        return tokens;
    }

    private char Peek(int offset) {
        var index = _index + offset;
        return index >= _input.Length ? '\0' : _input[index];
    }

    internal Token Lex() {
        _start = _index;
        _nodeKind = SyntaxNodeKind.TOKEN_BAD;
        _value = null;

        switch (Current) {
            case '{':
                _nodeKind = TokenType.TOKEN_BRACE_OPEN;
                _index++;
                break;
            case '}':
                _nodeKind = TokenType.TOKEN_BRACE_CLOSE;
                _index++;
                break;
            case ',':
                _nodeKind = TokenType.TOKEN_COMMA;
                _index++;
                break;
            case ';':
                _nodeKind = TokenType.TOKEN_SEMICOLON;
                _index++;
                break;
            case '=': {
                _nodeKind = TokenType.TOKEN_EQUAL;
                _index++;
                break;
            }
            case '\"':
                ReadStringToken();
                break;
            case '0' or '1' or '2' or '3' or '4' or '5' or '6' or '7' or '8' or '9':
                ReadNumberToken();
                break;
            default: {
                if (char.IsLetter(Current)) {
                    ReadLexicalToken();
                }
                else if (char.IsWhiteSpace(Current)) {
                    ReadEmptyToken();
                }
                else
                {
                    throw new Exception($"Illegal token '{Current}'");
                }

                break;
            }
        }

        _text = Syntax.GetText(_nodeKind) ?? _input[_start.._index];

        return new SyntaxToken(_nodeKind, _start, _text, _value);
    }

    private void ReadNumberToken() {
        var period = false;
        while (char.IsDigit(Current) || Current == '.') {
            if (Current == '.') {
                period = true;
            }

            _index++;
        }

        var text = _input[_start.._index];

        if (!period) {
            if (!int.TryParse(text, NumberStyles.Any, CultureInfo.InvariantCulture, out var value)) {
                _diagnostics.ReportInvalidNumber(new TextSpan(_start, _index - _start), text, typeof(int));
            }

            _nodeKind = TokenType.LITERAL_NUMBER;
            _value = value;
        }
        else {
            if (!double.TryParse(text, NumberStyles.Any, CultureInfo.InvariantCulture, out var value)) {
                _diagnostics.ReportInvalidNumber(new TextSpan(_start, _index - _start), text, typeof(double));
            }

            _nodeKind = TokenType.LITERAL_FLOAT;
            _value = value;
        }
    }

    private void ReadEmptyToken() {
        while (char.IsWhiteSpace(Current)) {
            _index++;
        }

        _nodeKind = SyntaxNodeKind.TOKEN_EMPTY;
    }

    private void ReadStringToken() {
        var builder = new StringBuilder();

        while(true) {
            _index++;

            switch(Current) {
                case '\\':
                    _index++;

                    switch(Current) {
                        case '\\':
                        case '\"':
                        case '\'':
                            builder.Append(Current);
                            break;
                        case 'r':
                            builder.Append('\r');
                            break;
                        case 'n':
                            builder.Append('\n');
                            break;
                        case 't':
                            builder.Append('\t');
                            break;
                        default:
                            throw new Exception("Illegal escape sequence!");
                    }
                    break;
                case '\0':
                    _diagnostics.ReportUnexpectedToken(new SyntaxToken(SyntaxNodeKind.TOKEN_EOF, _index, "\0", null), SyntaxNodeKind.TOKEN_SEMICOLON);
                    _nodeKind = SyntaxNodeKind.TOKEN_STRING;
                    _value = _input[(_start + 1).._index];
                    return;
                case '\"':
                    _nodeKind = SyntaxNodeKind.TOKEN_STRING;
                    _value = _input[(_start + 1).._index];
                    _index++;
                    return;
                default:
                    builder.Append(Current);
                    break;
            }
        }
    }

    private void ReadLexicalToken() {
        while (char.IsLetter(Current)) {
            _index++;
        }

        var text = _input[_start.._index];
        _nodeKind = Syntax.GetKeyword(text);
    }
}