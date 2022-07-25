package io.alliance.adn.test;

import io.alliance.adn.element.ADNInt8;
import io.alliance.adn.element.ADNObject;
import lombok.val;

public class Test2 {
    public static void main(String[] args) {
        val numbers = new ADNObject("root");
        numbers.put("_int8", new ADNInt8("_int8", (byte)12));
    }
}