package model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password; // hashed password
    private List<Order> orders;
    
    public User() {
        this.orders = new ArrayList<>(); // Initialize the orders list in the constructor
    }
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void addOrder(Order order) {
		this.orders.add(order);
	}

}