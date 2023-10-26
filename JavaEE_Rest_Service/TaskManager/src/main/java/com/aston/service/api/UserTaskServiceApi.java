package com.aston.service.api;

import com.aston.util.dto.UserTaskDto;
import com.aston.util.dto.UserTaskFullDto;

import java.sql.SQLException;
import java.util.List;

public interface UserTaskServiceApi {
    int createUserTask(UserTaskDto userTaskDtoSave) throws SQLException;
    List<UserTaskFullDto> getAllUserTaskByUser(int userid) throws SQLException;
    List<UserTaskFullDto>getAllUsersTask() throws SQLException;
    int deleteUserTask(int id) throws SQLException;
}
