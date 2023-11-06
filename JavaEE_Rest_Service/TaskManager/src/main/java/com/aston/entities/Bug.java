package com.aston.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bug", schema = "taskmanager")
@Data
public class Bug extends Activity{
    private String title;
    private String type;
}
