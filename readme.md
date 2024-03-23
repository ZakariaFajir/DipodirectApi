**Project Name: DipoDirectApi**

---

### Description
This project is a Java API developed for accessing supplier items using MongoDB as the database. It provides endpoints for retrieving items from suppliers, user authentication (sign up and log in), making orders, and viewing order history.

- **Retrieve Items from Supplier:**
  - Endpoint: `/suppliers/:libraryName/:type`
  - Example: `/suppliers/Arrisalla/papeterie`

- **User Authentication:**
  - Sign Up: `/user/auth/signup`
  - Log In: `/user/auth/login`
  - Upon successful login, a token is provided in the response.

- **Order Management:**
  - Make Order: `/suppliers/:libraryName/order` (POST request)
  - View Order History: `/user/Orderhistory`
  - Token in Authorization Header: starts with `Bearer`

### Dependencies
- Jakarta Servlet API
- Jackson Databind
- Gson
- JBCrypt
- JWT API
- JWT Implementation
- JWT Jackson
- Jakarta Servlet JSP JSTL API
- Jakarta EL API
- MongoDB Driver Sync

### Dockerizing the App
1. Create a Dockerfile to build the Docker image:
   ```Dockerfile
   # Use Tomcat & openjdk 17 image
   FROM tomcat:jdk17-openjdk
   
   # Copy the WAR file to Tomcat webapps directory
   COPY target/DipoDirectApi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
   
   # Expose port 8080
   EXPOSE 8080
   
   # Start Tomcat
   CMD ["catalina.sh", "run"]
   ```

2. Build the Docker image:
   ```bash
   docker build -t dipodirectapi .
   ```

3. Run the Docker container:
   ```bash
   docker run -p 8080:8080 dipodirectapi
   ```

### Deploy (Render)

To deploy this application on Render using Docker runtime:

1. **Push to GitHub:**
   ```
   git add .
   git commit -m "Initial commit"
   git push origin main
   ```

2. **Create a Render Account:**
   If you haven't already, sign up for an account on [Render](https://render.com/).

3. **Create a New Web Service:**
   - From the Render dashboard, click on **Add New** -> **Web Service**.
   - Connect your GitHub repository and select the repository for this project.

4. **Configure the Web Service:**
   - Choose Docker for the Environment.
   - Use the default Dockerfile (provided in the project).
   - Set environment variables (`DATABASE_URI`, `DATABASE_NAME`, `SECRET_KEY`) under the Environment tab.

5. **Deploy:**
   - Click on **Deploy** to deploy the application on Render.

6. **Access the Application:**
   - Once deployed, Render will provide a URL where the application can be accessed.
   ```

### Environment Variables
- `DATABASE_URI`: MongoDB URI. Use local if MongoDB is running locally (`localhost:27017`) or MongoDB Atlas URI if using a cloud database. (Ensure network access is allowed)
- `DATABASE_NAME`: Name of the MongoDB collection.
- `SECRET_KEY`: Secret key used for generating tokens.



---







