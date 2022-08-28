package io.alliance.adn.api

import io.alliance.adn.api.kotlin.DataList
import io.alliance.adn.api.kotlin.DataStruct
import io.alliance.adn.api.kotlin.DataType
import io.alliance.adn.api.kotlin.Dataset
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class ApiE2ETest {

    @Test
    internal fun givenNothing_thenCreateWriteReadAndPrintDataset() {
        val dataset = createPrimaryRoot()
        val secondary = createSecondaryRoot()

        dataset %= secondary

        val tmp = File.createTempFile("dataset", "adn")
        dataset.write(tmp)

        val readSet = Dataset.read(tmp)

        compareDataset(readSet)
    }

    private fun createPrimaryRoot(): Dataset {
        val mat0 = DataList("Matrix", DataType.F32, 0)
        mat0 += 0.5f
        mat0 += -1.0f

        val mat1 = DataList("Matrix", DataType.F32, 0)
        mat1 += arrayOf(0.0f, -0.5f)

        val matrix = DataList("Matrix", DataType.F32, 1)
        matrix += listOf(mat0, mat1)

        val list = DataList("List", DataType.I64, 0)
        list += listOf(0, 1, 2, 3, 4)

        val primary = DataStruct("Primary")
        primary += matrix

        val root = Dataset()
        root += primary

        return root
    }

    private fun createSecondaryRoot(): Dataset {
        val root = Dataset()
        root += DataStruct("Secondary")

        // Test accessor
        val secondary = root["Secondary"] as DataStruct

        // Test insertion
        secondary += Pair("boolean", true)
        secondary += Pair("byte", 0.toByte())
        secondary += Pair("short", 0.toShort())
        secondary += Pair("int", 0)
        secondary += Pair("long", 0L)
        secondary += Pair("float", 0.0f)
        secondary += Pair("double", 0.0)
        secondary += Pair("string", "0")

        // Test removal
        secondary += Pair("useless", 0)
        secondary -= "useless"

        return root
    }

    private fun compareDataset(dataset: Dataset) {
        run {
            val expected: String = "" +
                    "struct Secondary {\n" +
                    "\tboolean: bool = true;\n" +
                    "\tstring: str = \"0\";\n" +
                    "\tbyte: i8 = 0;\n" +
                    "\tdouble: f64 = 0.0;\n" +
                    "\tshort: i16 = 0;\n" +
                    "\tfloat: f32 = 0.0;\n" +
                    "\tint: i32 = 0;\n" +
                    "\tlong: i64 = 0;\n" +
                    "}\n" +
                    "\n" +
                    "struct Primary {\n" +
                    "\tMatrix: f32 [\n" +
                    "\t\t[\n" +
                    "\t\t\t0.5, -1.0\n" +
                    "\t\t], [\n" +
                    "\t\t\t0.0, -0.5\n" +
                    "\t\t]\n" +
                    "\t]\n" +
                    "}\n" +
                    "\n"
            Assertions.assertEquals(expected, dataset.toString(NotationFormat.PRETTY))
        }

        run {
            val expected: String = "" +
                    "Secondary {\n" +
                    "\tboolean true \n" +
                    "\tstring \"0\" \n" +
                    "\tbyte: i8 0 \n" +
                    "\tdouble: f64 0.0 \n" +
                    "\tshort: i16 0 \n" +
                    "\tfloat 0.0 \n" +
                    "\tint 0 \n" +
                    "\tlong: i64 0 \n" +
                    "}\n" +
                    "\n" +
                    "Primary {\n" +
                    "\tMatrix [\n" +
                    "\t\t[\n" +
                    "\t\t\t0.5, -1.0\n" +
                    "\t\t], [\n" +
                    "\t\t\t0.0, -0.5\n" +
                    "\t\t]\n" +
                    "\t]\n" +
                    "}\n" +
                    "\n"
            Assertions.assertEquals(expected, dataset.toString(NotationFormat.FORMATTED))
        }

        run {
            val expected = "Secondary{boolean true;string \"0\";byte: i8 0;double: f64 0.0;short: i16 0;float 0.0;int 0;long: i64 0;}Primary{Matrix[[0.5,-1.0],[0.0,-0.5]]}"
            Assertions.assertEquals(expected, dataset.toString())
        }
    }
}