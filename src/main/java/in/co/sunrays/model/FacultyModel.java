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
import in.co.sunrays.beans.FacultyBean;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.beans.SubjectBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.util.JDBCDataSource;

/**
 * JDBC Implementation of Faculty Model
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
public class FacultyModel {
	private static Logger log = Logger.getLogger(FacultyModel.class);

	/**
	 * Find next PK of Faculty
	 * 
	 * @throws DatabaseException
	 */
	public long nextPk() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_FACULTY");
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
	 * Find Faculty by pk
	 * 
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public FacultyBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Start");
		FacultyBean bean = null;
		Connection conn = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE ID=?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDOB(rs.getDate(5));
				bean.setEmailId(rs.getString(6));
				bean.setMobileNo(rs.getString(7));
				bean.setQualification(rs.getString(8));
				bean.setCollegeId(rs.getLong(9));
				bean.setCollegeName(rs.getString(10));
				bean.setSubjectId(rs.getLong(11));
				bean.setSubject(rs.getString(12));
				bean.setCourseId(rs.getLong(13));
				bean.setCourseName(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDateTime(rs.getTimestamp(17));
				bean.setModifiedDateTime(rs.getTimestamp(18));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;

	}

	/**
	 * Find Faculty By EmailId
	 * 
	 * @param Email
	 * @return
	 * @throws ApplicationException
	 */
	public FacultyBean findByEmailId(String Email) throws ApplicationException {
		log.debug("Model findByEmailId Start");
		FacultyBean bean = null;
		Connection conn = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE EMAIL_ID=?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, Email);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDOB(rs.getDate(5));
				bean.setEmailId(rs.getString(6));
				bean.setMobileNo(rs.getString(7));
				bean.setQualification(rs.getString(8));
				bean.setCollegeId(rs.getLong(9));
				bean.setCollegeName(rs.getString(10));
				bean.setSubjectId(rs.getLong(11));
				bean.setSubject(rs.getString(12));
				bean.setCourseId(rs.getLong(13));
				bean.setCourseName(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDateTime(rs.getTimestamp(17));
				bean.setModifiedDateTime(rs.getTimestamp(18));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting faculty by emailid");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByEmailId Start");
		return bean;

	}

	/**
	 * Add Faculty
	 * 
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DatabaseException
	 * @throws DuplicateRecordException
	 */
	public long add(FacultyBean bean) throws ApplicationException, DatabaseException, DuplicateRecordException {

		log.debug("model add started");
		Connection conn = null;
		CollegeModel cModel = new CollegeModel();
		CollegeBean collegeBean = cModel.findByPK(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		CourseModel csModel = new CourseModel();
		CourseBean courseBean = csModel.findByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectModel submodel = new SubjectModel();
		SubjectBean subjectBean = submodel.findByPK(bean.getSubjectId());
		bean.setSubject(subjectBean.getName());

		FacultyBean duplicateName = findByEmailId(bean.getEmailId());
		long pk = nextPk();
		System.out.println("next pk in faculty add is" + pk);
		if (duplicateName != null) {
			throw new DuplicateRecordException("Faculty already exists");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			System.out.println("in add ");
			PreparedStatement pstmt = conn
					.prepareCall("INSERT INTO ST_FACULTY VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getGender());
			pstmt.setDate(5, new java.sql.Date(bean.getDOB().getTime()));
			pstmt.setString(6, bean.getEmailId());
			pstmt.setString(7, bean.getMobileNo());
			pstmt.setString(8, bean.getQualification());
			pstmt.setLong(9, bean.getCollegeId());
			pstmt.setString(10, bean.getCollegeName());
			pstmt.setLong(11, bean.getSubjectId());
			pstmt.setString(12, bean.getSubject());
			pstmt.setLong(13, bean.getCourseId());
			pstmt.setString(14, bean.getCourseName());
			pstmt.setString(15, bean.getCreatedBy());
			pstmt.setString(16, bean.getModifiedBy());
			pstmt.setTimestamp(17, bean.getCreatedDateTime());
			pstmt.setTimestamp(18, bean.getModifiedDateTime());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				// ex.printStackTrace();
				log.error(ex);
				throw new ApplicationException("add : rollback exception" + ex.getMessage());
			}
			throw new ApplicationException("Exception:Exception in add faculty");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}

	/**
	 * Update faculty
	 * 
	 * @param bean
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("model update started");
		Connection conn = null;
		FacultyBean beanExist = findByEmailId(bean.getEmailId());
		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Email id already exists");
		}
		CollegeModel cModel = new CollegeModel();
		CollegeBean collegeBean = cModel.findByPK(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		CourseModel csModel = new CourseModel();
		CourseBean courseBean = csModel.findByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectModel submodel = new SubjectModel();
		SubjectBean subjectBean = submodel.findByPK(bean.getSubjectId());
		bean.setSubject(subjectBean.getName());
		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE ST_FACULTY SET FIRST_NAME=?,LAST_NAME=?,GENDER=?,DOB=?,EMAIL_ID=?,MOBILE_NO=?,QUALIFICATION=?,COLLEGE_ID=?,COLLEGE_NAME=?,SUBJECT_ID=?,SUBJECT_NAME=?,COURSE_ID=?,COURSE_NAME=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setString(3, bean.getGender());
			pstmt.setDate(4, new java.sql.Date(bean.getDOB().getTime()));
			pstmt.setString(5, bean.getEmailId());
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getQualification());
			pstmt.setLong(8, bean.getCollegeId());
			pstmt.setString(9, bean.getCollegeName());
			pstmt.setLong(10, bean.getSubjectId());
			pstmt.setString(11, bean.getSubject());
			pstmt.setLong(12, bean.getCourseId());
			pstmt.setString(13, bean.getCourseName());
			pstmt.setString(14, bean.getCreatedBy());
			pstmt.setString(15, bean.getModifiedBy());
			pstmt.setTimestamp(16, bean.getCreatedDateTime());
			pstmt.setTimestamp(17, bean.getModifiedDateTime());
			pstmt.setLong(18, bean.getId());
			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database exception" + e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception update rollback exception" + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating faculty" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("model update end");
	}

	/**
	 * Deletes a faculty
	 * 
	 * @param bean
	 * @throws ApplicationException
	 */
	public boolean deleteFaculty(String[] ids) throws ApplicationException {

		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;
			
		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				con.setAutoCommit(false); // Begin transaction
				for (String id : ids) {
					String query = "DELETE FROM ST_FACULTY WHERE ID=" + id + ";";
					log.debug("Query :: " + query);
					pstmt = con.prepareStatement(query);
					int recordCount = pstmt.executeUpdate();
					System.out.println("Record Count :: " + recordCount);
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
			throw new ApplicationException("Exception :: Exception in Delete Faculty");
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

	/**
	 * Search Faculty
	 * 
	 * @param bean : Search Parameters
	 * @throws ApplicationException DatabaseException
	 */
	public List search(FacultyBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search with pagination
	 * 
	 * @return list : List of Roles
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 * 
	 * @throws DatabaseException
	 */

	public List<FacultyBean> search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE 1=1");
		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" AND FIRST_NAME like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND COURSE_NAME like '" + bean.getCourseName() + "%'");
			}
			if (bean.getCourseId() > 0) {
				sql.append(" AND COURSE_ID = " + bean.getCourseId());
			}
			if (bean.getEmailId() != null && bean.getEmailId().length() > 0) {
				sql.append(" AND EMAIL_ID like '" + bean.getEmailId() + "%'");
			}
			if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
				sql.append(" AND MOBILE_NO like '" + bean.getMobileNo() + "%'");
			}

			if (bean.getQualification() != null && bean.getQualification().length() > 0) {
				sql.append(" AND QUALIFICATION like '" + bean.getQualification() + "%'");
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND COLLEGE_NAME like '" + bean.getCourseName() + "%'");
			}
			if (bean.getCollegeId() > 0) {
				sql.append(" AND COLLEGE_ID = " + bean.getCollegeId());
			}
		}

		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" Limit " + pageNo + ", " + pageSize);
			
		}
		System.out.println("lis sql" + sql);
		ArrayList<FacultyBean> list = new ArrayList<FacultyBean>();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDOB(rs.getDate(5));
				bean.setEmailId(rs.getString(6));
				bean.setMobileNo(rs.getString(7));
				bean.setQualification(rs.getString(8));
				bean.setCollegeId(rs.getLong(9));
				bean.setCollegeName(rs.getString(10));
				bean.setSubjectId(rs.getLong(11));
				bean.setSubject(rs.getString(12));
				bean.setCourseId(rs.getLong(13));
				bean.setCourseName(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDateTime(rs.getTimestamp(17));
				bean.setModifiedDateTime(rs.getTimestamp(18));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search faculty");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	/**
	 * gets list of faculty
	 * 
	 * @return list : list of Faculty
	 * @throws ApplicationException
	 */
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get a list of User with Pagination
	 * 
	 * @param pageNo:   current pageNo
	 * @param pageSize: Size of page
	 * @return list
	 * @throws ApplicationException
	 */
	public List<FacultyBean> list(int pageNo, int pageSize) throws ApplicationException {
	
		ArrayList<FacultyBean> list = new ArrayList<FacultyBean>();
		StringBuffer sql = new StringBuffer("select * from ST_FACULTY ");
		
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append("limit " + pageNo + "," + pageSize);
		}
		System.out.println("Query :" + sql);
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				FacultyBean bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDOB(rs.getDate(5));
				bean.setEmailId(rs.getString(6));
				bean.setMobileNo(rs.getString(7));
				bean.setQualification(rs.getString(8));
				bean.setCollegeId(rs.getLong(9));
				bean.setCollegeName(rs.getString(10));
				bean.setSubjectId(rs.getLong(11));
				bean.setSubject(rs.getString(12));
				bean.setCourseId(rs.getLong(13));
				bean.setCourseName(rs.getString(14));
				bean.setCreatedBy(rs.getString(15));
				bean.setModifiedBy(rs.getString(16));
				bean.setCreatedDateTime(rs.getTimestamp(17));
				bean.setModifiedDateTime(rs.getTimestamp(18));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}
}
