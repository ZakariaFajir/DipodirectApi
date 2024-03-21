package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import utils.PasswordHash;
import utils.ServletUtils;
import utils.TokenUtils;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import Dao.UserDao;
import Dao.UserDaoImpl;

/**
 * Servlet implementation class Auth
 */
@WebServlet("/auth/*")
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Auth() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		switch (pathInfo) {
		case "/login":
			login(request, response);
			break;
		case "/signup":
			signup(request, response);
			break;
		default:
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
			User user = userDaoImpl.getUserByEmail(email);
			System.out.println(email);

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

		// Check if email already exists
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		if (userDaoImpl.getUserByEmail(email) != null) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().write("Email already exists");
			return;
		}

		// Hash the password before storing
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
		response.setStatus(HttpServletResponse.SC_CREATED);
		response.getWriter().write("User created successfully");
	}

}
