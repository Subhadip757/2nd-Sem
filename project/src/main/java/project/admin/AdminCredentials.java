package project.admin;

import java.io.FileInputStream;
import java.util.Properties;

public class AdminCredentials {
    private static final Properties credentials = new Properties();
    private static final String CREDENTIALS_FILE = "admin.properties";

    static {
        try {
            FileInputStream input = new FileInputStream(CREDENTIALS_FILE);
            credentials.load(input);
            input.close();
        } catch (Exception e) {
            System.err.println("Error loading admin credentials: " + e.getMessage());
        }
    }

    public static boolean validateCredentials(String username, String password) {
        String storedPassword = credentials.getProperty(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}