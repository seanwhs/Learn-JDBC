package learn.metadata;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

public class ResultSetDemo {

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


			// 2. Run query
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select id, last_name, first_name, salary from employees");

			// 3. Get result set metadata
			ResultSetMetaData rsMetaData = myRs.getMetaData();

			// 4. Display info
			int columnCount = rsMetaData.getColumnCount();
			System.out.println("Column count: " + columnCount + "\n");

			for (int column = 1; column <= columnCount; column++) {
				System.out.println("Column name: " + rsMetaData.getColumnName(column));
				System.out.println("Column type name: " + rsMetaData.getColumnTypeName(column));
				System.out.println("Is Nullable: " + rsMetaData.isNullable(column));
				System.out.println("Is Auto Increment: " + rsMetaData.isAutoIncrement(column) + "\n");
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	private static void close(Connection myConn, Statement myStmt, ResultSet myRs)
			throws SQLException {

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
