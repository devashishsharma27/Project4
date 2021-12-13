package in.co.sunrays.util;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import in.co.sunrays.exception.ApplicationException;

/**
 * 
 * JDBCDataSource is a DataBase Connection Pool(DCP)
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */

public final class JDBCDataSource {
	/**
	 * JDBC DataBase Connection Pool(DCP)
	 */
	private static JDBCDataSource datasource = null;
	private ComboPooledDataSource cpds = null;

	private JDBCDataSource() {
	}

	/**
	 * Create instance of Connection Pool
	 * 
	 * @return
	 */
	public static JDBCDataSource getInstance() {
		if (datasource == null) {
			ResourceBundle rb = ResourceBundle.getBundle("in/co/sunrays/bundle/system");
			String driverClass=rb.getString("driver");
			String jdbcUrl=rb.getString("url");
			String user=rb.getString("username");
			String pass=rb.getString("password");
			//int acquireincrement=Integer.parseInt(rb.getString("acquireincrement"));
			//System.out.println("acquire increment is : "+acquireincrement);
			datasource = new JDBCDataSource();
			datasource.cpds=new ComboPooledDataSource();
			try {
				datasource.cpds.setDriverClass(driverClass);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			datasource.cpds.setJdbcUrl(jdbcUrl);
			datasource.cpds.setUser(user);
			datasource.cpds.setPassword(pass);
			datasource.cpds.setInitialPoolSize(new Integer((String)rb.getString("initialPoolSize")));
			datasource.cpds.setAcquireIncrement(new Integer((String)rb.getString("acquireincrement")));
			datasource.cpds.setMaxPoolSize(new Integer((String)rb.getString("maxPoolSize")));
			datasource.cpds.setMaxIdleTime(DataUtility.getInt(rb.getString("timeout")));
			datasource.cpds.setMinPoolSize(new Integer((String)rb.getString("minPoolSize")));
			}
		return datasource;
	}

	/**
	 * Gets the Connection from ComboPooledDataSource
	 * 
	 * @return Connection
	 * 
	 */
	public static Connection getConnection() throws Exception {
		return getInstance().cpds.getConnection();
	}

	/**
	 * Closes a Connection
	 * 
	 * @param Connection
	 * @throws Exception
	 * 
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
	}

	public static void trnRollback(Connection connection) throws ApplicationException {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new ApplicationException(ex.toString());
			}
		}
	}
}