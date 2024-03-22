package Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import db.DBConnection;
import model.Book;
import model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierDao {
    private final MongoCollection<Document> supplierCollection;

    public SupplierDao() {
        MongoDatabase connection = null;
        try {
            // Establish connection to MongoDB
            connection = DBConnection.connect();
            this.supplierCollection = connection.getCollection("Suppliers");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SupplierDao", e);
        }
    }

    public Supplier getSupplierByLibraryAndType(String libraryName, String suppliesType) {
        Document query = new Document("library", libraryName).append("type", suppliesType);
        Document supplierDocument = supplierCollection.find(query).first();
        return supplierDocument != null ? mapDocumentToSupplier(supplierDocument) : null;
    }

    private Supplier mapDocumentToSupplier(Document supplierDocument) {
        Supplier supplier = new Supplier();
        supplier.setId(supplierDocument.getObjectId("_id").toString());
        supplier.setType(supplierDocument.getString("type"));
        supplier.setLibraryName(supplierDocument.getString("library"));
        supplier.setTotalPrice(supplierDocument.getInteger("totalPrice"));

        // Map books
        List<Document> booksDocuments = supplierDocument.getList("books", Document.class);
        List<Book> books = new ArrayList<>();
        if (booksDocuments != null) {
            for (Document bookDocument : booksDocuments) {
                Book book = mapDocumentToBook(bookDocument);
                books.add(book);
            }
        }
        supplier.setBooks(books);
        return supplier;
    }

    private Book mapDocumentToBook(Document bookDocument) {
        Book book = new Book();
        book.setId(bookDocument.getInteger("id"));
        book.setTitle(bookDocument.getString("title"));
        book.setLevel(bookDocument.getString("level"));
        book.setLanguage(bookDocument.getString("language"));
        book.setImgSrc(bookDocument.getList("imgSrc", String.class));
        book.setPrice(bookDocument.getString("price"));
        book.setMaxQuantity(bookDocument.getInteger("maxQuantity", 0)); // Default value if not present
        return book;
    }
}
