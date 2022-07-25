using System.Text;
using System.Collections.Generic;
using System.Data;
using System.Linq;

namespace io.Alliance.ADN.Data;

internal sealed record Struc(string Name, Dictionary<string, Node> Children) : Node(Name) {
    internal static Struc New(string name) {
        return new Struc(name, new Dictionary<string, Node>());
    }
    
    internal override bool GetChild(string name, out Node node) {
        if (Children.TryGetValue(name, out node)) {
            return true;
        }

        node = Empty;
        return false;
    }

    protected internal override StringBuilder AppendDataString(StringBuilder builder) {
        builder.Append($"struc {Name}{{");

        foreach(var child in Children.Values) {
            child.AppendDataString(builder);
        }

        return builder.Append('}');
    }

    protected internal override StringBuilder AppendPrettyString(StringBuilder builder) {
        builder.AppendLine($"struc {Name} {{");

        foreach(var child in Children.Values) {
            child.AppendDataString(builder);
        }

        return builder.AppendLine("}");
    }

    protected internal override IEnumerable<Node> Collect(string parent) {
        return Children.Values.SelectMany(child => child.Collect($"{parent}.{Name}"));
    }

    public void Insert(string name, Node node) {
        if (!Children.TryAdd(name, node))
        {
            throw new DuplicateNameException();
        }
    }
}