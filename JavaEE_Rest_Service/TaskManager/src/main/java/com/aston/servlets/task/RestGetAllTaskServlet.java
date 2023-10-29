package com.aston.servlets.task;

import com.aston.service.api.TaskServiceApi;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/task/all")
public class RestGetAllTaskServlet extends HttpServlet {
    private TaskServiceApi taskServiceApi;
    @Override
    public void init(){
        final Object taskService = getServletContext().getAttribute("taskService");
        this.taskServiceApi = (TaskServiceImplementation) taskService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TaskDto> allTask = null;
        try {
            allTask = taskServiceApi.getAllTasks();
            log.info("Get all task with size list - [{}]",allTask.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String taskListJson = objectMapper.writeValueAsString(allTask);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        log.info("Return all task with json {} with status 200",taskListJson);
        resp.getWriter().write(taskListJson);
    }
}
