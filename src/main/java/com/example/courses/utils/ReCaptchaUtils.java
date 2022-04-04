package com.example.courses.utils;

import com.example.courses.exception.ServerErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Properties;

/**
 * Utils to check if user passed reCaptcha test
 */
public class ReCaptchaUtils {
    private static final Logger logger = LogManager.getLogger(ReCaptchaUtils.class.getName());

    private ReCaptchaUtils() {}

    public static boolean verifyCaptcha(String gRecaptchaResponse) {
        logger.trace("Verify response: " + gRecaptchaResponse);

        // check if response token is valid
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }

        try {
            return makeVerificationRequest(gRecaptchaResponse);
        } catch (Exception e) {
            logger.error("Error while verifying captcha response", e);
            return false;
        }
    }

    /**
     * This method makes request to google captcha api
     * @param gRecaptchaResponse token with info about captcha test
     * @return true if user passed test
     * @throws IOException
     */
    private static boolean makeVerificationRequest(String gRecaptchaResponse) throws IOException {
        String secretKey = getSecretKey();
        URI verifyUri = URI.create(
                String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
                secretKey, gRecaptchaResponse
        ));

        HttpsURLConnection conn = (HttpsURLConnection) verifyUri.toURL().openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            return getCheckResult(conn);
        }

        return false;
    }

    /**
     * Loads captcha secret key from properties file
     * @return captcha secret key
     */
    private static String getSecretKey() {
        try (InputStream input = ReCaptchaUtils.class.getClassLoader().getResourceAsStream("recaptcha/captcha.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            return properties.getProperty("secret_key");
        } catch (Exception e) {
            logger.error("Unable to find captcha.properties");
            throw new ServerErrorException();
        }
    }

    /**
     * Parses api response and returns boolean which represent test result
     * @param connection connection to google captcha api
     * @return true if user passed captcha test
     * @throws IOException
     */
    private static boolean getCheckResult(HttpsURLConnection connection) throws IOException {
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String response = builder.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        return jsonNode.get("success").asBoolean();
    }
}

