package Dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import db.DBConnection;
import model.Book;
import model.Supplier;

public class SupplierDao {
    private MongoDatabase connection;
    private MongoCollection<Document> fournisseurCollection;

    public SupplierDao() {
        try {
            // Establish connection to MongoDB
            this.connection = DBConnection.connect();
            
            // Access the fournisseur collection
            this.fournisseurCollection = connection.getCollection("Supplies");
        } catch (Exception e) {
            // Handle exceptions appropriately (e.g., log, throw, etc.)
            e.printStackTrace();
        }
    }

    public Supplier getFournisseurByLibraryAndType(String libraryName, String suppliesType) {
        // Construct the query to filter fournisseurs by libraryName and suppliesType
        Document query = new Document("library", libraryName)
                            .append("type", suppliesType);

        // Retrieve the first document from the fournisseur collection matching the query
        Document fournisseurDocument = fournisseurCollection.find(query).first();
        if (fournisseurDocument != null) {
            // Map the document to a Fournisseur object and return it
            return mapDocumentToFournisseur(fournisseurDocument);
        } else {
            // No matching fournisseur found, return null
            return null;
        }
    }

    private Supplier mapDocumentToFournisseur(Document fournisseurDocument) {
        Supplier fournisseur = new Supplier();
        fournisseur.setType(fournisseurDocument.getString("type"));
        fournisseur.setLibraryName(fournisseurDocument.getString("library"));
        fournisseur.setTotalPrice(fournisseurDocument.getInteger("totalPrice"));

        // Map books
        List<Document> booksDocuments = (List<Document>) fournisseurDocument.get("books");
        List<Book> books = new ArrayList<>();
        for (Document bookDocument : booksDocuments) {
            Book book = new Book();
            book.setId(bookDocument.getInteger("id"));
            book.setTitle(bookDocument.getString("title"));
            book.setLevel(bookDocument.getString("level"));
            book.setLanguage(bookDocument.getString("language"));
            book.setImgSrc((List<String>) bookDocument.get("imgSrc"));
            book.setPrice(bookDocument.getString("price"));
            if (bookDocument.containsKey("maxQuantity")) {
                book.setMaxQuantity(bookDocument.getInteger("maxQuantity"));
            }
            books.add(book);
        }
        fournisseur.setBooks(books);

        return fournisseur;
    }

}
