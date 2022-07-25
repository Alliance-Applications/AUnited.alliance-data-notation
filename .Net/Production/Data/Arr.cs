using System.Collections.Generic;
using System.Text;
using io.Alliance.ADN.Semantic;

namespace io.Alliance.ADN.Data;

internal sealed record Arr(string Name, DataType Type, Literal[] Values) : Node(Name) {
    internal override bool GetChild(string name, out Node node) {
        foreach (var value in Values)
        {
            if (!string.Equals(value.Name, name))
            {
                continue;
            }

            node = value;
            return true;
        }

        node = Empty;
        return false;
    }

    protected internal override StringBuilder AppendDataString(StringBuilder builder) {
        builder.Append($"struc {Name}{{");

        foreach(var value in Values) {
            value.AppendDataString(builder);
        }

        return builder.Append('}');
    }

    protected internal override StringBuilder AppendPrettyString(StringBuilder builder) {
        builder.AppendLine($"struc {Name} {{");

        foreach(var value in Values) {
            value.AppendDataString(builder);
        }

        return builder.AppendLine("}");
    }

    protected internal override IEnumerable<Node> Collect(string parent) {
        var self = $"{parent}.{Name}";

        foreach(var value in Values) {
            foreach (var node in value.Collect(self)) {
                yield return node;
            }
        }
    }

    public static Node New(string name, DataType type, Literal[] values) {
        return new Arr(name, type, values);
    }
}