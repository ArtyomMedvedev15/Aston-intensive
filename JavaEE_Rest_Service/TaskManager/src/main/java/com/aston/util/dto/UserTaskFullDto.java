package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTaskFullDto {
    private int id;
    private UserDto userId;
    private TaskDto taskId;
}
