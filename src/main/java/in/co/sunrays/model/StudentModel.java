package in.co.sunrays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;


public class StudentModel {
	private static Logger log = Logger.getLogger(StudentModel.class);

	/*
	 * private long getpk(StudentBean bean) { Connection con = null ;
	 * PreparedStatement pstmt = null; long id = 0; try { con =
	 * JDBCDataSource.getConnection(); pstmt =
	 * con.prepareStatement("SELECT ID FROM marksheet WHERE STUDENT_ID =?;");
	 * pstmt.setLong(1, bean.getId()); ResultSet resultSet = pstmt.executeQuery();
	 * 
	 * while (resultSet.next()) { id = resultSet.getLong("id");
	 * System.out.println("student id   " + id); } return id;
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return id; } finally { try {
	 * pstmt.close(); JDBCDataSource.closeConnection(con); } catch (SQLException se)
	 * { se.printStackTrace(); } } }
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
		//	e.printStackTrace();
			log.error("Exception : Exception in getting PK",e);
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

	public long getCollegeId(StudentBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		long id = 0;
		try {
			con = JDBCDataSource.getConnection();
			String query ="SELECT * FROM COLLEGE WHERE NAME='" + bean.getCollegeName() + "';";
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				id = resultSet.getLong("id");
			}

			return id;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return id;

	}

	public void addStudent(StudentBean bean) throws ApplicationException,DuplicateRecordException {

		Connection con = null;
		PreparedStatement pstmt = null;
		  StudentBean duplicateName = findByEmailId(bean.getEmail());  
		  if (duplicateName != null) { 
			  throw new DuplicateRecordException("Email ID Already Exists"); 
			}
		 
		try {
			con = JDBCDataSource.getConnection();
			pstmt = con.prepareStatement(" INSERT INTO STUDENT VALUES(?,?,?,?,?,?,?,?,?,?,?,?);");
			pstmt.setLong(1, nextPk("STUDENT", "ID"));
			pstmt.setLong(2, getCollegeId(bean));
			pstmt.setString(3, bean.getCollegeName());
			pstmt.setString(4, bean.getFirstName());
			pstmt.setString(5, bean.getLastName());
			pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(7, bean.getMobileNo());
			pstmt.setString(8, bean.getEmail());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDateTime());
			pstmt.setTimestamp(12, bean.getModifiedDateTime());

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
			throw new ApplicationException("Exception : Exception in add Student");

		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}
	

	public void updateStudent(StudentBean bean) throws ApplicationException,DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		StudentBean duplicateName = findByEmailId(bean.getEmail());  
		 
		 if (duplicateName != null && duplicateName.getId()!=bean.getId()) { 
			  throw new DuplicateRecordException("Email ID Already Exists");				  
		} 
		  
		try {
			con = JDBCDataSource.getConnection();
			String query ="UPDATE STUDENT SET COLLEGE_ID=?, COLLEGE_NAME= ?, FIRST_NAME=?, LAST_NAME=?, DATE_OF_BIRTH=?, MOBILE_NO=?, EMAIL=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID = ? ;";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			pstmt.setLong(1, bean.getCollegeId());
			pstmt.setString(2, bean.getCollegeName());
			pstmt.setString(3, bean.getFirstName());
			pstmt.setString(4, bean.getLastName());
			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getEmail());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getModifiedDateTime());
			pstmt.setLong(10, bean.getId());

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
			log.error("Exception in updating Student ",e);
			throw new ApplicationException("Exception in updating Student");

		} finally {
			try {
				if(pstmt!=null)
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public boolean deleteStudents(String[] ids) throws ApplicationException {
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				for (String id : ids) {
					String query = "DELETE FROM STUDENT WHERE ID=" + id + ";";
					log.debug("Query :: " + query);
					pstmt = con.prepareStatement(query);
					int recordCount = pstmt.executeUpdate();
				//	System.out.println("Record Count :: " + recordCount);
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
				log.error("Exception :: Delete Rollback Exception " , ex);
				throw new ApplicationException("Exception :: Delete Rollback Exception " + ex.getMessage());
			}
			log.error("Exception :: Exception in Delete Student" , e);
			throw new ApplicationException("Exception :: Exception in Delete Student");
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

	private StudentBean findByEmailId(String email) throws ApplicationException {
	       
        StringBuffer sql = new StringBuffer("SELECT * FROM STUDENT WHERE EMAIL=?");
        StudentBean student = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, email);
            log.debug(sql.toString());
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
            	
            	student = new StudentBean();
				student.setId(resultSet.getLong("ID"));
				student.setCollegeId(resultSet.getLong("COLLEGE_ID"));
				student.setCollegeName(resultSet.getString("COLLEGE_NAME"));
				student.setFirstName(resultSet.getString("FIRST_NAME"));
				student.setLastName(resultSet.getString("LAST_NAME"));
				student.setDob(resultSet.getDate("DATE_OF_BIRTH"));
				student.setMobileNo(resultSet.getString("MOBILE_NO"));
				student.setEmail(resultSet.getString("EMAIL"));
				student.setCreatedBy(resultSet.getString("CREATED_BY"));
				student.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				student.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				student.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
            }
            resultSet.close();
        } catch (Exception e) {
        	log.error("Exception : Database Exception in getting User by Email", e);
        	throw new ApplicationException( 		
                    "Exception : Database Exception in getting User by Email");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
     
        return student;
    }
	
	
	public ArrayList<StudentBean> studentList(int pageNo, int pageSize) throws ApplicationException {

		ArrayList<StudentBean> list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		StudentBean student = null;
		
		 StringBuffer query = new StringBuffer("SELECT * FROM STUDENT ");
		try {
			con = JDBCDataSource.getConnection();
			 
			if(pageSize>0) {
				if (pageNo > 1) {
					pageNo = (pageNo - 1) * 10;
				} else {
					pageNo = 0;
				}
				query =query.append("ORDER BY  MODIFIED_DATETIME DESC LIMIT " + pageNo + "," + pageSize
						+ ";");
			}
			
			log.debug("Query :: " + query);
			pstmt = con.prepareStatement(query.toString());
			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<StudentBean>();
			while (resultSet.next()) {

				/*
				 * long id = resultSet.getLong("ID"); long collegeId =
				 * resultSet.getLong("COLLEGE_ID"); String collegeName =
				 * resultSet.getString("COLLEGE_NAME"); String firstName =
				 * resultSet.getString("FIRST_NAME"); String lastName =
				 * resultSet.getString("LAST_NAME"); Date dob =
				 * resultSet.getDate("DATE_OF_BIRTH"); String mobileNo =
				 * resultSet.getString("MOBILE_NO"); String email =resultSet.getString("EMAIL");
				 * String createdBy = resultSet.getString("CREATED_BY"); String modifiedBy =
				 * resultSet.getString("MODIFIED_BY"); Timestamp createdDateTime =
				 * resultSet.getTimestamp("CREATED_DATETIME"); Timestamp modifiedDateTime
				 * =resultSet.getTimestamp("MODIFIED_DATETIME");
				 */
				/*
				 * System.out.println("student Id  " + id); System.out.println("collegeId  " +
				 * collegeId); System.out.println("collegeName  " + collegeName);
				 * System.out.println("firstName  " + firstName);
				 * System.out.println("lastName  " + lastName); System.out.println("dob  " +
				 * dob); System.out.println("mobileNo  " + mobileNo);
				 * System.out.println("email  " + email); System.out.println("createdBy  " +
				 * createdBy); System.out.println("modifiedBy  " + modifiedBy);
				 * System.out.println("createdDateTime  " + createdDateTime);
				 * System.out.println("modifiedDateTime  " + modifiedDateTime);
				 * 
				 */
				student = new StudentBean();
				student.setId(resultSet.getLong("ID"));
				student.setCollegeId(resultSet.getLong("COLLEGE_ID"));
				student.setCollegeName(resultSet.getString("COLLEGE_NAME"));
				student.setFirstName(resultSet.getString("FIRST_NAME"));
				student.setLastName(resultSet.getString("LAST_NAME"));
				student.setDob(resultSet.getDate("DATE_OF_BIRTH"));
				student.setMobileNo(resultSet.getString("MOBILE_NO"));
				student.setEmail(resultSet.getString("EMAIL"));
				student.setCreatedBy(resultSet.getString("CREATED_BY"));
				student.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				student.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				student.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));

				list.add(student);

			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Search Student");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return list ;
	}

	public StudentBean findByPK(long pk) throws ApplicationException {

		Connection con = JDBCDataSource.getConnection();
		PreparedStatement pstmt = null;
		StudentBean student = null;

		try {
			String query = "SELECT * FROM STUDENT WHERE ID=" + pk + ";";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				/*
				 * long id = resultSet.getLong("ID"); long collegeId =
				 * resultSet.getLong("COLLEGE_ID"); String collegeName =
				 * resultSet.getString("COLLEGE_NAME"); String firstName =
				 * resultSet.getString("FIRST_NAME"); String lastName =
				 * resultSet.getString("LAST_NAME"); Date dob =
				 * resultSet.getDate("DATE_OF_BIRTH"); String mobileNo =
				 * resultSet.getString("MOBILE_NO"); String email =resultSet.getString("EMAIL");
				 * String createdBy = resultSet.getString("CREATED_BY"); String modifiedBy =
				 * resultSet.getString("MODIFIED_BY"); Timestamp createdDateTime =
				 * resultSet.getTimestamp("CREATED_DATETIME"); Timestamp modifiedDateTime
				 * =resultSet.getTimestamp("MODIFIED_DATETIME");
				 */

				/*
				 * System.out.println("student Id  " + id); System.out.println("collegeId  " +
				 * collegeId); System.out.println("collegeName  " + collegeName);
				 * System.out.println("firstName  " + firstName);
				 * System.out.println("lastName  " + lastName); System.out.println("dob  " +
				 * dob); System.out.println("mobileNo  " + mobileNo);
				 * System.out.println("email  " + email); System.out.println("createdBy  " +
				 * createdBy); System.out.println("modifiedBy  " + modifiedBy);
				 * System.out.println("createdDateTime  " + createdDateTime);
				 * System.out.println("modifiedDateTime  " + modifiedDateTime);
				 */

				student = new StudentBean();
				student.setId(resultSet.getLong("ID"));
				student.setCollegeId(resultSet.getLong("COLLEGE_ID"));
				student.setCollegeName(resultSet.getString("COLLEGE_NAME"));
				student.setFirstName(resultSet.getString("FIRST_NAME"));
				student.setLastName(resultSet.getString("LAST_NAME"));
				student.setDob(resultSet.getDate("DATE_OF_BIRTH"));
				student.setMobileNo(resultSet.getString("MOBILE_NO"));
				student.setEmail(resultSet.getString("EMAIL"));
				student.setCreatedBy(resultSet.getString("CREATED_BY"));
				student.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				student.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				student.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));

			}
			resultSet.close();
			return student;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : Exception in getting User by pk");
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				log.error("Exception : SQLException");
				se.printStackTrace();
			}
		}
		// return student ;
	}

	public ArrayList<StudentBean> search(StudentBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		ArrayList<StudentBean> list = null;
		PreparedStatement pstmt = null;
		StudentBean student = null;

		String fname = bean.getFirstName();
		String lname = bean.getLastName();
		String clg = bean.getCollegeName();

		try {
			

			String query = "SELECT * FROM STUDENT ";

			if (fname.length() != 0 || lname.length() != 0 || clg.length() != 0) {
				query = query + "WHERE ";

				if (fname.length() != 0) {
					query = query + "first_name LIKE '%" + fname + "%'";
				} else {
					query = query + "first_name LIKE '%%'";
				}

				if (fname.length() != 0 || lname.length() != 0 || clg.length() != 0) {
					query = query + "AND LAST_NAME LIKE '%" + lname + "%'";
				} else {
					query = query + "AND LAST_NAME LIKE '%%'";
				}

				if (fname.length() != 0 || lname.length() != 0 || clg.length() != 0) {
					query = query + "AND college_name LIKE '%" + clg + "%'";
				} else {
					query = query + "AND college_name LIKE '%%';";
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
			list = new ArrayList<StudentBean>();
			while (resultSet.next()) {

				/*
				 * long id = resultSet.getLong("ID"); long collegeId =
				 * resultSet.getLong("COLLEGE_ID"); String collegeName =
				 * resultSet.getString("COLLEGE_NAME"); String firstName =
				 * resultSet.getString("FIRST_NAME"); String lastName =
				 * resultSet.getString("LAST_NAME"); Date dob =
				 * resultSet.getDate("DATE_OF_BIRTH"); String mobileNo =
				 * resultSet.getString("MOBILE_NO"); String email =resultSet.getString("EMAIL");
				 * String createdBy = resultSet.getString("CREATED_BY"); String modifiedBy =
				 * resultSet.getString("MODIFIED_BY"); Timestamp createdDateTime =
				 * resultSet.getTimestamp("CREATED_DATETIME"); Timestamp modifiedDateTime
				 * =resultSet.getTimestamp("MODIFIED_DATETIME");
				 * 
				 */
				/*
				 * System.out.println("student Id  " + id); System.out.println("collegeId  " +
				 * collegeId); System.out.println("collegeName  " + collegeName);
				 * System.out.println("firstName  " + firstName);
				 * System.out.println("lastName  " + lastName); System.out.println("dob  " +
				 * dob); System.out.println("mobileNo  " + mobileNo);
				 * System.out.println("email  " + email); System.out.println("createdBy  " +
				 * createdBy); System.out.println("modifiedBy  " + modifiedBy);
				 * System.out.println("createdDateTime  " + createdDateTime);
				 * System.out.println("modifiedDateTime  " + modifiedDateTime);
				 */

				student = new StudentBean();
				student.setId(resultSet.getLong("ID"));
				student.setCollegeId(resultSet.getLong("COLLEGE_ID"));
				student.setCollegeName(resultSet.getString("COLLEGE_NAME"));
				student.setFirstName(resultSet.getString("FIRST_NAME"));
				student.setLastName(resultSet.getString("LAST_NAME"));
				student.setDob(resultSet.getDate("DATE_OF_BIRTH"));
				student.setMobileNo(resultSet.getString("MOBILE_NO"));
				student.setEmail(resultSet.getString("EMAIL"));
				student.setCreatedBy(resultSet.getString("CREATED_BY"));
				student.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				student.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				student.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));

				list.add(student);

			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Student");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		// return list ;
	}

}
