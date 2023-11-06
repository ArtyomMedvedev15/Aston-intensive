package com.aston.servlets.task;

import com.aston.service.api.TaskServiceApi;
import com.aston.service.implementation.TaskServiceImplementation;
import com.aston.util.TaskNotFoundException;
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
@WebServlet(urlPatterns = "/api/v1/task/delete/")
public class RestDeleteTaskServlet extends HttpServlet {
    private TaskServiceApi taskServiceApi;
    @Override
    public void init(){
        final Object taskService = getServletContext().getAttribute("taskService");
        this.taskServiceApi = (TaskServiceImplementation) taskService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idDelete = req.getParameter("idDelete");
        try {
            taskServiceApi.deleteTask(Long.valueOf(idDelete));
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            log.info("Delete task by id with id {}",idDelete);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot delete by id task get error with server in %s", new Date()));
        } catch (TaskNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(String.format("Cannot delete by id task get error %s in %s", e.getMessage(),new Date()));
        }
    }
}
