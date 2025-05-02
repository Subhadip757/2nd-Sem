package project;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoDBConnection instance;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                String connectionString = Config.getMongoURI();
                String databaseName = Config.getDatabaseName();

                if (connectionString == null || databaseName == null) {
                    throw new RuntimeException("MongoDB configuration not found");
                }

                mongoClient = MongoClients.create(connectionString);
                database = mongoClient.getDatabase(databaseName);
                System.out.println("Connected to MongoDB successfully");
            } catch (Exception e) {
                System.err.println("Error connecting to MongoDB: " + e.getMessage());
                throw new RuntimeException("Failed to connect to MongoDB", e);
            }
        }
        return database;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
                System.out.println("MongoDB connection closed");
            } catch (Exception e) {
                System.err.println("Error closing MongoDB connection: " + e.getMessage());
            } finally {
                database = null;
                mongoClient = null;
            }
        }
    }

    public static MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }
}
