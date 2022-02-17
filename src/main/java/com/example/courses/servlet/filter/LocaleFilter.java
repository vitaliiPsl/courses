package com.example.courses.servlet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/*"})
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        String lang = null;
        if (req.getParameter("lang") != null) {
            lang = req.getParameter("lang");

            Cookie cookie = new Cookie("lang", lang);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            res.addCookie(cookie);

            session.setAttribute("lang", lang);
        } else {
            Cookie[] cookies = req.getCookies();
            lang = getLangFromCookies(cookies);

            lang = lang != null ? lang : "en";
            session.setAttribute("lang", lang);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getLangFromCookies(Cookie[] cookies) {
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("lang")){
                return cookie.getValue();
            }
        }

        return null;
    }
}
