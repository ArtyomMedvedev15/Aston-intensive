package com.aston.servlets.users;

import com.aston.service.api.UserServiceApi;
import com.aston.service.implementation.UserServiceImplementation;
import com.aston.util.UserNotFoundException;
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

@Slf4j
@WebServlet(urlPatterns = "/api/v1/user/by/id/")
public class RestGetUserByIdServlet extends HttpServlet {
    private UserServiceApi userService;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("userService");
        this.userService = (UserServiceImplementation) userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("idUser");
        try {
            UserDto userById = userService.getUserById(Integer.parseInt(userId));

            ObjectMapper objectMapper = new ObjectMapper();
            String userByIdJson = objectMapper.writeValueAsString(userById);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            log.info("Get user by id with id {}",userId);
            resp.getWriter().write(userByIdJson);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id user get error with server in %s", new Date()));
        } catch (UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get by id user get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
