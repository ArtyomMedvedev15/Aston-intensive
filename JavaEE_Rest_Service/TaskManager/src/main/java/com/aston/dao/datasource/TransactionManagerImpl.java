package com.aston.dao.datasource;

import com.aston.dao.api.ConnectionPool;
import com.aston.dao.api.TransactionManager;
import com.aston.util.TransactionException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class TransactionManagerImpl implements TransactionManager {
        private static final String BEGIN_TRANSACTION_ERROR = "The transaction has already started. ";
        private static final String NULL_CONNECTION_ERROR = "Connection cannot be null. ";
        private static final String TRANSACTION_COMMIT_ERROR = "Transaction commit error.";
        private static final String TRANSACTION_ROLLBACK_ERROR = "Transaction rollback error. ";

        private ConnectionPool connectionPool;
        private ThreadLocal<Connection> localConnection = new ThreadLocal<>();

    public TransactionManagerImpl(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
        }

        @Override
        public void beginTransaction() throws TransactionException {
            try {
                if (localConnection.get() == null) {
                    Connection connection = connectionPool.getConnection();
                    connection.setAutoCommit(false);
                    localConnection.set(connection);
                } else {
                    log.error(NULL_CONNECTION_ERROR);
                    throw new NullPointerException(NULL_CONNECTION_ERROR);
                }
            } catch (SQLException e) {
                log.error(BEGIN_TRANSACTION_ERROR, e);
                throw new TransactionException(BEGIN_TRANSACTION_ERROR, e);
            }
        }

        @Override
        public void commitTransaction() throws TransactionException {
            try {
                Connection connection = localConnection.get();
                if (connection != null) {
                    connection.commit();
                    connection.setAutoCommit(true);
                    connection.close();
                    localConnection.remove();
                } else {
                    log.error(NULL_CONNECTION_ERROR);
                    throw new NullPointerException(NULL_CONNECTION_ERROR);
                }
            } catch (SQLException e) {
                log.error(TRANSACTION_COMMIT_ERROR, e);
                throw new TransactionException(TRANSACTION_COMMIT_ERROR, e);
            }

        }

        @Override
        public void rollbackTransaction() throws TransactionException {
            try {
                Connection connection = localConnection.get();
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    connection.close();
                    localConnection.remove();
                } else {
                    log.error(NULL_CONNECTION_ERROR);
                    throw new NullPointerException(NULL_CONNECTION_ERROR);
                }
            } catch (SQLException e) {
                log.error(TRANSACTION_ROLLBACK_ERROR, e);
                throw new TransactionException(TRANSACTION_COMMIT_ERROR, e);
            }
        }

        @Override
        public ConnectionPool getConnectionPool() {
            return connectionPool;
        }

        @Override
        public Connection getConnection() {
            return localConnection.get();
        }
    }

