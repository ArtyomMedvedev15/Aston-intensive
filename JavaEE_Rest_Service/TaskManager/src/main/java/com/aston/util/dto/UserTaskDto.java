package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTaskDto {
    private int id;
    private int userId;
    private int taskId;
}
