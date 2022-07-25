namespace io.Alliance.ADN.API; 

public class InvalidReadException : Exception {
    public InvalidReadException() {
        
    }
    
    public InvalidReadException(string error) : base(error) {
        
    }
}