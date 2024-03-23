# Install Tomcat & openjdk 8 (openjdk has java and javac)
FROM tomcat:10.0.11-jdk17-openjdk

# Copy the WAR file built by Maven to the webapps directory of Tomcat
COPY target/DipoDirectApi.war /usr/local/tomcat/webapps/

# Serve Tomcat
CMD ["catalina.sh", "run"]
