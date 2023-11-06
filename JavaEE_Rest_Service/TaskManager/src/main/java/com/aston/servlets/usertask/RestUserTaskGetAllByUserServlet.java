package com.aston.servlets.usertask;

import com.aston.service.api.UserTaskServiceApi;
import com.aston.util.UserNotFoundException;
import com.aston.util.dto.UserTaskDto;
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
@WebServlet(urlPatterns = "/api/v1/user/task/")
public class RestUserTaskGetAllByUserServlet extends HttpServlet {
    private UserTaskServiceApi userTaskServiceApi;

    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("userTaskService");
        this.userTaskServiceApi = (UserTaskServiceApi) userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("idUser");
        try {
            UserTaskDto userTaskFullDtosList =
                    userTaskServiceApi.getAllUserTaskByUser(Long.valueOf(userId));
             ObjectMapper objectMapper = new ObjectMapper();
            String userTaskListJson = objectMapper.writeValueAsString(userTaskFullDtosList);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            log.info("Get user task by user id with id {}",userId);
            resp.getWriter().write(userTaskListJson);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot get user task by user id get error %s in %s", e.getMessage(), new Date()));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
