package com.aston.util;

public class UserTaskAlreadyExistsException extends Exception{
    public UserTaskAlreadyExistsException(String message) {
        super(message);
    }
}