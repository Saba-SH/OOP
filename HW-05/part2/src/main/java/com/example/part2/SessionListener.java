package com.example.part2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class SessionListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
        Map<String, Integer> shoppingCart = new HashMap<>();
        se.getSession().setAttribute("shoppingCart", shoppingCart);
    }
}
