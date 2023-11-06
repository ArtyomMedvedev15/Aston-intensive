package com.aston.service.implementation;

import com.aston.dao.api.MeetingDaoApi;
import com.aston.entities.Meeting;
import com.aston.service.api.MeetingServiceApi;
import com.aston.util.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;


@Slf4j
public class MeetingServiceImplementation implements MeetingServiceApi {

    private final MeetingDaoApi meetingDaoApi;
    private final SessionFactory sessionFactory;

    public MeetingServiceImplementation(MeetingDaoApi meetingDaoApi, SessionFactory sessionFactory) {
        this.meetingDaoApi = meetingDaoApi;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long createMeeting(Meeting meeting) {
        Long meetingId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                meetingId = meetingDaoApi.createMeeting(meeting);
                transaction.commit();
                log.info("Save new meeting with id {} in {}", meetingId, new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return meetingId;
    }

    @Override
    public Long deleteMeeting(Meeting meeting) {
        Long meetingId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                meetingId = meetingDaoApi.deleteMeeting(meeting);
                transaction.commit();
                log.info("Delete meeting with id {} in {}", meetingId, new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return meetingId;    }
}
