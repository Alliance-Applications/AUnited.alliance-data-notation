package io.alliance.adn.api

import io.alliance.adn.api.kotlin.*
import io.alliance.adn.api.kotlin.DataType.F64
import io.alliance.adn.api.kotlin.DataType.STR
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

        val mat0 = DataList.anonymous(F64, 0)
        mat0 += 0.5
        mat0 += -1.0

        val mat1 = DataList.anonymous(F64, 0)
        mat1 += 0.0
        mat1 += -0.5

        val matrix = DataList("Matrix", F64, 1)
        matrix += mat0
        matrix += mat1

        val array = DataList("Array", DataType.I32, 0)
        array += 0
        array += 1
        array += 2
        array += 3
        array += 4

        val struct = DataStruct.create("TestStruct", matrix, array, Datapoint.create("name", STR, "Matrix:"))

        root = Dataset.create(struct)
    }

    @Test
    fun givenElementRoot_thenShouldReturnDataString() {
        val dataString = "TestStruct{Array[0,1,2,3,4]Matrix:f64[[0.5,-1.0],[0.0,-0.5]]name \"Matrix:\";}"
        assertEquals(dataString, root.toString())
    }

    @Test
    fun givenElementRoot_thenShouldReturnFormatString() {
        val formatString = "" +
                "TestStruct {\n" +
                "\tArray [\n" +
                "\t\t0, 1, 2, 3, 4\n" +
                "\t]\n" +
                "\tMatrix: f64 [\n" +
                "\t\t[\n" +
                "\t\t\t0.5, -1.0\n" +
                "\t\t], [\n" +
                "\t\t\t0.0, -0.5\n" +
                "\t\t]\n" +
                "\t]\n" +
                "\tname \"Matrix:\" \n" +
                "}\n" +
                "\n"
        assertEquals(formatString, root.toString(NotationFormat.FORMATTED))
    }

    @Test
    fun givenElementRoot_thenShouldReturnPrettyString() {
        val prettyString = "" +
                "struct TestStruct {\n" +
                "\tArray: i32 [\n" +
                "\t\t0, 1, 2, 3, 4\n" +
                "\t]\n" +
                "\tMatrix: f64 [\n" +
                "\t\t[\n" +
                "\t\t\t0.5, -1.0\n" +
                "\t\t], [\n" +
                "\t\t\t0.0, -0.5\n" +
                "\t\t]\n" +
                "\t]\n" +
                "\tname: str = \"Matrix:\";\n" +
                "}\n" +
                "\n"
        assertEquals(prettyString, root.toString(NotationFormat.PRETTY))
    }
}