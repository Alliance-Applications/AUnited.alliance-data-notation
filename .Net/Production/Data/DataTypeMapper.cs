using System;
using io.Alliance.ADN.Semantic;

namespace io.Alliance.ADN.Data;

internal static class DataTypeMapper {
    internal static DataType? FromString(string type) {
        return type switch
        {
            "bool" => DataType.@bool,
            "i8" => DataType.i8,
            "i16" => DataType.i16,
            "i32" => DataType.i32,
            "i64" => DataType.i64,
            "f32" => DataType.f32,
            "f64" => DataType.f64,
            "str" => DataType.str,
            _ => null
        };
    }
    
    internal static string AsString(DataType type) {
        return type switch
        {
            DataType.@bool => "bool",
            DataType.i8 => "i8",
            DataType.i16 => "i16",
            DataType.i32 => "i32",
            DataType.i64 => "i64",
            DataType.f32 => "f32",
            DataType.f64 => "f64",
            DataType.str => "str"
        };
    }

    public static DataType FromTokenType(TokenType type) {
        return type switch
        {
            TokenType.KEYWORD_BOOL => DataType.@bool,
            TokenType.KEYWORD_I8 => DataType.i8,
            TokenType.KEYWORD_I16 => DataType.i16,
            TokenType.KEYWORD_I32 => DataType.i32,
            TokenType.KEYWORD_I64 => DataType.i64,
            TokenType.KEYWORD_F32 => DataType.f32,
            TokenType.KEYWORD_F64 => DataType.f64,
            TokenType.KEYWORD_STR => DataType.str,
            _ => throw new Exception("WTF")
        };
    }
}