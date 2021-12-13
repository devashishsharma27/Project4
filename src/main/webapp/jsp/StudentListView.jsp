<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.sunrays.beans.StudentBean"%>
<%@page import="in.co.sunrays.controller.StudentCtl"%>
<%@page import="in.co.sunrays.controller.StudentListCtl"%>
<%@page import="in.co.sunrays.controller.BaseCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Student List</title>
<link rel="stylesheet" href="../css/studentList.css">
<link rel="stylesheet" href="../css/common.css">


<!-- <script type="text/javascript" src="../js/studentList.js"></script> -->
<script type="text/javascript" src="../js/orsUtil.js"></script>

</head>
<body>
	<%@ include file="ORSHeader.jsp"%>



	<div style="text-align: center;">

		<h1>Student List</h1>

	</div>


	<%
		int pageNo = 0;
		ArrayList<StudentBean> list = (ArrayList<StudentBean>) request.getAttribute("list");
		ArrayList<String> collegeList = (ArrayList<String>) request.getAttribute("collegeList");
		if (request.getAttribute("pageNo") != null) {
			pageNo = (Integer) request.getAttribute("pageNo");
		} else {
			pageNo = 1;
		}
		int recordCount = (Integer) request.getAttribute("recordCount");
		int count = (pageNo == 1) ? 1 : (pageNo - 1) * 10 + 1;

		String fName = (String) request.getAttribute("fNameSearch");
		fName = (fName == null) ? "" : fName;

		String lNameSearch = (String) request.getAttribute("lNameSearch");
		lNameSearch = (lNameSearch == null) ? "" : lNameSearch;

		String clgNameSearch = (String) request.getAttribute("clgNameSearch");
		clgNameSearch = (clgNameSearch == null) ? "" : clgNameSearch;
	%>


	<form action="StudentListCtl" method="post" id="studentForm">


		<div style="text-align: center;">
			<font color="green" style="font-family: sans-serif; font-size: 50">
				<%
					if (request.getAttribute("addRecordMsg") != null) {
				%> <%=request.getAttribute("addRecordMsg")%> <%
 	}
 %> <%
 	if (request.getAttribute(BaseCtl.MSG_SUCCESS) != null) {
 %> <%=request.getAttribute(BaseCtl.MSG_SUCCESS)%> <%
 	}
 %>
			</font> <font color="orange" style="font-family: sans-serif; font-size: 50">
				<%
					if (request.getAttribute("updateRecordMsg") != null) {
				%> <%=request.getAttribute("updateRecordMsg")%> <%
 	}
 %>
			</font><font color="red" style="font-family: sans-serif; font-size: 50">
				<%
					if (request.getAttribute(BaseCtl.MSG_ERROR) != null) {
				%> <%=request.getAttribute(BaseCtl.MSG_ERROR)%> <%
 	}
 %>
			</font><font color="blue" style="font-family: sans-serif; font-size: 50">
				<%
					if (request.getAttribute("deleteRecordMsg") != null) {
				%> <%=request.getAttribute("deleteRecordMsg")%> <%
 	}
 %>

			</font>



		</div>

		<input type="hidden" id="selectedRecord" name="selectedRecord"
			value="abc"> <input type="hidden" name="pageNo"
			value="<%=pageNo%>"> <input type="hidden" name="recordCount"
			value="<%=recordCount%>">

		<div style="width: 50%; padding-left: 18%;">
			<table class="functionTable">

				<tr>
					<!-- <td width="8.5%">First Name</td> -->
					<td class="searchCriteria"><input type="text"
						value="<%=fName%>" name="firstName" placeholder="Enter First Name"></td>
					<!-- <td width="8.5%">Last Name</td> -->
					<td class="searchCriteria"><input type="text"
						value="<%=lNameSearch%>" name="lastName"
						placeholder="Enter Last Name"></td>
					<!-- <td width="11%">College Name</td> -->
					<td><select name="collegeName" id="collegeName"
						class="commonDropDownList">
							<option value="">Select a College</option>
							<%
								for (String college : collegeList) {
									if (clgNameSearch.equalsIgnoreCase(college)) {
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
					</select></td>
					<td><input type="submit" value="Search" name="operation"
						class="commonSearchButton"></td>
					<td><input type="submit" name="operation" value="Delete"
						class="commonDeleteButton" /></td>
					<td><input type="submit" name="operation" value="Add"
						class="commonAddButton" /></td>
					<td><input type="reset" name="operation" value="Reset"
							class="commonAddButton" /></td>		
				</tr>
			</table>
		</div>

		<div style="overflow-x: auto;">


			<table class="orsCommonRecordTable" align="center" style="width: 80%">
				<tr>
					<td><input type="checkbox" id="orsSelectAll"
						onchange="selectAll(this.form)"></td>
					<td>Serial No</td>
					<!-- 					<td>Student Id</td> -->
					<!-- 					<td>College ID</td> -->
					<td>College Name</td>
					<td>First Name</td>
					<td>Last Name</td>
					<td>Date of Birth</td>
					<td>Mobile No</td>
					<td>Email</td>
					<td></td>
				</tr>

				<%
					if (list != null && list.size() > 0) {
						for (StudentBean bean : list) {
				%>
				<tr>
					<td><input type="checkbox" id="<%=bean.getId()%>" name="id"
						value="<%=bean.getId()%>"></td>
					<td><%=count++%></td>
					<%-- 					<td><%=bean.getId()%></td> --%>
					<%-- 					<td><%=bean.getCollegeId()%></td> --%>
					<td><%=bean.getCollegeName()%></td>
					<td><%=bean.getFirstName()%></td>
					<td><%=bean.getLastName()%></td>
					<td><%=bean.getDob()%></td>
					<td><%=bean.getMobileNo()%></td>
					<td><%=bean.getEmail()%></td>
					<td><a
						href="<%=ORSView.STUDENT_CTL%>?id=<%=bean.getId()%>&operation=<%=StudentCtl.OP_EDIT%>">Edit</a></td>
				</tr>

				<%
					}
					}
				%>

	</table>
	<%-- <div class="bottomFunctionDiv"><table class="bottomFunctionTable" >
				<tr>					
					<td style="background-color: white; border-color: white"> <input
						type="submit" name="operation"
						value="<%=StudentListCtl.OP_PREVIOUS%>"
						class="commonPreviousButton"></td>
					
					<td style="background-color: white; border-color: white"><input
						type="submit" name="operation" value="<%=StudentListCtl.OP_NEXT%>"
						class="commonNextButton"></td>					
				</tr>
			</table></div> --%>
			<div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><c:choose>
								<c:when test="${pageNo==1}">
									<input type="submit" name="operation" disabled
										value="${StudentListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${StudentListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${StudentListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${StudentListCtl.OP_NEXT}" class="commonNextButton">
								</c:otherwise>
							</c:choose></td>
					</tr>
				</table>
			</div>
		</div>

	</form>
	<div>
		<%@ include file="ORSFooter.jsp"%>
	</div>
</body>
</html>