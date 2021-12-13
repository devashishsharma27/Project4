<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.RoleListCtl"%>
<%@page import="in.co.sunrays.beans.RoleBean"%>
<%@page import="in.co.sunrays.controller.RoleCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Role List</title>
<script type="text/javascript" src="../js/orsUtil.js"></script>
</head>
<body>
	<%@ include file="ORSHeader.jsp"%>

	<%
		int pageNo = 0;
		/* ArrayList<RoleBean> list = (ArrayList<RoleBean>) request.getAttribute("list"); */

		if (request.getAttribute("pageNo") != null) {
			pageNo = (Integer) request.getAttribute("pageNo");
		} else {
			pageNo = 1;
		}
		int recordCount = (Integer) request.getAttribute("recordCount");
		int count = (pageNo == 1) ? 1 : (pageNo - 1) * 10 + 1;

		String roleNameSearch = (String) request.getAttribute("roleNameSearch");
		roleNameSearch = (roleNameSearch == null) ? "" : roleNameSearch;
	%>
	<div style="text-align: center;">

		<h1>Role List</h1>
		
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

		<form action="RoleListCtl" method="post">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="recordCount" value="${recordCount}">

			<div style="width: 50%; padding-left: 18%;">
				<table class="functionTable">
					<tr>
						<td class="searchCriteria"><input type="text"
							value="${roleNameSearch }" name="roleName"
							placeholder="Enter Role Name"></td>
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
						<td>Role Name</td>
						<td>Description</td>
						<td></td>
					</tr>
					<c:if test="${list != null && list.size() > 0}">
						<c:forEach items="${list}" var="bean">
							<tr>
								<td><input type="checkbox" name="id" id="${bean.getId()}"
									value="${bean.getId()}"></td>
								<td><%=count++%></td>
								<td>${bean.getName()}</td>
								<td>${bean.getDescription()}</td>
								<td><a
									href="<%=ORSView.ROLE_CTL%>?id=${bean.getId()}&operation=<%=RoleCtl.OP_EDIT%>">Edit</a></td>
							</tr>
						</c:forEach>
					</c:if>
					<%-- <c:if test="${list == null || list.size() == 0}">
						<font class="errorMsg">No Record Found</font>
					</c:if> --%>
				</table>
			</div>

			<%-- <div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><input type="submit" name="operation"
							value="${RoleListCtl.OP_PREVIOUS}" class="commonPreviousButton"></td>
						<td><input type="submit" name="operation"
							value="${ RoleListCtl.OP_NEXT}" class="commonNextButton"></td>
					</tr>
				</table>
			</div> --%>
			<div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><c:choose>
								<c:when test="${pageNo==1}">
									<input type="submit" name="operation" disabled
										value="${RoleListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${RoleListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${RoleListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${RoleListCtl.OP_NEXT}" class="commonNextButton">
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