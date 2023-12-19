package learn.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;	 

class JdbcSelect {

	public static void main(String[] args) throws SQLException {

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			  // 1. Load the properties file from the classpath
            Properties props = new Properties();
            InputStream input = ConnectionDemo.class.getClassLoader().getResourceAsStream("demo.properties");
            props.load(input);

            // 2. Read the props
            String theUser = props.getProperty("user");
            String thePassword = props.getProperty("password");
            String theDburl = props.getProperty("dburl");

            System.out.println("Connecting to the database...");
            System.out.println("Database URL: " + theDburl);
            System.out.println("User: " + theUser);

            // 3. Get a connection to the database
            myConn = DriverManager.getConnection(theDburl, theUser, thePassword);

            System.out.println("\nConnection successful!\n");
			
			// 4. Create a statement --
			// Create a Statement or PreparedStatement object to execute SQL queries.
			myStmt = myConn.createStatement();
			
			// 3. Execute SQL query (executeQuery or executeUpdate) --
			// Use the executeQuery method for SELECT queries or 
			// executeUpdate for INSERT, UPDATE, DELETE, etc.
			myRs = myStmt.executeQuery("select * from employees");
			
			// 5. Process the result set
			// Iterate through the ResultSet to retrieve data.
			while (myRs.next()) {
				System.out.println(
					myRs.getString("last_name") + 
					", " + 
					myRs.getString("first_name") +
					", " +
					myRs.getString("email") 
					);
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();
			}
		}
	}

}

