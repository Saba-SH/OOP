package com.example.part2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "AddToCartServlet", value = "/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the shopping cart from the session
        Map<String, Integer> shoppingCart = (Map<String, Integer>) request.getSession().getAttribute("shoppingCart");
        // get the product ID parameter
        String productId = request.getParameter("id");
        // set count to 1 if the product is not in the cart, increment by 1 if it is
        if(!shoppingCart.containsKey(productId)) {
            shoppingCart.put(productId, 1);
        } else {
            shoppingCart.put(productId, shoppingCart.get(productId) + 1);
        }
        // redirect to shopping cart
        response.sendRedirect("shopping_cart");
    }
}
