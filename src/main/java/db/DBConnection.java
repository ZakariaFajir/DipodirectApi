package db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
	private static final String DATABASE_NAME = System.getenv("DATABASE_NAME");
	private static final String DATABASE_URI = System.getenv("DATABASE_URI");

	public static MongoDatabase connect() {
		try {
			if (DATABASE_NAME == null || DATABASE_NAME.isEmpty()) {
				throw new IllegalStateException("DATABASE_NAME environment variable is not set");
			}
			if (DATABASE_URI == null || DATABASE_URI.isEmpty()) {
				throw new IllegalStateException("DATABASE_URI environment variable is not set");
			}

			ConnectionString connectionString = new ConnectionString(DATABASE_URI);
			MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
					.build();
			MongoClient mongoClient = MongoClients.create(settings);

			MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
			System.out.println("Connected to the database successfully");
			return database;
		} catch (Exception e) {
			System.err.println("Failed to connect to the database: " + e.getMessage());
			return null;
		}
	}
}
