package Dao;

import java.util.List;

import model.Order;
import model.User;

public interface UserDao {
	 void addUser(User user);
	User getUserByEmail(String email);
    void updateUser(User user);
    void deleteUser(String email);
    List<User> getAllUsers();
	void addOrder(String username, Order order);
}
