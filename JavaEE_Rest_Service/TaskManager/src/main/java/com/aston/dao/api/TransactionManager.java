package com.aston.dao.api;

import com.aston.util.TransactionException;

import java.sql.Connection;

public interface TransactionManager {
    void beginTransaction() throws TransactionException;

    void commitTransaction() throws TransactionException;

    void rollbackTransaction() throws TransactionException;

    ConnectionPool getConnectionPool();

    Connection getConnection();
}
