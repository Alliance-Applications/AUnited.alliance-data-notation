package main.java;

import main.java.io.alliance.adn.element.ADNObject;
import main.java.io.alliance.adn.exception.ParsingException;

public class FullTest {
    public static void main(String[] args) {
        try {
            ADNObject object = ADNObject.parse("object testContainer = {\n" +
                    "\tuint64 id = 0;\n" +
                    "\tboolean isTestObject = false;\n" +
                    "\tstring name = \"test container\\\\\";\n" +
                    "\tobject testObject = {\n" +
                    "\t\tuint64 id = 1;\n" +
                    "\t\tboolean isTestObject = true;\n" +
                    "\t\tstring name = \"test object\\\\\";\n" +
                    "\t\tobject subObject = null;\n" +
                    "\t};\n" +
                    "};");

            object.putObject("test", new Outer());
            System.out.println(object.textify(new StringBuilder(), 0));
            // object.saveTo("C:/Users/mara/Desktop/ADNObject.adn");
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }
}