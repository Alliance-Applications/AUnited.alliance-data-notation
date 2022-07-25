using System;
using System.Collections.Generic;
using System.Linq;
using io.Alliance.ADN.API;
using io.Alliance.ADN.Data;
using io.Alliance.ADN.Semantic;

namespace io.Alliance.ADN.Lexical; 

internal sealed class Parser {
    private Token[] _input;
    private int _index;

    private Token Current => _input[_index];

    private TokenType Type
    {
        get
        {
            var type = Consume.Type;
            
            if (type is 
                TokenType.KEYWORD_BOOL or 
                TokenType.KEYWORD_I8 or 
                TokenType.KEYWORD_I16 or 
                TokenType.KEYWORD_I32 or 
                TokenType.KEYWORD_I64 or 
                TokenType.KEYWORD_F32 or 
                TokenType.KEYWORD_F64 or 
                TokenType.KEYWORD_STR)
            {
                return type;
            }

            throw new InvalidTokenException($"Invalid token {Current.Type}, expected literal!");
        }
    }
    
    private Token Consume { 
        get {
            var token = Current;
            _index++;
            return token;
        }
    }

    private Token Match(TokenType type) {
        if (Current.Type != type)
        {
            throw new InvalidTokenException($"Invalid token {Current.Type}, expected {type}!");
        }

        return Consume;
    }

    private TokenLiteral MatchLiteral() {
        if (Current.IsLiteral)
        {
            return (TokenLiteral)Consume;
        }

        throw new InvalidTokenException($"Invalid token {Current.Type}, expected literal!");

    }

    private Parser(Token[] input) {
        _input = input;
    }

    internal static Node Parse(IEnumerable<Token> input) {
        return new Parser(input.ToArray()).ParseRoot();
    }

    private Node ParseRoot() {
        var root = Struc.New("root");
        var length = _input.Length;

        while (_index < length)
        {
            var node = ParseNode();
            root.Insert(node.Name, node);
        }

        return root;
    }

    private Node ParseNode() {
        return Current.Type switch
        {
            TokenType.KEYWORD_STRUCT => ParseStruct(),
            TokenType.KEYWORD_ARR => ParseArray(),
            _ => ParseLiteral()
        };
    }

    private Node ParseStruct() {
        Match(TokenType.KEYWORD_STRUCT);
        var name = ((TokenIdentifier)Match(TokenType.TOKEN_IDENTIFIER)).Name;
        Match(TokenType.TOKEN_BRACE_OPEN);
        var struc = Struc.New(name);
        
        while (Current.Type != TokenType.TOKEN_BRACE_CLOSE)
        {
            var node = ParseNode();
            struc.Insert(node.Name, node);
        }

        Match(TokenType.TOKEN_BRACKET_CLOSE);
        return struc;
    }

    private Node ParseArray() {
        Match(TokenType.KEYWORD_ARR);
        var type = Type;
        var name = ((TokenIdentifier)Match(TokenType.TOKEN_IDENTIFIER)).Name;
        Match(TokenType.TOKEN_EQUAL);
        Match(TokenType.TOKEN_BRACKET_OPEN);
        var values = ParseValues(name, type);
        Match(TokenType.TOKEN_BRACKET_CLOSE);
        return Arr.New(name, DataTypeMapper.FromTokenType(type), values.ToArray());
    }

    private IEnumerable<Literal> ParseValues(string name, TokenType type) {
        var method = GetMethod(type);
        var index = 0;
        
        yield return method.Invoke($"{name}_{index}", (TokenLiteral)Consume);
        index++;
        
        while(Current.Type is TokenType.TOKEN_COMMA)
        {
            var _ = Consume; // Dispose of comma token
            yield return method.Invoke($"{name}_{index}", (TokenLiteral)Consume);
            index++;
        }
    }

    private Node ParseLiteral() {
        var type = Type;
        var name = ((TokenIdentifier)Match(TokenType.TOKEN_IDENTIFIER)).Name;
        Match(TokenType.TOKEN_EQUAL);
        var node = GetMethod(type).Invoke(name, MatchLiteral());
        Match(TokenType.TOKEN_SEMICOLON);
        return node;
    }

    private Func<string, TokenLiteral, Literal> GetMethod(TokenType type) {
        return type switch
        {
            TokenType.KEYWORD_BOOL => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_TRUE or TokenType.LITERAL_FALSE)
                {
                    return Literal.New(name, DataType.@bool, literal.Value);
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected 'true' or 'false'!");
            },
            TokenType.KEYWORD_I8 => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_NUMBER)
                {
                    return Literal.New(name, DataType.i8, (byte)(literal.Value & 0xFF));
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected 8-bit signed integer value!");
            },
            TokenType.KEYWORD_I16 => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_NUMBER)
                {
                    return Literal.New(name, DataType.i16, (short)(literal.Value & 0xFFFF));
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected 16-bit signed integer value!");
            },
            TokenType.KEYWORD_I32 => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_NUMBER)
                {
                    return Literal.New(name, DataType.i32, (int)(literal.Value & 0xFFFFFFFF));
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected 32-bit signed integer value!");
            },
            TokenType.KEYWORD_I64 => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_NUMBER)
                {
                    return Literal.New(name, DataType.i64, (long)(literal.Value & 0xFFFFFFFFFFFFFFFF));
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected 64-bit signed integer value!");
            },
            TokenType.KEYWORD_F32 => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_FLOAT)
                {
                    return Literal.New(name, DataType.f32, (float)literal.Value);
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected single precision floating point value!");
            },
            TokenType.KEYWORD_F64 => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_FLOAT)
                {
                    return Literal.New(name, DataType.f64, (double)literal.Value);
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected double precision floating point value!");
            },
            TokenType.KEYWORD_STR => (name, literal) =>
            {
                if (literal.Type is TokenType.LITERAL_NUMBER)
                {
                    return Literal.New(name, DataType.str, (string)literal.Value);
                }

                throw new InvalidTokenException($"Invalid token {literal.Type}, expected string value!");
            },
            _ => throw new InvalidTokenException($"Invalid token {type}, expected primitive type keyword!")
        };
    }
}