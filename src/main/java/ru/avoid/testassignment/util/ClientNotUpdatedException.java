package ru.avoid.testassignment.util;

public class ClientNotUpdatedException extends RuntimeException{
    public ClientNotUpdatedException(String msg) {
        super(msg);
    }
}
