namespace io.Alliance.ADN.API;

public class InvalidTypeException : Exception {
    public InvalidTypeException() {
        
    }
    
    public InvalidTypeException(string message) : base(message) {
        
    }
}