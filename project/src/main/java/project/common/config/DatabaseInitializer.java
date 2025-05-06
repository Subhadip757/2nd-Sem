package project.common.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import java.util.ArrayList;

public class DatabaseInitializer {
    public static void initialize() {
        try {
            MongoDatabase database = MongoDBConnection.getDatabase();

            // Create collection if it doesn't exist
            if (!database.listCollectionNames().into(new ArrayList<>()).contains("students")) {
                database.createCollection("students");
            }

            // Create indexes
            database.getCollection("students").createIndex(
                    Indexes.ascending("email"),
                    new IndexOptions().unique(true));

            database.getCollection("students").createIndex(
                    Indexes.ascending("phoneNumber"),
                    new IndexOptions().unique(true));

            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
