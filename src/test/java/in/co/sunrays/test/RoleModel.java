package in.co.sunrays.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import in.co.sunrays.beans.RoleBean;

public class RoleModel {

	
	public Connection getConnection() {
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
	
	
	public long nextPk(String tableName, String columnName) {
		long pk = 0;
		PreparedStatement pstmt = null;
		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("SELECT MAX(" + columnName + ") FROM " + tableName + ";");
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				pk = resultSet.getLong("max(id)") + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return pk;
	}
	public void addRole(RoleBean bean) {
			
		PreparedStatement pstmt = null;
		try {
			Connection con = getConnection();
			
			System.out.println(nextPk("role", "id"));
			pstmt = con.prepareStatement("INSERT INTO role VALUES(?,?, ?,?,?,?,?);");
			pstmt.setLong(1, nextPk("role", "id"));
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, null);
			pstmt.setString(5, null);
			pstmt.setDate(6, null);
			pstmt.setDate(7, null);

			int recordCount = pstmt.executeUpdate();
			con.commit();
			System.out.println("successfully inserted " + recordCount + " record");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		
		
	}

	
	
	 
}
