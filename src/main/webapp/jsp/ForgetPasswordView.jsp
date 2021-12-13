<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Forget Password</title>
</head>
<body>
	<%@ include file="ORSHeader.jsp" %>

	<form action="<%=ORSView.FORGET_PASSWORD_CTL%>" method="post">

		<table align="center">
			<tr>
				<th><h1>Forget Your Password ?</h1></th>
			</tr>
		</table>

		<table align="center">

			<tr>
				<td><h3>Please Enter Your Email Id</h3></td>
			</tr>
			<tr>
				<th>Email Id <input type="text" name="emailId" id="emailId">
					<input type="submit" name="operation" id="forgotPwdButton"
					value="Go">
				</th>
			</tr>

			<tr>

				<th style="color: red">
					<%
						if (request.getAttribute("usernameErr") != null) {
					%> <%=request.getAttribute("usernameErr")%>
					<%
						}
					if (request.getAttribute("invalidEmailErrorMsg") != null) {
					%> <%=request.getAttribute("invalidEmailErrorMsg")%> <% }  %>
				</th>
			</tr>
			<tr>
				<th style="color: green">
					<%
						if (!((String) request.getAttribute("password") == null)) {
					%> <%=request.getAttribute("password")%> <% } %>
					</th>				
			</tr>
		</table>
	</form>
	<%@ include file="ORSFooter.jsp"%>
</body>
</html>