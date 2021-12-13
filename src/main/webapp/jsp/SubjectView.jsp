<%@page import="in.co.sunrays.model.SubjectModel"%>
<%@page import="in.co.sunrays.controller.SubjectCtl"%>
<%@page import="java.util.*"%>
<%@page import="in.co.sunrays.util.*"%>
<%@page import="in.co.sunrays.exception.*"%>
<%-- <%@page errorPage="ErrorView.jsp" %> --%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Subject Details</title>
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

	<form action="SubjectCtl" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Subject Details</h1> 
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
						<td>Subject Name</td>
						<td><input type="text" name="name"
							placeholder="Enter Subject Name Here" value="${bean.getName() }"></td>
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
						<td><c:if test="${requestScope.courseNameIdErr != null}">
				 ${requestScope["courseNameIdErr"] }
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