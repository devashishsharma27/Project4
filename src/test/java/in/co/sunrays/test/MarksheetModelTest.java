package in.co.sunrays.test;

import java.util.ArrayList;

import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.model.MarksheetModel;

public class MarksheetModelTest {
	
	public static MarksheetBean bean = new MarksheetBean();

	public static void main(String[] args) {

		MarksheetModelTest marksheetTestObj = new MarksheetModelTest();
	//	marksheetTestObj.testAdd();
	//	marksheetTestObj.testDelete(26);
	//	marksheetTestObj.testupdate();
	//	marksheetTestObj.testfindByRollNo(29);
		marksheetTestObj.testList();
	//	marksheetTestObj.testSearch();
	//marksheetTestObj.testMeritList();
	}

	public void testAdd() {
		
		bean.setRollNo("2020IT1003");
		bean.setStudentId(30);
		bean.setName("nakul rathore");
		bean.setPhyMarks(71);
		bean.setChemMarks(87);
		bean.setMathMarks(68);

		MarksheetModel modelObj = new MarksheetModel();
		modelObj.add(bean);

	}

	public void testDelete(int rollNo) {
		
		MarksheetModel modelObj = new MarksheetModel();
		 ;
		modelObj.delete(rollNo);

	}

	public void testupdate() {
		MarksheetBean bean = new MarksheetBean();
		bean.setRollNo("2020IT1010");
		bean.setStudentId(29);
		bean.setName("Anil Dhakad");
		bean.setPhyMarks(85);
		bean.setChemMarks(77);
		bean.setMathMarks(65);
		
		MarksheetModel modelObj = new MarksheetModel();
		modelObj.update(bean);

	}

	public void testfindByRollNo(int rollNo) {
		
		MarksheetModel modelObj = new MarksheetModel();
		 
		modelObj.testfindByRollNo(rollNo);
	}

	public void testfindByPk() {
		MarksheetBean bean = new MarksheetBean();
		bean.setRollNo("2020IT1001");
		bean.setStudentId(26);
		bean.setName("Anil Dhakad");
		bean.setPhyMarks(85);
		bean.setChemMarks(77);
		bean.setMathMarks(65);

		MarksheetModel modelObj = new MarksheetModel();
		modelObj.testfindByPk(bean);

	}

	public void testSearch() {
		MarksheetBean bean = new MarksheetBean();

		MarksheetModel modelObj = new MarksheetModel();
		
		String columnName = "roll_no"; 
		String value = "2020IT1010";
		
	//	String columnName = "student_name"; // roll_no
	//	String value = "kunal bagora";
		
		
		ArrayList<MarksheetBean> list = modelObj.testSearch(columnName,value);

	}

	public void testMeritList() {
		
		MarksheetModel modelObj = new MarksheetModel();
		ArrayList<MarksheetBean> list = modelObj.testMeritList();

	}

	public void testList() {
	
		MarksheetModel modelObj = new MarksheetModel();
		ArrayList<MarksheetBean> list = modelObj.testList();

	}

}
