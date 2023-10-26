package com.aston.dao.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserTaskDaoApi;
import com.aston.entities.UserTask;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserTaskImplementation implements UserTaskDaoApi {

    private static final String INSERT_USER_TASK_QUERY = "INSERT INTO usertask(userid,taskid) VALUES(?,?)";
    private static final String DELETE_USER_TASK_QUERY = "DELETE FROM usertask WHERE id = ?";
    private static final String SELECT_USER_TASK_BY_USER_QUERY = "SELECT * FROM usertask WHERE userid = ?";
    private static final String SELECT_ALL_USER_TASK_QUERY = "SELECT * FROM usertask";

    private final TransactionManager transactionManager;

    public UserTaskImplementation(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public int createUserTask(UserTask userTask) throws SQLException {
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(INSERT_USER_TASK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, userTask.getUserId());
            pst.setInt(2, userTask.getTaskId());
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
    public List<UserTask> getAllUserTaskByUser(int userid) throws SQLException {
        List<UserTask> userTaskList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_USER_TASK_BY_USER_QUERY)) {
            pst.setInt(1,userid);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    userTaskList.add(parseUserTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return userTaskList;
    }

    @Override
    public List<UserTask> getAllUsersTask() throws SQLException {
        List<UserTask> userTaskList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_ALL_USER_TASK_QUERY)) {
             try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    userTaskList.add(parseUserTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return userTaskList;
    }

    @Override
    public int deleteUserTask(int id) throws SQLException {
        int updated_rows;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(DELETE_USER_TASK_QUERY)) {
            pst.setLong(1, id);
            updated_rows = pst.executeUpdate();
            transactionManager.commitSession();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return updated_rows;
    }

    private UserTask parseUserTaskFromResultSet(ResultSet rs) throws SQLException {
        UserTask userTaskMapper = UserTask.builder().build();
        userTaskMapper.setId(Integer.parseInt(rs.getString("id")));
        userTaskMapper.setUserId(rs.getInt("userid"));
        userTaskMapper.setTaskId(rs.getInt("taskid"));
        return userTaskMapper;
    }
}
