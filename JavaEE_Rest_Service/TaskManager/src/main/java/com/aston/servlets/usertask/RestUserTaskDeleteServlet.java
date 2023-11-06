package com.aston.servlets.usertask;

import com.aston.service.api.UserTaskServiceApi;
import com.aston.util.TaskNotFoundException;
import com.aston.util.UserNotFoundException;
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
@WebServlet(urlPatterns = "/api/v1/user/task/delete/")
public class RestUserTaskDeleteServlet extends HttpServlet {

    private UserTaskServiceApi userTaskServiceApi;

    @Override
    public void init(){
        final Object userService = getServletContext().getAttribute("userTaskService");
        this.userTaskServiceApi = (UserTaskServiceApi) userService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idUser = req.getParameter("idUser");
        String idTask = req.getParameter("idTask");

        try {
            userTaskServiceApi.deleteUserTask(Long.valueOf(idUser),Long.valueOf(idTask));
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            log.info("Delete user task by id with id {}",Long.valueOf(idUser));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot delete by id user task get error %s in %s", e.getMessage(), new Date()));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
