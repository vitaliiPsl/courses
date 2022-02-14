package com.example.courses.servlet.listener;


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        // create filter map for current session
        addSessionCourseFiltersMap(session);

        // set default sorting
        session.setAttribute("sorting", "title");
        //set default sorting order
        session.setAttribute("sorting_order", "ascending");
    }

    private void addSessionCourseFiltersMap(HttpSession session){
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("subject", new ArrayList<>());
        filters.put("teacher", new ArrayList<>());
        session.setAttribute("filters", filters);
    }
}
