package com.aston.dao.api;

import com.aston.entities.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDaoApi {
     int createTask(Task task) throws SQLException;
     Task getTaskById(int taskId) throws SQLException;
     List<Task> getAllTasks() throws SQLException;
     List<Task> getAllTasksByProject(int projectId) throws SQLException;
     int updateTask(Task task) throws SQLException;
     int deleteTask(int taskId) throws SQLException;
}
