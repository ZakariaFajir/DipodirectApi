package Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import db.DBConnection;
import model.Book;
import model.Order;
import model.User;

public class UserDaoImpl implements UserDao {
	private MongoCollection<Document> userCollection;
	private MongoDatabase connection;

	public UserDaoImpl() {
		try {
			// Establish connection to MongoDB
			this.connection = DBConnection.connect();

			// Access the user collection
			this.userCollection = connection.getCollection("User");
		} catch (Exception e) {
			// Handle exceptions appropriately (e.g., log, throw, etc.)
			e.printStackTrace();
		}
	}

	@Override
	public User getUserByEmail(String email, boolean includePassword) {
		Document query = new Document("email", email);
		Document userDocument = userCollection.find(query).first();
		if (userDocument != null) {
			return documentToUser(userDocument, includePassword);
		}
		return null; // User not found
	}

	@Override
	public void addUser(User user) {
		userCollection.insertOne(userToDocument(user));
	}

	@Override
	public void addOrder(String email, Order order) {
	    try {
	        // Check if the user exists
	        User user = getUserByEmail(email, false);
	        if (user == null) {
	            throw new IllegalArgumentException("User does not exist: " + email);
	        }

	        // Check if all books exist for the given supplier
	        boolean allBooksExist = checkAllBooksExist(order.getSupplierName(), order.getBooks());
	        if (!allBooksExist) {
	            System.out.println("Some books not found for the supplier: " + order.getSupplierName());
	            return;
	        }

	        // Add the email and library name to the order
	        String fullname = user.getFirstname() + " " + user.getLastname();
	        order.setCustomerName(fullname);


	        // Update the user's orders
	        Document filter = new Document("email", email);
	        Document update = new Document("$push", new Document("orders", orderToDocument(order)));
	        userCollection.updateOne(filter, update);
	    } catch (Exception e) {
	        // Handle the exception here
	        e.printStackTrace();
	    }
	}
	
	private boolean checkAllBooksExist(String supplierName, List<Book> books) {
	    // Instanciation de la classe SupplierDaoImpl
	    SupplierDaoImpl supplierDao = new SupplierDaoImpl();
	    
	    for (Book book : books) {
	        // Utilisation de l'instance de SupplierDaoImpl pour appeler bookExistsForSupplier
	        if (!supplierDao.bookExistsForSupplier(supplierName, book.getTitle())) {
	            return false;
	        }
	    }
	    return true;
	}


	private Document userToDocument(User user) {
		return new Document("email", user.getEmail()).append("password", user.getPassword())
				.append("firstname", user.getFirstname()).append("lastname", user.getLastname());
	}

	private Document orderToDocument(Order order) {
		List<Document> bookDocuments = new ArrayList<>();
		for (Book book : order.getBooks()) {
			bookDocuments.add(bookToDocument(book));
		}

		return new Document("supplierName", order.getSupplierName())
				.append("customerName", order.getCustomerName()).append("orderDate", order.getOrderDateFormatted())
				.append("books", bookDocuments);
	}

	private Document bookToDocument(Book book) {
		return new Document("id", book.getId()).append("title", book.getTitle()).append("level", book.getLevel())
				.append("langue", book.getLangue())
				.append("price", book.getPrice());
	}

	private User documentToUser(Document document, boolean includePassword) {
	    User user = new User();
	    user.setId(document.getObjectId("_id").toString());
	    user.setEmail(document.getString("email"));
	    user.setFirstname(document.getString("firstname"));
	    user.setLastname(document.getString("lastname"));

	    if (includePassword) {
	        user.setPassword(document.getString("password"));
	    }

	    if (document.containsKey("orders") && document.get("orders") != null) {
	        user.setOrders(documentToOrderList(document.getList("orders", Document.class)));
	    }

	    return user;
	}

	private List<Order> documentToOrderList(List<Document> documents) {
		List<Order> orders = new ArrayList<>();
		for (Document document : documents) {
			orders.add(documentToOrder(document));
		}
		return orders;
	}

	private Order documentToOrder(Document document) {
		Order order = new Order();
		order.setSupplierName(document.getString("supplierName"));
		order.setOrderDate(new Date(document.getString("orderDate")));
		order.setBooks(documentToBookList(document.getList("books", Document.class)));
		return order;
	}

	private List<Book> documentToBookList(List<Document> documents) {
		List<Book> books = new ArrayList<>();
		for (Document document : documents) {
			books.add(documentToBook(document));
		}
		return books;
	}

	private Book documentToBook(Document document) {
	    Book book = new Book();
	    book.setId(document.getInteger("id"));
	    book.setTitle(document.getString("title"));
	    book.setLevel(document.getString("level"));
	    book.setLangue(document.getString("langue"));

	    if (document.containsKey("imgSrc") && document.get("imgSrc") != null) {
	        book.setImgSrc(document.getList("imgSrc", String.class));
	    }

	    // Check if "maxQuantity" exists in the document
	    if (document.containsKey("maxQuantity")) {
	        Object maxQuantityObject = document.get("maxQuantity");
	        if (maxQuantityObject != null) {
	            book.setMaxQuantity(maxQuantityObject.toString());
	        }
	    }

	    book.setPrice(document.getString("price"));
	    return book;
	}

}
