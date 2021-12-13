<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="in.co.sunrays.model.SubjectModel"%>
<%@page import="in.co.sunrays.util.*"%>
<%@page import="in.co.sunrays.controller.*"%>
<%@page import="in.co.sunrays.exception.*"%>
<%@page import="in.co.sunrays.util.ServletUtility"%>
<%-- <%@page errorPage="ErrorView.jsp" %> --%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Faculty Details</title>
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
<body>
	<%@ include file="ORSHeader.jsp"%>
	
	<%
		String operation = (String) request.getParameter("operation");
		if (operation != null && (operation.equalsIgnoreCase("Edit")||operation.equalsIgnoreCase("Save"))) {
			operation = BaseCtl.OP_SAVE;
		} else {
			operation = BaseCtl.OP_ADD;
		}
	%>
	
	<form action="FacultyCtl" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Faculty Details</h1> 
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
						<td>First Name</td>
						<td><input type="text" name="fName"
							placeholder="Enter First Name Here" value="${bean.getFirstName() }"></td>
					</tr>
					
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="lName"
							placeholder="Enter Last Name Here" value="${bean.getLastName() }"></td>
					</tr>
					
					<tr>
						<td>Qualification</td>
						<td><input type="text" name="qualification"
							placeholder="Enter Qualification Here" value="${bean.getQualification() }"></td>
					</tr>
					
					<tr>
						<td>Login Id</td>
						<td><input type="text" name="loginId"
							placeholder="Enter Email ID Here" value="${bean.getEmailId() }"></td>
					</tr>
					
					<tr>
						<td>Phone No</td>
						<td><input type="text" name="mobileNo"
							placeholder="Enter Mobile No Here" value="${bean.getMobileNo() }"></td>
					</tr>
					
					<tr>
						<td>Gender</td>
						<td><c:choose>
								<c:when test="${bean.getGender() == 'male'}">
									<input type="radio" id="male" name="gender" value="male"
										checked="checked">
									<label for="male">Male</label>
								</c:when>
								<c:otherwise>
									<input type="radio" id="male" name="gender" value="male">
									<label for="male">Male</label>
								</c:otherwise>
							</c:choose> <c:choose>
								<c:when test="${bean.getGender() == 'female'}">
									<input type="radio" id="female" name="gender" value="female"
										checked="checked">
									<label for="female">Female</label>
								</c:when>
								<c:otherwise>
									<input type="radio" id="female" name="gender" value="female">
									<label for="female">Female</label>
								</c:otherwise>
							</c:choose> <c:choose>
								<c:when test="${bean.getGender() == 'other'}">
									<input type="radio" id="other" name="gender" value="other"
										checked="checked">
									<label for="other">Other</label>
								</c:when>
								<c:otherwise>
									<input type="radio" id="other" name="gender" value="other">
									<label for="other">Other</label>
								</c:otherwise>
							</c:choose></td>
					</tr>
					
					
					<tr>
						<td>Date of Birth</td>
						<td><input type="text" id="dob" name="dob"
							value="${ServletUtility.getDateFormat(bean.getDOB()) }"></td>
					</tr>
					
					<tr>
						<td>College Name</td>
						<td><select name="collegeNameId"
							style="width: 170px; height: 30px; padding-left: 55px;">
							<option  value="">Select</option>
								<c:forEach items="${collegeList}" var="college">
									<c:choose>
										<c:when test="${bean.getCollegeId()==college.getId()}">
											<option selected="selected" value="${college.getId()}">
												${college.getName()}</option>
										</c:when>
										<c:otherwise>
											<option value="${college.getId()}">
												${college.getName()}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select>						
						</td>
					</tr>
					
					<tr>
						<td>Course Name</td>
						<td><select name="courseNameId"
							style="width: 170px; height: 30px; padding-left: 55px;">
							<option  value="">Select</option>
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
						</select>						
						</td>
					</tr>
					
					
					<tr>
						<td>Subject Name</td>
						<td><select name="subjectNameId"
							style="width: 170px; height: 30px; padding-left: 55px;">
							<option  value="">Select</option>
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
						</select>						
						</td>
					</tr>					
				</table>
			</div>

			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.firstNameErr != null}">
				 ${requestScope["firstNameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.lastNameErr != null}">
				 ${requestScope["lastNameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.qualificationErr != null}">
				 ${requestScope["qualificationErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.loginIdErr != null}">
				 ${requestScope["loginIdErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.mobileNoErr != null}">
				 ${requestScope["mobileNoErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.genderErr != null}">
				 ${requestScope["genderErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.dobErr != null}">
				 ${requestScope["dobErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.collegeNameErr != null}">
				 ${requestScope["collegeNameErr"] }
			</c:if></td>
					</tr>
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