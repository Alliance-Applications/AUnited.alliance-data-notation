ng System;
using System.Collections.Generic;
using System.Text;

namespace io.Alliance.ADN.Data;

internal abstract record Node(string Name) {
    internal static readonly Node Empty = new Arr("", DataType.str, Array.Empty<Literal>());
    
    protected internal string Name { get; private set; } = Name;

    internal IEnumerable<Node> ChildNodes => Collect(Name);

    internal string DataString => AppendDataString(new StringBuilder()).ToString();

    internal string PrettyString => AppendPrettyString(new StringBuilder()).ToString();
    
    internal abstract bool GetChild(string name, out Node node);

    protected internal abstract StringBuilder AppendDataString(StringBuilder builder);

    protected internal abstract StringBuilder AppendPrettyString(StringBuilder builder);

    protected internal abstract IEnumerable<Node> Collect(string parent);

    internal void Rename(string name) {
        Name = name;
    }
}