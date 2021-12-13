<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.HashMap"%>
<%@page import="in.co.sunrays.controller.TimetableCtl"%>
<%@page import="in.co.sunrays.util.*"%>
<%@page import="java.util.List"%>
<%@page import="in.co.sunrays.controller.ORSView"%>
<%@page import="in.co.sunrays.util.ServletUtility"%>
<%-- <%@page errorPage="ErrorView.jsp"%> --%>
<%-- <%@page errorPage="ErrorView.jsp" %> --%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Time Table Details</title>
<link href="../css/jquery-ui.css" rel="stylesheet">
<script src="../js/jquery-1.10.2.js"></script>
<script src="../js/jquery-ui.js"></script>

<script>
	$(function() {
		$("#orsExamDate").datepicker();
		/*  $( "#datepicker-13" ).datepicker("show"); */
	});
</script>
</head>

<body>
<body>
	<%@ include file="ORSHeader.jsp"%>

	<%
		String operation = (String) request.getParameter("operation");
		if (operation != null && (operation.equalsIgnoreCase("Edit") || operation.equalsIgnoreCase("Save"))) {
			operation = BaseCtl.OP_SAVE;
		} else {
			operation = BaseCtl.OP_ADD;
		}
	%>

	<form action="TimetableCtl" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Time Table Details</h1>
				</td>
			</tr>
		</table>
		<div class="displayMsgDiv">
			<c:if test="${requestScope.error != null}">
				<font class="errorMsg"> ${requestScope.error }</font>
			</c:if>
		</div>

		<div style="width: 100%; display: inline-block;">

			<div class="" style="width: 20%; height: 1px; float: left;">

				<table class="" align="center">
					<tr>
						<td><input type="hidden" name="id" value="${bean.getId() }"></td>
					</tr>
				</table>
			</div>
			<div class="" style="width: 50%; float: left;">
				<table class="orsCommonRecordTable" style="width: 100%;">
					<tr>
						<td>Course Name</td>
						<td><select name="courseNameId"
							style="width: 170px; height: 30px; padding-left: 55px;">
								<option value="">Select</option>
								<c:forEach items="${courseList}" var="course">
									<c:choose>
										<c:when test="${bean.getCourseId()==course.getId()}">
											<option selected="selected" value="${course.getId()}">
												${course.getName()}</option>
										</c:when>
										<c:otherwise>
											<option value="${course.getId()}">
												${course.getName()}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
					</tr>


					<tr>
						<td>Subject Name</td>
						<td><select name="subjectNameId"
							style="width: 170px; height: 30px; padding-left: 55px;">
								<option value="">Select</option>
								<c:forEach items="${subjectList}" var="subject">
									<c:choose>
										<c:when test="${bean.getSubjectId()==subject.getId()}">
											<option selected="selected" value="${subject.getId()}">
												${subject.getName()}</option>
										</c:when>
										<c:otherwise>
											<option value="${subject.getId()}">
												${subject.getName()}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
					</tr>

					<tr>
						<td>Exam Time</td>
						<td><select name="orsExamTime"
							style="width: 170px; height: 30px; padding-left: 55px;">
								<option value="">Select</option>
								<c:forEach items="${examTimeMap}" var="examTime">
									<c:choose>
										<c:when test="${bean.getExamTime()==examTime.key}">
											<option selected="selected" value="${examTime.key}">
												${examTime.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${examTime.key}">${examTime.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>Semester</td>
						<td><select name="orsSemester"
							style="width: 170px; height: 30px; padding-left: 55px;">
								<option selected="selected" value="">Select</option>
								<c:forEach items="${semesterMap}" var="semester">
									<c:choose>
										<c:when test="${bean.getSemester()==semester.key}">
											<option selected="selected" value="${semester.key}">
												${semester.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${semester.key}">${semester.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
					</tr>

					<tr>
						<td>Exam Date</td>
						<td><input type="text" autocomplete="off" id="orsExamDate"
							name="orsExamDate"
							value="${ServletUtility.getDateFormat(bean.getExamDate()) }"
							placeholder="Enter Exam Date"></td>

					</tr>
				</table>
			</div>

			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.courseNameErr != null}">
				 ${requestScope["courseNameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.subjectNameErr != null}">
				 ${requestScope["subjectNameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.examTimeErr != null}">
				 ${requestScope["examTimeErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.semesterErr != null}">
				 ${requestScope["semesterErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.examDateErr != null}">
				 ${requestScope["examDateErr"] }
			</c:if></td>
					</tr>
				</table>
			</div>
		</div>

		<div style="margin: auto;">
			<table align="center">
				<tr>
					<td><input type="submit" value="<%=operation%>"
						name="operation" class="commonSaveAndAddButton"></td>
					<td><input type="submit" value="Cancel" name="operation"
						class="commonCancelButton"></td>
				</tr>
			</table>
		</div>
	</form>

	<%@ include file="ORSFooter.jsp"%>
</body>
</html>