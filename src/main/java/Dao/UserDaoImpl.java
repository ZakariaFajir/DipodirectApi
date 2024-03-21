package Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import db.DBConnection;
import model.Book;
import model.Order;
import model.User;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private MongoCollection<Document> userCollection;
    private MongoDatabase connection;

    public UserDaoImpl() {
            try {
                // Establish connection to MongoDB
                this.connection = DBConnection.connect();
                
                // Access the fournisseur collection
                this.userCollection = connection.getCollection("User");
            } catch (Exception e) {
                // Handle exceptions appropriately (e.g., log, throw, etc.)
                e.printStackTrace();
        }
      }

    @Override
    public User getUserByEmail(String email) {
        Document query = new Document("email", email);
        Document userDocument = userCollection.find(query).first();
        if (userDocument != null) {
            return documentToUser(userDocument);
        }
        return null; // User not found
    }

    @Override
    public void addUser(User user) {
        userCollection.insertOne(userToDocument(user));
    }

    @Override
    public void updateUser(User user) {
        Document query = new Document("email", user.getEmail());
        userCollection.replaceOne(query, userToDocument(user));
    }

    @Override
    public void deleteUser(String email) {
        Document query = new Document("email", email);
        userCollection.deleteOne(query);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        MongoCursor<Document> cursor = userCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document userDocument = cursor.next();
                userList.add(documentToUser(userDocument));
            }
        } finally {
            cursor.close();
        }
        return userList;
    }
    
    @Override
    public void addOrder(String email, Order order) {
        try {
            // Check if the user exists
            User user = getUserByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User does not exist: " + email);
            }

            // Add the order to the user's list of orders
            user.addOrder(order);

            // Convert the updated user object to a document
            Document updatedUserDocument = userToDocument(user);
            System.out.println(updatedUserDocument);

            // Update the user's document in the database
            Document filter = new Document("email", email);
            userCollection.replaceOne(filter, updatedUserDocument);
        } catch (Exception e) {
            // Handle the exception here
            e.printStackTrace();
        }
    }


    private Document orderToDocument(String email, Order order) {
        List<Document> bookDocuments = new ArrayList<>();
        for (Book book : order.getBooks()) {
            Document bookDocument = bookToDocument(book);
            bookDocuments.add(bookDocument);
        }

        return new Document("email", email)
                .append("fournisseurName", order.getFournisseurName())
                .append("books", bookDocuments);
    }
    private Document bookToDocument(Book book) {
        return new Document("id", book.getId())
                .append("title", book.getTitle())
                .append("level", book.getLevel())
                .append("langue", book.getLanguage())
                .append("imgSrc", book.getImgSrc())
                .append("price", book.getPrice())
                .append("maxQuantity", book.getMaxQuantity());
    }

    private Document userToDocument(User user) {
        List<Document> orderDocuments = new ArrayList<>();
        for (Order order : user.getOrders()) {
            orderDocuments.add(orderToDocument(user.getEmail(), order));
        }

        return new Document("email", user.getEmail())
                .append("password", user.getPassword())
                .append("firstname", user.getFirstname())
                .append("lastname", user.getLastname())
                .append("orders", orderDocuments);
    }


    private User documentToUser(Document document) {
        User user = new User();
        user.setEmail(document.getString("email"));
        user.setPassword(document.getString("password")); 
        return user;
    }
}
