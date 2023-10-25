package com.aston.dao.api;

import com.aston.util.TransactionManagerException;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionManager {
    void beginSession() throws TransactionManagerException;
    void commitSession() throws TransactionManagerException;
    void rollbackSession() throws TransactionManagerException;
    void close() throws SQLException;
    Connection getCurrentSession() throws SQLException;
}
