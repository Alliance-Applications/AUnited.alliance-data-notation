package io.alliance.adn.api.kotlin

import io.alliance.adn.api.InvalidReadException
import io.alliance.adn.api.kotlin.DataType.*
import kotlin.text.StringBuilder

open class DataStruct(name: String) : DataNode(name) {
    protected val values: HashMap<String, DataNode> = HashMap()

    override fun internalDataString(builder: StringBuilder): StringBuilder {
        builder.append(name).append('{')

        for ((_, node) in values) {
            node.internalDataString(builder)
        }

        return builder.append('}')
    }

    override fun internalFormatString(builder: StringBuilder, depth: Int): StringBuilder {
        builder.append("\t".repeat(depth))
            .append(name)
            .appendLine(" {")

        for ((_, node) in values) {
            node.internalFormatString(builder, depth + 1).appendLine()
        }

        return builder
            .append("\t".repeat(depth))
            .appendLine('}')
    }

    override fun internalPrettyString(builder: StringBuilder, depth: Int): StringBuilder {
        builder.append("\t".repeat(depth))
            .append("struct ")
            .append(name)
            .appendLine(" {")

        for ((_, node) in values) {
            node.internalPrettyString(builder, depth + 1).appendLine()
        }

        return builder
            .append("\t".repeat(depth))
            .appendLine('}')
    }

    operator fun get(name: String): DataNode {
        return values[name] ?: throw InvalidReadException("Property not found!")
    }

    operator fun get(vararg names: String): DataNode {
        var result: DataNode = this

        for (name in names) {
            result = (result as DataStruct)[name]
        }

        return result
    }

    operator fun set(name: String, dataNode: DataNode) {
        values[name] = dataNode
    }

    operator fun remAssign(dataStruct: DataStruct) {
        dataStruct.values.forEach {
            val (name, node) = it
            values[name] = node
        }
    }

    operator fun plusAssign(dataNode: DataNode) {
        if (dataNode is Dataset) {
            this %= dataNode
        }

        values[dataNode.name] = dataNode
    }

    operator fun <T>plusAssign(properties: Pair<String, T>) {
        val (name, value) = properties

        when (value) {
            is Boolean -> values[name] = Datapoint(name, value, BOOL)
            is Byte -> values[name] = Datapoint(name, value, I8)
            is Short -> values[name] = Datapoint(name, value, I16)
            is Int -> values[name] = Datapoint(name, value, I32)
            is Long -> values[name] = Datapoint(name, value, I64)
            is Float -> values[name] = Datapoint(name, value, F32)
            is Double -> values[name] = Datapoint(name, value, F64)
            is String -> values[name] = Datapoint(name, value, STR)
        }
    }

    operator fun minusAssign(dataNode: DataNode) {
        values.remove(dataNode.name)
    }

    operator fun minusAssign(name: String) {
        values.remove(name)
    }

    fun size(): Int {
        return values.size
    }

    companion object {
        fun create(name: String, vararg nodes: DataNode): DataStruct {
            val result = DataStruct(name)

            for (node in nodes) {
                result += node
            }

            return result
        }
    }
}