package com.aston.servlets;

import com.aston.dao.api.*;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.dao.implementation.*;
import com.aston.service.api.*;
import com.aston.service.implementation.*;
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
    private UserServiceApi userServiceApi;
    private ProjectServiceApi projectServiceApi;
    private TaskServiceApi taskServiceApi;
    private UserTaskServiceApi userTaskServiceApi;
    private UserTaskDaoApi userTaskDaoApi;
    private ActivityDaoApi activityDaoApi;
    private BugDaoApi bugDaoApi;
    private MeetingDaoApi meetingDaoApi;
    private ActivityServiceApi activityServiceApi;
    private BugServiceApi bugServiceApi;
    private MeetingServiceApi meetingServiceApi;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres")
                .schemas("taskmanager")
                .locations("classpath:db/migration")
                .load();
        //flyway.migrate();

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        this.userDaoApi = new UserDaoImplementation(sessionFactory);
        this.taskDaoApi = new TaskDaoImplementation(sessionFactory);
        this.projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        this.userTaskDaoApi = new UserTaskDaoImplementation(sessionFactory);
        this.activityDaoApi = new ActivityDaoImplementation(sessionFactory);
        this.bugDaoApi = new BugDaoImplementation(sessionFactory);
        this.meetingDaoApi = new MeetingDaoImplemntation(sessionFactory);

        this.activityServiceApi = new ActivityServiceImplementation(sessionFactory,activityDaoApi);
        this.bugServiceApi = new BugServiceImplementation(bugDaoApi,sessionFactory);
        this.meetingServiceApi = new MeetingServiceImplementation(meetingDaoApi,sessionFactory);
        this.userServiceApi = new UserServiceImplementation(userDaoApi,sessionFactory);
        this.userTaskServiceApi = new UserTaskServiceImplementation(userDaoApi, sessionFactory, taskDaoApi, new UserTaskDaoImplementation(sessionFactory));
        this.projectServiceApi = new ProjectServiceImplementation(projectDaoApi, sessionFactory);
        this.taskServiceApi = new TaskServiceImplementation(taskDaoApi,projectServiceApi,sessionFactory, projectDaoApi);

        servletContext.setAttribute("userService",userServiceApi);
        servletContext.setAttribute("userDao", userDaoApi);

        servletContext.setAttribute("taskService",taskServiceApi);
        servletContext.setAttribute("taskDao", taskDaoApi);

        servletContext.setAttribute("projectService",projectServiceApi);
        servletContext.setAttribute("projectDao", projectDaoApi);

        servletContext.setAttribute("userTaskService",userTaskServiceApi);
        servletContext.setAttribute("userTaskDao",userTaskDaoApi);

        servletContext.setAttribute("activityDao",activityDaoApi);
        servletContext.setAttribute("activityService",activityServiceApi);

        servletContext.setAttribute("bugDao",bugDaoApi);
        servletContext.setAttribute("bugService",bugServiceApi);

        servletContext.setAttribute("meetingDao",meetingDaoApi);
        servletContext.setAttribute("meetingService",meetingServiceApi);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
