<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.controller.CourseCtl"%>
<%@page import="in.co.sunrays.controller.CourseCtl"%>
<%@page import="in.co.sunrays.util.DataUtility"%>
<%@page import="in.co.sunrays.util.ServletUtility"%>
<%@page import="in.co.sunrays.util.HTMLUtility"%>
<%@page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Course Details</title>
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

	<form action="CourseCtl" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Course Details</h1>
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
						<td><input type="text" name="name"
							placeholder="Enter Course Name Here" value="${bean.getName() }"></td>
					</tr>
					<tr>
						<td>Duration</td>
						<td><select name="duration"
							style="width: 170px; height: 30px; padding-left: 55px;">
							<option  value="">Select</option>
								<c:forEach items="${courseDurationList}" var="duration">
									<c:choose>
										<c:when test="${bean.getDuration()==duration.toString()}">
											<option selected="selected" value="${duration}">
												${duration}</option>
										</c:when>
										<c:otherwise>
											<option value="${duration}">
												${duration}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select>						
						</td>
					</tr>
					<tr>
						<td>Description</td>
						<td>							
					<textarea name="description" placeholder="Enter Description Here"
						rows="4" cols="22">${bean.getDescription() }</textarea>						
							</td>
					</tr>
				</table>
			</div>

			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.nameErr != null}">
				 ${requestScope["nameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.durationErr != null}">
				 ${requestScope["durationErr"] }
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