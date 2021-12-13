<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.MyProfileCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Profile</title>
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="css/student.css">
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
	<%@ include file="ORSHeader.jsp"%>

	<form action="<%=ORSView.MY_PROFILE_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>My Profile</h1> 
				</td>
			</tr>
		</table>
		<div class="displayMsgDiv">
			<c:if test="${requestScope.error != null}">
				<font class="errorMsg"> ${requestScope.error }</font>
			</c:if>
			<c:if test="${requestScope.success != null}">
			<font class="successMsg"> ${requestScope.success }</font>
		</c:if>		
		<c:if test="${requestScope.updateRecordMsg != null}">
			<font class="updateMsg"> ${requestScope.updateRecordMsg }</font>
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
						<td><input type="text" name="fname"
							value="${bean.getFirstName() }"></td>
					</tr>
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="lname"
							value="${bean.getLastName() }"></td>
					</tr>
					<tr>
						<td>Login Id</td>
						<td><input type="text" name="loginId" readonly="readonly"
							value="${bean.getLoginId() }"></td>
					</tr>
					<tr>
						<td>Mobile</td>
						<td><input type="text" name="mobNo"
							value="${bean.getMobNo() }"></td>
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
							value="${MyProfileCtl.getDateFormat(bean.getDob()) }"></td>
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
						<td><c:if test="${requestScope.loginErr != null}">
				 ${requestScope["loginErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.mobNoErr != null}">
				 ${requestScope["mobNoErr"] }
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
				</table>
			</div>
		</div>

		<div style="margin: auto;">
			<table align="center">
				<tr>
				<td><input type="submit" value="<%=MyProfileCtl.OP_CHANGE_MY_PASSWORD%>"
						name="operation" class="commonSaveAndAddButton" style="width: 180px"></td>
					<td><input type="submit" value="<%=BaseCtl.OP_SAVE%>"
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