package com.aston.servlets.project;

import com.aston.service.api.ProjectServiceApi;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

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
@WebServlet(urlPatterns = "/api/v1/project/save")
public class RestProjectCreateServlet extends HttpServlet {
    private ProjectServiceApi projectServiceApi;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("projectService");
        this.projectServiceApi = (ProjectServiceApi) userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonPayload.toString());

        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();

        ProjectDto projectDtoSave = ProjectDto.builder()
                .name(name)
                .description(description)
                .build();
        int projectId = 0;

        try {
            projectId = projectServiceApi.createProject(projectDtoSave);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Save new project with id %s in %s", projectId, new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new project get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
