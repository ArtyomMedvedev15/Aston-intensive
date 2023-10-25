package com.aston.servlets;

import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@WebServlet(urlPatterns = "/api/v1/user/*")
public class RestUserServlet extends HttpServlet {

    private UserDaoImplementation userDao;

    @Override
    public void init(){
        final Object userDao = getServletContext().getAttribute("userDao");
        this.userDao = (UserDaoImplementation) userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> allUsers = null;
        try {
             allUsers = userDao.getAllUsers();
             log.info("Get all user with size list - [{}]",allUsers.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String userListJson = objectMapper.writeValueAsString(allUsers);
        resp.setContentType("application/json");
        log.info("Return all user with json {} with status 200",userListJson);
        resp.getWriter().write(userListJson);
    }
}
