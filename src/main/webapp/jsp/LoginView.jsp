<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@page import="in.co.sunrays.controller.LoginCtl"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Login</title>
<link rel="stylesheet" href="../css/common.css">

</head>
<body>
<%@ include file="ORSHeader.jsp"%>
	<form action="<%=ORSView.LOGIN_CTL%>" method="post">
		
		<table align="center">
			<tr>
				<td>
					<h1>ORS Welcomes You</h1>
				</td>
			</tr>
		</table>

		<table  align="center">
			<tr>
				<td>User Name</td>
				<td><input type="text" id="userName" name="userName"></td>
				<td style="color:red"><span>
					<%
						if (request.getAttribute("usernameErr") != null) { %>
						<%= request.getAttribute("usernameErr") %> <% 
					}
					if (request.getAttribute("NoRecordErr") != null) { %>
						<%= request.getAttribute("NoRecordErr")%> <% 
					}
					%>
		</span>		</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" id="password" name="password">
				</td>
				<td style="color:red"><span>
					<%
						if (request.getAttribute("passwordErr") != null) { %>
						<%= request.getAttribute("passwordErr") %> <%
					}
					%>
				</span></td>
			</tr>
			</table><table align="center">
				
			<tr>
				<th></th>
				<td><input type="submit" value="<%=LoginCtl.OP_SIGN_IN%>"
					name="operation" class="commonSaveButton">
				 <input class="commonResetButton" type="reset" value="Reset" >
				 </td>
			</tr>
			</table><table align="center">
			<tr>
				<th></th>
				<td><a href="<%=ORSView.USER_REGISTRATION_CTL%>">Create an
						account?</a></td>
			</tr>
			<tr>
				<th></th>
				<td><a href="<%=ORSView.FORGET_PASSWORD_CTL%>">Forgot
						Password</a></td>
			</tr>
		</table>
		<table align="center">
		<tr><td><input type="hidden" name="requestedURI" value="<%= request.getAttribute("requestedURI") %>"></td></tr>
		<tr><td style="color:red"><span>
					<%
						if (request.getAttribute("sessionExpiredMessage") != null) { %>
						<%= request.getAttribute("sessionExpiredMessage") %> <%
					}
					%>
				</span></td></tr>
		</table>
		
	</form>



	<%@ include file="ORSFooter.jsp"%>
</body>
</html>