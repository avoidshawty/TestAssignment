package ru.avoid.testassignment.util;

public class DepositNotUpdatedException extends RuntimeException{
    public DepositNotUpdatedException(String msg) {
        super(msg);
    }
}
