package com.aston.service.api;

import com.aston.entities.Project;
import com.aston.util.ProjectInvalidParameterException;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.dto.ProjectDto;

import java.sql.SQLException;
import java.util.List;

public interface ProjectServiceApi {
    Long createProject(ProjectDto projectDtoSave) throws SQLException, ProjectInvalidParameterException;
    ProjectDto getProjectById(Long projectId) throws SQLException, ProjectNotFoundException;
    List<ProjectDto> getProjectByName(String name) throws SQLException;
    List<ProjectDto> getAllProject() throws SQLException;
    Long updateProject(ProjectDto projectDtoUpdate) throws SQLException, ProjectInvalidParameterException;
    Long deleteProject(Long projectId) throws SQLException, ProjectNotFoundException;
}
