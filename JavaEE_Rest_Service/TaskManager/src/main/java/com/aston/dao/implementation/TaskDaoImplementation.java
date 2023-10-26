package com.aston.dao.implementation;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.entities.Task;
import com.aston.entities.User;
import com.aston.util.TransactionManagerException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TaskDaoImplementation implements TaskDaoApi {

    private static final String INSERT_TASK_QUERY = "INSERT INTO task(title,description,deadline,status,projectId) VALUES(?,?,?,?,?)";
    private static final String SELECT_TASK_BY_ID_QUERY = "SELECT * FROM task WHERE id = ?";
    private static final String SELECT_TASK_BY_PROJECT_ID_QUERY = "SELECT * FROM Task WHERE projectId=?";
    private static final String SELECT_ALL_TASK_QUERY = "SELECT * FROM task";
    private static final String UPDATE_TASK_QUERY = "UPDATE task SET title=?,description=?,deadline=?,status=?,projectId=? WHERE id=?";
    private static final String DELETE_TASK_QUERY = "DELETE FROM task WHERE id=?";

    private final TransactionManager transactionManager;

    public TaskDaoImplementation(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Override
    public int createTask(Task task) throws SQLException {
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(INSERT_TASK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, task.getDeadline());
            pst.setString(4, task.getStatus());
            pst.setLong(5, task.getProjectId());

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
        }
    }

    @Override
    public Task getTaskById(int taskId) throws SQLException {
        Task dbTask = null;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_TASK_BY_ID_QUERY)) {
            pst.setInt(1, taskId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbTask = parseTaskFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return dbTask;
    }

    @Override
    public List<Task> getAllTasks() throws SQLException {
        List<Task> allTaskList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_ALL_TASK_QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    allTaskList.add(parseTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return allTaskList;
    }

    @Override
    public List<Task> getAllTasksByProject(int projectId) throws SQLException {
        List<Task> allTaskList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_TASK_BY_PROJECT_ID_QUERY)) {
            pst.setInt(1, projectId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    allTaskList.add(parseTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return allTaskList;
    }

    @Override
    public int updateTask(Task task) throws SQLException {
        int rowsUpdated = 0;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(UPDATE_TASK_QUERY)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, task.getDeadline());
            pst.setString(4, task.getStatus());
            pst.setLong(5, task.getProjectId());
            pst.setLong(6,task.getId());
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
    public int deleteTask(int taskId) throws SQLException {
        int updated_rows;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(DELETE_TASK_QUERY)) {
            pst.setLong(1, taskId);
            updated_rows = pst.executeUpdate();
            transactionManager.commitSession();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return updated_rows;
    }

    private Task parseTaskFromResultSet(ResultSet rs) throws SQLException {
        Task taskMapper = Task.builder().build();
        taskMapper.setId((long) Integer.parseInt(rs.getString("id")));
        taskMapper.setTitle(rs.getString("title"));
        taskMapper.setDescription(rs.getString("description"));
        taskMapper.setDeadline(rs.getDate("deadline"));
        taskMapper.setStatus(rs.getString("status"));
        taskMapper.setProjectId(rs.getInt("projectId"));
        return taskMapper;
    }
}
