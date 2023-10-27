package com.aston.util;

public class UserInvalidParameter extends Exception{
    public UserInvalidParameter() {
        super();
    }

    public UserInvalidParameter(String message) {
        super(message);
    }

    public UserInvalidParameter(String message, Throwable cause) {
        super(message, cause);
    }

    public UserInvalidParameter(Throwable cause) {
        super(cause);
    }
}