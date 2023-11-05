package com.aston.dao.implementation;

import com.aston.entities.Project;
import com.aston.entities.Task;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public class ProjectDaoImplementationTest {

    private static ProjectDaoImplementation projectDaoImplementation;
    private static SessionFactory sessionFactory;
     @BeforeClass
    public static void init(){
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
        projectDaoImplementation = new ProjectDaoImplementation(sessionFactory);
    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Project").executeUpdate();
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
    public void CreateProjectTest_ReturnTrue(){
        Project projectSave = new Project();
                projectSave.setName("TestProject");
                projectSave.setDescription("TestProject");


        Long projectSaveResult = projectDaoImplementation.createProject(projectSave);

        Assert.assertTrue(projectSaveResult>0);
    }

    @Test
    public void UpdateProjectTest_ReturnTrue(){

        Project projectUpdate = new Project();
        projectUpdate.setId(777L);
        projectUpdate.setName("Updated");
        projectUpdate.setDescription("Update");

        Long projectUpdateResult = projectDaoImplementation.updateProject(projectUpdate);

        Assert.assertTrue(projectUpdateResult>0);
    }

    @Test
    public void DeleteProjectTest_ReturnTrue(){
        Project projectForDelete = new Project();
        projectForDelete.setName("TestProject");
        projectForDelete.setDescription("TestProject");

        Long projectDelete = projectDaoImplementation.createProject(projectForDelete);

        Long projectDeleteResult = projectDaoImplementation.deleteProject(projectDelete);

        Assert.assertTrue(projectDeleteResult>0);

    }

    @Test
    public void GetProjectByIdTest_WithId777_ReturnTrue(){
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");


        Long projectSaveId = projectDaoImplementation.createProject(projectSave);

        Project projectById = projectDaoImplementation.getProjectById(projectSaveId);

        Assert.assertEquals("TestProject", projectById.getName());
    }

    @Test
    public void GetProjectByNameTest_WithNameTestProject1_ReturnTrue(){
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");
        projectDaoImplementation.createProject(projectSave);

        List<Project> projectByName = projectDaoImplementation.getProjectByName("TestProject");
        Assert.assertEquals("TestProject", projectByName.get(0).getName());

    }

    @Test
    public void GetAllProjectTest_ReturnTrue(){
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");

        projectDaoImplementation.createProject(projectSave);

        List<Project> allProject = projectDaoImplementation.getAllProject();
        Assert.assertTrue(allProject.size()>0);
    }

}