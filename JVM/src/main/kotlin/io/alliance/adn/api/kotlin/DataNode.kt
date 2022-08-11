package io.alliance.adn.api.kotlin

import io.alliance.adn.api.NotationFormat
import kotlin.text.StringBuilder

abstract class DataNode(val name: String) {
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
}