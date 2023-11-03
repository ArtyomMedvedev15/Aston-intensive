package com.aston.servlets.task;

import com.aston.service.api.TaskServiceApi;
import com.aston.service.api.UserServiceApi;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.service.implementation.UserServiceImplementation;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/task/by/id/")
public class RestGetTaskByIdServlet extends HttpServlet {
    private TaskServiceApi taskServiceApi;
    @Override
    public void init(){
        final Object taskService = getServletContext().getAttribute("taskService");
        this.taskServiceApi = (TaskServiceImplementation) taskService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String taskId = req.getParameter("idTask");
        try {
            TaskDto taskById = taskServiceApi.getTaskById(Long.valueOf(taskId));
             ObjectMapper objectMapper = new ObjectMapper();
            String taskByIdJson = objectMapper.writeValueAsString(taskById);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            log.info("Get task by id with id {}",taskId);
            resp.getWriter().write(taskByIdJson);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id task server error %s in %s", e.getMessage(), new Date()));
        } catch (TaskNotFoundException | ProjectNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id task get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
