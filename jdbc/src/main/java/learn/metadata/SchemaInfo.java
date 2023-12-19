package learn.metadata;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import learn.properties_file.ConnectionDemo;

public class SchemaInfo {

	public static void main(String[] args) throws SQLException {

		String catalog = null;
		String schemaPattern = null;
		String tableNamePattern = null;
		String columnNamePattern = null;
		String[] types = null;

		Connection myConn = null;
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

			// 4. Get metadata
			DatabaseMetaData databaseMetaData = myConn.getMetaData();

			// 5. Get list of tables
			System.out.println("List of Tables");
			System.out.println("--------------");

			myRs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern,
					types);

			while (myRs.next()) {
				System.out.println(myRs.getString("TABLE_NAME"));
			}

			// 6. Get list of columns
			System.out.println("\n\nList of Columns");
			System.out.println("--------------");

			myRs = databaseMetaData.getColumns(
					catalog, schemaPattern, "employees", columnNamePattern);

			while (myRs.next()) {
				System.out.println(myRs.getString("COLUMN_NAME"));
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myConn, myRs);
		}
	}

	private static void close(Connection myConn, ResultSet myRs)
			throws SQLException {

		if (myRs != null) {
			myRs.close();
		}

		if (myConn != null) {
			myConn.close();
		}
	}

}
