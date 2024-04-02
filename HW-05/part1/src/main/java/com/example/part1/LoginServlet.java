package com.example.part1;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager accountManager = (AccountManager) request.getServletContext().getAttribute("accountManager");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean userExists = accountManager.checkUser(username, password);

        if(userExists) {
            request.getSession().setAttribute("username", username);
            getServletContext().getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
        } else {
            getServletContext().getRequestDispatcher("/WEB-INF/please_try_again.html").forward(request, response);
        }
    }
}
