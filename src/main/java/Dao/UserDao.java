package Dao;

import model.Order;
import model.User;

public interface UserDao {
	void addUser(User user);

	User getUserByEmail(String email, boolean includePassword);

	void addOrder(String username, Order order);
}
