package model;

import java.util.List;

public class Order {
    private int id;
    private List<Book> books;
    private String username;
    private String fournisseurName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public String getFournisseurName() {
		return fournisseurName;
	}
	public void setFournisseurName(String fournisseurName) {
		this.fournisseurName = fournisseurName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

    // Getters and setters
}