package com.aston.servlets;

import com.aston.dao.api.*;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.dao.implementation.*;
import com.aston.service.api.ProjectServiceApi;
import com.aston.service.api.TaskServiceApi;
import com.aston.service.api.UserServiceApi;
import com.aston.service.api.UserTaskServiceApi;
import com.aston.service.implementation.ProjectServiceImplementation;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.service.implementation.UserServiceImplementation;
import com.aston.service.implementation.UserTaskServiceImplementation;
import com.aston.util.ConnectionPoolException;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Slf4j
public class ContextListener implements ServletContextListener {
    private static final String APP_CONTEXT_INIT_MESSAGE = "ApplicationContext was initialized. ";
    private static final String CONNECTION_POOL_DESTROY_ERROR = "Cannot destroy connection pool. ";
    private static final String CONNECTION_POOL_INITIALIZATION_ERROR = "Connection pool initialization error. ";
    private UserDaoApi userDaoApi;
    private TaskDaoApi taskDaoApi;
    private ProjectDaoApi projectDaoApi;
    private UserTaskDaoApi userTaskDaoApi;
    private UserServiceApi userServiceApi;
    private ProjectServiceApi projectServiceApi;
    private TaskServiceApi taskServiceApi;
    private UserTaskServiceApi userTaskServiceApi;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try {
            connectionPool.init("database");
        } catch (ConnectionPoolException e) {
            log.error(CONNECTION_POOL_INITIALIZATION_ERROR, e);
            e.printStackTrace();
        }
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);

        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres")
                .schemas("taskmanager")
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        this.userDaoApi = new UserDaoImplementation(sessionFactory);
        this.taskDaoApi = new TaskDaoImplementation(sessionFactory);
        this.projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        this.userTaskDaoApi = new UserTaskDaoImplementation(connectionManager);
        this.userServiceApi = new UserServiceImplementation(userDaoApi,connectionManager);

        this.projectServiceApi = new ProjectServiceImplementation(projectDaoApi, connectionManager,taskDaoApi,userTaskDaoApi);

        this.taskServiceApi = new TaskServiceImplementation(taskDaoApi,
                new ProjectServiceImplementation(new ProjectDaoImplementation(sessionFactory),
                        connectionManager,new TaskDaoImplementation(sessionFactory)
                ,new UserTaskDaoImplementation(connectionManager)),connectionManager);

        this.userTaskServiceApi = new UserTaskServiceImplementation(userTaskDaoApi,userServiceApi,taskServiceApi, connectionManager);

        servletContext.setAttribute("userService",userServiceApi);
        servletContext.setAttribute("userDao", userDaoApi);

        servletContext.setAttribute("taskService",taskServiceApi);
        servletContext.setAttribute("taskDao", taskDaoApi);

        servletContext.setAttribute("projectService",projectServiceApi);
        servletContext.setAttribute("projectDao", projectDaoApi);

        servletContext.setAttribute("userTaskDao", userTaskDaoApi);
        servletContext.setAttribute("userTaskService",userTaskServiceApi);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
