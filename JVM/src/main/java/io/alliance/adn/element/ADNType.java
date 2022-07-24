package main.java.io.alliance.adn.element;

public enum ADNType {
    BOOLEAN("boolean"),
    INT8("int8"),
    INT16("int16"),
    INT32("int32"),
    INT64("int64"),
    UINT8("uint8"),
    UINT16("uint16"),
    UINT32("uint32"),
    UINT64("uint64"),
    FP32("fp32"),
    FP64("fp64"),
    STRING("string"),
    OBJECT("object");


    private final String typeString;

    ADNType(String typeString) {

        this.typeString = typeString;
    }

    @Override
    public String toString() {
        return typeString;
    }
}