package com.example.courses.servlet.user;

import com.example.courses.exception.ServerErrorException;
import com.example.courses.persistence.entity.User;
import com.example.courses.service.UserService;
import com.example.courses.servlet.Constants;
import com.example.courses.service.AwsImageService;
import com.example.courses.utils.UserValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This servlet allows user to update profile info
 */
@WebServlet("/user/edit")
@MultipartConfig(
        fileSizeThreshold=1024*1024*2,
        maxFileSize=1024*1024*10,
        maxRequestSize=1024*1024*50
)
public class EditUserInfoServlet extends HttpServlet {
    private static final UserService userService = new UserService();
    private final AwsImageService imageUtils = new AwsImageService();

    private static final Logger logger = LogManager.getLogger(EditUserInfoServlet.class.getName());

    public EditUserInfoServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("EditUserInfo: get");
        request.getRequestDispatcher(Constants.TEMPLATES_CONSTANTS.EDIT_USER_INFO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("EditUserInfo: post");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");

        if(!UserValidation.isNameValid(firstName) || !UserValidation.isNameValid(lastName) || !UserValidation.isEmailValid(email)){
            logger.warn("Provided properties aren't valid");
            request.setAttribute("error", "Properties are invalid");
            this.doGet(request, response);
            return;
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        String imageName = imageUtils.saveUserImage(request);
        if(imageName != null){
            user.setImageName(imageName);
        }

        try {
            userService.updateUser(user);
        } catch (SQLException e) {
            logger.error("SQLException while updating new user", e);
            throw new ServerErrorException();
        }

        response.sendRedirect(request.getContextPath() + "/user?user_id=" + user.getId());
    }
}
