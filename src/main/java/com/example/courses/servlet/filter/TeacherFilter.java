package com.example.courses.servlet.filter;

import com.example.courses.exception.ForbiddenException;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Requires 'Teacher' role to access teacher's endpoints
 */
public class TeacherFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User user = (User) httpRequest.getSession().getAttribute("user");

        if (user.getRole().equals(Role.TEACHER)) {
            chain.doFilter(request, response);
        } else {
            throw new ForbiddenException();
        }
    }
}
