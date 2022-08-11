package io.alliance.adn.api.kotlin

import io.alliance.adn.api.NotationFormat
import io.alliance.adn.api.NotationFormat.*
import io.alliance.adn.semantic.Lexer
import io.alliance.adn.syntactic.Parser
import java.io.File
import java.io.Reader
import java.io.Writer

class Dataset: DataStruct("_root") {

    override fun internalDataString(builder: StringBuilder): StringBuilder {
        values.forEach { (_, element) ->
            builder.append(element.toString())
        }

        return builder
    }

    override fun internalFormatString(builder: StringBuilder, depth: Int): StringBuilder {
        values.forEach { (_, element) ->
            builder.appendLine(element.toString(FORMATTED))
        }

        return builder
    }

    override fun internalPrettyString(builder: StringBuilder, depth: Int): StringBuilder {
        values.forEach { (_, element) ->
            builder.appendLine(element.toString(PRETTY))
        }

        return builder
    }

    fun write(file: File, format: NotationFormat = DATA) {
        val writer = file.writer()
        write(writer, format)
        writer.close()
    }

    fun write(writer: Writer, format: NotationFormat = DATA) {
        writer.write(toString(format))
    }

    companion object {
        fun read(file: File): Dataset {
            val reader = file.reader()
            val result = read(reader)
            reader.close()
            return result
        }

        fun read(reader: Reader): Dataset {
            return Parser(Lexer(reader.readText().toList()).lex()).parse()
        }

        fun create(vararg nodes: DataNode): Dataset {
            val result = Dataset()

            for (node in nodes) {
                result += node
            }

            return result
        }
    }

}