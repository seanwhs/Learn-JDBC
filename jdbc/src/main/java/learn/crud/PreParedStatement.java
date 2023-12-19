package learn.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

public class PreParedStatement {

	public static void main(String[] args) throws SQLException {

		Connection myConn = null;
		PreparedStatement myStmt = null;
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
			
			// 4. Prepare statement
			myStmt = myConn.prepareStatement("select * from employees where salary > ? and department=?");
			
			// 5. Set the parameters
			myStmt.setDouble(1, 80000);
			myStmt.setString(2, "Legal");
			
			System.out.println("\n\nPrepared statement:  salary > 8000,  department = Legal");
			
			// 6. Execute SQL query
			myRs = myStmt.executeQuery();
			
			// 5. Display the result set
			display(myRs);
		
			//
			// Reuse the prepared statement:  salary > 25000,  department = HR
			//

			System.out.println("\n\nReuse the prepared statement:  salary > 25000,  department = HR");
			
			// 7. Set the parameters
			myStmt.setDouble(1, 25000);
			myStmt.setString(2, "HR");
			
			// 8. Execute SQL query
			myRs = myStmt.executeQuery();
			
			// 9. Display the result set
			display(myRs);


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

	private static void display(ResultSet myRs) throws SQLException {
		while (myRs.next()) {
			String lastName = myRs.getString("last_name");
			String firstName = myRs.getString("first_name");
			double salary = myRs.getDouble("salary");
			String department = myRs.getString("department");
			
			System.out.printf("%s, %s, %.2f, %s\n", lastName, firstName, salary, department);
		}
	}
}
