<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.beans.MarksheetBean"%>
<%@page import="in.co.sunrays.controller.MarksheetCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Marksheet</title>
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

	<form action="<%=ORSView.MARKSHEET_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>MARKSHEET</h1>
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
						<td>Roll No</td>
						<td><input type="text" name="rollNo"
							value="${bean.getRollNo() }"></td>
					</tr>
					<tr>
						<td>Name</td>
						<td><select name="studentId"
							style="width: 170px; height: 30px; padding-left: 55px;">
							<option  value="">Select</option>
								<c:forEach items="${studentList}" var="student">
									<c:choose>
										<c:when test="${bean.getStudentId()==student.getId()}">
											<option selected="selected" value="${student.getId()}">
												${student.getFirstName()} ${student.getLastName()}</option>
										</c:when>
										<c:otherwise>
											<option value="${student.getId()}">${student.getFirstName()}
												${student.getLastName()}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>


						</select></td>
					</tr>
					<tr>
						<td>Physics</td>
						<td><c:choose>
								<c:when test="${bean.getPhyMarks() == 0}">
									<input type="text" name="phyMarks" value="">
								</c:when>
								<c:otherwise>
									<input type="text" name="phyMarks"
										value="${bean.getPhyMarks() }">
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<td>Chemistry</td>
						<td><c:choose>
								<c:when test="${bean.getChemMarks() == 0}">
									<input type="text" name="chemMarks" value="">
								</c:when>
								<c:otherwise>
									<input type="text" name="chemMarks"
										value="${bean.getChemMarks() }">
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<td>Maths</td>
						<td><c:choose>
								<c:when test="${bean.getMathMarks() == 0}">
									<input type="text" name="mathMarks" value="">
								</c:when>
								<c:otherwise>
									<input type="text" name="mathMarks"
										value="${bean.getMathMarks() }">
								</c:otherwise>
							</c:choose></td>
					</tr>
				</table>
			</div>

			<div class="" style="width: 30%; float: left;">
				<table class="orsCommonErrorMsgTable" style="width: 100%;">
					<tr>
						<td><c:if test="${requestScope.rollNoErr != null}">
				 ${requestScope["rollNoErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.nameErr != null}">
				 ${requestScope["nameErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.phyMarksErr != null}">
				 ${requestScope["phyMarksErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.chemMarksErr != null}">
				 ${requestScope["chemMarksErr"] }
			</c:if></td>
					</tr>
					<tr>
						<td><c:if test="${requestScope.mathMarksErr != null}">
				 ${requestScope["mathMarksErr"] }
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