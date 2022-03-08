package com.example.courses.servlet.filter;

import com.example.courses.persistence.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filters requires user to be authenticated
 */
public class RequireLogin implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        User user = (User) httpRequest.getSession().getAttribute("user");
        if(user == null){
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/log_in");
        } else{
            chain.doFilter(request, response);
        }
    }
}
