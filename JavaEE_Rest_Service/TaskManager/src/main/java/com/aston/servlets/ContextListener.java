package com.aston.servlets;

import com.aston.dao.api.*;
import com.aston.dao.datasource.HikariPostgreSQLConfig;
import com.aston.dao.implementation.*;
import com.aston.service.api.ProjectServiceApi;
import com.aston.service.api.TaskServiceApi;
import com.aston.service.api.UserServiceApi;
import com.aston.service.implementation.ProjectServiceImplementation;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.service.implementation.UserServiceImplementation;

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
    private UserServiceApi userServiceApi;
    private ProjectServiceApi projectServiceApi;
    private TaskServiceApi taskServiceApi;

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
        this.userServiceApi = new UserServiceImplementation(userDaoApi,transactionManager);
        this.projectServiceApi = new ProjectServiceImplementation(projectDaoApi,transactionManager);
        this.taskServiceApi = new TaskServiceImplementation(taskDaoApi,transactionManager,projectServiceApi);


        servletContext.setAttribute("userService",userServiceApi);
        servletContext.setAttribute("userDao", userDaoApi);

        servletContext.setAttribute("taskService",taskServiceApi);
        servletContext.setAttribute("taskDao", taskDaoApi);

        servletContext.setAttribute("projectService",projectServiceApi);
        servletContext.setAttribute("projectDao", projectDaoApi);

        servletContext.setAttribute("userTaskDao", userTaskDaoApi);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
