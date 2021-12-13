<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.CollegeListCtl"%>
<%@page import="in.co.sunrays.controller.FacultyListCtl"%>
<%@page import="in.co.sunrays.beans.FacultyBean"%>
<%@page import="java.util.*"%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Faculty List</title>
<script type="text/javascript" src="../js/orsUtil.js"></script>
</head>
<body>

	<%@include file="ORSHeader.jsp"%>

	<%
		int pageNo = 0;
		if (request.getAttribute("pageNo") != null) {
			pageNo = (Integer) request.getAttribute("pageNo");
		} else {
			pageNo = 1;
		}
		int recordCount = (Integer) request.getAttribute("recordCount");
		int count = (pageNo == 1) ? 1 : (pageNo - 1) * 10 + 1;

		 String collegeSearch = (String) request.getAttribute("collegeSearch");
		collegeSearch = (collegeSearch == null) ? "" : collegeSearch;
		
		String courseIdSearch = (String) request.getAttribute("courseIdSearch");
	 	courseIdSearch = (courseIdSearch == null) ? "" : courseIdSearch;  
	 	
	%>
	<div style="text-align: center;">

		<h1>Faculty List</h1>
	</div>
	<div style="text-align: center;">

		<c:if test="${requestScope.error != null}">
			<font class="errorMsg"> ${requestScope.error }</font>
		</c:if>
		<c:if test="${requestScope.success != null}">
			<font class="successMsg"> ${requestScope.success }</font>
		</c:if>
		<c:if test="${requestScope.addRecordMsg != null}">
			<font class="addMsg"> ${requestScope.addRecordMsg }</font>
		</c:if>
		<c:if test="${requestScope.updateRecordMsg != null}">
			<font class="updateMsg"> ${requestScope.updateRecordMsg }</font>
		</c:if>
		<c:if test="${requestScope.deleteRecordMsg != null}">
			<font class="deleteMsg"> ${requestScope.deleteRecordMsg }</font>
		</c:if>
	</div>

	<div style="text-align: center;">

		<form action="FacultyListCtl" method="post">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="recordCount" value="${recordCount}">

			<div style="width: 50%; padding-left: 18%;">
				<table class="functionTable">
					<tr>
						<td class="searchCriteria"><input type="text"
							value="${firstNameSearch }" name="firstName"
							placeholder="Enter First Name"></td>
						
						<td class="searchCriteria"><input type="text"
							value="${loginIdSearch }" name="login"
							placeholder="Enter Login Id"></td>		
												
						<td><select class="commonDropDownList"
							name="collegeId" style="width: 220px">
								<option selected="selected" value="">Select College</option>
								<c:forEach items="${collegeList}" var="college">									
									<c:choose>
										<c:when test="${collegeSearch==college.toString()}">
											<option selected="selected" value="${college}">
												${college}</option>
										</c:when>
										<c:otherwise>
											<option value="${college}">${college}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
						
						<td><select class="commonDropDownList"
							name="courseId" style="width: 220px">
								<option selected="selected" value="${courseIdSearch }">Select Course</option>
								<c:forEach items="${courseList}" var="course">								
									<c:choose>
										<c:when test="${courseIdSearch==course.getId().toString()}">
											<option selected="selected" value="${course.getId()}">
												${course.getName()}</option>
										</c:when>
										<c:otherwise>
											<option value="${course.getId()}">${course.getName()}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
						
						
						
						<td><input type="submit" name="operation" value="Search"
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
				<table class="orsCommonRecordTable" align="center"
					style="width: 80%">
					<tr>
						<td><input type="checkbox" id="orsSelectAll"
							onchange="selectAll(this.form)"></td>
						<td>Serial No</td>
						<td>First Name</td>
						<td>Last Name</td>
						<td>LoginId</td>
						<td>Gender</td>
						<td>Phone No</td>
						<td>Qualification</td>
						<td>College Name</td>
						<td>Course Name</td>
						<td>Subject Name</td>
						<td>DOB</td>						
						<td>Edit</td>
					</tr>
					<c:if test="${list != null && list.size() > 0}">
						<c:forEach items="${list}" var="bean">
							<tr>
								<td><input type="checkbox" name="id" id="${bean.getId()}"
									value="${bean.getId()}"></td>
								<td><%=count++%></td>
								<td>${bean.getFirstName()}</td>
								<td>${bean.getLastName()}</td>
								<td>${bean.getEmailId()}</td>
								<td>${bean.getGender()}</td>
								<td>${bean.getMobileNo()}</td>
								<td>${bean.getQualification()}</td>
								<td>${bean.getCollegeName()}</td>
								<td>${bean.getCourseName()}</td>
								<td>${bean.getSubject()}</td>
								<td>${bean.getDOB()}</td>
								<td><a
									href="<%=ORSView.FACULTY_CTL%>?id=${bean.getId()}&operation=<%=FacultyListCtl.OP_EDIT%>">Edit</a></td>
							</tr>
						</c:forEach>
					</c:if>
					<%-- <c:if test="${list == null || list.size() == 0}">
						<font class="errorMsg">No Record Found</font>
					</c:if> --%>
				</table>
			</div>


			<div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><c:choose>
								<c:when test="${pageNo==1}">
									<input type="submit" name="operation" disabled
										value="${FacultyListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${FacultyListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${FacultyListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${FacultyListCtl.OP_NEXT}" class="commonNextButton">
								</c:otherwise>
							</c:choose></td>
					</tr>
				</table>
			</div>
		</form>
		<div>
			<%@ include file="ORSFooter.jsp"%>
		</div>
	</div>
</body>
</html>