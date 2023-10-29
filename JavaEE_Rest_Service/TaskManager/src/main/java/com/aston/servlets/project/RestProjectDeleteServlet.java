package com.aston.servlets.project;

import com.aston.service.api.ProjectServiceApi;
import com.aston.util.ProjectNotFoundException;
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
@WebServlet(urlPatterns = "/api/v1/project/delete/")
public class RestProjectDeleteServlet extends HttpServlet {
    private ProjectServiceApi projectServiceApi;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("projectService");
        this.projectServiceApi = (ProjectServiceApi) userService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idDelete = req.getParameter("idDelete");
        try {
            projectServiceApi.deleteProject(Integer.parseInt(idDelete));
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            log.info("Delete project by id with id {}",idDelete);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot delete by id project get error in %s", new Date()));
        } catch (ProjectNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot delete by id project get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
