package io.alliance.adn.element;

import io.alliance.adn.exception.InvalidCastException;
import io.alliance.adn.exception.InvalidKeyException;
import io.alliance.adn.util.Caster;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class ADNObject extends HashMap<String, ADNElement> implements ADNElement {

    @NotNull
    private final String name;
    @Setter
    private int indent;

    public ADNObject(@NotNull String name) {
        this.name = name;
    }

    public ADNObject(@NotNull String name, @NotNull List<ADNElement> elementList) {
        this.name = name;

        for (val element : elementList) {
            put(element.getName(), element);
        }
    }

    @Override
    @Nullable
    public ADNElement put(@NotNull String key, @NotNull ADNElement value) {
        return super.put(key, value);
    }

    @Override
    public @NotNull ADNType getType() {
        return ADNType.OBJECT;
    }

    @Override
    public @Nullable Object getValue() {
        return null;
    }
    public ADNElement getElementByIdentifier(@NotNull String identifier) throws InvalidKeyException {
        final ADNElement element = get(identifier);

        if (element == null) {
            throw new InvalidKeyException();
        }

        return element;
    }

    public ADNElement get(@NotNull String key) throws InvalidKeyException {
        val element = super.get(key);

        if (element == null) {
            throw new InvalidKeyException();
        }

        return element;
    }

    @Nullable
    public Boolean putBoolean(@NotNull String key, boolean value) {
        return (Boolean) ADNElement.valueOf(put(key, new ADNBoolean(key, value)));
    }

    @Nullable
    public Byte putInt8(@NotNull String key, byte value) {
        return (Byte) ADNElement.valueOf(put(key, new ADNInt8(key, value)));
    }

    @Nullable
    public Short putInt16(@NotNull String key, short value) {
        return (Short) ADNElement.valueOf(put(key, new ADNInt16(key, value)));
    }

    @Nullable
    public Integer putInt32(@NotNull String key, int value) {
        return (Integer) ADNElement.valueOf(put(key, new ADNInt32(key, value)));
    }

    @Nullable
    public Long putInt64(@NotNull String key, long value) {
        return (Long) ADNElement.valueOf(put(key, new ADNInt64(key, value)));
    }

    @Nullable
    public Float putFloat32(@NotNull String key, float value) {
        return (Float) ADNElement.valueOf(put(key, new ADNFP32(key, value)));
    }

    @Nullable
    public Double putFloat64(@NotNull String key, double value) {
        return (Double) ADNElement.valueOf(put(key, new ADNFP64(key, value)));
    }

    @Nullable
    public String putString(@NotNull String key, @NotNull String value) {
        return (String) ADNElement.valueOf(put(key, new ADNString(key, value)));
    }

    @Nullable
    public Object putObject(@NotNull String key, @NotNull List<ADNElement> value) {
        return ADNElement.valueOf(put(key, new ADNObject(key, value)));
    }

    @Nullable
    public Object putObject(@NotNull String key, @NotNull ADNElement[] value) {
        return ADNElement.valueOf(put(key, new ADNObject(key, List.of(value))));
    }

    public boolean getBoolean(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findBoolean(key).getValue();
    }

    @NotNull
    public ADNBoolean findBoolean(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toBoolean(get(key));
    }

    public Boolean tryGetBoolean(@NotNull String key) {
        val value = tryFindBoolean(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNBoolean tryFindBoolean(@NotNull String key) {
        try {
            return findBoolean(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public byte getInt8(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findInt8(key).getValue();
    }

    @NotNull
    public ADNInt8 findInt8(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toInt8(get(key));
    }

    public Byte tryGetInt8(@NotNull String key) {
        val value = tryFindInt8(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNInt8 tryFindInt8(@NotNull String key) {
        try {
            return findInt8(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public short getInt16(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findInt16(key).getValue();
    }

    @NotNull
    public ADNInt16 findInt16(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toInt16(get(key));
    }

    public Short tryGet16(@NotNull String key) {
        val value = tryFindInt16(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNInt16 tryFindInt16(@NotNull String key) {
        try {
            return findInt16(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public int getInt32(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findInt32(key).getValue();
    }

    @NotNull
    public ADNInt32 findInt32(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toInt32(get(key));
    }

    public Integer tryGetInt32(@NotNull String key) {
        val value = tryFindInt32(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNInt32 tryFindInt32(@NotNull String key) {
        try {
            return findInt32(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public long getInt64(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findInt64(key).getValue();
    }

    @NotNull
    public ADNInt64 findInt64(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toInt64(get(key));
    }

    public Long tryGetInt64(@NotNull String key) {
        val value = tryFindInt64(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNInt64 tryFindInt64(@NotNull String key) {
        try {
            return findInt64(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public float getFloat32(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findFloat32(key).getValue();
    }

    @NotNull
    public ADNFP32 findFloat32(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toFloat32(get(key));
    }

    public Float tryGetFloat32(@NotNull String key) {
        val value = tryFindFloat32(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNFP32 tryFindFloat32(@NotNull String key) {
        try {
            return findFloat32(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public double getFloat64(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findFloat64(key).getValue();
    }

    @NotNull
    public ADNFP64 findFloat64(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toFloat64(get(key));
    }

    public Double tryGetFloat64(@NotNull String key) {
        val value = tryFindFloat64(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNFP64 tryFindFloat64(@NotNull String key) {
        try {
            return findFloat64(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    @NotNull
    public String getString(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findString(key).getValue();
    }

    @NotNull
    public ADNString findString(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toString(get(key));
    }

    @Nullable
    public String tryGetString(@NotNull String key) {
        val value = tryFindString(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNString tryFindString(@NotNull String key) {
        try {
            return findString(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    @NotNull
    public Object getObject(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return findObject(key).getValue();
    }

    @NotNull
    public ADNObject findObject(@NotNull String key) throws InvalidKeyException, InvalidCastException {
        return Caster.toObject(get(key));
    }

    @Nullable
    public Object tryGetObject(@NotNull String key) {
        val value = tryFindObject(key);
        return value != null ? value.getValue() : null;
    }

    @Nullable
    public ADNObject tryFindObject(@NotNull String key) {
        try {
            return findObject(key);
        } catch (InvalidKeyException | InvalidCastException ignore) {
            return null;
        }
    }

    public void prettyPrint() {
        System.out.println(getIndent() + name + " = [");

        val values = values();
        for (val value : values) {
            if (!(value instanceof ADNObject)) {
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
        return "\t".repeat(Math.max(0, indent));
    }
}