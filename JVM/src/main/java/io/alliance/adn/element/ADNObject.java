package main.java.io.alliance.adn.element;

import main.java.io.alliance.adn.exception.InvalidCastException;
import main.java.io.alliance.adn.exception.InvalidKeyException;
import main.java.io.alliance.adn.exception.ParsingException;
import main.java.io.alliance.adn.lexer.Lexer;
import main.java.io.alliance.adn.lexer.Token;
import main.java.io.alliance.adn.parser.Parser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class ADNObject extends HashMap<String, ADNElement> implements ADNElement {

    @NotNull
    private final String name;

    public static ADNObject parse(String input) throws ParsingException {
        val tokens = new Lexer(input).lex();
        return new Parser(tokens.toArray(new Token[0])).parse();
    }

    @Override
    public @NotNull ADNType getType() {
        return ADNType.OBJECT;
    }

    public ADNObject(@NotNull String name) {
        this.name = name;
    }

    public ADNObject(@NotNull String name, @NotNull List<ADNElement> elementList) {
        this.name = name;

        for(val element : elementList) {
            put(element.getName(), element);
        }
    }

    public ADNElement getElementByIdentifier(@NotNull String identifier) throws InvalidKeyException {
        final ADNElement element = get(identifier);

        if(element == null) {
            throw new InvalidKeyException();
        }

        return element;
    }

    @Override
    public @Nullable Object getValue() {
        return null;
    }

    public boolean putBoolean(@NotNull String name, Boolean value) {
        if(value == null) {
            return (boolean)put(name, new ADNBoolean(name, false)).getValue();
        }
        return (boolean)put(name, new ADNBoolean(name, value)).getValue();
    }

    public byte putByte(@NotNull String name, Byte value) {
        if(value == null) {
            return putInt8(name, (byte) 0x00);
        }
        return putInt8(name, value);
    }

    public char putCharacter(@NotNull String name, Character value) {
        if(value == null) {
            return putInt16(name, (char) 0x00);
        }
        return putInt16(name, value);
    }

    public int putInteger(@NotNull String name, Integer value) {
        if(value == null) {
            return putInt32(name, 0);
        }
        return putInt32(name, value);
    }

    public long putLong(@NotNull String name, Long value) {
        if(value == null) {
            return putInt64(name, 0);
        }
        return putInt64(name, value);
    }

    public float putFloat(@NotNull String name, Float value) {
        if(value == null) {
            return putFP32(name, 0);
        }
        return putFP32(name, value);
    }

    public double putDouble(@NotNull String name, Double value) {
        if(value == null) {
            return putFP64(name, 0);
        }
        return putFP64(name, value);
    }

    public byte putInt8(@NotNull String name, byte value) {
        val element = new ADNInt8(name, value);
        put(name, element);
        return element.getValue();
    }

    public char putInt16(@NotNull String name, char value) {
        val element = new ADNInt16(name, value);
        put(name, element);
        return element.getValue();
    }

    public int putInt32(@NotNull String name, int value) {
        val element = new ADNInt32(name, value);
        put(name, element);
        return element.getValue();
    }

    public long putInt64(@NotNull String name, long value) {
        val element = new ADNInt64(name, value);
        put(name, element);
        return element.getValue();
    }

    public long putUInt8(@NotNull String name, long value) {
        val element = new ADNUInt8(name, value);
        put(name, element);
        return element.getValue();
    }

    public long putUInt16(@NotNull String name, long value) {
        val element = new ADNUInt16(name, value);
        put(name, element);
        return element.getValue();
    }

    public long putUInt32(@NotNull String name, long value) {
        val element = new ADNUInt32(name, value);
        put(name, element);
        return element.getValue();
    }

    public long putUInt64(@NotNull String name, long value) {
        val element = new ADNUInt64(name, value);
        put(name, element);
        return element.getValue();
    }

    public float putFP32(@NotNull String name, float value) {
        val element = new ADNFP32(name, value);
        put(name, element);
        return element.getValue();
    }

    public double putFP64(@NotNull String name, double value) {
        val element = new ADNFP64(name, value);
        put(name, element);
        return element.getValue();
    }

    public String putString(@NotNull String name, String value) {
        val element = new ADNString(name, value);
        put(name, element);
        return element.getValue();
    }

    public ADNObject putObject(@NotNull String name, Object value) {
        if(value == null) {
            put(name, null);
            return null;
        }

        val object = new ADNObject(name);
        val fields = value.getClass().getDeclaredFields();

        for(val field : fields) {
            if(Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                val fieldName = field.getName();
                val fieldValue = field.get(value);

                if(fieldValue instanceof Boolean) {
                    object.putBoolean(fieldName, (Boolean)fieldValue);
                    continue;
                }

                if(fieldValue instanceof Byte) {
                    object.putByte(fieldName, (Byte)fieldValue);
                    continue;
                }

                if(fieldValue instanceof Character) {
                    object.putCharacter(fieldName, (Character)fieldValue);
                    continue;
                }

                if(fieldValue instanceof Integer) {
                    object.putInteger(fieldName, (Integer)fieldValue);
                    continue;
                }

                if(fieldValue instanceof Long) {
                    object.putLong(fieldName, (Long)fieldValue);
                    continue;
                }

                if(fieldValue instanceof Float) {
                    object.putFloat(fieldName, (Float)fieldValue);
                    continue;
                }

                if(fieldValue instanceof Double) {
                    object.putDouble(fieldName, (Double)fieldValue);
                    continue;
                }

                if(fieldValue instanceof String) {
                    object.putString(fieldName, (String)fieldValue);
                    continue;
                }

                object.putObject(fieldName, fieldValue);

            } catch (IllegalAccessException ignored) { }
        }

        put(name, object);
        return object;
    }

    public boolean getBoolean(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement bool = getElementByIdentifier(key);

        if(bool.typeOf(ADNType.BOOLEAN)) {
            throw new InvalidCastException();
        }

        return ((ADNBoolean)bool).getValue();
    }

    public byte getInt8(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement int8 = getElementByIdentifier(key);

        if(int8.typeOf(ADNType.INT8)) {
            throw new InvalidCastException();
        }

        return ((ADNInt8)int8).getValue();
    }

    public char getInt16(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement int16 = getElementByIdentifier(key);

        if(int16.typeOf(ADNType.INT16)) {
            throw new InvalidCastException();
        }

        return ((ADNInt16)int16).getValue();
    }

    public int getInt32(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement int32 = getElementByIdentifier(key);

        if(int32.typeOf(ADNType.INT32)) {
            throw new InvalidCastException();
        }

        return ((ADNInt32)int32).getValue();
    }

    public long getInt64(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement int64 = getElementByIdentifier(key);

        if(int64.typeOf(ADNType.INT64)) {
            throw new InvalidCastException();
        }

        return ((ADNInt64)int64).getValue();
    }

    public long getUInt8(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement uint8 = getElementByIdentifier(key);

        if(uint8.typeOf(ADNType.UINT8)) {
            throw new InvalidCastException();
        }

        return ((ADNUInt8)uint8).getValue();
    }

    public long getUInt16(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement uint16 = getElementByIdentifier(key);

        if(uint16.typeOf(ADNType.UINT16)) {
            throw new InvalidCastException();
        }

        return ((ADNUInt16)uint16).getValue();
    }

    public long getUInt32(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement uint32 = getElementByIdentifier(key);

        if(uint32.typeOf(ADNType.UINT32)) {
            throw new InvalidCastException();
        }

        return ((ADNUInt32)uint32).getValue();
    }

    public long getUInt64(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement uint64 = getElementByIdentifier(key);

        if(uint64.typeOf(ADNType.UINT64)) {
            throw new InvalidCastException();
        }

        return ((ADNUInt64)uint64).getValue();
    }

    public String getString(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement element = getElementByIdentifier(key);

        if(element.typeOf(ADNType.STRING)) {
            throw new InvalidCastException();
        }

        return ((ADNString)element).getValue();
    }

    public ADNObject getObject(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        final ADNElement element = getElementByIdentifier(key);

        if(element.typeOf(ADNType.OBJECT)) {
            throw new InvalidCastException();
        }

        return (ADNObject)element;
    }

    @Setter
    private int indent;

    public void prettyPrint() {
        System.out.println(getIndent() + name + " = [");

        val values = values();
        for(val value : values) {
            if(!(value instanceof ADNObject)) {
                val v = value.getValue();
                System.out.println(getIndent() + value.getName() + " -> " + (v == null ? "null" : v.toString()));
                continue;
            }

            val obj = (ADNObject) value;
            obj.setIndent(indent + 1);
            obj.prettyPrint();
        }

        System.out.println(getIndent() + "]");
    }


    private @NotNull String getIndent() {
        StringBuilder s = new StringBuilder();
        for(int i = indent; i > 0; i--) {
            s.append("\t");
        }
        return s.toString();
    }

    public void saveTo(String path) throws IOException {
        File file = new File(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(textify(new StringBuilder(), 0));
        writer.flush();
        writer.close();
    }

    @Override
    public String textify(StringBuilder builder, int indent) {
        builder.append(getIndent(indent)).append("object ").append(name).append(" = {").append('\n');

        for (ADNElement element : values()) {
            if(element == null) {
                builder.append(getIndent(indent + 1)).append("object ").append(name).append(" = null;\n");
                continue;
            }

            element.textify(builder, indent + 1);
        }

        builder.append(getIndent(indent)).append("}\n");
        return builder.toString();
    }
}