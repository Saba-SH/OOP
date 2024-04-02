package com.example.part1;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CreateServlet", value = "/CreateServlet")
public class CreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/create_new_account.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountManager accountManager = (AccountManager) request.getServletContext().getAttribute("accountManager");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean userNameExists = accountManager.checkUserName(username);

        if(userNameExists) {
            response.getWriter().println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Create Account</title>\n" +
                    "</head>");
            response.getWriter().println("<body>\n" +
                    "  <h1>Username " + username + " is already in use.</h1>\n" +
                    "  <h4>Please enter proposed name and password.</h4>\n" +
                    "  <form action=\"CreateServlet\" method=\"post\">\n" +
                    "    <label for=\"username\">User Name:</label>\n" +
                    "    <input type=\"text\" id=\"username\" name=\"username\">\n" +
                    "    <br><br>\n" +
                    "    <label for=\"password\">Password:</label>\n" +
                    "    <input type=\"password\" id=\"password\" name=\"password\">\n" +
                    "    &nbsp<button>Sign Up</button>\n" +
                    "  </form>\n" +
                    "</body>\n" +
                    "</html>");
        } else {
            accountManager.addUser(username, password);
            request.getSession().setAttribute("username", username);

            getServletContext().getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
        }
    }
}
