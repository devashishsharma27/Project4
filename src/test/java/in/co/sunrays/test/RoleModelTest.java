package in.co.sunrays.test;

import in.co.sunrays.beans.RoleBean;

public class RoleModelTest {
	
	
	public static RoleModelTest testObj = new RoleModelTest();
	public static void main(String[] args) {
		
		// testObj.testAddRole();
	     testObj.testDeleteRole();
	    // testObj.testUpdate();
	    // testObj.testFindByPK();
	    // testObj.testFindByName();
	    // testObj.testSearch();
	    //testObj.testList();
		
	}
	
	public void testDeleteRole() {
		RoleBean bean = new RoleBean();
		//bean.deleteRole(bean);
		
	}

	public void testAddRole () {
		RoleBean bean = new RoleBean();
		bean.setName("Kiosk");
		bean.setDescription("able to register the user, change or get password");
		RoleModel model = new RoleModel();
		model.addRole(bean);
		
		
	}

}
