package com.aston.dao.implementation;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Task;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class TaskDaoImplementation implements TaskDaoApi {

    private static final String INSERT_TASK_QUERY = "INSERT INTO taskmaneger.task(title,description,deadline,status,projectId) VALUES(?,?,?,?,?)";
    private static final String SELECT_TASK_BY_ID_QUERY = "SELECT * FROM taskmaneger.task WHERE id = ?";
    private static final String SELECT_TASK_BY_PROJECT_ID_QUERY = "SELECT * FROM taskmaneger.task WHERE projectId=?";
    private static final String SELECT_ALL_TASK_QUERY = "SELECT * FROM taskmaneger.task";
    private static final String UPDATE_TASK_QUERY = "UPDATE taskmaneger.task SET title=?,description=?,deadline=?,status=?,projectId=? WHERE id=?";
    private static final String DELETE_TASK_QUERY = "DELETE FROM taskmaneger.task WHERE id=?";

    private final ConnectionManager connectionManager;
    public TaskDaoImplementation(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public int createTask(Task task) throws SQLException {

        try (Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(INSERT_TASK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, task.getDeadline());
            pst.setString(4, task.getStatus());


            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                log.info("Create new task with id {} in {}",id,new Date());
                return id;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public Task getTaskById(int taskId) throws SQLException {
        Task dbTask = null;

        try (Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(SELECT_TASK_BY_ID_QUERY)) {
            pst.setInt(1, taskId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbTask = parseTaskFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get task by id with id {} in {}",taskId,new Date());
        return dbTask;
    }

    @Override
    public List<Task> getAllTasks() throws SQLException {
        List<Task> allTaskList = new ArrayList<>();

        try ( Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(SELECT_ALL_TASK_QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    allTaskList.add(parseTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
             throw ex;
        }
        log.info("Get all task in {}",new Date());
        return allTaskList;
    }

    @Override
    public List<Task> getAllTasksByProject(int projectId) throws SQLException {

        List<Task> allTaskList = new ArrayList<>();
        try ( Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(SELECT_TASK_BY_PROJECT_ID_QUERY)) {
            pst.setInt(1, projectId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    allTaskList.add(parseTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get all task by project with id {} in {}",projectId,new Date());
        return allTaskList;
    }

    @Override
    public int updateTask(Task task) throws SQLException {

        int rowsUpdated = 0;
         try ( Connection connection = connectionManager.getConnection();
                 PreparedStatement pst = connection.prepareStatement(UPDATE_TASK_QUERY)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, task.getDeadline());
            pst.setString(4, task.getStatus());
             pst.setLong(6,task.getId());
            rowsUpdated = pst.executeUpdate();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
         log.info("Update task with id {} in {}",rowsUpdated,new Date());
        return rowsUpdated;
    }

    @Override
    public int deleteTask(int taskId) throws SQLException {

        int updated_rows;
         try ( Connection connection = connectionManager.getConnection();
                 PreparedStatement pst = connection.prepareStatement(DELETE_TASK_QUERY)) {
            pst.setLong(1, taskId);
            updated_rows = pst.executeUpdate();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
         log.info("Delete task with id {} in {}",taskId,new Date());
        return updated_rows;
    }

    private Task parseTaskFromResultSet(ResultSet rs) throws SQLException {
        Task taskMapper = new Task();
        taskMapper.setId((long) Integer.parseInt(rs.getString("id")));
        taskMapper.setTitle(rs.getString("title"));
        taskMapper.setDescription(rs.getString("description"));
        taskMapper.setDeadline(rs.getDate("deadline"));
        taskMapper.setStatus(rs.getString("status"));
         return taskMapper;
    }
}
