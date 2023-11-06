package com.aston.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bug", schema = "taskmanager")
public class Bug extends Activity{
    private String title;
    private String type;
}
