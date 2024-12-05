package ru.avoid.testassignment.util;

public class DepositNotCreatedException extends RuntimeException{
    public DepositNotCreatedException(String msg){
        super(msg);
    }
}
