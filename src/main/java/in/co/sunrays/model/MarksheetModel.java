package in.co.sunrays.model;

import java.sql.Connection;

import java.sql.DriverManager;

import in.co.sunrays.beans.CollegeBean;
import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
/**
 * JDBC Implementation of Marksheet Model
 *
 * @author Devashish Sharma
 * @version 1.0
 */
public class MarksheetModel {

	private static Logger log = Logger.getLogger(MarksheetModel.class);

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

	public void testfindByRollNo(int stuRollNo) {

		PreparedStatement pstmt = null;
		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("select *  from marksheet where student_id=" + stuRollNo + ";");
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				String roll_no = resultSet.getString("roll_no");
				long student_id = resultSet.getLong("student_id");
				String student_name = resultSet.getString("student_name");
				int phy_marks = resultSet.getInt("phy_marks");
				int chem_marks = resultSet.getInt("chem_marks");
				int math_marks = resultSet.getInt("math_marks");

				System.out.println("roll_no  " + roll_no);
				System.out.println("student_id  " + student_id);
				System.out.println("student_name  " + student_name);
				System.out.println("phy_marks  " + phy_marks);
				System.out.println("chem_marks  " + chem_marks);
				System.out.println("math_marks  " + math_marks);

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
	}

	public ArrayList<MarksheetBean> testMeritList() {

		PreparedStatement pstmt = null;
		ArrayList<MarksheetBean> list = new ArrayList<MarksheetBean>();
		try {

			Connection con = getConnection();
			// String query = "SELECT ID,ROLL_NO,STUDENT_ID,PHY_MARKS, CHEM_MARKS,
			// MATH_MARKS, PHY_MARKS + CHEM_MARKS + MATH_MARKS AS TOTAL, ((PHY_MARKS +
			// CHEM_MARKS + MATH_MARKS)/300)*100 AS PERCENTAGE FROM MARKSHEET WHERE
			// (((PHY_MARKS + CHEM_MARKS + MATH_MARKS)/300)*100) > 40 ORDER BY PERCENTAGE
			// DESC;";
			// String query = "SELECT ID,ROLL_NO,STUDENT_ID,PHY_MARKS, CHEM_MARKS,
			// MATH_MARKS, PHY_MARKS + CHEM_MARKS + MATH_MARKS AS TOTAL, ((TOTAL)/300)*100
			// AS PERCENTAGE FROM MARKSHEET WHERE PERCENTAGE > 40 ORDER BY PERCENTAGE
			// DESC;";
			String query = "SELECT ID,ROLL_NO,STUDENT_ID,PHY_MARKS, CHEM_MARKS, MATH_MARKS, PHY_MARKS + CHEM_MARKS + MATH_MARKS AS TOTAL, (PHY_MARKS + CHEM_MARKS + MATH_MARKS)/100 AS PERCENTAGE FROM MARKSHEET;";

			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				MarksheetBean bean = new MarksheetBean();
				String roll_no = resultSet.getString("roll_no");
				long student_id = resultSet.getLong("student_id");
				// String student_name = resultSet.getString("student_name");
				int phy_marks = resultSet.getInt("phy_marks");
				int chem_marks = resultSet.getInt("chem_marks");
				int math_marks = resultSet.getInt("math_marks");

				System.out.println("roll_no  " + roll_no);
				System.out.println("student_id  " + student_id);
				// System.out.println("student_name " + student_name);
				System.out.println("phy_marks  " + phy_marks);
				System.out.println("chem_marks  " + chem_marks);
				System.out.println("math_marks  " + math_marks);

				bean.setRollNo(resultSet.getString("roll_no"));
				bean.setStudentId(resultSet.getLong("student_id"));
				// bean.setName(resultSet.getString("student_name"));
				bean.setPhyMarks(resultSet.getInt("phy_marks"));
				bean.setChemMarks(resultSet.getInt("chem_marks"));
				bean.setMathMarks(resultSet.getInt("math_marks"));

				list.add(bean);

			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return list;
		} finally {
			try {
				pstmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public ArrayList<MarksheetBean> marksheetList(int pageNo, int pageSize) throws ApplicationException {
		ArrayList<MarksheetBean> list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		MarksheetBean marksheet = null;
		try {
			con = JDBCDataSource.getConnection();
			if (pageNo > 1) {
				pageNo = (pageNo - 1) * 10;
			} else {
				pageNo = 0;
			}

			String query = "SELECT * FROM MARKSHEET ORDER BY  MODIFIED_DATETIME DESC LIMIT " + pageNo + "," + pageSize
					+ ";";
			log.debug("Query :: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<MarksheetBean>();
			while (resultSet.next()) {
				marksheet = new MarksheetBean();
				marksheet.setId(resultSet.getLong("ID"));
				marksheet.setStudentId(resultSet.getLong("STUDENT_ID"));
				marksheet.setRollNo(resultSet.getString("ROLL_NO"));
				marksheet.setName(resultSet.getString("STUDENT_NAME"));
				marksheet.setPhyMarks(resultSet.getInt("PHY_MARKS"));
				marksheet.setChemMarks(resultSet.getInt("CHEM_MARKS"));
				marksheet.setMathMarks(resultSet.getInt("MATH_MARKS"));
				marksheet.setCreatedBy(resultSet.getString("CREATED_BY"));
				marksheet.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				marksheet.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				marksheet.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(marksheet);
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

	public boolean deleteMarksheets(String[] ids) throws ApplicationException {
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				for (String id : ids) {
					String query = "DELETE FROM MARKSHEET WHERE ID=" + id + ";";
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
			log.error("Exception :: Exception in Delete Marksheet", e);
			throw new ApplicationException("Exception :: Exception in Delete Marksheet");
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

	public ArrayList<MarksheetBean> search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		ArrayList<MarksheetBean> list = null;
		PreparedStatement pstmt = null;
		MarksheetBean marksheet = null;		

		try {
			
			String name = bean.getName();
			String rollNo = bean.getRollNo();				

			String query = "SELECT * FROM MARKSHEET ";

				if (name.length() != 0 || rollNo.length() != 0) {
					query = query + "WHERE ";

					if (name.length() != 0) {
						query = query + "STUDENT_NAME LIKE '%" + name + "%'";
					} else {
						query = query + "STUDENT_NAME LIKE '%%'";
					}

					if (name.length() != 0 || rollNo.length() != 0) {
						query = query + "AND ROLL_NO LIKE '%" + rollNo + "%'";
					} else {
						query = query + "AND ROLL_NO LIKE '%%'";
					}
				}
				
			query = query + " ORDER BY MODIFIED_DATETIME DESC ";

			if (pageNo > 0) {
				pageNo = (pageNo - 1) * 10;
			}
			// else { pageNo=1 ; }

			query = query + "LIMIT " + pageNo + "," + pageSize;
			log.debug("Search Query :: " + query);
			pstmt = con.prepareStatement(query);

			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<MarksheetBean>();
			while (resultSet.next()) {
				marksheet = new MarksheetBean();
				marksheet.setId(resultSet.getLong("ID"));
				marksheet.setStudentId(resultSet.getLong("STUDENT_ID"));
				marksheet.setRollNo(resultSet.getString("ROLL_NO"));
				marksheet.setName(resultSet.getString("STUDENT_NAME"));
				marksheet.setPhyMarks(resultSet.getInt("PHY_MARKS"));
				marksheet.setChemMarks(resultSet.getInt("CHEM_MARKS"));
				marksheet.setMathMarks(resultSet.getInt("MATH_MARKS"));
				marksheet.setCreatedBy(resultSet.getString("CREATED_BY"));
				marksheet.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				marksheet.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				marksheet.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(marksheet);
			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Search Marksheet");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public MarksheetBean findByPK(long pk) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		PreparedStatement pstmt = null;
		MarksheetBean marksheet = null;

		try {
			String query = "SELECT * FROM MARKSHEET WHERE ID=" + pk + ";";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				marksheet = new MarksheetBean();
				marksheet.setId(resultSet.getLong("ID"));
				marksheet.setStudentId(resultSet.getLong("STUDENT_ID"));
				marksheet.setRollNo(resultSet.getString("ROLL_NO"));
				marksheet.setName(resultSet.getString("STUDENT_NAME"));
				marksheet.setPhyMarks(resultSet.getInt("PHY_MARKS"));
				marksheet.setChemMarks(resultSet.getInt("CHEM_MARKS"));
				marksheet.setMathMarks(resultSet.getInt("MATH_MARKS"));
				marksheet.setCreatedBy(resultSet.getString("CREATED_BY"));
				marksheet.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				marksheet.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				marksheet.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
			}
			resultSet.close();
			return marksheet;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : Exception in getting Marksheet by pk");
			throw new ApplicationException("Exception : Exception in getting Marksheet by pk");
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

	public MarksheetBean findByRollNo(String rollNo) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM MARKSHEET WHERE ROLL_NO=?");
		MarksheetBean marksheet = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rollNo);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				marksheet = new MarksheetBean();
				marksheet.setId(resultSet.getLong("ID"));
				marksheet.setStudentId(resultSet.getLong("STUDENT_ID"));
				marksheet.setRollNo(resultSet.getString("ROLL_NO"));
				marksheet.setName(resultSet.getString("STUDENT_NAME"));
				marksheet.setPhyMarks(resultSet.getInt("PHY_MARKS"));
				marksheet.setChemMarks(resultSet.getInt("CHEM_MARKS"));
				marksheet.setMathMarks(resultSet.getInt("MATH_MARKS"));
				marksheet.setCreatedBy(resultSet.getString("CREATED_BY"));
				marksheet.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				marksheet.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				marksheet.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
			}
			resultSet.close();
		} catch (Exception e) {
			log.error(e);
			throw new ApplicationException("Exception in getting Marksheet by Roll No");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return marksheet;
	}
	  /**
     * Adds a Marksheet
     *
     * @param bean
     * @throws DatabaseException
     *
     */
	public void addMarksheet(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		
		System.out.println("Student id " + bean.getStudentId());
		
		StudentModel model = new StudentModel();
		StudentBean studentbean = model.findByPK(bean.getStudentId());
		bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

		MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());
		if (duplicateMarksheet != null) {
			throw new DuplicateRecordException("Roll Number Already Exists");
		}
		try {
			con = JDBCDataSource.getConnection();
			pstmt = con.prepareStatement("INSERT INTO MARKSHEET VALUES(?,?,?,?,?,?,?,?,?,?,?);");

			pstmt.setLong(1, nextPk("MARKSHEET ", "ID"));
			pstmt.setString(2, bean.getRollNo());
			pstmt.setLong(3, bean.getStudentId());
			pstmt.setString(4, bean.getName());
			pstmt.setInt(5, bean.getPhyMarks());
			pstmt.setInt(6, bean.getChemMarks());
			pstmt.setInt(7, bean.getMathMarks());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDateTime());
			pstmt.setTimestamp(11, bean.getModifiedDateTime());

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
			throw new ApplicationException("Exception : Exception in add Marksheet");

		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public void updateMarksheet(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;		
		MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());
		
		 if (duplicateMarksheet != null && duplicateMarksheet.getId() != bean.getId()) {
			throw new DuplicateRecordException("Roll No Already Exists");
		}
		 
		try {
			StudentModel model = new StudentModel();
	        StudentBean studentbean = model.findByPK(bean.getStudentId());
	        bean.setName(studentbean.getFirstName() + " "+ studentbean.getLastName());
	        
			con = JDBCDataSource.getConnection();
			String query = "UPDATE MARKSHEET SET ROLL_NO=?,STUDENT_ID=?,STUDENT_NAME=?,PHY_MARKS=?,CHEM_MARKS=?,MATH_MARKS=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?;";
			
			log.debug(query);
			pstmt = con.prepareStatement(query);
			  pstmt.setString(1, bean.getRollNo());
	            pstmt.setLong(2, bean.getStudentId());
	            pstmt.setString(3, bean.getName());
	            pstmt.setInt(4, bean.getPhyMarks());
	            pstmt.setInt(5, bean.getChemMarks());
	            pstmt.setInt(6, bean.getMathMarks());
	            pstmt.setString(7, bean.getCreatedBy());
	            pstmt.setString(8, bean.getModifiedBy());
	            pstmt.setTimestamp(9, bean.getCreatedDateTime());
	            pstmt.setTimestamp(10, bean.getModifiedDateTime());
	            pstmt.setLong(11, bean.getId());

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
			log.error("Exception in Update Marksheet", e);
			throw new ApplicationException("Exception in Update Marksheet");

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
	
	
	public ArrayList<StudentBean> studentNameList() {	
		ArrayList<StudentBean> list = null ;
        try {
        	StudentModel model = new StudentModel();
        	list = model.studentList(0,0);  
        } catch (ApplicationException e) {
            log.error(e);
        }
        return list ;
	}

	public ArrayList<MarksheetBean> getMeritList(int pageNo, int pageSize)
            throws ApplicationException {
       
        ArrayList<MarksheetBean> list = new ArrayList<MarksheetBean>();
        StringBuffer sql = new StringBuffer("SELECT ID,ROLL_NO,STUDENT_NAME,PHY_MARKS, CHEM_MARKS, MATH_MARKS, PHY_MARKS + CHEM_MARKS + MATH_MARKS AS TOTAL FROM MARKSHEET ORDER BY TOTAL DESC");
        Connection con = null;        
      
        if (pageSize > 0) {
            // Calculate start record index
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }

       

        try {
        	con = JDBCDataSource.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql.toString());
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                MarksheetBean marksheet = new MarksheetBean();
                marksheet.setId(resultSet.getLong("ID"));
                marksheet.setRollNo(resultSet.getString("ROLL_NO"));
                marksheet.setName(resultSet.getString("STUDENT_NAME"));
                marksheet.setPhyMarks(resultSet.getInt("PHY_MARKS"));
                marksheet.setChemMarks(resultSet.getInt("CHEM_MARKS"));
                marksheet.setMathMarks(resultSet.getInt("MATH_MARKS"));
                list.add(marksheet);
                
            }
            resultSet.close();
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException(
                    "Exception in getting merit list of Marksheet");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        log.debug("Model MeritList End");
        return list;
    }

	public MarksheetBean getMarksheet(MarksheetBean bean) throws ApplicationException {
	
		return findByRollNo(bean.getRollNo());
		
	}

	public String getCollegeName(MarksheetBean marksheet) throws ApplicationException {
		StudentModel student = new StudentModel();
		return	student.findByPK(marksheet.getStudentId()).getCollegeName();
		
	}
}
