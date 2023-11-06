package com.aston.service.implementation;

import com.aston.dao.implementation.ActivityDaoImplementation;
import com.aston.dao.implementation.BugDaoImplementation;
import com.aston.dao.implementation.MeetingDaoImplemntation;
import com.aston.entities.Activity;
import com.aston.entities.Bug;
import com.aston.entities.Meeting;
import org.checkerframework.checker.units.qual.A;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ActivityServiceImplementationTest {

    private static ActivityServiceImplementation activityServiceImplementation;
    private static BugDaoImplementation bugDaoImplementation;
    private static ActivityDaoImplementation activityDaoImplementation;

    private static MeetingDaoImplemntation meetingDaoImplemntation;
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
        activityDaoImplementation = new ActivityDaoImplementation(sessionFactory);
        activityServiceImplementation = new ActivityServiceImplementation(sessionFactory,activityDaoImplementation);
        bugDaoImplementation = new BugDaoImplementation(sessionFactory);
        meetingDaoImplemntation = new MeetingDaoImplemntation(sessionFactory);
    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Activity").executeUpdate();
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
    public void GetAllActivityTest_ReturnTrue() {
        Bug bug = new Bug();
        bug.setTitle("Test");
        bug.setType("Test");
        bugDaoImplementation.createBug(bug);

        Meeting meeting = new Meeting();
        meeting.setDateMeeting(new Date(new java.util.Date().getTime()));
        meeting.setLocationMeeting("Test");
        meetingDaoImplemntation.createMeeting(meeting);

        List<Activity> activityList = activityServiceImplementation.getAllActivity();

        Assert.assertTrue(activityList.size()>0);
    }
}