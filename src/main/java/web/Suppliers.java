package web;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import utils.ServletUtils;
import utils.TokenUtils;
import Dao.SupplierDao;
import Dao.UserDaoImpl;

import java.io.IOException;

@WebServlet("/suppliers/*")
public class Suppliers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int STATUS_SUCCESS = HttpServletResponse.SC_OK;
	private static final int STATUS_CREATED = HttpServletResponse.SC_CREATED;
	private static final int STATUS_UNAUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED;
	private static final int STATUS_NOT_FOUND = HttpServletResponse.SC_NOT_FOUND;
	private static final int STATUS_INTERNAL_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

	private final SupplierDao supplierDao = new SupplierDao();
	private final UserDaoImpl userDao = new UserDaoImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathParams = request.getPathInfo().split("/");
		switch (pathParams.length) {
		case 3:
			getSupplier(request, response);
			break;
		default:
			ServletUtils.sendResponse(response, STATUS_NOT_FOUND, "Route not found");
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathParams = request.getPathInfo().split("/");
		switch (pathParams.length) {
		case 3:
			String action = pathParams[2];
			switch (action) {
			case "order":
				addOrder(request, response);
				break;
			default:
				ServletUtils.sendResponse(response, STATUS_NOT_FOUND, "Route not found");
				break;
			}
			break;
		default:
			ServletUtils.sendResponse(response, STATUS_NOT_FOUND, "Route not found");
			break;
		}
	}

	private void getSupplier(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] pathParams = request.getPathInfo().split("/");
		String libraryName = pathParams[1];
		String suppliesType = pathParams[2];

		try {
			model.Supplier supplier = supplierDao.getSupplierByLibraryAndType(libraryName, suppliesType);

			ServletUtils.sendResponse(response, STATUS_SUCCESS, new Gson().toJson(supplier));
		} catch (Exception e) {
			e.printStackTrace();
			ServletUtils.sendResponse(response, STATUS_INTERNAL_ERROR, "Error occurred while fetching supplier.");
		}
	}

	private void addOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			ServletUtils.sendResponse(response, STATUS_UNAUTHORIZED, "Unauthorized");
			return;
		}
		String token = authorizationHeader.substring(7);

		String email = TokenUtils.getEmailFromToken(token);
		if (email == null) {
			ServletUtils.sendResponse(response, STATUS_UNAUTHORIZED, "Unauthorized");
			return;
		}

		String[] pathParams = request.getPathInfo().split("/");
		String libraryName = pathParams[1];

		String requestBody = ServletUtils.getRequestBody(request);
		Gson gson = new Gson();
		Order order = gson.fromJson(requestBody, Order.class);
		order.setSupplierName(libraryName);

		userDao.addOrder(email, order);

		ServletUtils.sendResponse(response, STATUS_CREATED, "Order added successfully");
	}

}
