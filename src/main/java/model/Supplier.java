package model;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
	private int id;
    private String type;
    private String libraryName;
//    private String name;
    private double totalPrice;
    private List<Book> books;
    
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLibraryName() {
		return libraryName;
	}
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	 public void addBook(Book book) {
	        if (books == null) {
	            books = new ArrayList<>();
	        }
	        books.add(book);
	    }

}


