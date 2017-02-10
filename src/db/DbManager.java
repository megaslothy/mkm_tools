package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {

	public static Connection getConnection() {
		Connection conn = null;

		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/mkm","root","");
		    System.out.println("Finalizado OK");
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    System.out.println("Finalizado con errores");
		}
		return conn;
	}
}