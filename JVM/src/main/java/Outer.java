package main.java;

import java.time.LocalDateTime;

public class Outer {
    public int id = 0;
    public String text = "Hello";
    public LocalDateTime ldt = LocalDateTime.now();
    public Inner o = new Inner();
}