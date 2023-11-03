package com.aston.dao.api;

import com.aston.entities.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface TaskDaoApi {
     Long createTask(Task task) throws SQLException;
     Task getTaskById(Long taskId) throws SQLException;
     List<Task> getAllTasks() throws SQLException;
     Long updateTask(Task task) throws SQLException;
     Long deleteTask(Long taskId) throws SQLException;
}
