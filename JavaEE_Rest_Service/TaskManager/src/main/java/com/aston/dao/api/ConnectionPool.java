package com.aston.dao.api;

import com.aston.util.ConnectionPoolException;

import java.sql.Connection;

public interface ConnectionPool {
    void init(String dbPropertiesName) throws ConnectionPoolException;

    void destroy() throws ConnectionPoolException;

    Connection getConnection();
}
