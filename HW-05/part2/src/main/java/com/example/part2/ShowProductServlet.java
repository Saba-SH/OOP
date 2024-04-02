package com.example.part2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ShowProductServlet", value = "/ShowProductServlet")
public class ShowProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productService = new SQLProductDAO();
        // get the product from the database
        Product product = productService.getProductById(request.getParameter("id"));
        // set attributes for JSP to display
        request.setAttribute("productName", product.getName());
        request.setAttribute("productImage", "store-images/" + product.getImagefile());
        request.setAttribute("productPrice", String.format("%.2f", product.getPrice()));
        request.setAttribute("productId", product.getId());
        // redirect to JSP to display the product
        request.getRequestDispatcher("WEB-INF/show-product.jsp").forward(request, response);
    }
}
