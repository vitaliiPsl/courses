package com.example.courses.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class ImageUtils {
    public static final String DIR_COURSE_IMAGE = "/home/calmy/epam/courses/images/course";
    public static final String DIR_USER_IMAGE = "/home/calmy/epam/courses/images/user";

    public static final String DEFAULT_IMAGE = "default.jpeg";

    public static final String IMAGE_EXTENSION_REGEX = "(jp?g|png|bmp)";

    public static String saveCourseImage(HttpServletRequest request) throws ServletException, IOException {
        return saveImage(request, DIR_COURSE_IMAGE);
    }

    public static String saveUserImage(HttpServletRequest request) throws ServletException, IOException {
        return saveImage(request, DIR_USER_IMAGE);
    }

    public static byte[] loadCourseImage(String imageName) throws IOException {
        return loadImage(imageName, DIR_COURSE_IMAGE);
    }

    public static byte[] loadUserImage(String imageName) throws IOException {
        return loadImage(imageName, DIR_USER_IMAGE);
    }

    private static byte[] loadImage(String imageName, String imageDir) throws IOException {
        File image = new File(imageDir + File.separator + imageName);

        if(!image.exists()){
            image = new File(imageDir + File.separator + DEFAULT_IMAGE);
        }

        FileInputStream in = new FileInputStream(image);

        return in.readAllBytes();

    }

    private static String saveImage(HttpServletRequest request, String imageDir) throws IOException, ServletException {
        String imageName = null;

        Part filePart = request.getPart("file");
        String extension = getFileExtension(filePart);

        File dir = new File(imageDir);
        if(!dir.exists()){
            dir.mkdir();
        }

        if (filePart.getSize() != 0 && extension != null && extension.matches(IMAGE_EXTENSION_REGEX)) {
            imageName = UUID.randomUUID() + "." + extension;

            for (Part part : request.getParts()) {
                part.write(dir + File.separator + imageName);
            }
        }

        return imageName;
    }

    private static String getFileExtension(Part filePart) {
        String extension = null;

        String name = filePart.getSubmittedFileName();

        if (name != null) {
            String[] nameParts = name.split("\\.");
            extension = nameParts[nameParts.length - 1];
        }

        return extension;
    }
}
