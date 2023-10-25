package com.aston.servlets;

import com.aston.dao.api.*;
import com.aston.dao.datasource.HikariPostgreSQLConfig;
import com.aston.dao.implementation.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {

    private UserDaoApi userDaoApi;
    private TaskDaoApi taskDaoApi;
    private ProjectDaoApi projectDaoApi;

    private UserTaskDaoApi userTaskDaoApi;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = HikariPostgreSQLConfig.getHikariDataSource();
        TransactionManager transactionManager = new TransactionManagerImplementation(dataSource);;



        this.userDaoApi = new UserDaoImplementation(transactionManager);
        this.taskDaoApi = new TaskDaoImplementation(transactionManager);
        this.projectDaoApi = new ProjectDaoImplementation(transactionManager);
        this.userTaskDaoApi = new UserTaskImplementation(transactionManager);

        servletContext.setAttribute("userDao", userDaoApi);
        servletContext.setAttribute("taskDao", taskDaoApi);
        servletContext.setAttribute("projectDao", projectDaoApi);
        servletContext.setAttribute("userTaskDao", userTaskDaoApi);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
