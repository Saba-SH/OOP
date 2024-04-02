package com.example.part2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductsServlet", value = "/ProductsServlet")
public class ProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productService = new SQLProductDAO();
        List<Product> products = productService.list(); // Obtain all products.
        StringBuilder productBuilder = new StringBuilder();
        // build a list of products
        for (Product p : products) {
            productBuilder.append("<li>"
                    + "<a href=\"show_product?id=" + p.getId() + "\">" + p.getName()
                    + "</a>" +
                    "</li>\n");
        }
        String productsList = productBuilder.toString();
        request.setAttribute("productsList", productsList); // Store products in request scope.
        request.getRequestDispatcher("WEB-INF/products.jsp").forward(request, response); // Forward to JSP page to display them in a HTML table.
    }
}
