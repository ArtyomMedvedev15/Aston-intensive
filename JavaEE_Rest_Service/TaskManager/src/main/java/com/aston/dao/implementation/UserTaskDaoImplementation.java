package com.aston.dao.implementation;

import com.aston.dao.api.UserTaskDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.UserTask;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class UserTaskDaoImplementation implements UserTaskDaoApi {

    private static final String INSERT_USER_TASK_QUERY = "INSERT INTO taskmaneger.usertask(userid,taskid) VALUES(?,?)";
    private static final String DELETE_USER_TASK_QUERY = "DELETE FROM taskmaneger.usertask WHERE id = ?";
    private static final String SELECT_USER_TASK_BY_USER_QUERY = "SELECT * FROM taskmaneger.usertask WHERE userid = ?";
    private static final String SELECT_ALL_USER_TASK_QUERY = "SELECT * FROM taskmaneger.usertask";

    private final ConnectionManager connectionManager;


    public UserTaskDaoImplementation(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;
    }

    @Override
    public int createUserTask(UserTask userTask) throws SQLException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement pst = connection.prepareStatement(INSERT_USER_TASK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, userTask.getUserId());
            pst.setInt(2, userTask.getTaskId());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                log.info("Save new user task with id {} in {}",id,new Date());
                return id;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<UserTask> getAllUserTaskByUser(int userid) throws SQLException {
        Connection connection = connectionManager.getConnection();
        List<UserTask> userTaskList = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_USER_TASK_BY_USER_QUERY)) {
            pst.setInt(1,userid);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    userTaskList.add(parseUserTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get all user task with user id {} in {}",userid, new Date());
        return userTaskList;
    }

    @Override
    public List<UserTask> getAllUsersTask() throws SQLException {
        Connection connection = connectionManager.getConnection();
        List<UserTask> userTaskList = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_ALL_USER_TASK_QUERY)) {
             try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    userTaskList.add(parseUserTaskFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get all user task in {}",new Date());
        return userTaskList;
    }

    @Override
    public int deleteUserTask(int id) throws SQLException {
        Connection connection = connectionManager.getConnection();
        int updated_rows;
        try (PreparedStatement pst = connection.prepareStatement(DELETE_USER_TASK_QUERY)) {
            pst.setLong(1, id);
            updated_rows = pst.executeUpdate();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Delete user task with id {} in {}",id,new Date());
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
