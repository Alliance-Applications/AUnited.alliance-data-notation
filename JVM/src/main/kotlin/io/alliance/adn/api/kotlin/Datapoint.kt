package io.alliance.adn.api.kotlin

import io.alliance.adn.semantic.TokenIdentifier
import io.alliance.adn.semantic.TokenKind
import io.alliance.adn.semantic.TokenLiteral

internal class Datapoint<T>(name: String, val value: T, val type: DataType) : DataNode(name) {

    private val valueString get() = if (type == DataType.STR) "\"$value\"" else value.toString()
    private val typeString get() = ": ${type.keyword}"

    override fun internalDataString(builder: StringBuilder): StringBuilder {
        builder.append(name)

        if (!type.implicit) {
            builder.append(typeString)
        }

        return builder.append(' ').append(valueString).append(';')
    }

    override fun internalFormatString(builder: StringBuilder, depth: Int): StringBuilder {
        builder.append("\t".repeat(depth)).append(name)

        if (!type.implicit) {
            builder.append(typeString)
        }

        return builder.append(' ').append(valueString).append(' ')
    }

    override fun internalPrettyString(builder: StringBuilder, depth: Int): StringBuilder {
        return builder
            .append("\t".repeat(depth))
            .append(name)
            .append(typeString)
            .append(" = ")
            .append(valueString)
            .append(';')
    }

    internal fun <V> get(): V {
        return (this as Datapoint<V>).value
    }

    companion object {
        private const val ANONYMOUS = ""

        fun <T> create(name: String, type: DataType, value: T): Datapoint<T> {
            return Datapoint(name, value, type)
        }

        internal fun create(identifier: TokenIdentifier, type: TokenKind, value: TokenLiteral): Datapoint<*> {
            return when (type) {
                TokenKind.KEYWORD_BOOL -> Datapoint(identifier.name, value.value.toBooleanStrict(), DataType.BOOL)
                TokenKind.KEYWORD_I8 -> Datapoint(identifier.name, value.value.toByte(), DataType.I8)
                TokenKind.KEYWORD_I16 -> Datapoint(identifier.name, value.value.toShort(), DataType.I16)
                TokenKind.KEYWORD_I32 -> Datapoint(identifier.name, value.value.toInt(), DataType.I32)
                TokenKind.KEYWORD_I64 -> Datapoint(identifier.name, value.value.toLong(), DataType.I64)
                TokenKind.KEYWORD_F32 -> Datapoint(identifier.name, value.value.toFloat(), DataType.F32)
                TokenKind.KEYWORD_F64 -> Datapoint(identifier.name, value.value.toDouble(), DataType.F64)
                TokenKind.KEYWORD_STR -> Datapoint(identifier.name, value.value, DataType.STR)
                else -> throw Exception("That should not be able to happen.")
            }
        }

        fun <T> anonymous(value: T, type: DataType): Datapoint<T> {
            return Datapoint(ANONYMOUS, value, type)
        }

        fun anonymous(value: String, type: DataType): Datapoint<*> {
            return when (type) {
                DataType.NULL -> throw NullPointerException("Attempting to convert anonymous array value to null!")
                DataType.BOOL -> Datapoint(ANONYMOUS, value.toBooleanStrict(), DataType.BOOL)
                DataType.I8 -> Datapoint(ANONYMOUS, value.toByte(), DataType.I8)
                DataType.I16 -> Datapoint(ANONYMOUS, value.toShort(), DataType.I16)
                DataType.I32 -> Datapoint(ANONYMOUS, value.toInt(), DataType.I32)
                DataType.I64 -> Datapoint(ANONYMOUS, value.toLong(), DataType.I64)
                DataType.F32 -> Datapoint(ANONYMOUS, value.toFloat(), DataType.F32)
                DataType.F64 -> Datapoint(ANONYMOUS, value.toDouble(), DataType.F64)
                DataType.STR -> Datapoint(ANONYMOUS, value, DataType.STR)
            }
        }
    }
}