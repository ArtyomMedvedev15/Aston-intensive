package com.aston.servlets;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.implementation.TaskDaoImplementation;
import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.entities.Task;
import com.aston.entities.User;
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
@WebServlet(urlPatterns = "/api/v1/task/*")
public class RestTaskServlet extends HttpServlet {

    private TaskDaoApi taskDao;


    @Override
    public void init(){
        final Object taskDao = getServletContext().getAttribute("taskDao");
        this.taskDao = (TaskDaoApi) taskDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Task> allTaskList = null;
        try {
            allTaskList = taskDao.getAllTasks();
            log.info("Get all task with size list - [{}]",allTaskList.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String userListJson = objectMapper.writeValueAsString(allTaskList);
        resp.setContentType("application/json");
        log.info("Return all user with json {} with status 200",userListJson);
        resp.getWriter().write(userListJson);
    }
}
