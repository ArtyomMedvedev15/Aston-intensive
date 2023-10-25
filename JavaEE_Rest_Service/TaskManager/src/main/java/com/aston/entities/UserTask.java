package com.aston.entities;

import lombok.Data;

@Data
public class UserTask {
    private int id;
    private int userId;
    private int taskId;
}
