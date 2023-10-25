package com.aston.servlets;

import com.aston.dao.api.UserTaskDaoApi;
import com.aston.entities.UserTask;
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
@WebServlet(urlPatterns = "/api/v1/usertask/*")
public class RestUserTaskServlet extends HttpServlet {

    private UserTaskDaoApi userTaskDao;


    @Override
    public void init(){
        final Object userTaskDao = getServletContext().getAttribute("userTaskDao");
        this.userTaskDao = (UserTaskDaoApi) userTaskDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<UserTask> allUserTaskList = null;
        try {
            allUserTaskList = userTaskDao.getAllUsersTask();
            log.info("Get all user task with size list - [{}]",allUserTaskList.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String userListJson = objectMapper.writeValueAsString(allUserTaskList);
        resp.setContentType("application/json");
        log.info("Return all user with json {} with status 200",userListJson);
        resp.getWriter().write(userListJson);
    }

}
