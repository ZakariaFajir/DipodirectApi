package model;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String level;
    private String langue;
    private List<String> imgSrc;
    private String price;
    private String maxQuantity;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLangue() {
		return langue;
	}
	public void setLangue(String langue) {
		this.langue = langue;
	}
	public List<String> getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(List<String> imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(String maxQuantity) {
		this.maxQuantity = maxQuantity;
	}


}
