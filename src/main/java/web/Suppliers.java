package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import utils.ServletUtils;
import utils.TokenUtils;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import Dao.SupplierDao;
import Dao.UserDaoImpl;

/**
 * Servlet implementation class Fournisseur
 */
@WebServlet("/suppliers/*")
public class Suppliers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Suppliers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    	    // Extract dynamic parameters from the request URL
    	    String[] pathParams = request.getPathInfo().split("/");
    	    String libraryName = pathParams[1];
    	    String suppliesType = pathParams[2];

    	    // Initialize fournisseur DAO
    	    SupplierDao supplierDao = new SupplierDao();

    	    // Retrieve fournisseur by library name and supplies type
    	    model.Supplier supplier = supplierDao.getFournisseurByLibraryAndType(libraryName, suppliesType);

    	    // Set response content type
    	    response.setContentType("application/json");

    	    // Convert fournisseur object to JSON string
    	    Gson gson = new Gson();
    	    String fournisseurJson = gson.toJson(supplier);

    	    // Write fournisseur JSON string to the response
    	    PrintWriter out = response.getWriter();
    	    out.println(fournisseurJson);
    	} catch (Exception e) {
    	    // Handle exceptions appropriately (e.g., log, throw, etc.)
    	    e.printStackTrace();
    	    // Set appropriate HTTP status code and error message in the response
    	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	    response.getWriter().write("Error occurred while fetching fournisseur.");
    	}
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
            // Authenticate the request
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String token = authorizationHeader.substring(7); // Extract token from header

            // Validate the token (You need to implement this logic)

            // Extract dynamic parameters from the request URL
            String[] pathParams = request.getPathInfo().split("/");
            if (pathParams.length < 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String libraryName = pathParams[1];

            if (pathParams.length == 3 && pathParams[2].equals("order")) {
                // Handle request for adding an order
                addOrder(request, response, libraryName);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle exceptions appropriately (e.g., log, throw, etc.)
            e.printStackTrace();
            // Set appropriate HTTP status code and error message in the response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error occurred while processing request.");
        }
    }
	
	private void addOrder(HttpServletRequest request, HttpServletResponse response, String libraryName)
	        throws IOException {
	    try {
	        // Authenticate the request
	        String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;
	        }
	        String token = authorizationHeader.substring(7); // Extract token from header

	        // Validate the token and extract the username
	        String username = TokenUtils.getUsernameFromToken(token); // Example method, implement according to your token validation logic
	        if (username == null) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;
	        }

	        // Read the request body
	        String requestBody = ServletUtils.getRequestBody(request);
	        // Parse the JSON request body into an Order object
	        Gson gson = new Gson();
	        Order order = gson.fromJson(requestBody.toString(), Order.class);

	        // Add the username and library name to the order
	        order.setUsername(username);
	        order.setFournisseurName(libraryName);

	        // Initialize fournisseur DAO
	        UserDaoImpl u = new UserDaoImpl();

	        // Add the order
	        u.addOrder(username, order);

	        // Set success response
	        response.setStatus(HttpServletResponse.SC_CREATED);
	        response.getWriter().write("Order added successfully");
	    } catch (Exception e) {
	        // Log the exception
	        e.printStackTrace();

	        // Set appropriate error response
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("Error occurred while processing the request");
	    }
	}


}
