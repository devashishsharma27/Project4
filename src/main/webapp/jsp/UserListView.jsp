<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="in.co.sunrays.controller.UserListCtl"%>
<%@page import="in.co.sunrays.beans.UserBean"%>
<%@page import="in.co.sunrays.controller.UserCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User List</title>
<script type="text/javascript" src="../js/orsUtil.js"></script>
</head>
<body>

	<%@ include file="ORSHeader.jsp"%>

	<%
		int pageNo = 0;

		if (request.getAttribute("pageNo") != null) {
			pageNo = (Integer) request.getAttribute("pageNo");
		} else {
			pageNo = 1;
		}
		int recordCount = (Integer) request.getAttribute("recordCount");
		int count = (pageNo == 1) ? 1 : (pageNo - 1) * 10 + 1;

		String firstNameSearch = (String) request.getAttribute("firstNameSearch");
		firstNameSearch = (firstNameSearch == null) ? "" : firstNameSearch;

		String lastNameSearch = (String) request.getAttribute("lastNameSearch");
		lastNameSearch = (lastNameSearch == null) ? "" : lastNameSearch;

		String loginSearch = (String) request.getAttribute("loginSearch");
		loginSearch = (loginSearch == null) ? "" : loginSearch;
	%>
	<div style="text-align: center;">

		<h1>User List</h1>

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

		<form action="UserListCtl" method="post">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="recordCount" value="${recordCount}">

			<div style="width: 50%; padding-left: 18%;">
				<table class="functionTable">
					<tr>
						<td class="searchCriteria"><input type="text"
							value="${firstNameSearch }" name="fname"
							placeholder="Enter First Name"></td>
						<td class="searchCriteria"><input type="text"
							value="${lastNameSearch }" name="lname"
							placeholder="Enter Last Name"></td>
						<td class="searchCriteria"><input type="text"
							value="${loginSearch }" name="loginId"
							placeholder="Enter Login Id"></td>

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
						<td>Login</td>
						<td>Gender</td>
						<td>Role</td>
						<td>DOB</td>
						<td>Mobile No</td>
						<td></td>
					</tr>
					<c:if test="${list != null && list.size() > 0}">
						<c:forEach items="${list}" var="bean">
							<tr>
								<td><c:choose>
										<c:when test="${bean.getRoleId()==1}">
											<input type="checkbox" name="" disabled id="${bean.getId()}"
												value="${bean.getId()}">
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="id" id="${bean.getId()}"
												value="${bean.getId()}">
										</c:otherwise>
									</c:choose></td>
								<td><%=count++%></td>
								<td>${bean.getFirstName()}</td>
								<td>${bean.getLastName()}</td>
								<td>${bean.getLoginId()}</td>
								<td>${bean.getGender()}</td>
								<td><c:choose>
										<c:when test="${bean.getRoleId()==1}">
											ADMIN
										</c:when>
										<c:when test="${bean.getRoleId()==2}">
											STUDENT
										</c:when>
										<c:when test="${bean.getRoleId()==3}">
											FACULTY
										</c:when>
										<c:when test="${bean.getRoleId()==4}">
											KIOSK
										</c:when>
										<c:when test="${bean.getRoleId()==5}">
											COLLEGE_SCHOOL
										</c:when>
										<c:otherwise>
											N/A
										</c:otherwise>
									</c:choose></td>
									<td>${bean.getDob()}</td>
								<td>${bean.getMobNo()}</td>
								<td>
								<c:choose>
										<c:when test="${bean.getRoleId()==1}">
											<a href="<%=ORSView.USER_CTL%>?id=${bean.getId()}&operation=<%=BaseCtl.OP_EDIT%>" style="pointer-events: none">Edit</a>										</c:when>
										<c:otherwise>
											<a href="<%=ORSView.USER_CTL%>?id=${bean.getId()}&operation=<%=BaseCtl.OP_EDIT%>">Edit</a>
										</c:otherwise>
									</c:choose>
								</td>
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
							value="${UserListCtl.OP_PREVIOUS}" class="commonPreviousButton"></td>
						<td><input type="submit" name="operation"
							value="${UserListCtl.OP_NEXT}" class="commonNextButton"></td>
					</tr>
				</table>
			</div> --%>
			<div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><c:choose>
								<c:when test="${pageNo==1}">
									<input type="submit" name="operation" disabled
										value="${UserListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${UserListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${UserListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${UserListCtl.OP_NEXT}" class="commonNextButton">
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