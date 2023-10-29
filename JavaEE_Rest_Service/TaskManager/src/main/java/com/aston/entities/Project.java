package com.aston.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {
    private int id;
    private String name;
    private String description;
}
