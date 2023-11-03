package com.aston.servlets.project;

import com.aston.service.api.ProjectServiceApi;
import com.aston.util.ProjectInvalidParameterException;
import com.aston.util.dto.ProjectDto;
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
@WebServlet(urlPatterns = "/api/v1/project/update")
public class RestProjectUpdateServlet extends HttpServlet {
    private ProjectServiceApi projectServiceApi;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("projectService");
        this.projectServiceApi = (ProjectServiceApi) userService;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonPayload.toString());

        Long id = jsonNode.get("id").asLong();
        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();

        ProjectDto projectDtoUpdate = ProjectDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
        int projectId = 0;
        try {
            projectId = projectServiceApi.updateProject(projectDtoUpdate);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Update project with id %s in %s", projectId, new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot update project get error with server in %s", new Date()));
        } catch (ProjectInvalidParameterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot update project get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
