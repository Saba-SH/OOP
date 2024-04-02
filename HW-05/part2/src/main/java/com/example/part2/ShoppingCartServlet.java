package com.example.part2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

import static java.lang.Math.round;

@WebServlet(name = "ShoppingCartServlet", value = "/ShoppingCartServlet")
public class ShoppingCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the shopping cart from the session
        Map<String, Integer> shoppingCart = (Map<String, Integer>) request.getSession().getAttribute("shoppingCart");
        ProductDAO productService = new SQLProductDAO();

        // built a list of items, with their counts as form inputs
        StringBuilder itemsBuilder = new StringBuilder();
        double fullPrice = 0;
        for(String productId : shoppingCart.keySet()) {
            Product product = productService.getProductById(productId);
            itemsBuilder.append("<li><input type=\"number\" name=\"" + productId + "\" " +
                    "value=\"" + shoppingCart.get(productId) + "\"></input> "
                    + product.getName() + ", " + String.format("%.2f", product.getPrice()) + "</li>");
            fullPrice += shoppingCart.get(productId) * product.getPrice();
        }
        String itemsList = itemsBuilder.toString();

        // set the list of items and their total price as attributes for JSP
        String totalPrice = String.format("%.2f", ((double) round(fullPrice * 100)) / 100);
        request.setAttribute("itemsList", itemsList);
        request.setAttribute("totalPrice", totalPrice);

        // forward the request to the JSP to display
        request.getRequestDispatcher("WEB-INF/shopping-cart.jsp").forward(request, response);
    }
}
