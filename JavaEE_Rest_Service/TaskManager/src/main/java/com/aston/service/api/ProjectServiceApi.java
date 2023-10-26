package com.aston.service.api;

import com.aston.entities.Project;
import com.aston.util.dto.ProjectDto;

import java.sql.SQLException;
import java.util.List;

public interface ProjectServiceApi {
    int createProject(ProjectDto projectDtoSave) throws SQLException;
    ProjectDto getProjectById(int projectId) throws SQLException;
    List<ProjectDto> getProjectByName(String name) throws SQLException;
    List<ProjectDto> getAllProject() throws SQLException;
    int updateProject(ProjectDto projectDtoUpdate) throws SQLException;
    int deleteProject(int projectId) throws SQLException;
}
