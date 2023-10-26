package com.aston.util;

import java.sql.SQLException;

public class ConnectionPoolException extends SQLException {
    public ConnectionPoolException(String reason) {
        super(reason);
    }

    public ConnectionPoolException(String msg, Exception e) {
        super(msg, e);
    }
}
