package com.aston.dao.api;

import com.aston.entities.Project;
import com.aston.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDaoApi {
    Long createProject(Project project);
    Project getProjectById(Long projectId);
    List<Project> getProjectByName(String name);
    List<Project> getAllProject();
    Long updateProject(Project project);
    Long deleteProject(Long projectId);
}
