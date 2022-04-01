package com.example.courses.servlet.filter;

import com.example.courses.persistence.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This filter wouldn't allow user to open 'log in' or 'sign up' pages
 * if user is already logged in or registered
 *
 */
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession();

        User user = (User) session.getAttribute("user");
        if(user != null){
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
        } else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
