package com.aston.util;

import org.hibernate.HibernateException;

import java.sql.SQLException;

public class TransactionException extends HibernateException {
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
