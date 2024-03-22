package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order {
	private String id;
	private List<Book> books;
	private String customerName;
	private String supplierName;
	private Date orderDate;

	public Order() {
		this.setOrderDate(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOrderDateFormatted() {
		// Convert the Date object to a LocalDateTime object
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String formattedDate = dateFormat.format(this.orderDate);
		return formattedDate;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

}
