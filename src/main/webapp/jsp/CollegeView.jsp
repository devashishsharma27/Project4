<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.beans.CollegeBean"%>
<%@page import="in.co.sunrays.controller.CollegeCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>College Detail</title>
</head>
<body>
	<%@ include file="ORSHeader.jsp"%>

	<%
		String operation = (String) request.getParameter("operation");
		if (operation != null && operation.equalsIgnoreCase("Edit")) {
			operation = BaseCtl.OP_SAVE;
		} else {
			operation = BaseCtl.OP_ADD;
		}
	%>


	<form action="<%=ORSView.COLLEGE_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>College</h1>
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
						<td>College Name</td>
						<td><input type="text" name="name" value="${bean.getName() }"></td>
					</tr>
					<tr>
						<td>Address</td>
						<td><input type="text" name="address"
							value="${bean.getAddress() }"></td>
					</tr>
					<tr>
						<td>State</td>
						<td><input type="text" name="state"
							value="${bean.getState() }"></td>
					</tr>
					<tr>
						<td>City</td>
						<td><input type="text" name="city" value="${bean.getCity() }"></td>
					</tr>
					<tr>
						<td>Phone No</td>
						<td><input type="text" name="phoneNo"
							value="${bean.getPhoneNo() }"></td>
					</tr>
				</table>
			</div>

			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.collegeNameErr != null}">
				 ${requestScope["collegeNameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.addressErr != null}">
				 ${requestScope["addressErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.stateErr != null}">
				 ${requestScope["stateErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.cityErr != null}">
				 ${requestScope["cityErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.phoneNoErr != null}">
				 ${requestScope["phoneNoErr"] }
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