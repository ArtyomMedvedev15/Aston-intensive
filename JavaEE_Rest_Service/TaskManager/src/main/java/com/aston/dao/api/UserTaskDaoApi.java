package com.aston.dao.api;

import com.aston.entities.UserTask;

import java.sql.SQLException;
import java.util.List;

public interface UserTaskDaoApi {
    int createUserTask(UserTask userTask) throws SQLException;
    List<UserTask>getAllUserTaskByUser(int userid) throws SQLException;
    List<UserTask>getAllUsersTask() throws SQLException;
    int deleteUserTask(int id) throws SQLException;
}
