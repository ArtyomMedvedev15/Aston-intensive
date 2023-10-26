package com.aston.util;

import java.sql.SQLException;

public class TransactionException extends SQLException {
    public TransactionException(String reason) {
        super(reason);
    }

    public TransactionException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
