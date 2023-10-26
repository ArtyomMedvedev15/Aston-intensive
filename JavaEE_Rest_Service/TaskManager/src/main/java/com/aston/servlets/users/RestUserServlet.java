package com.aston.servlets.users;

import com.aston.dao.api.UserDaoApi;
import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.service.api.UserServiceApi;
import com.aston.service.implementation.UserServiceImplementation;
import com.aston.util.dto.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
@WebServlet(urlPatterns = "/api/v1/user/*")
public class RestUserServlet extends HttpServlet {


    private UserServiceApi userService;
    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("userService");
        this.userService = (UserServiceImplementation) userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<UserDto> allUsers = null;
        try {
             allUsers = userService.getAllUsers();
             log.info("Get all user with size list - [{}]",allUsers.size());
        } catch (SQLException e) {
            log.error("Error with connection to db, get exception with message {}",e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String userListJson = objectMapper.writeValueAsString(allUsers);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        log.info("Return all user with json {} with status 200",userListJson);
        resp.getWriter().write(userListJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonPayload.toString());

        String username = jsonNode.get("username").asText();
        String email = jsonNode.get("email").asText();

        UserDto userDtoSave = UserDto.builder()
                .username(username)
                .email(email)
                .build();
        int userId = 0;

        try {
            userId = userService.createUser(userDtoSave);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Save new user with id %s in %s", userId, new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot save new user get error %s in %s", e.getMessage(), new Date()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonPayload.toString());

        Long id = jsonNode.get("id").asLong();
        String username = jsonNode.get("username").asText();
        String email = jsonNode.get("email").asText();

        UserDto userDtoUpdate = UserDto.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
         try {
            int userId = userService.updateUser(userDtoUpdate);
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Update user with id %s in %s", userDtoUpdate.getId(), new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot update user get error %s in %s", e.getMessage(), new Date()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deleteUserId = req.getParameter("idDelete");
        try {
            int userId = userService.deleteUser(Integer.parseInt(deleteUserId));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Delete user with id %s in %s", deleteUserId, new Date()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot delete user get error %s in %s", e.getMessage(), new Date()));
        }
    }
}
