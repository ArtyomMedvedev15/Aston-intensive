package com.aston.service.api;

import com.aston.entities.Meeting;

public interface MeetingServiceApi {
    Long createMeeting(Meeting meeting);
    Long deleteMeeting(Meeting meeting);
}
