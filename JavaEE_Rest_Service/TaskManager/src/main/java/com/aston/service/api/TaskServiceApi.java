package com.aston.service.api;

import com.aston.util.dto.TaskDto;

import java.sql.SQLException;
import java.util.List;

public interface TaskServiceApi {
    int createTask(TaskDto taskDtoSave) throws SQLException;
    TaskDto getTaskById(int taskId) throws SQLException;
    List<TaskDto> getAllTasks() throws SQLException;
    List<TaskDto> getAllTasksByProject(int projectId) throws SQLException;
    int updateTask(TaskDto taskDtoUpdate) throws SQLException;
    int deleteTask(int taskId) throws SQLException;
}
