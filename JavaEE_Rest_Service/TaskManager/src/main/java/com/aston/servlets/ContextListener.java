package com.aston.servlets;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.HikariPostgreSQLConfig;
import com.aston.dao.implementation.TransactionManagerImplementation;
import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.util.TransactionManagerException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {

    private UserDaoApi userDaoApi;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = HikariPostgreSQLConfig.getHikariDataSource();
        TransactionManager transactionManager = new TransactionManagerImplementation(dataSource);;



        this.userDaoApi = new UserDaoImplementation(transactionManager);

        servletContext.setAttribute("userDao", userDaoApi);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
