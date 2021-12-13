<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.CourseListCtl"%>
<%@page import="in.co.sunrays.beans.CourseBean"%>
<%@page import="in.co.sunrays.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Course List</title>
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

		String courseNameSearch = (String) request.getAttribute("courseNameSearch");
		courseNameSearch = (courseNameSearch == null) ? "" : courseNameSearch;

		String courseDurationSearch = (String) request.getAttribute("courseDurationSearch");
		courseDurationSearch = (courseDurationSearch == null) ? "" : courseDurationSearch;
	%>
	<div style="text-align: center;">

		<h1>Course List</h1>
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

		<form action="CourseListCtl" method="post">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="recordCount" value="${recordCount}">

			<div style="width: 50%; padding-left: 18%;">
				<table class="functionTable">
					<tr>
						<td class="searchCriteria"><input type="text"
							value="${courseNameSearch }" name="courseName"
							placeholder="Enter Course Name"></td>
						<td><select class="commonDropDownList" name="courseDuration"
							style="width: 220px">
								<option selected="selected" value="">Select Course
									Duration</option>
								<c:forEach items="${courseDurationList}" var="duration">
									<c:choose>
										<c:when test="${courseDurationSearch==duration}">
											<option selected="selected" value="${duration}">
												${duration}</option>
										</c:when>
										<c:otherwise>
											<option value="${duration}">${duration}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>

						</select></td>

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
						<td>Name</td>
						<td>Description</td>
						<td>Duration</td>
						<td>Edit</td>
					</tr>
					<c:if test="${list != null && list.size() > 0}">
						<c:forEach items="${list}" var="bean">
							<tr>
								<td><input type="checkbox" name="id" id="${bean.getId()}"
									value="${bean.getId()}"></td>
								<td><%=count++%></td>
								<td>${bean.getName()}</td>
								<td>${bean.getDescription()}</td>
								<td>${bean.getDuration()}</td>
								<td><a
									href="<%=ORSView.COURSE_CTL%>?id=${bean.getId()}&operation=<%=CourseListCtl.OP_EDIT%>">Edit</a></td>
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
										value="${CourseListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${CourseListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${CourseListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${CourseListCtl.OP_NEXT}" class="commonNextButton">
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