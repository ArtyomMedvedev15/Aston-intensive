package com.aston.util;

import java.sql.SQLException;

public class TransactionManagerException extends SQLException {
    public TransactionManagerException() {
        super();
    }

    public TransactionManagerException(String message) {
        super(message);
    }
}
