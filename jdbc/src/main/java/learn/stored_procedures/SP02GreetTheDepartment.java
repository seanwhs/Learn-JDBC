// Test calling stored procedure with INOUT parameters
package learn.stored_procedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
 
public class SP02GreetTheDepartment {

	public static void main(String[] args) throws Exception {

		Connection myConn = null;
		CallableStatement myStmt = null;

		String dbUrl = "jdbc:mysql://localhost:3306/demo";
		String user = "root";		
		String pass = "Welcome_1";

		try {
			// Get a connection to database
			myConn = DriverManager.getConnection(dbUrl, user, pass);


			String theDepartment = "Engineering";
			
			// Prepare the stored procedure call
			myStmt = myConn
					.prepareCall("{call greet_the_department(?)}");

			// Set the parameters
			myStmt.registerOutParameter(1, Types.VARCHAR);
			myStmt.setString(1, theDepartment);

			// Call stored procedure
			System.out.println("Calling stored procedure.  greet_the_department('" + theDepartment + "')");
			myStmt.execute();
			System.out.println("Finished calling stored procedure");			
			
			// Get the value of the INOUT parameter
			String theResult = myStmt.getString(1);
			
			System.out.println("\nThe result = " + theResult);

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myConn, myStmt);
		}
	}

	private static void close(Connection myConn, Statement myStmt) throws SQLException {
		if (myStmt != null) {
			myStmt.close();
		}

		if (myConn != null) {
			myConn.close();
		}
	}
}
