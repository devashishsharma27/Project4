/**
 * 
 */
package in.co.sunrays.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.CollegeBean;
import in.co.sunrays.beans.CourseBean;
import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.beans.SubjectBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.util.JDBCDataSource;

public class SubjectModel {
	Logger log = Logger.getLogger(SubjectModel.class);

	public Long nextPK() throws DatabaseException {
		log.debug("SubjectModel nextPK Started");
		Connection conn = null;
		Long pk = 0L;
		try {
			conn = JDBCDataSource.getConnection();
			System.out.println("Connection Succesfully Establish");

			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from ST_SUBJECT");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error(e);
			throw new DatabaseException("Exception in Subject getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("SUBJECTModel nextPK End");
		return pk + 1;
	}

	/**
	 * Add a Subject
	 * 
	 * @param bean
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * @throws DatabaseException 
	 */

	public long addSubject(SubjectBean bean) throws ApplicationException, DuplicateRecordException, DatabaseException {
		Connection conn = null;
		long pk = nextPK();
		CourseModel cModel = new CourseModel();
		CourseBean courseBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getName());
		SubjectBean duplicateSubject = findByName(bean.getName());

		if (duplicateSubject != null && bean.getCourseId() == duplicateSubject.getCourseId()) {
			throw new DuplicateRecordException("subject already exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_SUBJECT VALUES(?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDateTime());
			pstmt.setTimestamp(7, bean.getModifiedDateTime());
			pstmt.setString(8, bean.getCourseName());
			pstmt.setLong(9, bean.getCourseId());
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
			throw new ApplicationException("Exception : Exception in add subject");
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
	public boolean deleteSubjects(String[] ids) throws ApplicationException {
		
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				con.setAutoCommit(false); // Begin transaction
				for (String id : ids) {
					String query = "DELETE FROM ST_SUBJECT WHERE ID=" + id + ";";
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
			log.error("Exception :: Exception in Delete Subject", e);
			throw new ApplicationException("Exception :: Exception in Delete Subject");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return flag;	
		
		
		
		/*
		 * log.debug("Model delete Started"); Connection conn = null; try { conn =
		 * JDBCDataSource.getConnection(); conn.setAutoCommit(false); // Begin
		 * transaction PreparedStatement pstmt =
		 * conn.prepareStatement("DELETE FROM ST_SUBJECT WHERE ID=?"); pstmt.setLong(1,
		 * bean.getId()); pstmt.executeUpdate(); conn.commit(); // End transaction
		 * pstmt.close();
		 * 
		 * } catch (Exception e) { log.error("Database Exception..", e); try {
		 * conn.rollback(); } catch (Exception ex) { throw new
		 * ApplicationException("Exception : Delete rollback exception " +
		 * ex.getMessage()); } throw new
		 * ApplicationException("Exception : Exception in delete Subject"); } finally {
		 * JDBCDataSource.closeConnection(conn); } log.debug("Model delete Started");
		 */
	}

	/**
	 * Update a Course
	 * 
	 * @param bean
	 * @throws ApplicationException,
	 *             DuplicateRecordException
	 **/
	public void updateSubject(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		CourseModel cModel = new CourseModel();
		CourseBean courseBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getName());
		SubjectBean beanExist = findByName(bean.getName());

		// System.out.println("bean id " + bean.getId() + "exis id" +
		// beanExist.getId());

		// System.out.println("bean cid " + bean.getCourseId() + "exis cid" +beanExist.getCourseId());

		// Check if updated Roll no already exist
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Subject is already exist");
		}
		
		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE ST_SUBJECT SET NAME=?,COURSE_NAME=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=?,COURSE_ID=? WHERE ID=?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getCourseName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDateTime());
			pstmt.setTimestamp(7, bean.getModifiedDateTime());
			pstmt.setLong(8, bean.getCourseId());
			pstmt.setLong(9, bean.getId());

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
			throw new ApplicationException("Exception in updating subject ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	/**
	 * Deletes a Marksheet
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(StudentBean bean) throws ApplicationException {

		log.debug("Model delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_STUDENT WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			System.out.println("Deleted Subject");
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {
			log.error(e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				log.error(ex);
				throw new ApplicationException("Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in delete subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model delete End");
	}

	/**
	 * Find Subject by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public SubjectBean findByName(String name) throws ApplicationException {
		log.debug("Model findBy EmailId Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE NAME=?");
		SubjectBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDateTime(rs.getTimestamp(6));
				bean.setModifiedDateTime(rs.getTimestamp(7));
				bean.setCourseName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting Subject by Name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBy EmailId End");
		return bean;
	}

	/**
	 * Find Subject by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public SubjectBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE ID=?");
		SubjectBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDateTime(rs.getTimestamp(6));
				bean.setModifiedDateTime(rs.getTimestamp(7));
				bean.setCourseName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting Subject by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}

	/**
	 * Search Subject
	 * 
	 * @param bean
	 *            : Search Parameters
	 * @throws DatabaseException
	 */
	public List search(SubjectBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Students with pagination
	 * 
	 * @return list : List of Roles
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * @throws DatabaseException
	 */
	public List<SubjectBean> search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getCourseId() > 0) {
				sql.append(" AND COURSE_ID = " + bean.getCourseId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND NAME like '" + bean.getName() + "%'");
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" AND DESCRIPTION like '" + bean.getDescription() + "%'");
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND COURSE_NAME like '" + bean.getCourseName() + "%'");
			}

		}
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}
		ArrayList<SubjectBean> list = new ArrayList<SubjectBean>();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDateTime(rs.getTimestamp(6));
				bean.setModifiedDateTime(rs.getTimestamp(7));
				bean.setCourseName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;

	}

	/**
	 * Get List of Role
	 * 
	 * @return list : List of Role
	 * @throws DatabaseException
	 */

	public List<SubjectBean> list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Role with pagination
	 * 
	 * @return list : List of Role
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */

	public List<SubjectBean> list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList<SubjectBean> list = new ArrayList<SubjectBean>();
		StringBuffer sql = new StringBuffer("select * from ST_SUBJECT");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SubjectBean bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDateTime(rs.getTimestamp(6));
				bean.setModifiedDateTime(rs.getTimestamp(7));
				bean.setCourseName(rs.getString(8));
				bean.setCourseId(rs.getLong(9));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of Subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}

}
