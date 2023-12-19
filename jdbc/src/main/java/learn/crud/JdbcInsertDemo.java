package learn.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

public class JdbcInsertDemo {

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

			// 5. Insert a new employee
			System.out.println("Inserting a new employee to database\n");

			int rowsAffected = myStmt.executeUpdate(
					"insert into employees " +
							"(last_name, first_name, email, department, salary) " +
							"values " +
							"('Wright', 'Eric', 'eric.wright@foo.com', 'HR', 33000.00)");

			// Print the number of rows affected
			System.out.println("Rows affected: " + rowsAffected);

			// 6. Verify this by getting a list of employees
			myRs = myStmt.executeQuery("select * from employees order by last_name");

			// 7. Process the result set
			while (myRs.next()) {
				System.out.println(
						myRs.getString("last_name") +
								", " + myRs.getString("first_name"));
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
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
