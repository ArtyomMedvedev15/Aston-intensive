package com.aston.dao.api;

import com.aston.entities.Project;
import com.aston.entities.User;
import com.aston.util.TransactionManagerException;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDaoApi {
    int createProject(Project project) throws SQLException;
    Project getProjectById(int projectId) throws SQLException;
    List<Project> getProjectByName(String name) throws SQLException;
    List<Project> getAllProject() throws SQLException;
    int updateProject(Project project) throws SQLException;
    int deleteProject(int projectId) throws SQLException;
}
