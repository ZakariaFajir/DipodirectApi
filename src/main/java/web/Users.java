package web;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.User;
import utils.PasswordHash;
import utils.ServletUtils;
import utils.TokenUtils;

import Dao.UserDaoImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/user/*")
public class Users extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		switch (pathInfo) {
		case "/order-history":
			getOrderHistory(request, response);
			break;
		default:
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {
			ServletUtils.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
			return;
		}

		switch (pathInfo) {
		case "/auth/login":
			login(request, response);
			break;
		case "/auth/signup":
			signup(request, response);
			break;
		default:
			ServletUtils.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
		}
	}

	private void getOrderHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String authorizationHeader = request.getHeader("Authorization");
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				ServletUtils.sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
						"Authorization token is missing or invalid");
				return;
			}

			String token = authorizationHeader.substring(7); // Extract token from header

			String email = TokenUtils.getEmailFromToken(token);
			if (email == null) {
				ServletUtils.sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
						"Invalid token or expired token");
				return;
			}

			// Use UserDao to retrieve order history
			UserDaoImpl userDao = new UserDaoImpl();
			List<Order> orderHistory = userDao.getUserByEmail(email, false).getOrders();
			System.out.print(orderHistory.get(0).getOrderDateFormatted());
			System.out.print(orderHistory.get(0).getSupplierName());

			// Convert order history to JSON and send response
			Gson gson = new Gson();
			String orderHistoryJson = gson.toJson(orderHistory);
			System.out.println(orderHistoryJson);

			response.setContentType("application/json");
			response.getWriter().write(orderHistoryJson);
		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();

			ServletUtils.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Error occurred while processing the request");
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String requestBody = ServletUtils.getRequestBody(request);
			User requestBodyObject = new Gson().fromJson(requestBody, User.class);

			String email = requestBodyObject.getEmail();
			String password = requestBodyObject.getPassword();

			// Check if user exists in the database
			UserDaoImpl userDaoImpl = new UserDaoImpl();
			User user = userDaoImpl.getUserByEmail(email, true);
			if (user != null && PasswordHash.verifyPassword(password, user.getPassword())) {
				// Generate token
				String token = TokenUtils.generateToken(user.getEmail());

				// Set token in response
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.println(new Gson().toJson(token));
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid email or password");
			}
		} catch (Exception e) {
			throw new IOException("Error occurred during login.", e);
		}
	}

	private void signup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestBody = ServletUtils.getRequestBody(request);
		User requestBodyObject = new Gson().fromJson(requestBody, User.class);

		String email = requestBodyObject.getEmail();
		String password = requestBodyObject.getPassword();

		if (!isValidEmail(email)) {
			ServletUtils.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid email format");
			return;
		}

		if (!isValidPassword(password)) {
			ServletUtils.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST,
					"Password must contain at least 8 characters");
			return;
		}

		UserDaoImpl userDaoImpl = new UserDaoImpl();
		if (userDaoImpl.getUserByEmail(email, false) != null) {
			ServletUtils.sendResponse(response, HttpServletResponse.SC_CONFLICT, "Email already exists");
			return;
		}

		String firstname = requestBodyObject.getFirstname();
		String lastname = requestBodyObject.getLastname();
		String hashedPassword = PasswordHash.hashPassword(password);

		// Create user object and add to database
		User user = new User();
		user.setEmail(email);
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setPassword(hashedPassword);
		userDaoImpl.addUser(user);

		// Respond with success message
		ServletUtils.sendResponse(response, HttpServletResponse.SC_CREATED, "User created successfully");
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	private boolean isValidPassword(String password) {
		return password.length() >= 8;
	}

}
