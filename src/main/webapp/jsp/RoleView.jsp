<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="in.co.sunrays.beans.RoleBean"%>
<%@page import="in.co.sunrays.controller.RoleCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>RoleView</title>
</head>
<body>
<%@ include file="ORSHeader.jsp"%>
	<%
		RoleBean bean = (RoleBean) request.getAttribute("bean");
		String operation = (String) request.getParameter("operation");
		
		String id = "";
		String roleName = "";
		String description = "";
		if (bean != null) {
			id = String.valueOf(bean.getId());
			roleName = bean.getName();
			description = bean.getDescription();
		}
		if (operation != null && (operation.equalsIgnoreCase("Edit")||operation.equalsIgnoreCase("Save"))) {
			operation = BaseCtl.OP_SAVE;
		} else {
			operation = BaseCtl.OP_ADD;
		}	
	%>

	

	<form action="<%=ORSView.ROLE_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Role</h1>
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
				<td><input type="hidden"  name="id"  value="${bean.getId() }"></td>
			</tr>
		</table>
			
			
			</div>
			<div class="" style="width: 50%; float: left;">
				<table class="orsCommonRecordTable" style="width: 100%;">
					<tr>
						<td>Role Name</td>
						<td><input type="text" id="roleName" name="roleName"
							value="${bean.getName() }"></td>
					</tr>
					<tr>
						<td>Description</td>
						<td><input type="text" id="description" name="description"
							value="${bean.getDescription() }"></td>
					</tr>
				</table>
			</div>
			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.roleNameErr != null}">
				 ${requestScope["roleNameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.descriptionErr != null}">
				 ${requestScope["descriptionErr"] }
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