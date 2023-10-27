package com.aston.dao.implementation;

import com.aston.dao.api.ConnectionPool;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.entities.Project;
import com.aston.util.ConnectionPoolException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class ProjectDaoImplementationTest {

    private static ProjectDaoImplementation projectDaoImplementation;
    private static ConnectionPool connectionPool;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        projectDaoImplementation = new ProjectDaoImplementation(connectionManager);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    @Test
    public void CreateProjectTest_ReturnTrue() throws SQLException {
        Project projectSave = Project.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        int projectSaveResult = projectDaoImplementation.createProject(projectSave);

        Assert.assertTrue(projectSaveResult>0);

        projectDaoImplementation.deleteProject(projectSaveResult);

    }

    @Test
    public void UpdateProjectTest_ReturnTrue() throws SQLException {
        Project projectUpdate = Project.builder()
                .id(777)
                .name("Updated")
                .description("Update")
                .build();

        int projectUpdateResult = projectDaoImplementation.updateProject(projectUpdate);

        Assert.assertTrue(projectUpdateResult>0);
    }

    @Test
    public void DeleteProjectTest_ReturnTrue() throws SQLException {
        Project projectForDelete = Project.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        int projectDelete = projectDaoImplementation.createProject(projectForDelete);

        int projectDeleteResult = projectDaoImplementation.deleteProject(projectDelete);

        Assert.assertTrue(projectDeleteResult>0);

    }

    @Test
    public void GetProjectByIdTest_WithId777_ReturnTrue() throws SQLException {
        Project projectById = projectDaoImplementation.getProjectById(778);
        Assert.assertEquals("TestProject2", projectById.getName());
    }

    @Test
    public void GetProjectByNameTest_WithNameTestProject1_ReturnTrue() throws SQLException {
        List<Project> projectByName = projectDaoImplementation.getProjectByName("TestProject2");
        Assert.assertEquals("TestProject2", projectByName.get(0).getName());

    }

    @Test
    public void GetAllProjectTest_ReturnTrue() throws SQLException {
        List<Project> allProject = projectDaoImplementation.getAllProject();
        Assert.assertTrue(allProject.size()>0);
    }
}