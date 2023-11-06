package com.aston.service.api;

import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskInvalidParameterException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.dto.TaskDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface TaskServiceApi {
    Long createTask(TaskDto taskDtoSave) throws SQLException, TaskInvalidParameterException, ProjectNotFoundException;
    TaskDto getTaskById(Long taskId) throws SQLException, TaskNotFoundException, ProjectNotFoundException;
    List<TaskDto> getAllTasks() throws SQLException;
    Long updateTask(TaskDto taskDtoUpdate) throws SQLException, TaskInvalidParameterException;
    Long deleteTask(Long taskId) throws SQLException, TaskNotFoundException;
    Set<TaskDto> getAllTasksByProject(Long projectId) throws ProjectNotFoundException;

}
