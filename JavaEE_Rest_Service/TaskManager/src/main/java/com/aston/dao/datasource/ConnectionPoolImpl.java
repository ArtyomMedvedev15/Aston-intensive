package com.aston.dao.datasource;


import com.aston.dao.api.ConnectionPool;
import com.aston.util.ConnectionPoolException;
 import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConnectionPoolImpl implements ConnectionPool {
    private static final String CONNECTION_POOL_START_INITIALIZING = "Connection pool start initializing.";
    private static final String CONNECTION_POOL_WAS_INITIALIZED = "Connection pool was initialized.";
    private static final String CONNECTION_POOL_GET_CONNECTION_EXCEPTION = "Cannot get free connection from connection pool.";
    private static final String CONNECTION_RETURNING_ERROR = "Exception during returning connection in the pool.";
    private static final String CONNECTION_POOL_DESTROY_EXCEPTION = "Cannot destroy connection pool.";
    private static final String CONNECTION_CREATION_EXCEPTION = "Impossible to create connection.";
    private static final String CONNECTION_POOL_INITIALIZATION_EXCEPTION = "Impossible to initialize connection pool.";

    private static final ReentrantLock locker = new ReentrantLock();
    private static final String MISSING_RESOURCE_POOL_EXCEPTION = "All necessary resources were not found. ";
    private static final String CONNECTIONS_WERE_DELETED_SUCCESSFULLY = "Connections were deleted successfully. ";

    private static ConnectionPool instance;

    private volatile BlockingQueue<Connection> availableConnections = new LinkedBlockingQueue<>();
    private volatile BlockingQueue<Connection> usedConnections = new LinkedBlockingQueue<>();
    private String driver;
    private String url;
    private String user;
    private String pass;

    private ConnectionPoolImpl() {
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            try {
                locker.lock();
                if (instance == null) {
                    instance = new ConnectionPoolImpl();
                }
            } finally {
                locker.unlock();
            }
        }
        return instance;
    }

    @Override
    public void init(String dbPropertiesName) throws ConnectionPoolException {
        log.info(CONNECTION_POOL_START_INITIALIZING);
        try {
            ResourceBundle resource = ResourceBundle.getBundle(dbPropertiesName);
            driver = resource.getString("db.driver");
            url = resource.getString("db.url");
            user = resource.getString("db.user");
            pass = resource.getString("db.password");
            int poolSize = Integer.parseInt(resource.getString("db.pool.size"));

            Class.forName(driver);
            for (int i = 0; i < poolSize; i++) {
                Connection cn = getNewConnection(driver, url, user, pass);
                availableConnections.put(cn);
            }
            log.info(CONNECTION_POOL_WAS_INITIALIZED);
        } catch (MissingResourceException e) {
            log.error(MISSING_RESOURCE_POOL_EXCEPTION);
            throw new ConnectionPoolException(MISSING_RESOURCE_POOL_EXCEPTION, e);
        } catch (ClassNotFoundException | InterruptedException e) {
            log.error(CONNECTION_POOL_INITIALIZATION_EXCEPTION + e);
            throw new ConnectionPoolException(CONNECTION_POOL_INITIALIZATION_EXCEPTION, e);
        }
    }


    private Connection getNewConnection(String driver, String url, String user, String pass)
            throws ConnectionPoolException {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            log.error(CONNECTION_CREATION_EXCEPTION, e);
            throw new ConnectionPoolException(CONNECTION_CREATION_EXCEPTION, e);
        }
    }

    public Connection getConnection() {
        Connection proxyConnection = null;
        try {
            Connection connection;
            connection = availableConnections.take();
            usedConnections.put(connection);
            proxyConnection = createProxyConnection(connection);
        } catch (InterruptedException e) {
            log.error(CONNECTION_POOL_GET_CONNECTION_EXCEPTION);
        }

        return proxyConnection;
    }


    private Connection createProxyConnection(Connection connection) {
        return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        returnConnectionToPool(connection);
                        return null;
                    } else if ("hashCode".equals(method.getName())) {
                        return connection.hashCode();
                    } else {
                        return method.invoke(connection, args);
                    }
                });
    }

    private void returnConnectionToPool(Connection connection) throws ConnectionPoolException {
        if (connection == null) {
            log.error(CONNECTION_RETURNING_ERROR);
            throw new ConnectionPoolException(CONNECTION_RETURNING_ERROR);
        }
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    @Override
    public void destroy() throws ConnectionPoolException {
        try {
            for (Connection con : availableConnections) {
                con.close();
                availableConnections.remove(con);
            }
            for (Connection con : usedConnections) {
                con.close();
                usedConnections.remove(con);
            }
            if (availableConnections.size() == 0 && usedConnections.size() == 0) {
                log.debug(CONNECTIONS_WERE_DELETED_SUCCESSFULLY);
            }
        } catch (SQLException e) {
            log.error(CONNECTION_POOL_DESTROY_EXCEPTION + e);
            throw new ConnectionPoolException(CONNECTION_POOL_DESTROY_EXCEPTION, e);
        }
    }
}
