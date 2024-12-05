package ru.avoid.testassignment.util;

public class ClientNotCreatedException extends RuntimeException{
    public ClientNotCreatedException(String msg){
        super(msg);
    }
}