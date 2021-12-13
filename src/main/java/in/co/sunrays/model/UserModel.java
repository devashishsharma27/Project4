package in.co.sunrays.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import in.co.sunrays.beans.CollegeBean;
import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.exception.RecordNotFoundException;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.EmailBuilder;
import in.co.sunrays.util.EmailMessage;
import in.co.sunrays.util.EmailUtility;

import org.apache.log4j.Logger;

public class UserModel {

	private static Logger log = Logger.getLogger(UserModel.class);

	public Connection getConnection() {
		Connection con = null;
		try {
			ResourceBundle rbObj = ResourceBundle.getBundle("config");
			Class.forName(rbObj.getString("driver"));
			con = DriverManager.getConnection(rbObj.getString("url"), rbObj.getString("username"),
					rbObj.getString("password"));
			con.setAutoCommit(false);
		} catch (Exception e) {
			log.error("Database Exception..", e);
			System.out.println("Database Exception.." + e);
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
			log.error("Exception..", e);
			System.out.println("Exception.." + e.getMessage());
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

	public UserBean authenticate(UserBean bean) throws ApplicationException {

		PreparedStatement pstmt = null;
		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("SELECT * FROM USER where login=? and password =?;");
			pstmt.setString(1, bean.getLoginId());
			pstmt.setString(2, bean.getPassword());
			ResultSet result = pstmt.executeQuery();

			bean = new UserBean();
			while (result.next()) {

				bean.setId(result.getLong("ID"));
				bean.setFirstName(result.getString("FIRST_NAME"));
				bean.setLastName(result.getString("LAST_NAME"));
				bean.setLoginId(result.getString("LOGIN"));
				bean.setPassword(result.getString("PASSWORD"));
				bean.setDob(result.getDate("DOB"));
				bean.setMobNo(result.getString("MOBILE_NO"));
				bean.setRoleId(result.getLong("ROLE_ID"));
				bean.setGender(result.getString("GENDER"));
				bean.setCreatedBy(result.getString("CREATED_BY"));
				bean.setModifiedBy(result.getString("MODIFIED_BY"));
				bean.setCreatedDateTime(result.getTimestamp("CREATED_DATETIME"));
				bean.setModifiedDateTime(result.getTimestamp("MODIFIED_DATETIME"));
			}

		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception in Authenticating User ", e);
			// System.out.println("Exception in Authenticating User" + e);
			throw new ApplicationException("Exception in Authenticating User");

		} finally {
			try {
				pstmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return bean;
	}

	public boolean changePassword(Long id, String oldPassword, String newPassword)
			throws RecordNotFoundException, DuplicateRecordException, ApplicationException {

		boolean flag = false;
		UserBean beanExist = null;

		beanExist = findByPK(id);		
		if (beanExist != null && beanExist.getPassword().equals(oldPassword)) {
			beanExist.setPassword(newPassword);
			try {
				log.info("Updating New Password");
				updateUser(beanExist);
				sendEmail(beanExist);
			} catch (DuplicateRecordException e) {
				log.error(e);
				throw new ApplicationException("LoginId is already exist");
			}
			flag = true;
		} else {
			log.info("Invalid Old Password");
			throw new RecordNotFoundException("Invalid Old Password");
		}			
		return flag;

	}
	
	public void sendEmail(UserBean beanExist) throws ApplicationException {
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("login", beanExist.getLoginId());
		map.put("password", beanExist.getPassword());
		map.put("firstName", beanExist.getFirstName());
		map.put("lastName", beanExist.getLastName());

		String message = EmailBuilder.getChangePasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(beanExist.getLoginId());
		msg.setSubject("ORS Password has been changed Successfully.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);	
	}

	public void addUser(UserBean bean) throws ApplicationException, DuplicateRecordException {

		PreparedStatement pstmt = null;
		UserBean duplicateUser = findByLogin(bean.getLoginId());
		if (duplicateUser != null) {
			throw new DuplicateRecordException("LoginId is already exist");
		}

		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("INSERT INTO USER VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);");
			pstmt.setLong(1, nextPk("user", "id"));
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getLoginId());
			pstmt.setString(5, bean.getPassword());
			pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(7, bean.getMobNo());
			pstmt.setLong(8, bean.getRoleId());
			pstmt.setString(9, bean.getGender());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDateTime());
			pstmt.setTimestamp(13, bean.getModifiedDateTime());

			int recordCount = pstmt.executeUpdate();
			con.commit();
			log.info("Successfully Inserted " + recordCount + " Record(s)");
			// System.out.println("Successfully Inserted " + recordCount + " Record(s)");

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

	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {

		UserBean userData = findByLogin(login);
		boolean flag = false;

		if (userData == null) {
			throw new RecordNotFoundException("Email ID does not exists !");

		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLoginId());
		map.put("password", userData.getPassword());
		map.put("firstName", userData.getFirstName());
		map.put("lastName", userData.getLastName());

		String message = EmailBuilder.getForgetPasswordMessage(map);
		EmailMessage msg = new EmailMessage();

		msg.setTo(login);
		msg.setSubject("ORS Password Reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);
		flag = true;

		return flag;

	}

	public UserBean findByLogin(String loginId) {

		PreparedStatement pstmt = null;
		UserBean bean = null;
		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("SELECT * FROM USER WHERE LOGIN=?;");
			pstmt.setString(1, loginId);
			ResultSet result = pstmt.executeQuery();
			// System.out.println("Query :: SELECT * FROM USER WHERE LOGIN=" + loginId);
			while (result.next()) {
				bean = new UserBean();
				bean.setId(result.getLong("ID"));
				bean.setFirstName(result.getString("FIRST_NAME"));
				bean.setLastName(result.getString("LAST_NAME"));
				bean.setLoginId(result.getString("LOGIN"));
				bean.setPassword(result.getString("PASSWORD"));
				bean.setDob(result.getDate("DOB"));
				bean.setMobNo(result.getString("MOBILE_NO"));
				bean.setRoleId(result.getLong("ROLE_ID"));
				bean.setGender(result.getString("GENDER"));
				bean.setCreatedBy(result.getString("CREATED_BY"));
				bean.setModifiedBy(result.getString("MODIFIED_BY"));
				bean.setCreatedDateTime(result.getTimestamp("CREATED_DATETIME"));
				bean.setModifiedDateTime(result.getTimestamp("MODIFIED_DATETIME"));

				/*
				 * System.out.println("FIRST_NAME " + bean.getFirstName());
				 * System.out.println("LAST_NAME " + bean.getLastName());
				 * System.out.println("LOGIN " + bean.getLoginId());
				 * System.out.println("PASSWORD " + bean.getPassword());
				 * System.out.println("DOB " + bean.getDob()); System.out.println("MOBILE_NO " +
				 * bean.getMobNo()); System.out.println("ROLE_ID " + bean.getRoleId());
				 * System.out.println("GENDER " + bean.getGender());
				 * System.out.println("CREATED_BY " + bean.getCreatedBy());
				 * System.out.println("MODIFIED_BY " + bean.getModifiedBy());
				 * System.out.println("CREATED_DATETIME " + bean.getCreatedDateTime());
				 * System.out.println("MODIFIED_DATETIME " + bean.getModifiedDateTime());
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException se) {
				log.error("Exception ", se);
				System.out.println("Exception " + se.getMessage());
				se.printStackTrace();
			}
		}
		return bean;

	}

	public ArrayList<UserBean> userList(int pageNo, int pageSize) throws ApplicationException {
		ArrayList<UserBean> list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		UserBean user = null;
		try {
			con = JDBCDataSource.getConnection();
			if (pageNo > 1) {
				pageNo = (pageNo - 1) * 10;
			} else {
				pageNo = 0;
			}

			String query = "SELECT * FROM USER ORDER BY  MODIFIED_DATETIME DESC LIMIT " + pageNo + "," + pageSize + ";";
			log.debug("Query :: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<UserBean>();
			while (resultSet.next()) {
				user = new UserBean();
				user.setId(resultSet.getLong("ID"));
				user.setFirstName(resultSet.getString("FIRST_NAME"));
				user.setLastName(resultSet.getString("LAST_NAME"));
				user.setLoginId(resultSet.getString("LOGIN"));
				user.setPassword(resultSet.getString("PASSWORD"));
				user.setDob(resultSet.getDate("DOB"));
				user.setMobNo(resultSet.getString("MOBILE_NO"));
				user.setRoleId(resultSet.getLong("ROLE_ID"));
				user.setGender(resultSet.getString("GENDER"));
				user.setCreatedBy(resultSet.getString("CREATED_BY"));
				user.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				user.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				user.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(user);
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

	public boolean deleteUsers(String[] ids) throws ApplicationException {
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				for (String id : ids) {
					System.out.println("user ids " + id);
					String query = "DELETE FROM USER WHERE ID=" + id + ";";
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
			log.error("Exception :: Exception in Delete User", e);
			throw new ApplicationException("Exception :: Exception in Delete User");
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

	public ArrayList<UserBean> search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		ArrayList<UserBean> list = null;
		PreparedStatement pstmt = null;
		UserBean user = null;
		try {
			String firstName = bean.getFirstName();
			String lastName = bean.getLastName();
			String login = bean.getLoginId();

			String query = "SELECT * FROM USER ";

			if (firstName.length() != 0 || lastName.length() != 0 || login.length() != 0) {
				query = query + "WHERE ";

				if (firstName.length() != 0) {
					query = query + "FIRST_NAME LIKE '%" + firstName + "%'";
				} else {
					query = query + "FIRST_NAME LIKE '%%'";
				}

				if (firstName.length() != 0 || lastName.length() != 0 || login.length() != 0) {
					query = query + "AND LAST_NAME LIKE '%" + lastName + "%'";
				} else {
					query = query + "AND LAST_NAME LIKE '%%'";
				}

				if (firstName.length() != 0 || lastName.length() != 0 || login.length() != 0) {
					query = query + "AND LOGIN LIKE '%" + login + "%'";
				} else {
					query = query + "AND LOGIN LIKE '%%';";
				}
			}

			query = query + " ORDER BY  MODIFIED_DATETIME DESC ";

			if (pageNo > 0) {
				pageNo = (pageNo - 1) * 10;
			}

			query = query + "LIMIT " + pageNo + "," + pageSize;
			log.debug("Search Query :: " + query);
			pstmt = con.prepareStatement(query);

			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<UserBean>();
			while (resultSet.next()) {
				user = new UserBean();
				user.setId(resultSet.getLong("ID"));
				user.setFirstName(resultSet.getString("FIRST_NAME"));
				user.setLastName(resultSet.getString("LAST_NAME"));
				user.setLoginId(resultSet.getString("LOGIN"));
				user.setPassword(resultSet.getString("PASSWORD"));
				user.setDob(resultSet.getDate("DOB"));
				user.setMobNo(resultSet.getString("MOBILE_NO"));
				user.setRoleId(resultSet.getLong("ROLE_ID"));
				user.setGender(resultSet.getString("GENDER"));
				user.setCreatedBy(resultSet.getString("CREATED_BY"));
				user.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				user.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				user.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(user);
			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search User");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public UserBean findByPK(long pk) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		PreparedStatement pstmt = null;
		UserBean user = null;

		try {
			String query = "SELECT * FROM USER WHERE ID=" + pk + ";";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				user = new UserBean();
				user.setId(resultSet.getLong("ID"));
				user.setFirstName(resultSet.getString("FIRST_NAME"));
				user.setLastName(resultSet.getString("LAST_NAME"));
				user.setLoginId(resultSet.getString("LOGIN"));
				user.setPassword(resultSet.getString("PASSWORD"));
				user.setDob(resultSet.getDate("DOB"));
				user.setMobNo(resultSet.getString("MOBILE_NO"));
				user.setRoleId(resultSet.getLong("ROLE_ID"));
				user.setGender(resultSet.getString("GENDER"));
				user.setCreatedBy(resultSet.getString("CREATED_BY"));
				user.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				user.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				user.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));

			}
			resultSet.close();
			return user;
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

	}

	public void updateUser(UserBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		UserBean duplicateUser = findByLogin(bean.getLoginId());

		if (duplicateUser != null && duplicateUser.getId() != bean.getId()) {
			throw new DuplicateRecordException("LoginId is already exist");
		}

		try {

			/*
			 * System.out.println("FIRST_NAME " + bean.getFirstName());
			 * System.out.println("LAST_NAME " +bean.getLastName());
			 * System.out.println("LOGIN " +bean.getLoginId());
			 * System.out.println("PASSWORD " +bean.getPassword());
			 * System.out.println("DOB " +bean.getDob().getTime());
			 * System.out.println("MOBILE_NO " +bean.getMobNo());
			 * System.out.println("ROLE_ID " + bean.getRoleId());
			 * System.out.println("GENDER " +bean.getGender());
			 * System.out.println("CREATED_BY " +bean.getCreatedBy());
			 * System.out.println("MODIFIED_BY " +bean.getModifiedBy());
			 * System.out.println("CREATED_DATETIME " +bean.getCreatedDateTime());
			 * System.out.println("MODIFIED_DATETIME " +bean.getModifiedDateTime());
			 * System.out.println("ID " +bean.getId());
			 */

			con = JDBCDataSource.getConnection();
			String query = "UPDATE USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,PASSWORD=?,DOB=?,MOBILE_NO=?,ROLE_ID=?,GENDER=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setString(3, bean.getLoginId());
			pstmt.setString(4, bean.getPassword());
			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(6, bean.getMobNo());
			pstmt.setLong(7, bean.getRoleId());
			pstmt.setString(8, bean.getGender());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDateTime());
			pstmt.setTimestamp(12, bean.getModifiedDateTime());
			pstmt.setLong(13, bean.getId());
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
			log.error("Exception in Update User", e);
			throw new ApplicationException("Exception in Update User");

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
