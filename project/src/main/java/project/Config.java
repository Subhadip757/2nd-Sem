package project;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream input = new FileInputStream(".env");
            properties.load(input);
            input.close();
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    public static String getMongoURI() {
        return properties.getProperty("MONGODB_URI");
    }

    public static String getDatabaseName() {
        return properties.getProperty("DATABASE_NAME");
    }
}
