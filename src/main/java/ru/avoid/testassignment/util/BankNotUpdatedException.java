package ru.avoid.testassignment.util;

public class BankNotUpdatedException extends RuntimeException{
    public BankNotUpdatedException(String msg) {
        super(msg);
    }
}
