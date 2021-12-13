<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.beans.StudentBean"%>
<%@page import="in.co.sunrays.controller.StudentCtl"%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Student</title>
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="css/student.css">

<style>
</style>
<link href="../css/jquery-ui.css" rel="stylesheet">
<script src="../js/jquery-1.10.2.js"></script>
<script src="../js/jquery-ui.js"></script>
<script>
	$(function() {
		$("#dob").datepicker();
		/*  $( "#datepicker-13" ).datepicker("show"); */
	});
</script>
</head>
<body>
	<%
		StudentBean bean = (StudentBean) request.getAttribute("bean");
		String operation = (String)request.getAttribute("operation");
		ArrayList<String> collegeList = (ArrayList<String>) request.getAttribute("collegeList");
	
		String id = "";
		String collegeId = "";
		String collegeName = "";
		String firstname = "";
		String lastName = "";
		String dob = "";
		String mobNo = "";
		String email = "";
		if (bean != null) {
			id = String.valueOf(bean.getId());
			collegeId = String.valueOf(bean.getCollegeId());
			collegeName = bean.getCollegeName();
			firstname = bean.getFirstName();
			lastName = bean.getLastName();
	//		dob = String.valueOf(bean.getDob());
	        dob = StudentCtl.getDateFormat(bean.getDob());
	       if(dob==null){
	    	   dob="";
	       }
			mobNo = bean.getMobileNo();
			email = bean.getEmail();
		}
		
		
		/* if(operation!=null && operation.equalsIgnoreCase("add")){
		 operation = StudentCtl.OP_ADD;
		}else{
		 operation = StudentCtl.OP_SAVE;
		}
		 */
		
		 if (operation != null && (operation.equalsIgnoreCase("Edit")||operation.equalsIgnoreCase("Save"))) {
			operation = BaseCtl.OP_SAVE;
		} else {
			operation = BaseCtl.OP_ADD;
		} 
	%>


	<%@ include file="ORSHeader.jsp"%>


	<form action="<%=ORSView.STUDENT_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Student Profile</h1><%=operation%>
				</td>
			</tr>
		</table>
		<div style="text-align: center;">
		<font color="red" style="font-family: sans-serif; font-size: 50">
				<%
					if (request.getAttribute(BaseCtl.MSG_ERROR) != null) {
				%> <%=request.getAttribute(BaseCtl.MSG_ERROR)%> <%
 	}
 %>
			</font>
		</div>
		<table class="" align="center">
			<tr>

				<td><input type="hidden" id="studentId" name="studentId"
					value="<%=id%>"></td>
				<td><input type="hidden" id="collegeId" name="collegeId"
					value="<%=collegeId%>"></td>

			</tr>
		</table>
		<div class="">
			<table class="orsCommonRecordTable" align="center">	

				<tr>
					<td>College Name</td>
					<td><select name="collegeName" id="collegeName"
						style="width: 190px; height: 20px">
						<!-- <option  value="">Select</option> -->
							<%
								for (String college : collegeList) {
									if (collegeName.equalsIgnoreCase(college)) {
							%>
							<option selected="selected" value="<%=college%>"><%=college%></option>
							<%
								} else {
							%>
							<option value="<%=college%>">
								<%=college%></option>
							<%
								}
								}
							%>
					</select> 
					<td
						style="color: red; background-color: white; border-color: white">
						<%
							if (request.getAttribute("collegeNameErr") != null) {
						%> <%=request.getAttribute("collegeNameErr")%> <%
 	}
 %>
					</td>
				</tr>
				<tr>
					<td>First Name</td>
					<td><input type="text" id="firstName" name="firstName"
						value="<%=firstname%>"></td>
					<td
						style="color: red; background-color: white; border-color: white">
						<%
							if (request.getAttribute("firstNameErr") != null) {
						%> <%=request.getAttribute("firstNameErr")%> <%
 	}
 %>
					</td>
				</tr>
				<tr>
					<td>Last Name</td>
					<td><input type="text" id="lastName" name="lastName"
						value="<%=lastName%>"></td>
					<td
						style="color: red; background-color: white; border-color: white">
						<%
							if (request.getAttribute("lastNameErr") != null) {
						%> <%=request.getAttribute("lastNameErr")%> <%
 	}
 %>
					</td>
				</tr>
				<tr>
					<td>Date of Birth</td>
					<td><input type="text" autocomplete="off" id="dob" name="dob" value="<%=dob%>"></td>
					<td
						style="color: red; background-color: white; border-color: white">
						<%
							if (request.getAttribute("dobErr") != null) {
						%> <%=request.getAttribute("dobErr")%> <%
 	}
 %>
					</td>
				</tr>
				<tr>
					<td>Mobile No</td>
					<td><input type="text" id="mobNo" name="mobNo"
						value="<%=mobNo%>"></td>
					<td
						style="color: red; background-color: white; border-color: white">
						<%
							if (request.getAttribute("mobNoErr") != null) {
						%> <%=request.getAttribute("mobNoErr")%> <%
 	}
 %>
					</td>
				</tr>
				<tr>
					<td>Email</td>
					<td><input type="text" id="email" name="email"
						value="<%=email%>"></td>
					<td
						style="color: red; background-color: white; border-color: white">
						<%
							if (request.getAttribute("emailErr") != null) {
						%> <%=request.getAttribute("emailErr")%> <%
 	}
 %>
					</td>
				</tr>
			</table>
		</div>


		<table class="" align="center">
			<tr>
				<td><input type="submit" value="<%=operation%>"
					name="operation" class="commonSaveAndAddButton"></td>
			<td><input type="submit" value="Cancel"
					name="operation" class="commonCancelButton"></td>
			</tr>
		</table>
	</form>

	<%@ include file="ORSFooter.jsp"%>




</body>
</html>