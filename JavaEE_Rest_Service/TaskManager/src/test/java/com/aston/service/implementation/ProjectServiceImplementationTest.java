package com.aston.service.implementation;


import com.aston.dao.api.*;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.dao.implementation.TaskDaoImplementation;
import com.aston.util.ConnectionPoolException;
import com.aston.util.ProjectInvalidParameterException;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.dto.ProjectDto;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;


public class ProjectServiceImplementationTest {

    private static ProjectServiceImplementation projectServiceImplementation;
    private static ConnectionPool connectionPool;
    private static SessionFactory sessionFactory;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        ProjectDaoApi projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        TaskDaoApi taskDaoApi = new TaskDaoImplementation(sessionFactory);
        UserTaskDaoApi userTaskDaoApi = null;

        projectServiceImplementation = new ProjectServiceImplementation(projectDaoApi,connectionManager,taskDaoApi,userTaskDaoApi);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    @Test
    public void CreateProjectTest_WithValidProject_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveResult = projectServiceImplementation.createProject(projectDtoSave);

        Assert.assertTrue(projectSaveResult>0);

        projectServiceImplementation.deleteProject(projectSaveResult);
    }

    @Test
    public void UpdateProjectTest_WithValid_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectDtoUpdate = ProjectDto.builder()
                .id(projectSaveId)
                .name("Update")
                .description("TestProject")
                .build();

        Long projectUpdateResult = projectServiceImplementation.updateProject(projectDtoUpdate);

        Assert.assertTrue(projectUpdateResult>0);

        projectServiceImplementation.deleteProject(projectUpdateResult);
    }

    @Test
    public void DeleteProjectTest_WithExistsProject_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);
        Long projectDeleteResult = projectServiceImplementation.deleteProject(projectSaveId);
        Assert.assertFalse(projectDeleteResult>0);
    }

    @Test
    public void GetProjectByIdTest_WithExistsProject_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectById = projectServiceImplementation.getProjectById(projectSaveId);

        Assert.assertEquals("TestProject", projectById.getName());
        projectServiceImplementation.deleteProject(projectSaveId);
    }

    @Test
    public void GetProjectByNameTest_WithValidName_ReturnTrue() throws SQLException, ProjectInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestFind")
                .description("TestProject")
                .build();

        projectServiceImplementation.createProject(projectDtoSave);

        List<ProjectDto> projectByNameList = projectServiceImplementation.getProjectByName("TestFind");

        Assert.assertEquals("TestFind", projectByNameList.get(0).getName());
    }

    @Test
    public void GetAllProjectTest_ReturnTrue() throws SQLException, ProjectInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestFind")
                .description("TestProject")
                .build();

        projectServiceImplementation.createProject(projectDtoSave);

        List<ProjectDto> allProject = projectServiceImplementation.getAllProject();

        Assert.assertTrue(allProject.size()>0);
    }
}