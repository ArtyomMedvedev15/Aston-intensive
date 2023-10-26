package com.aston.servlets.project;

import com.aston.service.api.ProjectServiceApi;
import com.aston.util.dto.ProjectDto;
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
@WebServlet(urlPatterns = "/api/v1/project/all")
public class RestGetAllProjectServlet extends HttpServlet {
    private ProjectServiceApi projectServiceApi;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("projectService");
        this.projectServiceApi = (ProjectServiceApi) userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProjectDto> allProject = null;
        try {
            allProject = projectServiceApi.getAllProject();
            log.info("Get all project with size list - [{}]",allProject.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String projectListJson = objectMapper.writeValueAsString(allProject);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        log.info("Return all project with json {} with status 200",projectListJson);
        resp.getWriter().write(projectListJson);
    }
}
