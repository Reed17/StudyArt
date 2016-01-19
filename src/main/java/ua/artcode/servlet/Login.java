package ua.artcode.servlet;

import ua.artcode.exception.AppException;
import ua.artcode.service.UserService;
import ua.artcode.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Razer on 18.01.16.
 */
@WebServlet(urlPatterns = "/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserServiceImpl();
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        try {
            if (userService.authenticate(id, password)) {
                resp.sendRedirect("/menu");
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/pages/index.jsp").forward(req, resp);
        }
    }
}
