package com.aston.dao.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Project;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
public class ProjectDaoImplementation implements ProjectDaoApi {

    private final static String INSERT_PROJECT_QUERY = "INSERT INTO taskmaneger.project(name,description)VALUES(?,?)";
    private final static String SELECT_PROJECT_BY_ID_QUERY = "SELECT * FROM taskmaneger.project WHERE id = ?";
    private final static String SELECT_PROJECT_BY_NAME_QUERY ="SELECT *FROM taskmaneger.project WHERE name LIKE ?";
    private final static String SELECT_ALL_PROJECT_QUERY = "SELECT * FROM taskmaneger.project";
    private final static String UPDATE_PROJECT_QUERY = "UPDATE taskmaneger.project SET name=?, description=? WHERE id=?";
    private final static String DELETE_PROJECT_QUERY = "DELETE FROM taskmaneger.project WHERE id = ?";

     private final ConnectionManager connectionManager;

    public ProjectDaoImplementation( ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public int createProject(Project project) throws SQLException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst = connection.prepareStatement(INSERT_PROJECT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, project.getName());
            pst.setString(2, project.getDescription());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                log.info("Create new project with id {} in {}",id,new Date());
                return id;
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }
    @Override
    public int updateProject(Project project) throws SQLException {
        int rowsUpdated = 0;
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst = connection.prepareStatement(UPDATE_PROJECT_QUERY)) {
            pst.setString(1, project.getName());
            pst.setString(2, project.getDescription());
            pst.setLong(3, project.getId());
            rowsUpdated = pst.executeUpdate();
            log.info("Update project with id {} in {}",rowsUpdated,new Date());
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        return rowsUpdated;
    }

    @Override
    public int deleteProject(int projectId) throws SQLException {
        int updated_rows;
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst = connection.prepareStatement(DELETE_PROJECT_QUERY)) {
            pst.setLong(1, projectId);
            updated_rows = pst.executeUpdate();
            log.info("Delete project with id {} in {}",projectId,new Date());
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
             throw ex;
        }
        return updated_rows;
    }
    @Override
    public Project getProjectById(int projectId) throws SQLException {
        Project dbProject = null;
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_PROJECT_BY_ID_QUERY)) {
            pst.setInt(1, projectId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbProject = parseProjectFromResultSet(rs);
                }
            }
            log.info("Get project with id {} in {}",projectId,new Date());
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
             throw ex;
        }
        return dbProject;
    }

    @Override
    public  List<Project> getProjectByName(String name) throws SQLException {
        Connection connection = connectionManager.getConnection();
        List<Project> projectByNameList = new ArrayList<>();
         try (PreparedStatement pst = connection.prepareStatement(SELECT_PROJECT_BY_NAME_QUERY)) {
            String pattern = "%"+name+"%";
            pst.setString(1,pattern);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    projectByNameList.add(parseProjectFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
             throw ex;
        }
        return projectByNameList;
    }

    @Override
    public List<Project> getAllProject() throws SQLException {
        List<Project> projectAllList = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_ALL_PROJECT_QUERY)) {
             try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    projectAllList.add(parseProjectFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
             throw ex;
        }
        return projectAllList;
    }

    private Project parseProjectFromResultSet(ResultSet rs) throws SQLException {
        Project projectMapper = Project.builder()
                .id(Integer.parseInt(rs.getString("id")))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .build();
        return projectMapper;
    }
}
