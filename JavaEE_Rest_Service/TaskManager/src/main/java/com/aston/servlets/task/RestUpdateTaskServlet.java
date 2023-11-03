package com.aston.servlets.task;

import com.aston.service.api.TaskServiceApi;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.util.TaskInvalidParameterException;
import com.aston.util.dto.TaskDto;
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
@WebServlet(urlPatterns = "/api/v1/task/update")
public class RestUpdateTaskServlet extends HttpServlet {
    private TaskServiceApi taskServiceApi;
    @Override
    public void init(){
        final Object taskService = getServletContext().getAttribute("taskService");
        this.taskServiceApi = (TaskServiceImplementation) taskService;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonPayload.toString());

        Long id = jsonNode.get("id").asLong();
        String title = jsonNode.get("title").asText();
        String description = jsonNode.get("description").asText();
        String deadline = jsonNode.get("deadline").asText();
        String status = jsonNode.get("status").asText();
        int projectId = jsonNode.get("projectid").asInt();

        TaskDto taskDtoSave = TaskDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .deadline(java.sql.Date.valueOf(deadline))
                .status(status)
                .projectId(projectId)
                .build();
        Long taskId;
        try {
            taskId = taskServiceApi.updateTask(taskDtoSave);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Update task with id %s in %s", taskId, new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new task get error with server in %s", new Date()));
        } catch (TaskInvalidParameterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new task get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
