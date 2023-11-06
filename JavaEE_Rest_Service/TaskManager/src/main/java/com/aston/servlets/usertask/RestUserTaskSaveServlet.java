package com.aston.servlets.usertask;

import com.aston.service.api.UserTaskServiceApi;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.UserNotFoundException;
import com.aston.util.UserTaskAlreadyExistsException;
import com.aston.util.dto.UserTaskSaveDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/user/task/save")
public class RestUserTaskSaveServlet extends HttpServlet {

    private UserTaskServiceApi userTaskServiceApi;

    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("userTaskService");
        this.userTaskServiceApi = (UserTaskServiceApi) userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonPayload.toString());

        int userId = jsonNode.get("userId").asInt();
        int taskId = jsonNode.get("taskId").asInt();

        UserTaskSaveDto userTaskDto = UserTaskSaveDto.builder()
                .userId((long) userId)
                .taskId((long) taskId)
                .build();

        int userTaskId = 0;

        try {
            userTaskId = userTaskServiceApi.createUserTask(userTaskDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Save new user task with id %s in %s", userTaskId, new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new user task get error with server in %s", new Date()));
        } catch (UserNotFoundException | TaskNotFoundException | ProjectNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new user task get error %s in %s", e.getMessage(), new Date()));
        } catch (UserTaskAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new user task get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
