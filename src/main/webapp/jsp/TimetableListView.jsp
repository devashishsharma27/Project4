<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.TimetableListCtl"%>
<%@page import="in.co.sunrays.beans.TimetableBean"%>
<%@page import="java.util.*"%>
<%@page import="in.co.sunrays.util.ServletUtility"%>
<%-- <%@page errorPage="ErrorView.jsp"%> --%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Time Table List</title>

<link href="../css/jquery-ui.css" rel="stylesheet">
<script src="../js/jquery-1.10.2.js"></script>
<script src="../js/jquery-ui.js"></script>
<script type="text/javascript" src="../js/orsUtil.js"></script>
<script>
	$(function() {
		$("#examDate").datepicker();
	});
</script>
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
	
		String courseIdSearch = (String) request.getAttribute("courseIdSearch");
		courseIdSearch = (courseIdSearch == null) ? "" : courseIdSearch;
		
		String subjectNameSearch = (String) request.getAttribute("subjectNameSearch");
		subjectNameSearch = (subjectNameSearch == null) ? "" : subjectNameSearch;
		
		String semesterSearch = (String) request.getAttribute("semesterSearch");
		semesterSearch = (semesterSearch == null) ? "" : semesterSearch;
		
		String examDateSearch = (String) request.getAttribute("examDateSearch");
		examDateSearch = (examDateSearch == null) ? "" : examDateSearch;

		
	%>
	<div style="text-align: center; ">

		<h1>Time Table List</h1>
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

		<form action="TimetableListCtl" method="post">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="recordCount" value="${recordCount}">

			<div style="width: 50%; padding-left: 18%;">
				<table class="functionTable">
					<tr>
						<td><select class="commonDropDownList" name="courseId"
							style="width: 220px">
								<option selected="selected" value="">Select Course</option>
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


						<td><select class="commonDropDownList" name="subjectId"
							style="width: 220px">
								<option selected="selected" value="">Select Subject</option>
								<c:forEach items="${subjectList}" var="subject">
									<c:choose>
										<c:when test="${subjectNameSearch==subject.getId().toString()}">
											<option selected="selected" value="${subject.getId()}">
												${subject.getName()}</option>
										</c:when>
										<c:otherwise>
											<option value="${subject.getId()}">${subject.getName()}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>

						<td><select class="commonDropDownList" name="semester"
							style="width: 220px">
								<option selected="selected" value="">Select Semester</option>
								<c:forEach items="${semesterMap}" var="semester">
									<c:choose>
										<c:when test="${semesterSearch==semester.key}">
											<option selected="selected" value="${semester.key}">
												${semester.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${semester.key}">${semester.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>

						<td class="searchCriteria"><input type="text" autocomplete="off" id="examDate"
							name="examDate" value="${examDateSearch }" placeholder="Enter Exam Date"></td>

						<%-- <td class="searchCriteria"><input type="text"
							id="dob" name="dob" value="<%=ServletUtility.getDateFormat(dobSearch)%>"
							placeholder="Enter Exam Date"></td> --%>

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
						<td>Subject</td>
						<td>Course</td>
						<td>Exam Time</td>
						<td>Exam Date</td>
						<td>Semester</td>
						<td>Edit</td>
					</tr>
					<c:if test="${list != null && list.size() > 0}">
						<c:forEach items="${list}" var="bean">
							<tr>
								<td><input type="checkbox" name="id" id="${bean.getId()}"
									value="${bean.getId()}"></td>
								<td><%=count++%></td>
								<td>${bean.getSubjectName()}</td>
								<td>${bean.getCourseName()}</td>
								<td>${bean.getExamTime()}</td>
								<td>${bean.getExamDate()}</td>
								<td>${bean.getSemester()}</td>
								<td><a
									href="TimetableCtl?id=${bean.getId()}&operation=<%=TimetableListCtl.OP_EDIT%>">Edit</a></td>
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
										value="${TimetableListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${TimetableListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${TimetableListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${TimetableListCtl.OP_NEXT}" class="commonNextButton">
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