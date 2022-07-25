using System;

namespace io.Alliance.ADN.API; 

public class InvalidTokenException : Exception {
    public InvalidTokenException(string message) : base(message) {
        
    }
}