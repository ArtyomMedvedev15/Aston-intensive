package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectUpdateDto {
    private Long id;
    private String name;
    private String description;

}
