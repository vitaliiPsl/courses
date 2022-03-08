package com.example.courses.servlet.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * This filters sets utf-8 encoding in request and response parameters
 */
public class RequestEncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
