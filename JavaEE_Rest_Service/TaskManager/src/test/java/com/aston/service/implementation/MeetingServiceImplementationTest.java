package com.aston.service.implementation;

import com.aston.dao.api.MeetingDaoApi;
import com.aston.dao.implementation.BugDaoImplementation;
import com.aston.dao.implementation.MeetingDaoImplemntation;
import com.aston.entities.Meeting;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.Date;

import static org.junit.Assert.*;

public class MeetingServiceImplementationTest {
    private static MeetingServiceImplementation meetingServiceImplementation;
    private static MeetingDaoApi meetingDaoApi;

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

        meetingDaoApi = new MeetingDaoImplemntation(sessionFactory);
        meetingServiceImplementation = new MeetingServiceImplementation(meetingDaoApi,sessionFactory);
    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Meeting").executeUpdate();
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
    public void CreateMeetingTest_ReturnTrue() {
        Meeting meeting = new Meeting();
        meeting.setDateMeeting(new Date(new java.util.Date().getTime()));
        meeting.setLocationMeeting("Test");
        Long meetingSaveResult = meetingServiceImplementation.createMeeting(meeting);

        Assert.assertTrue(meetingSaveResult>0);
    }

    @Test
    public void DeleteMeetingTest_ReturnTrue() {
        Meeting meeting = new Meeting();
        meeting.setDateMeeting(new Date(new java.util.Date().getTime()));
        meeting.setLocationMeeting("Test");
        meetingServiceImplementation.createMeeting(meeting);
        Long meetingDeleteResult = meetingServiceImplementation.deleteMeeting(meeting);
        Assert.assertTrue(meetingDeleteResult>0);
    }
}