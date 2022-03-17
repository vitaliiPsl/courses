package com.example.courses.servlet;

import com.example.courses.exception.NotFoundException;
import com.example.courses.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    private final ImageUtils imageUtils = new ImageUtils();

    public static final String TYPE_COURSE = "course";
    public static final String TYPE_USER = "user";
    private static final Logger logger = LogManager.getLogger(ImageServlet.class.getName());

    public ImageServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.trace("Image servlet: get");

        String imageType = request.getParameter("image_type");
        String imageName = request.getParameter("image_name");

        logger.debug("Image type: " + imageType);
        logger.debug("Image name: " + imageName);

        if(imageType == null || imageName == null){
            logger.warn("Image name or type is null");
            throw new NotFoundException();
        }

        byte[] imageBytes = null;

        try {
            if (imageType.equals(TYPE_COURSE)) {
                imageBytes = imageUtils.loadCourseImage(imageName);
            } else if (imageType.equals(TYPE_USER)) {
                imageBytes = imageUtils.loadUserImage(imageName);
            }
        } catch (IOException e){
            logger.error("Error while loading image: " + imageName, e);
            return;
        }

        if(imageBytes != null) {
            OutputStream out = response.getOutputStream();
            out.write(imageBytes);
            out.close();
        }
    }
}
