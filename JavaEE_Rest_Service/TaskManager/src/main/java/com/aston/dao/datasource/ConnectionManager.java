package com.aston.dao.datasource;

import com.aston.dao.api.TransactionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
public class ConnectionManager {
    private static final String CONNECTION_POOL_NOT_INITIALIZED = "The connection pool is not initialized. ";
    private TransactionManager transactionManager;

    public ConnectionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Connection getConnection() {
        Connection connection = transactionManager.getConnection();
        if (connection == null) {
            connection = transactionManager.getConnectionPool().getConnection();
        }
        if (connection == null) {
            log.error(CONNECTION_POOL_NOT_INITIALIZED);
            throw new IllegalStateException(CONNECTION_POOL_NOT_INITIALIZED);
        }
        return connection;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}
