package in.co.sunrays.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class JDBCDataSource {

	
	public JDBCDataSource() {
		
	}
	
	public static Connection getConnection() {
		Connection con = null;
		try {
			ResourceBundle rbObj = ResourceBundle.getBundle("config");
			Class.forName(rbObj.getString("driver"));
			con = DriverManager.getConnection(rbObj.getString("url"), rbObj.getString("username"),
					rbObj.getString("password"));
			con.setAutoCommit(false);
		} catch (Exception e) {

		}
		return con;
	}
	
	public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
    }

	
}
