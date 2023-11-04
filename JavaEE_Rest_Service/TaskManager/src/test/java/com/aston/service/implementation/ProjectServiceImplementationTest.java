package com.aston.service.implementation;


import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.entities.Project;
import com.aston.util.ProjectInvalidParameterException;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.ProjectUpdateDto;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;


public class ProjectServiceImplementationTest {

    private static ProjectServiceImplementation projectServiceImplementation;
    private static SessionFactory sessionFactory;
    private static ProjectDaoApi projectDaoApi;

    @BeforeClass
    public static void init() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/taskmanagertest",
                        "postgres", "postgres")
                .schemas("taskmanager")
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        Configuration configuration = new Configuration();
        configuration.configure("hibernate-test.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        projectServiceImplementation = new ProjectServiceImplementation(projectDaoApi,sessionFactory);
    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Project ").executeUpdate();
        tx.commit();
        session.close();
    }

    @AfterClass
    public static void closeSession() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void CreateProjectTest_WithValidProject_ReturnTrue(){
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveResult = projectServiceImplementation.createProject(projectDtoSave);

        Assert.assertTrue(projectSaveResult>0);

     }

    @Test
    public void UpdateProjectTest_WithValid_ReturnTrue(){
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveResult = projectServiceImplementation.createProject(projectDtoSave);

        ProjectUpdateDto projectUpdateDto = ProjectUpdateDto.builder()
                .id(projectSaveResult)
                .name("Updated")
                .description("TestProject")
                .build();

        Long projectUpdateResult = projectServiceImplementation.updateProject(projectUpdateDto);

        Assert.assertTrue(projectUpdateResult>0);

     }

    @Test
    public void DeleteProjectTest_WithExistsProject_ReturnTrue() throws ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);
        Long projectDeleteResult = projectServiceImplementation.deleteProject(projectSaveId);
        Assert.assertTrue(projectDeleteResult>0);
    }

    @Test
    public void GetProjectByIdTest_WithExistsProject_ReturnTrue(){
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectById = projectServiceImplementation.getProjectById(projectSaveId);

        Assert.assertEquals("TestProject", projectById.getName());
     }

    @Test
    public void GetProjectByNameTest_WithValidName_ReturnTrue() {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestFind")
                .description("TestProject")
                .build();

        projectServiceImplementation.createProject(projectDtoSave);

        List<ProjectDto> projectByNameList = projectServiceImplementation.getProjectByName("TestFind");

        Assert.assertEquals("TestFind", projectByNameList.get(0).getName());
    }

    @Test
    public void GetAllProjectTest_ReturnTrue(){
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestFind")
                .description("TestProject")
                .build();

        projectServiceImplementation.createProject(projectDtoSave);

        List<ProjectDto> allProject = projectServiceImplementation.getAllProject();

        Assert.assertTrue(allProject.size()>0);
    }
}