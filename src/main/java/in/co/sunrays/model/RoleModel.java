package in.co.sunrays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;

public class RoleModel {

	private static Logger log = Logger.getLogger(RoleModel.class);

	public ArrayList<RoleBean> roleList(int pageNo, int pageSize) throws ApplicationException {
		ArrayList<RoleBean> list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		RoleBean role = null;
		try {
			con = JDBCDataSource.getConnection();
			if (pageNo > 1) {
				pageNo = (pageNo - 1) * 10;
			} else {
				pageNo = 0;
			}

			String query = "SELECT * FROM ROLE ORDER BY  MODIFIED_DATETIME DESC LIMIT " + pageNo + "," + pageSize
					+ ";";
			log.debug("Query :: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();
			list = new ArrayList<RoleBean>();
			while (resultSet.next()) {

				role = new RoleBean();
				role.setId(resultSet.getLong("ID"));
				role.setName(resultSet.getString("NAME"));
				role.setDescription(resultSet.getString("DESCRIPTION"));
				role.setCreatedBy(resultSet.getString("CREATED_BY"));
				role.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				role.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				role.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(role);
			}
			resultSet.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Search Role");
		} finally {
			try {
				pstmt.close();
				JDBCDataSource.closeConnection(con);
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public boolean deleteRoles(String[] ids) throws ApplicationException {
		boolean flag = true;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			if (ids.length > 0) {
				con = JDBCDataSource.getConnection();
				for (String id : ids) {
					String query = "DELETE FROM ROLE WHERE ID=" + id + ";";
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
			throw new ApplicationException("Exception :: Exception in Delete Role");
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

	public ArrayList<RoleBean> search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		ArrayList<RoleBean> list = null;
		PreparedStatement pstmt = null;
		RoleBean role = null;

		String name = bean.getName();

		try {
			String query = "SELECT * FROM ROLE ";

			if (name.length() != 0) {
				query = query + "WHERE NAME LIKE '%" + name + "%'";
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
			list = new ArrayList<RoleBean>();
			while (resultSet.next()) {
				role = new RoleBean();
				role.setId(resultSet.getLong("ID"));
				role.setName(resultSet.getString("NAME"));
				role.setDescription(resultSet.getString("DESCRIPTION"));
				role.setCreatedBy(resultSet.getString("CREATED_BY"));
				role.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				role.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				role.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
				list.add(role);
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

	public RoleBean findByPK(long pk) throws ApplicationException {
		Connection con = JDBCDataSource.getConnection();
		PreparedStatement pstmt = null;
		RoleBean role = null;

		try {
			String query = "SELECT * FROM ROLE WHERE ID=" + pk + ";";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				role = new RoleBean();
				role.setId(resultSet.getLong("ID"));
				role.setName(resultSet.getString("NAME"));
				role.setDescription(resultSet.getString("DESCRIPTION"));
				role.setCreatedBy(resultSet.getString("CREATED_BY"));
				role.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				role.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				role.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
			}
			resultSet.close();
			return role;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : Exception in getting Role by pk");
			throw new ApplicationException("Exception : Exception in getting Role by pk");
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

	private RoleBean findByName(String name) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM ROLE WHERE NAME=?");
		RoleBean role = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			log.debug(sql.toString());
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				role = new RoleBean();
				role.setId(resultSet.getLong("ID"));
				role.setName(resultSet.getString("NAME"));
				role.setDescription(resultSet.getString("DESCRIPTION"));
				role.setCreatedBy(resultSet.getString("CREATED_BY"));
				role.setModifiedBy(resultSet.getString("MODIFIED_BY"));
				role.setCreatedDateTime(resultSet.getTimestamp("CREATED_DATETIME"));
				role.setModifiedDateTime(resultSet.getTimestamp("MODIFIED_DATETIME"));
			}
			resultSet.close();
		} catch (Exception e) {
			log.error("Exception : Database Exception in getting Role by Name", e);
			throw new ApplicationException("Exception : Database Exception in getting Role by Name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return role;
	}

	public void addStudent(RoleBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		RoleBean duplicataRole = findByName(bean.getName());
		if (duplicataRole != null) {
			throw new DuplicateRecordException("Role Name Already Exists");
		}
		try {
			con = JDBCDataSource.getConnection();
			pstmt = con.prepareStatement(" INSERT INTO ROLE VALUES(?,?,?,?,?,?,?);");
			pstmt.setLong(1, nextPk("ROLE", "ID"));
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDateTime());
			pstmt.setTimestamp(7, bean.getModifiedDateTime());

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

	public void updateRole(RoleBean bean) throws ApplicationException, DuplicateRecordException {
		Connection con = null;
		PreparedStatement pstmt = null;
		RoleBean duplicataRole = findByName(bean.getName());

		if (duplicataRole != null && duplicataRole.getId() != bean.getId()) {
			throw new DuplicateRecordException("Role Name Already Exists");
		}

		try {
			con = JDBCDataSource.getConnection();
			String query = "UPDATE ROLE SET NAME= ?, DESCRIPTION=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID = ? ;";
			log.debug(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getModifiedBy());
			pstmt.setTimestamp(4, bean.getModifiedDateTime());
			pstmt.setLong(5, bean.getId());

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
			log.error("Exception in updating Role ", e);
			throw new ApplicationException("Exception in updating Role");

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

	public ArrayList<RoleBean> roleNameList() {
		ArrayList<RoleBean> list = null ;
        try {
        	
        	list = roleList(0,10);  
        } catch (ApplicationException e) {
            log.error(e);
        }
        return list ;
	}
	

}
