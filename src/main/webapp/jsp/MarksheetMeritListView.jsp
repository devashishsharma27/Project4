<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.beans.MarksheetBean"%>
<%@page import="in.co.sunrays.controller.MarksheetMeritListCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Marksheet Merit</title>
</head>
<body>
	<%
		int pageNo = 0;

		if (request.getAttribute("pageNo") != null) {
			pageNo = (Integer) request.getAttribute("pageNo");
		} else {
			pageNo = 1;
		}
		int recordCount = (Integer) request.getAttribute("recordCount");
		int count = (pageNo == 1) ? 1 : (pageNo - 1) * 10 + 1;
	%>
	<div>
		<%@ include file="ORSHeader.jsp"%>
	</div>
	<div style="text-align: center;">

		<h1>Merit List</h1>

	</div>

	<div style="text-align: center;">

		<c:if test="${requestScope.error != null}">
			<font class="errorMsg"> ${requestScope.error }</font>
		</c:if>
		<c:if test="${requestScope.success != null}">
			<font class="successMsg"> ${requestScope.success }</font>
		</c:if>

	</div>
	<form action="MarksheetMeritListCtl" method="post">

		<input type="hidden" name="pageNo" value="${pageNo}"> <input
			type="hidden" name="recordCount" value="${recordCount}">

		<div style="overflow-x: auto;">
			<table class="orsCommonRecordTable" align="center" style="width: 80%">
				<tr>

					<td>Rank</td>
					<td>ID</td>
					<td>Roll No</td>
					<td>Name</td>
					<td>Physics</td>
					<td>Chemistry</td>
					<td>Mathematics</td>

				</tr>
				<c:if test="${list != null && list.size() > 0}">
					<c:forEach items="${list}" var="bean">
						<tr>
							<td><%=count++%></td>
							<td>${bean.getId()}</td>
							<td>${bean.getRollNo()}</td>
							<td>${bean.getName()}</td>
							<td>${bean.getPhyMarks()}</td>
							<td>${bean.getChemMarks()}</td>
							<td>${bean.getMathMarks()}</td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${list == null || list.size() == 0}">
					<font class="errorMsg">No Record Found</font>
				</c:if>
			</table>
		</div>

		<div class="bottomFunctionDiv">
			<table class="bottomFunctionTable">
				<tr>
					<td><input type="submit" name="operation"
						value="${MarksheetMeritListCtl.OP_BACK}"
						class="commonPreviousButton"></td>

				</tr>
			</table>
		</div>
	</form>
	<div>
		<%@ include file="ORSFooter.jsp"%>
	</div>
</body>
</html>