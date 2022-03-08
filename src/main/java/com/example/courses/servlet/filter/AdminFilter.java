package com.example.courses.servlet.filter;

import com.example.courses.exception.ForbiddenException;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Requires 'Admin' role to access admin endpoints
 */
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User user = (User) httpRequest.getSession().getAttribute("user");

        if (user.getRole().equals(Role.ADMIN)) {
            chain.doFilter(request, response);
        } else {
            throw new ForbiddenException();
        }
    }
}
