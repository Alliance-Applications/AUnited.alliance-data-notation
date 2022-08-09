package io.alliance.adn.api

import io.alliance.adn.api.kotlin.DataList
import io.alliance.adn.api.kotlin.Datapoint
import io.alliance.adn.api.kotlin.Dataset
import io.alliance.adn.api.kotlin.DataStruct
import io.alliance.adn.api.kotlin.DataType.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DataNodeTest {
    private lateinit var root: Dataset

    @BeforeEach
    fun setUp() {
        // struct MyStruct {
        //     name: str = "Matrix:";
        //     matrix: f64[][] [
        //         [0.5, -1],
        //         [0, -0.5]
        //     ]
        // }
        val mat00 = Datapoint.anonymous("0.5", F64)
        val mat01 = Datapoint.anonymous("-1", F64)
        val mat10 = Datapoint.anonymous("0", F64)
        val mat11 = Datapoint.anonymous("-0.5", F64)

        val mat0 = DataList("matrix", F64, 0)
        mat0 += mat00
        mat0 += mat01

        val mat1 = DataList("matrix", F64, 0)
        mat1 += mat10
        mat1 += mat11

        val matrix = DataList("matrix", F64, 1)
        matrix += mat0
        matrix += mat1

        val name = Datapoint("name", "Matrix:", STR)

        val myStruct = DataStruct("MyStruct")
        myStruct += name
        myStruct += matrix
        root = Dataset()

    }

    @Test
    fun givenElementRoot_thenShouldReturnDataString() {
        val dataString = "a"
        assertEquals(dataString, root.dataString)
    }

    @Test
    fun givenElementRoot_thenShouldReturnFormatString() {
        val formatString = "b"
        assertEquals(formatString, root.formatString)
    }

    @Test
    fun givenElementRoot_thenShouldReturnPrettyString() {
        val prettyString = "c"
        assertEquals(prettyString, root.prettyString)
    }
}