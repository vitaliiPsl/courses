package com.example.courses.servlet.admin;

import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.Language;
import com.example.courses.service.LanguageService;
import com.example.courses.service.SubjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/subject/new")
public class NewSubjectServlet extends HttpServlet {
    private static final LanguageService languageService = new LanguageService();
    private static final SubjectService subjectService = new SubjectService();

    private static final Logger logger = LogManager.getLogger(NewSubjectServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Language> languageList = null;

        try{
            languageList = languageService.getAllLanguages();
        } catch (SQLException e) {
            logger.error("SQLException", e);
            throw new ServerErrorException();
        }

        request.setAttribute("languages", languageList);
        request.getRequestDispatcher("/WEB-INF/templates/admin/new_subject.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Language> languageList = null;

        try{
            languageList = languageService.getAllLanguages();

            Map<Language, String> subjectTranslations = new HashMap<>();
            for(Language lang: languageList){
                String subjectTranslation = request.getParameter("subject_" + lang.getLanguageCode());
                if(subjectTranslation != null){
                    subjectTranslations.put(lang, subjectTranslation);
                } else {
                    request.setAttribute("error", "You have to provide translation in all available languages");
                    this.doGet(request, response);
                    return;
                }
            }

            subjectService.saveSubject(subjectTranslations);
        } catch (SQLException e) {
            logger.error("SQLException while saving subject", e);
            throw new ServerErrorException();
        }

        response.sendRedirect(request.getContextPath() + "/admin/subject/new");
    }
}
