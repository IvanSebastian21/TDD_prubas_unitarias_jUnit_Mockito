package org.example.jUnit.exceptions;

public class DineroInsuficiente extends RuntimeException{
    public DineroInsuficiente(String message) {
        super(message);
    }
}
