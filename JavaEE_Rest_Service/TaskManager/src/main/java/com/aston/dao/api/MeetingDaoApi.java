package com.aston.dao.api;

import com.aston.entities.Meeting;

public interface MeetingDaoApi {
    Long createMeeting(Meeting meeting);
    Long deleteMeeting(Meeting meeting);
}
