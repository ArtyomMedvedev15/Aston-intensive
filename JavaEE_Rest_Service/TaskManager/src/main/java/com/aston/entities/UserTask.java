package com.aston.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTask {
    private int id;
    private int userId;
    private int taskId;
}
