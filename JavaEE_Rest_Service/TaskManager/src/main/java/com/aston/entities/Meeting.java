package com.aston.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;

@Data

@Entity
@Table(name = "meeting", schema = "taskmanager")
public class Meeting extends Activity{
    private Date dateMeeting;
    private String locationMeeting;
}
