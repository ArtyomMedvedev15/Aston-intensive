package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserTaskDto {
    private UserDto user;
    private Set<TaskDto> tasks;
}
