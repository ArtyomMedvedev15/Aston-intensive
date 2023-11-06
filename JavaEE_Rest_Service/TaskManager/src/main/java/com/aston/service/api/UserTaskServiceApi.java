package com.aston.service.api;

import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.UserNotFoundException;
import com.aston.util.UserTaskAlreadyExistsException;
import com.aston.util.dto.UserTaskDto;
import com.aston.util.dto.UserTaskSaveDto;

import java.sql.SQLException;
import java.util.List;

public interface UserTaskServiceApi {
    int createUserTask(UserTaskSaveDto userTaskDtoSave) throws SQLException, UserNotFoundException, ProjectNotFoundException, TaskNotFoundException, UserTaskAlreadyExistsException;
    UserTaskDto getAllUserTaskByUser(Long userid) throws SQLException, UserNotFoundException;
    Long deleteUserTask(Long userId,Long taskDeleteId) throws SQLException, UserNotFoundException, TaskNotFoundException;
}
