package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserTaskSaveDto {
    private Long userId;
    private Long taskId;
}
