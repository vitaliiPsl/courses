package com.example.courses.servlet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/*"})
public class CookieLocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if (req.getParameter("lang") != null) {
            Cookie cookie = new Cookie("lang", req.getParameter("lang"));
            cookie.setMaxAge(60 * 60 * 24 * 7);
            res.addCookie(cookie);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
