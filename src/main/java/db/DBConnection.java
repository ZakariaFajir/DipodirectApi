package db;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
    private static final String DATABASE_NAME = "Dipodirect"; // Change to your database name

    public static MongoDatabase connect() {
        try {
            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + DATABASE_NAME);

            MongoClient mongoClient = MongoClients.create(connectionString);

            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connected to the database successfully");
            return database;
        } catch (Exception e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            return null;
        }
    }
}
