using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace io.Alliance.ADN.Data;

internal sealed record Literal(string Name, DataType Type, dynamic Value) : Node(Name) {
    private DataType Type { get; } = Type;
    private dynamic Value { get; set; } = Value;

    internal override bool GetChild(string name, out Node node) {
        node = Empty;
        return false;
    }

    protected internal override StringBuilder AppendDataString(StringBuilder builder) {
        return builder.Append($"{DataTypeMapper.AsString(Type)} {Value.ToString()};");
    }

    protected internal override StringBuilder AppendPrettyString(StringBuilder builder) {
        return builder.AppendLine($"{DataTypeMapper.AsString(Type)} {Value.ToString()};");
    }

    protected internal override IEnumerable<Node> Collect(string parent) {
        return Enumerable.Empty<Node>();
    }

    internal dynamic GetValue() {
        return Type switch {
            DataType.@bool => (bool)Value,
            DataType.i8 => (byte)Value,
            DataType.i16 => (short)Value,
            DataType.i32 => (int)Value,
            DataType.i64 => (long)Value,
            DataType.f32 => (float)Value,
            DataType.f64 => (double)Value,
            DataType.str => (string)Value
        };
    }

    internal dynamic SetValue(bool value) {
        var old = GetValue();
        if (Type != DataType.@bool) {
            throw new ArgumentException($"The new value (boolean) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(byte value) {
        var old = GetValue();
        if (Type != DataType.i8) {
            throw new ArgumentException($"The new value (byte) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(short value) {
        var old = GetValue();
        if (Type != DataType.i16) {
            throw new ArgumentException($"The new value (short) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(int value) {
        var old = GetValue();
        if (Type != DataType.i32) {
            throw new ArgumentException($"The new value (int) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(long value) {
        var old = GetValue();
        if (Type != DataType.i64) {
            throw new ArgumentException($"The new value (long) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(float value) {
        var old = GetValue();
        if (Type != DataType.f32) {
            throw new ArgumentException($"The new value (float) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(double value) {
        var old = GetValue();
        if (Type != DataType.f64) {
            throw new ArgumentException($"The new value (double) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal dynamic SetValue(string value) {
        var old = GetValue();
        if (Type != DataType.str) {
            throw new ArgumentException($"The new value (string) is not of the required type ({Type.ToString()})!");
        }
        
        Value = value;
        return old;
    }

    internal static Literal New(string name, DataType type, dynamic value) {
        return new Literal(name, type, value);
    }
}