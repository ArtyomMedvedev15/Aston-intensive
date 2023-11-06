package com.aston.dao.api;

import com.aston.entities.Task;
import com.aston.entities.User;
import com.aston.entities.UserTask;

import java.util.List;

public interface UserTaskDaoApi {
    Long createUserTask(UserTask UserTask);
    List<UserTask> getAllUsersTaskByUser(Long userId);
    Long deleteUserTask(UserTask UserTask);
    UserTask getUserTaskByUserAndTask(User user, Task task);
}
