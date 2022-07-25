using io.Alliance.ADN.Data;

using System.IO;

namespace io.Alliance.ADN.API.Data; 

public class Dataset {
    private Node Root { get; }

    private Dataset(Node root) {
        Root = root;
    }

    public static Dataset Import(string path) {
        throw new NotImplementedException();
    }

    public static Dataset Import(File file) {
        throw new NotImplementedException();
    }

    public static Dataset Export(string path) {
        throw new NotImplementedException();
    }

    public static Dataset Export(File file) {
        throw new NotImplementedException();
    }

    public bool TryFindDatapoint(out Datapoint datapoint, params string[] names) {
        var node = Root;

        if (names.Any(name => !node.GetChild(name, out node)))
        {
            datapoint = Datapoint.Empty;
            return false;
        }

        datapoint = Datapoint.Of(node);
        return true;
    }
}