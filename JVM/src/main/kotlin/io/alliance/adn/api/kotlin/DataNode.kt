package io.alliance.adn.api.kotlin

import io.alliance.adn.api.NotationFormat

abstract class DataNode internal constructor(val name: String) {
    internal abstract fun internalDataString(builder: StringBuilder): StringBuilder
    internal abstract fun internalFormatString(builder: StringBuilder, depth: Int): StringBuilder
    internal abstract fun internalPrettyString(builder: StringBuilder, depth: Int): StringBuilder

    override fun toString(): String {
        return toString(NotationFormat.DATA)
    }

    fun toString(format: NotationFormat): String {
        return when (format) {
            NotationFormat.DATA -> internalDataString(StringBuilder()).toString()
            NotationFormat.FORMATTED -> internalFormatString(StringBuilder(), 0).toString()
            NotationFormat.PRETTY -> internalPrettyString(StringBuilder(), 0).toString()
        }
    }

    fun asList(): Result<DataList> {
        if (this is DataList) {
            return Result.success(this)
        }

        return Result.failure(RuntimeException("'this' is not a DataList!"))
    }

    fun asStruct(): Result<DataStruct> {
        if (this is DataStruct) {
            return Result.success(this)
        }

        return Result.failure(RuntimeException("'this' is not a DataStruct!"))
    }

    inline fun <reified T> asDatapoint(): Result<Datapoint<T>> {
        if (this is Datapoint<*> && this.value is T) {
            @Suppress("UNCHECKED_CAST")
            return Result.success(this as Datapoint<T>)
        }

        return Result.failure(RuntimeException("'this' is not a Datapoint with given type!"))
    }

    inline fun <reified T> getDatapointValue(): Result<T> {
        if (this is Datapoint<*> && this.value is T) {
            return Result.success(this.value)
        }

        return Result.failure(RuntimeException("'this' is not a Datapoint with given type!"))
    }
}