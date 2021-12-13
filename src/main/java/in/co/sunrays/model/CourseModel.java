/**
 * 
 */
package in.co.sunrays.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import in.co.sunrays.beans.CourseBean;
import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.util.JDBCDataSource;

/**
 * JDBC Implementation of Course Model
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public class CourseModel {
	private static org.apache.log4j.Logger log=org.apache.log4j.Logger.getLogger(CourseModel.class);
	
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
	
	
	
	/**
	 * Find Next Pk of Course
	 * 
	 * @throws DatabaseException
	 */
	public Integer nextPK() throws DatabaseException {
		 log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			 conn = getConnection();
		//	conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_COURSE");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			 log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model nextPK End");
		return pk + 1;
	}

	/**
	 * Add a Course
	 * 
	 * @param bean
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * @throws DatabaseException
	 * 
	 */

	public long addCourse(CourseBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		long pk = 0;
		CourseBean duplicateCourse = findByName(bean.getName());
		if (duplicateCourse != null) {
			throw new DuplicateRecordException("course already exist");
		}
		try {
			 conn = getConnection();
		//	conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COURSE VALUES(?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDuration());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDateTime());
			pstmt.setTimestamp(8, bean.getModifiedDateTime());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception: add Rollback Exception");
			}
			throw new ApplicationException("Exception : Exception in add course");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	/**
	 * Delete a Course
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(CourseBean bean) throws ApplicationException {
		 log.debug("Model delete Started");
		Connection conn = null;
		try {
			
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COURSE WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			 log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model delete Started");
	}

	/**
	 * Update a Course
	 * 
	 * @param bean
	 * @throws ApplicationException,
	 *             DuplicateRecordException
	 **/
	public void updateCourse(CourseBean bean) throws ApplicationException, DuplicateRecordException {
		 log.debug("Model update Started");
		Connection conn = null;

		CourseBean duplicateCourse = findByName(bean.getName());
		// Check if updated Role already exist
		if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
			throw new DuplicateRecordException("Course already exists");
		}
		try {
		//	 conn = getConnection();
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE ST_COURSE SET NAME=?,DURATION=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDuration());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDateTime());
			pstmt.setTimestamp(7, bean.getModifiedDateTime());
			pstmt.setLong(8, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			 log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Course ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model update End");
	}

	/**
	 * Find Course by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */
	public CourseBean findByName(String name) throws ApplicationException {
		 log.debug("Model findBy EmailId Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE NAME=?");
		CourseBean bean = null;
		Connection conn = null;
		try {
		//	 conn = getConnection();
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
			}
			rs.close();
		} catch (Exception e) {
			 log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model findBy EmailId End");
		return bean;
	}

	/**
	 * Find Course by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */
	public CourseBean findByPK(long pk) throws ApplicationException {
		 log.debug("Model findBy EmailId Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE ID=?");
		CourseBean bean = null;
		Connection conn = null;
		try {
		//	 conn = getConnection();
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
			}
			rs.close();
		} catch (Exception e) {
			 log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model findBy EmailId End");
		return bean;
	}

	/**
	 * Search Course
	 * 
	 * @param bean
	 *            : Search Parameters
	 * @throws DatabaseException
	 */
	public List search(CourseBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List<CourseBean> search(CourseBean bean, int pageNo, int pageSize) throws ApplicationException {
		 log.debug("Model Search Starts");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE 1=1");
		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND NAME like '" + bean.getName() + "%'");
			}
			if (bean.getDuration() != null && bean.getDuration().length() > 0) {
				sql.append(" AND DURATION like '" + bean.getDuration() + "%'");
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" AND DESCRIPTION like '" + bean.getDescription() + "%'");
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + ", " + pageSize);
		}
		System.out.println(sql);
		List<CourseBean> list = new ArrayList<CourseBean>();
		Connection conn = null;
		try {
		//	 conn = getConnection();
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
				list.add(bean);
			}
			rs.close();

		} catch (Exception e) {
			 log.error("Database Exception..", e);
			throw new ApplicationException("Exception: Exception in search Course");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model Search End");
		return list;
	}

	/**
	 * Get List of Course
	 * 
	 * @return list : List of Course
	 * @throws DatabaseException
	 */

	public List<CourseBean> list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Course with pagination
	 * 
	 * @return list : List of Role
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws ApplicationException
	 * @throws DatabaseException
	 */
	public List<CourseBean> list(int pageNo, int pageSize) throws ApplicationException {
		 log.debug("Model List Start");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + ", " + pageSize);
		}
		System.out.println(sql);
		ArrayList<CourseBean> list = new ArrayList<CourseBean>();
		Connection conn = null;
		try {
		//	 conn = getConnection();
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CourseBean bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
				list.add(bean);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception: Exception in getting List of Course");
			 
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		 log.debug("Model List End");
		return list;
	}



	public boolean deleteCourses(String[] ids) throws ApplicationException{
		
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				con.setAutoCommit(false); // Begin transaction
				for (String id : ids) {
					String query = "DELETE FROM ST_COURSE WHERE ID=" + id + ";";
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
			log.error("Exception :: Exception in Delete Course", e);
			throw new ApplicationException("Exception :: Exception in Delete Course");
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



	public ArrayList<String> getCourseDurationList() {
		ArrayList<String> courseDurationList = new ArrayList<String>();
		courseDurationList.add("1 year");
		courseDurationList.add("2 year");
		courseDurationList.add("3 year");
		courseDurationList.add("4 year");
		courseDurationList.add("5 year");
		courseDurationList.add("6 year");
		return courseDurationList ;		
	}



	public HashMap<String,String> getSemesterList() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("1", "1st Semester");
		map.put("2", "2nd Semester");
		map.put("3", "3rd Semester");
		map.put("4", "4th Semester");
		map.put("5", "5th Semester");
		map.put("6", "6th Semester");		
		return map;
	}

}
