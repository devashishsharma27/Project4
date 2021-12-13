package in.co.sunrays.model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import in.co.sunrays.beans.CollegeBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;

/**
 * JDBC Implementation of CollegeModel
 *
 * @author Devashish Sharma
 * @version 1.0
 *
 */
public class CollegeModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);

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

	public ArrayList<String> getCollegeList() {
		PreparedStatement pstmt = null;
		ArrayList<String> list = null;
		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("SELECT * FROM COLLEGE");
			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<String>();
			while (resultSet.next()) {

				list.add(resultSet.getString("NAME").toUpperCase());
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
		return list;
	}

	public ArrayList<CollegeBean> collegeList(int pageNo, int pageSize) throws ApplicationException {
		ArrayList<CollegeBean> list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		CollegeBean college = null;
		try {
			con = JDBCDataSource.getConnection();
			if (pageNo > 1) {
				pageNo = (pageNo - 1) * 10;
			} else {
				pageNo = 0;
			}
//			String query = "SELECT * FROM COLLEGE ORDER BY  MODIFIED_DATETIME DESC LIMIT " + pageNo + "," + pageSize+ ";";
			StringBuffer query = new StringBuffer("SELECT * FROM COLLEGE ORDER BY  MODIFIED_DATETIME DESC");
		
			if (pageSize > 0) {	           
				query.append(" LIMIT " + pageNo + "," + pageSize
						+ ";");
	        }
			
			log.debug("Query :: " + query);
			pstmt = con.prepareStatement(query.toString());
			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<CollegeBean>();
			while (resultSet.next()) {
				college = new CollegeBean();
				college.setId(resultSet.getLong("ID"));
				college.setName(resultSet.getString("NAME"));
				college.setAddress(resultSet.getString("ADDRESS"));
				college.setState(resultSet.getString("STATE"));
				college.setCity(resultSet.getString("CITY"));
				college.setPhoneNo(resultSet.getString("PHONE_NO"));
				college.setCreatedBy(resultSet.getString("CREATED_BY"));
				college.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				college.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				college.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(college);
			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Search College");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}
	  /**
     * Delete a College
     *
     * @param bean
     * @throws DatabaseException
     */
	public boolean deleteColleges(String[] ids) throws ApplicationException {
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				for (String id : ids) {
					String query = "DELETE FROM COLLEGE WHERE ID=" + id + ";";
					log.debug("Query :: " + query);
					pstmt = con.prepareStatement(query);
					int recordCount = pstmt.executeUpdate();
					// System.out.println("Record Count :: " + recordCount);
					if (recordCount < 1) {
						flag = false;
					}
				}
			}

			if (flag) {
				con.commit();
				log.info("Successfully Deleted " + ids.length + " Record");
			}

		} catch (Exception e) {
			flag = false;
			try {
				con.rollback();
			} catch (Exception ex) {
				log.error("Exception :: Delete Rollback Exception ", ex);
				throw new ApplicationException("Exception :: Delete Rollback Exception " + ex.getMessage());
			}
			log.error("Exception :: Exception in Delete Role", e);
			throw new ApplicationException("Exception :: Exception in Delete College");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return flag;
	}

	public ArrayList<CollegeBean> search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		ArrayList<CollegeBean> list = null;
		PreparedStatement pstmt = null;
		CollegeBean college = null;
		try {
			String collegeName = bean.getName();
			String stateName = bean.getState();
			String cityName = bean.getCity();

			String query = "SELECT * FROM COLLEGE ";

			if (collegeName.length() != 0 || stateName.length() != 0 || cityName.length() != 0) {
				query = query + "WHERE ";

				if (collegeName.length() != 0) {
					query = query + "NAME LIKE '%" + collegeName + "%'";
				} else {
					query = query + "NAME LIKE '%%'";
				}

				if (collegeName.length() != 0 || stateName.length() != 0 || cityName.length() != 0) {
					query = query + "AND STATE LIKE '%" + stateName + "%'";
				} else {
					query = query + "AND STATE LIKE '%%'";
				}

				if (collegeName.length() != 0 || stateName.length() != 0 || cityName.length() != 0) {
					query = query + "AND CITY LIKE '%" + cityName + "%'";
				} else {
					query = query + "AND CITY LIKE '%%';";
				}

			}

			query = query + " ORDER BY  MODIFIED_DATETIME DESC ";

			if (pageNo > 0) {
				pageNo = (pageNo - 1) * 10;
			}
			// else { pageNo=1 ; }

			query = query + "LIMIT " + pageNo + "," + pageSize;
			log.debug("Search Query :: " + query);
			pstmt = con.prepareStatement(query);

			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<CollegeBean>();
			while (resultSet.next()) {
				college = new CollegeBean();
				college.setId(resultSet.getLong("ID"));
				college.setName(resultSet.getString("NAME"));
				college.setAddress(resultSet.getString("ADDRESS"));
				college.setState(resultSet.getString("STATE"));
				college.setCity(resultSet.getString("CITY"));
				college.setPhoneNo(resultSet.getString("PHONE_NO"));
				college.setCreatedBy(resultSet.getString("CREATED_BY"));
				college.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				college.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				college.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(college);
			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Role");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}
    /**
     * Find User by College
     *
     * @param pk
     *            : get parameter
     * @return bean
     * @throws DatabaseException
     */
	public CollegeBean findByPK(long pk) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		PreparedStatement pstmt = null;
		CollegeBean college = null;

		try {
			String query = "SELECT * FROM COLLEGE WHERE ID=" + pk + ";";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				college = new CollegeBean();
				college.setId(resultSet.getLong("ID"));
				college.setName(resultSet.getString("NAME"));
				college.setAddress(resultSet.getString("ADDRESS"));
				college.setState(resultSet.getString("STATE"));
				college.setCity(resultSet.getString("CITY"));
				college.setPhoneNo(resultSet.getString("PHONE_NO"));
				college.setCreatedBy(resultSet.getString("CREATED_BY"));
				college.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				college.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				college.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
			}
			resultSet.close();
			return college;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : Exception in getting College by pk");
			throw new ApplicationException("Exception : Exception in getting College by pk");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				log.error("Exception : SQLException");
				se.printStackTrace();
			}
		}

	}
	 /**
     * Find next PK of College
     *
     * @throws DatabaseException
     */
	public long nextPk(String tableName, String columnName) throws DatabaseException {
		long pk = 0;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = JDBCDataSource.getConnection();
			String query = "SELECT MAX(" + columnName + ") FROM " + tableName + ";";
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				pk = resultSet.getLong("max(id)") + 1;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : Exception in getting PK", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return pk;
	}
	 /**
     * Find User by College
     *
     * @param login
     *            : get parameter
     * @return bean
     * @throws DatabaseException
     */
	private CollegeBean findByName(String name) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM COLLEGE WHERE NAME=?");
		CollegeBean college = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			log.debug(sql.toString());
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				college = new CollegeBean();
				college.setId(resultSet.getLong("ID"));
				college.setName(resultSet.getString("NAME"));
				college.setAddress(resultSet.getString("ADDRESS"));
				college.setState(resultSet.getString("STATE"));
				college.setCity(resultSet.getString("CITY"));
				college.setPhoneNo(resultSet.getString("PHONE_NO"));
				college.setCreatedBy(resultSet.getString("CREATED_BY"));
				college.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				college.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				college.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
			}
			resultSet.close();
		} catch (Exception e) {
			log.error("Exception : Database Exception in getting College by Name", e);
			throw new ApplicationException("Exception : Database Exception in getting College by Name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return college;
	}

	public void addStudent(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		CollegeBean duplicateCollege = findByName(bean.getName());
		if (duplicateCollege != null) {
			throw new DuplicateRecordException("College Name Already Exists");
		}
		try {
			con = JDBCDataSource.getConnection();
			pstmt = con.prepareStatement("INSERT INTO COLLEGE VALUES(?,?,?,?,?,?,?,?,?,?);");
			pstmt.setLong(1, nextPk("COLLEGE", "ID"));
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getCity());
			pstmt.setString(6, bean.getPhoneNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDateTime());
			pstmt.setTimestamp(10, bean.getModifiedDateTime());

			int recordCount = pstmt.executeUpdate();
			con.commit();
			log.info("Successfully Inserted " + recordCount + " Record");

		} catch (Exception e) {
			// e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Role");

		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public void updateRole(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		CollegeBean duplicataRole = findByName(bean.getName());

		if (duplicataRole != null && duplicataRole.getId() != bean.getId()) {
			throw new DuplicateRecordException("College Name Already Exists");
		}

		try {
			con = JDBCDataSource.getConnection();
			String query = "UPDATE COLLEGE SET NAME= ?, STATE=?, ADDRESS=?, CITY=?, PHONE_NO=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID = ? ;";

			log.debug(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getState());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getModifiedDateTime());
			pstmt.setLong(8, bean.getId());

			int recordCount = pstmt.executeUpdate();
			con.commit();
			log.info("Successfully Updated " + recordCount + " Record");
		} catch (Exception e) {
			// e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception ex) {
				log.error("Exception : Delete rollback exception ", ex);
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			log.error("Exception in Updating College ", e);
			throw new ApplicationException("Exception in Updating College");

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

}
