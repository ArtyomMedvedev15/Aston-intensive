package com.aston.dao.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.util.TransactionManagerException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManagerImplementation implements TransactionManager {
    private static final int TIMEOUT_IN_SECONDS = 10;
    private final DataSource dataSource;
    private Connection connection;

    public TransactionManagerImplementation(DataSource dataSource){
        if (dataSource == null) {
            System.out.println("Null");
        }
        this.dataSource = dataSource;

    }

    @Override
    public void beginSession() throws TransactionManagerException {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new TransactionManagerException(e.getMessage());
        }
    }

    @Override
    public void commitSession() throws TransactionManagerException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionManagerException(e.getMessage());
        }
    }

    @Override
    public void rollbackSession() throws TransactionManagerException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionManagerException(e.getMessage());
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public Connection getCurrentSession() throws SQLException {
        return dataSource.getConnection();
    }


}
