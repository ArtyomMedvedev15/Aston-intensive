package com.aston.servlets.project;

import com.aston.service.api.ProjectServiceApi;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.UserDto;
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

@Slf4j
@WebServlet(urlPatterns = "/api/v1/project/by/id/*")
public class RestGetProjectByIdServlet extends HttpServlet {
    private ProjectServiceApi projectServiceApi;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("projectService");
        this.projectServiceApi = (ProjectServiceApi) userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("idProject");
        try {
            ProjectDto projectDtoById = projectServiceApi.getProjectById(Long.valueOf(projectId));

            ObjectMapper objectMapper = new ObjectMapper();
            String projectById = objectMapper.writeValueAsString(projectDtoById);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            log.info("Get project by id with id {}",projectId);
            resp.getWriter().write(projectById);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id project get error with server in %s", new Date()));
        } catch (ProjectNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id project get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
