package com.aston.service.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserTaskDaoApi;
import com.aston.entities.User;
import com.aston.entities.UserTask;
import com.aston.service.api.TaskServiceApi;
import com.aston.service.api.UserServiceApi;
import com.aston.service.api.UserTaskServiceApi;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.UserDto;
import com.aston.util.dto.UserTaskDto;
import com.aston.util.dto.UserTaskFullDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserTaskServiceImplementation implements UserTaskServiceApi {

    private final UserTaskDaoApi userTaskDao;
    private final UserServiceApi userServiceApi;
    private final TaskServiceApi taskServiceApi;
    private final TransactionManager transactionManager;

    public UserTaskServiceImplementation(UserTaskDaoApi userTaskDao, UserServiceApi userServiceApi, TaskServiceApi taskServiceApi, TransactionManager transactionManager) {
        this.userTaskDao = userTaskDao;
        this.userServiceApi = userServiceApi;
        this.taskServiceApi = taskServiceApi;
        this.transactionManager = transactionManager;
    }


    @Override
    public int createUserTask(UserTaskDto userTaskDtoSave) throws SQLException {
        UserTask userTaskEntity = fromDto(userTaskDtoSave);
        int userTaskId = 0;
        try {
            userTaskId = userTaskDao.createUserTask(userTaskEntity);
        } catch (SQLException e) {
            log.error("Cannot save user task get exception {}", e.getMessage());
            throw e;
        }
        return userTaskId;
    }

    @Override
    public List<UserTaskFullDto> getAllUserTaskByUser(int userid) throws SQLException {
        List<UserTaskFullDto> userTaskByUserList = new ArrayList<>();
        try {
            userTaskByUserList = userTaskDao.getAllUserTaskByUser(userid).stream().map(this::fromEntity)
                    .collect(Collectors.toList());
            setUserAndTaskByUserId(userid, userTaskByUserList);
        } catch (SQLException e) {
            log.error("Cannot get all user task by user with with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return userTaskByUserList;
    }



    @Override
    public List<UserTaskFullDto> getAllUsersTask() throws SQLException {
        List<UserTaskFullDto> userTaskByUserList = new ArrayList<>();
        try {
            userTaskByUserList = userTaskDao.getAllUsersTask().stream().map(this::fromEntity)
                    .collect(Collectors.toList());
            userTaskByUserList.forEach(o1->{
                try {
                    o1.setTaskId(taskServiceApi.getTaskById(Math.toIntExact(o1.getTaskId().getId())));
                    o1.setUserId(userServiceApi.getUserById(Math.toIntExact(o1.getUserId().getId())));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            log.error("Cannot get all user task by user with with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return userTaskByUserList;
    }

    @Override
    public int deleteUserTask(int id) throws SQLException {
        try {
            id = userTaskDao.deleteUserTask(id);
        } catch (SQLException e) {
            log.error("Cannot delete user task get exception {}", e.getMessage());
            throw e;
        }
        return id;
    }

    private UserTask fromDto(UserTaskDto userTaskDto) {
        UserTask userTask = UserTask.builder()
                .userId(userTaskDto.getUserId())
                .taskId(userTaskDto.getTaskId())
                .build();
        return userTask;
    }

    private void setUserAndTaskByUserId(int userid, List<UserTaskFullDto> userTaskByUserList) {
        userTaskByUserList.forEach(o1->{
            try {
                o1.setUserId(userServiceApi.getUserById(userid));
                o1.setTaskId(taskServiceApi.getTaskById(Math.toIntExact(o1.getTaskId().getId())));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    private UserTaskFullDto fromEntity(UserTask userTaskEntity) {
        UserTaskFullDto userTaskFullDto = UserTaskFullDto.builder()
                .taskId(TaskDto.builder().id((long) userTaskEntity.getTaskId()).build())
                .userId(UserDto.builder().id((long) userTaskEntity.getUserId()).build())
                .build();

        return userTaskFullDto;
    }
}
