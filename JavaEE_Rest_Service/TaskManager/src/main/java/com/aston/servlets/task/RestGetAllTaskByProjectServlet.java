package com.aston.servlets.task;

import com.aston.service.api.TaskServiceApi;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.dto.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/task/by/projectid/")
public class RestGetAllTaskByProjectServlet extends HttpServlet {
    private TaskServiceApi taskServiceApi;
    @Override
    public void init(){
        final Object taskService = getServletContext().getAttribute("taskService");
        this.taskServiceApi = (TaskServiceImplementation) taskService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("idProject");
        Set<TaskDto> allTaskByProject = null;
        try {
            allTaskByProject = taskServiceApi.getAllTasksByProject(Long.valueOf(projectId));
            log.info("Get all task with size list - [{}]",allTaskByProject.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        } catch (ProjectNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id project get error %s in %s", e.getMessage(), new Date()));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String taskListByProjectJson = objectMapper.writeValueAsString(allTaskByProject);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        log.info("Return all task by project with json {} with status 200",taskListByProjectJson);
        resp.getWriter().write(taskListByProjectJson);
    }
}
