package com.example.part2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UpdateCartServlet", value = "/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // for a get request, just forward to the shopping cart
        request.getRequestDispatcher("/shopping_cart").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get a map of product IDs to their counts
        Map<String, String[]> newProducts = request.getParameterMap();
        // construct a new cart
        Map<String, Integer> newCart = new HashMap<>();
        // add the new counts to the cart
        for (String productId : newProducts.keySet()) {
            String countString = newProducts.get(productId)[0];
            // skip the item if its text field is empty
            if(countString.equals(""))
                continue;
            Integer count = Integer.valueOf(countString);
            // skip this item if its count is 0 or less
            if(count <= 0)
                continue;
            // put the product in the cart with its new count
            newCart.put(productId, count);
        }
        // set the new shopping cart as a session attribute
        request.getSession().setAttribute("shoppingCart", newCart);
        // redirect to the shopping cart servlet to display the new cart
        response.sendRedirect("shopping_cart");
    }
}
