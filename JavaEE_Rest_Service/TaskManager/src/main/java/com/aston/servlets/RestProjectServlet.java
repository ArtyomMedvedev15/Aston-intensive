package com.aston.servlets;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.entities.Project;
import com.aston.entities.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/project/*")
public class RestProjectServlet extends HttpServlet {
    private ProjectDaoApi projectDao;

    @Override
    public void init(){
        final Object taskDao = getServletContext().getAttribute("projectDao");
        this.projectDao = (ProjectDaoApi) taskDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Project> allProjectList = null;
        try {
            allProjectList = projectDao.getAllProject();
            log.info("Get all projects with size list - [{}]",allProjectList.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String userListJson = objectMapper.writeValueAsString(allProjectList);
        resp.setContentType("application/json");
        log.info("Return all user with json {} with status 200",userListJson);
        resp.getWriter().write(userListJson);
    }
}
