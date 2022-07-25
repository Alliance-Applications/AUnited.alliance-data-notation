using io.Alliance.ADN.Data;

namespace io.Alliance.ADN.API.Data; 

public class Datapoint {
    public static Datapoint Empty { get; } = new();
    
    private bool Valid { get; set; }
    private Node Node { get; }

    private void Validate() {
        if (!Valid)
        {
            throw new InvalidReadException("Attempting to read from invalid datapoint ");
        }
    }

    private Datapoint() {
        Valid = false;
        Node = Node.Empty;
    }

    private Datapoint(Node node) {
        Valid = true;
        Node = node;
    }

    internal static Datapoint Of(Node node) {
        return new Datapoint(node);
    }

    public Datapoint Rename(string name) {
        Node.Rename(name);
        return this;
    }

    public dynamic Read() {
        
    }
}