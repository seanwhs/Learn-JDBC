package learn.metadata;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

public class MetaDataBasicInfo {

	public static void main(String[] args) throws SQLException {

		Connection myConn = null;

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


			// 4. Get metadata
			DatabaseMetaData databaseMetaData = myConn.getMetaData();

			// 5. Display info about database
			System.out.println("Product name: " + databaseMetaData.getDatabaseProductName());
			System.out.println("Product version: " + databaseMetaData.getDatabaseProductVersion());
			System.out.println();

			// 6. Display info about JDBC Driver
			System.out.println("JDBC Driver name: " + databaseMetaData.getDriverName());
			System.out.println("JDBC Driver version: " + databaseMetaData.getDriverVersion());

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myConn);
		}
	}

	private static void close(Connection myConn)
			throws SQLException {

		if (myConn != null) {
			myConn.close();
		}
	}
}