package com.aston.servlets.users;

import com.aston.service.api.UserServiceApi;
import com.aston.service.implementation.UserServiceImplementation;
import com.aston.util.UserNotFoundException;
import com.aston.util.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/user/by/username/")
public class RestGetUserByUsernameServlet extends HttpServlet {
    private UserServiceApi userService;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("userService");
        this.userService = (UserServiceImplementation) userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        try {
            UserDto userByUsername = userService.getUserByUsername(username);

            ObjectMapper objectMapper = new ObjectMapper();
            String userByIdJson = objectMapper.writeValueAsString(userByUsername);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            log.info("Get user by username with username {}",username);
            resp.getWriter().write(userByIdJson);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by username user get error with server in %s", new Date()));
        } catch (UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by username user get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
