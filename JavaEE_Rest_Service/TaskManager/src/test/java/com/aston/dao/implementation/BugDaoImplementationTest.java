package com.aston.dao.implementation;

import com.aston.entities.Bug;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import static org.junit.Assert.*;

public class BugDaoImplementationTest {

     private static BugDaoImplementation bugDaoImplementation;
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
        bugDaoImplementation = new BugDaoImplementation(sessionFactory);
     }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Bug").executeUpdate();
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
    public void CreateBugTest_ReturnTrue() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setType("Test");
        Long bugSaveResult = bugDaoImplementation.createBug(bug);

        Assert.assertTrue(bugSaveResult>0);
    }

    @Test
    public void DeleteBugTest_ReturnTrue() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setType("Test");
        bugDaoImplementation.createBug(bug);
        Long bugDeleteResult = bugDaoImplementation.deleteBug(bug);
        Assert.assertTrue(bugDeleteResult>0);
    }
}