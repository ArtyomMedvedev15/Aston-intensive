package com.aston.service.implementation;

import com.aston.dao.implementation.BugDaoImplementation;
import com.aston.entities.Bug;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

public class BugServiceImplementationTest {
    private static BugServiceImplementation bugServiceImplementation;
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
        bugServiceImplementation = new BugServiceImplementation(bugDaoImplementation,sessionFactory);
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
    public void DeleteBugTest_ReturnTrue() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setType("Test");
        bugServiceImplementation.createBug(bug);
        Long bugDeleteResult = bugServiceImplementation.deleteBug(bug);
        Assert.assertTrue(bugDeleteResult>0);
    }

    @Test
    public void CreateBugTest_ReturnTrue() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setType("Test");
        Long bugSaveResult = bugServiceImplementation.createBug(bug);

        Assert.assertTrue(bugSaveResult>0);
    }
}