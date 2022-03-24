package com.example.courses.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class AwsImageService {
    private final AmazonS3 s3client;
    private final String AWS_BUCKET_NAME;
    private final String DIR_COURSE_IMAGE;
    private final String DIR_USER_IMAGE;

    public static final String DEFAULT_IMAGE = "default.jpeg";
    private static final String IMAGE_EXTENSION_REGEX = "(jp?g|png|bmp)";

    public AwsImageService() throws IOException {
        // get properties and init AmazonS3Client
        try (InputStream input = AwsImageService.class.getClassLoader().getResourceAsStream("aws/aws.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            AWSCredentials credentials = new BasicAWSCredentials(
                    prop.getProperty("aws.access_key_id"),
                    prop.getProperty("aws.secret_access_key")
            );

            this.s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.EU_CENTRAL_1)
                    .build();

            AWS_BUCKET_NAME = prop.getProperty("s3.bucket_name");
            DIR_COURSE_IMAGE = prop.getProperty("s3.course_images_folder");
            DIR_USER_IMAGE = prop.getProperty("s3.user_images_folder");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public String saveCourseImage(HttpServletRequest request) throws ServletException, IOException {
        return saveImage(request, DIR_COURSE_IMAGE);
    }

    public String saveUserImage(HttpServletRequest request) throws ServletException, IOException {
        return saveImage(request, DIR_USER_IMAGE);
    }

    public byte[] loadCourseImage(String imageName) throws IOException {
        return loadImage(imageName, DIR_COURSE_IMAGE);
    }

    public byte[] loadUserImage(String imageName) throws IOException {
        return loadImage(imageName, DIR_USER_IMAGE);
    }

    private byte[] loadImage(String imageName, String imageDir) throws IOException {
        S3Object s3object = s3client.getObject(
                AWS_BUCKET_NAME,
                imageDir + "/" + imageName
        );

        S3ObjectInputStream inputStream = s3object.getObjectContent();
        return inputStream.readAllBytes();
    }

    private String saveImage(HttpServletRequest request, String imageDir) throws IOException, ServletException {
        String imageName = null;

        Part filePart = request.getPart("file");
        String extension = getFileExtension(filePart);

        if (filePart.getSize() != 0 && extension != null && extension.matches(IMAGE_EXTENSION_REGEX)) {
            imageName = UUID.randomUUID() + "." + extension;

            InputStream imageIS = filePart.getInputStream();
            s3client.putObject(
                    AWS_BUCKET_NAME,
                    imageDir + File.separator + imageName,
                    imageIS,
                    new ObjectMetadata()
            );
        }

        return imageName;
    }

    private String getFileExtension(Part filePart) {
        String extension = null;

        String name = filePart.getSubmittedFileName();

        if (name != null) {
            String[] nameParts = name.split("\\.");
            extension = nameParts[nameParts.length - 1];
        }

        return extension;
    }
}
