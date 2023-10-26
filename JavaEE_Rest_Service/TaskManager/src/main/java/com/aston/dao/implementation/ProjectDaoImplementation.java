package com.aston.dao.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.entities.Project;
import com.aston.entities.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ProjectDaoImplementation implements ProjectDaoApi {

    private final static String INSERT_PROJECT_QUERY = "INSERT INTO project(name,description)VALUES(?,?)";
    private final static String SELECT_PROJECT_BY_ID_QUERY = "SELECT * FROM project WHERE id = ?";
    private final static String SELECT_PROJECT_BY_NAME_QUERY ="SELECT *FROM project WHERE name LIKE ?";
    private final static String SELECT_ALL_PROJECT_QUERY = "SELECT * FROM project";
    private final static String UPDATE_PROJECT_QUERY = "UPDATE project SET name=?, description=? WHERE id=?";
    private final static String DELETE_PROJECT_QUERY = "DELETE FROM project WHERE id = ?";

    private final TransactionManager transactionManager;

    public ProjectDaoImplementation(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Override
    public int createProject(Project project) throws SQLException {
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(INSERT_PROJECT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, project.getName());
            pst.setString(2, project.getDescription());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                transactionManager.commitSession();
                return id;
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }finally {
            transactionManager.close();
        }
    }
    @Override
    public int updateProject(Project project) throws SQLException {
        int rowsUpdated = 0;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(UPDATE_PROJECT_QUERY)) {
            pst.setString(1, project.getName());
            pst.setString(2, project.getDescription());
            pst.setLong(3, project.getId());
            rowsUpdated = pst.executeUpdate();
            transactionManager.commitSession();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return rowsUpdated;
    }

    @Override
    public int deleteProject(int projectId) throws SQLException {
        int updated_rows;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(DELETE_PROJECT_QUERY)) {
            pst.setLong(1, projectId);
            updated_rows = pst.executeUpdate();
            transactionManager.commitSession();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }finally {
            transactionManager.close();
        }
        return updated_rows;
    }
    @Override
    public Project getProjectById(int projectId) throws SQLException {
        Project dbProject = null;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_PROJECT_BY_ID_QUERY)) {
            pst.setInt(1, projectId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbProject = parseProjectFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }finally {
            transactionManager.close();
        }
        return dbProject;
    }

    @Override
    public  List<Project> getProjectByName(String name) throws SQLException {
        List<Project> projectByNameList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_PROJECT_BY_NAME_QUERY)) {
            String pattern = "%"+name+"%";
            pst.setString(1,pattern);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    projectByNameList.add(parseProjectFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return projectByNameList;
    }

    @Override
    public List<Project> getAllProject() throws SQLException {
        List<Project> projectAllList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_ALL_PROJECT_QUERY)) {
             try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    projectAllList.add(parseProjectFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return projectAllList;
    }



    private Project parseProjectFromResultSet(ResultSet rs) throws SQLException {
        Project projectMapper = Project.builder().build();
        projectMapper.setId(Integer.parseInt(rs.getString("id")));
        projectMapper.setName(rs.getString("name"));
        projectMapper.setDescription(rs.getString("description"));
        return projectMapper;
    }
}
