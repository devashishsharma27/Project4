<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.ChangePasswordCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Change Password</title>
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

	<form action="<%=ORSView.CHANGE_PASSWORD_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Change Password</h1> 
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
						<td>Old Password</td>
						<td><input type="text" name="oldPassword"
							value="${bean.getFirstName() }"></td>
					</tr>
					<tr>
						<td>New Password</td>
						<td><input type="text" name="newPassword"
							value="${bean.getLastName() }"></td>
					</tr>
					<tr>
						<td>Confirm Password</td>
						<td><input type="text" name="confirmPassword" 
							value="${bean.getLoginId() }"></td>
					</tr>					
				</table>
			</div>

			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.oldPasswordErr != null}">
				 ${requestScope["oldPasswordErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.newPasswordErr != null}">
				 ${requestScope["newPasswordErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.confirmPasswordErr != null}">
				 ${requestScope["confirmPasswordErr"] }
			</c:if></td>
					</tr>
				</table>
			</div>
		</div>

		<div style="margin: auto;">
			<table align="center">
				<tr>
				<td><input type="submit" value="<%=ChangePasswordCtl.OP_CHANGE_MY_PROFILE%>"
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