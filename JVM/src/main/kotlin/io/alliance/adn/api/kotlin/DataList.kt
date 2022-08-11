package io.alliance.adn.api.kotlin

internal class DataList(name: String, private val type: DataType, val depth: Int) : DataNode(name) {
    private val values: ArrayList<DataNode> = ArrayList()

    override fun internalDataString(builder: StringBuilder): StringBuilder {
        // array: i32 [0, 1]
        // array [0, 1]

        builder.append(name)

        if (!type.implicit) {
            builder.append(':').append(type.keyword)
        }

        return stringifyData(builder)
    }

    override fun internalFormatString(builder: StringBuilder, depth: Int): StringBuilder {
        // array: i32 [
        //     0, 1
        // ]
        // array [
        //     0, 1
        // ]

        builder.append("\t".repeat(depth)).append(name)

        if (!type.implicit) {
            builder.append(": ").append(type.keyword)
        }

        return stringifyFormat(builder.append(' '), depth + 1)
    }

    override fun internalPrettyString(builder: StringBuilder, depth: Int): StringBuilder {
        // array: i32 [
        //     0, 1
        // ]

        return stringifyFormat(builder.append("\t".repeat(depth)).append(name).append(": ").append(type.keyword).append(' '), depth + 1)
    }


    private fun stringifyData(builder: StringBuilder): StringBuilder {
        builder.append('[')

        if (depth > 0) {
            for (value in values) {
                val node = value as DataList
                node.stringifyData(builder).append(',')
            }
        } else {
            for (value in values) {
                val node = value as Datapoint<*>
                builder.append(node.value.toString()).append(',')
            }
        }

        builder[builder.length - 1] = ']'
        return builder
    }


    private fun stringifyFormat(builder: StringBuilder, depth: Int): StringBuilder {
        builder.appendLine('[').append("\t".repeat(depth))

        if (this.depth > 0) {
            for (value in values) {
                val node = value as DataList
                node.stringifyFormat(builder, depth + 1)
                    .append(", ")
            }
        } else {
            for (value in values) {
                val node = value as Datapoint<*>
                builder
                    .append(node.value.toString())
                    .append(", ")
            }
        }

        return builder
            .replace(builder.length - 2, builder.length, "")
            .appendLine()
            .append("\t".repeat(depth - 1))
            .append(']')
    }

    internal operator fun get(vararg indices: Int): DataNode {
        if (indices.size > 1) {
            var result: DataNode = this

            for (index in indices) {
                result = (result as DataList)[index]
            }

            return result
        }

        return values[indices[0]]
    }

    internal operator fun set(index: Int, dataNode: DataNode) {
        values[index] = dataNode
    }

    internal fun size(): Int {
        return values.size
    }

    internal operator fun plusAssign(dataNode: DataNode) {
        values.add(dataNode)
    }

    internal operator fun <T> plusAssign(value: T) {
        if (this.depth > 0 && value is DataList && value.depth == this.depth - 1) {
            this.values.add(value)
            return
        }

        if (value is Datapoint<*> && value.type == this.type) {
            this.values.add(value)
            return
        }

        if (when (this.type) {
                DataType.BOOL -> value is Boolean
                DataType.I8 -> value is Byte
                DataType.I16 -> value is Short
                DataType.I32 -> value is Int
                DataType.I64 -> value is Long
                DataType.F32 -> value is Float
                DataType.F64 -> value is Double
                DataType.STR -> value is String
                else -> false
            }
        ) {
            this.values.add(Datapoint.anonymous(value, this.type))
        }
    }

    internal operator fun <T> plusAssign(values: Array<T>) {
        this += values.asList()
    }

    internal operator fun <T> plusAssign(values: List<T>) {
        if (values.isEmpty()) {
            return
        }

        if (this.depth > 0) {
            values.forEach {
                if (it is DataList && it.depth == this.depth - 1) {
                    this.values.add(it)
                }
            }
            return
        }

        if (values[0] is Datapoint<*>) {
            values.forEach {
                if ((it as Datapoint<*>).type == this.type) {
                    this.values.add(it)
                }
            }
            return
        }

        if (when (this.type) {
                DataType.BOOL -> values[0] is Boolean
                DataType.I8 -> values[0] is Byte
                DataType.I16 -> values[0] is Short
                DataType.I32 -> values[0] is Int
                DataType.I64 -> values[0] is Long
                DataType.F32 -> values[0] is Float
                DataType.F64 -> values[0] is Double
                DataType.STR -> values[0] is String
                else -> false
            }
        ) {
            values.forEach {
                this.values.add(Datapoint.anonymous(it, this.type))
            }
        }
    }

    internal operator fun minusAssign(dataNode: DataNode) {
        values.remove(dataNode)
    }

    internal operator fun minusAssign(dataNodes: Array<DataNode>) {
        values.removeAll(dataNodes.toSet())
    }

    internal operator fun minusAssign(dataNodes: List<DataNode>) {
        values.removeAll(dataNodes.toSet())
    }

    internal operator fun minusAssign(index: Int) {
        values.removeAt(index)
    }

    companion object {
        internal fun anonymous(dataType: DataType, depth: Int): DataList {
            return DataList("_anonymous", dataType, depth)
        }
    }
}