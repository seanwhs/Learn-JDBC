package learn.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

public class JdbcDeleteDemo {

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
			
			// 4. Create a statement
			myStmt = myConn.createStatement();

			// Call helper method to display the employee's information
			System.out.println("BEFORE THE DELETE...");
			displayEmployee(myConn, "John", "Doe");
			
			// 5. DELETE the employee
			System.out.println("\nDELETING THE EMPLOYEE: John Doe\n");
			
			int rowsAffected = myStmt.executeUpdate(
					"delete from employees " +
					"where last_name='Doe' and first_name='John'");

			// Print the number of rows affected
			System.out.println("Rows affected: " + rowsAffected);
			
			// Call helper method to display the employee's information
			System.out.println("AFTER THE DELETE...");
			displayEmployee(myConn, "John", "Doe");
			
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			close(myConn, myStmt, myRs);
		}
	}

	private static void displayEmployee(Connection myConn, String firstName, String lastName) throws SQLException {
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			// Prepare statement
			myStmt = myConn
					.prepareStatement("select last_name, first_name, email from employees where last_name=? and first_name=?");

			myStmt.setString(1, lastName);
			myStmt.setString(2, firstName);
			
			// Execute SQL query
			myRs = myStmt.executeQuery();

			// Process result set
			boolean found = false;
			
			while (myRs.next()) {
				String theLastName = myRs.getString("last_name");
				String theFirstName = myRs.getString("first_name");
				String email = myRs.getString("email");
			
				System.out.printf("Found employee: %s %s, %s\n", theFirstName, theLastName, email);
				found=true;
			}
			
			if (!found) {
				System.out.println("Employee NOT FOUND: " + firstName + " " + lastName);				
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myStmt, myRs);
		}

	}

	private static void close(Connection myConn, Statement myStmt,
			ResultSet myRs) throws SQLException {
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

	private static void close(Statement myStmt, ResultSet myRs)
			throws SQLException {

		close(null, myStmt, myRs);
	}	
}
