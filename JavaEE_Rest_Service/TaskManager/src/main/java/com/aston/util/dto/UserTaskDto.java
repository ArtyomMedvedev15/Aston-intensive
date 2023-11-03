package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTaskDto {
    private Long id;
    private Long userId;
    private Long taskId;
}
