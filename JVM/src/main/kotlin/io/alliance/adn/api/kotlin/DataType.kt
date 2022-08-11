package io.alliance.adn.api.kotlin

internal enum class DataType(internal val implicit: Boolean, internal val keyword: String) {
    NULL(false, "none"),
    BOOL(true, "bool"),
    I8(false, "i8"),
    I16(false, "i16"),
    I32(true, "i32"),
    I64(false, "i64"),
    F32(true, "f32"),
    F64(false, "f64"),
    STR(true, "str");

    internal operator fun times(type: DataType): DataType {
        // NULL is always overwritten

        if (this == NULL) {
            return type
        }

        // Same types do not change either
        if (this == type || type == NULL) {
            return this
        }

        // Any combination of a type with STR or BOOL results in a STR
        if (this == BOOL || this == STR || type == BOOL || type == STR) {
            return STR
        }

        // Combining two numbers results in the type, with the highest amount of bits.
        // Is a floating point number included, the result will be a floating point number as well.
        // Since the mantissa is not the full bit width of a float, I32 + F32 will result in an F64.
        // For I64 and F64, there is no bigger option, so a small data loss must be acceptable, since
        // a STR result would be a behaviour no one expects.

        when (this) {
            I8 -> return I8
            I16 -> when (type) {
                I8 -> return I16
                I32 -> return I32
                I64 -> return I64
                F32 -> return F32
                F64 -> return F64
                else -> {}
            }
            I32 -> when (type) {
                I8 -> return I32
                I16 -> return I32
                I64 -> return I64
                F32 -> return F64
                F64 -> return F64
                else -> {}
            }
            I64 -> when (type) {
                I8 -> return I64
                I16 -> return I64
                I32 -> return I64
                F32 -> return F64
                F64 -> return F64
                else -> {}
            }
            F32 -> when (type) {
                I8 -> return F32
                I16 -> return F32
                I32 -> return F64
                I64 -> return F64
                F64 -> return F64
                else -> {}
            }
            F64 -> return F64
            else -> {}
        }

        throw Exception("Can't happen anyways but whatever")
    }
}