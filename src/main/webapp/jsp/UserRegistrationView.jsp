<%@page import="in.co.sunrays.controller.UserRegistrationCtl"%>
<html>
<head>
<title>ORS User Registration</title>
<link rel="stylesheet" href="../css/common.css">
</head>
<body>
<%@ include file="ORSHeader.jsp"%>
	<form action="<%=ORSView.USER_REGISTRATION_CTL%>" method="post">
		<table class="orsCommonRecordTable" align="center">
			<tr>
				<td>
					<h1>User Registration</h1>
				</td>
			</tr>
		</table>

		<table class="orsCommonRecordTable" align="center">
			<tr>
				<td>First Name</td>
				<td><input type="text" id="fname" name="fname"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("firstNameErr") != null) { %>
						<%= request.getAttribute("firstNameErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Last Name</td>
				<td><input type="text" id="lname" name="lname"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("lastNameErr") != null) { %>
						<%= request.getAttribute("lastNameErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Login Id</td>
				<td><input type="text" id="loginId" name="loginId"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("loginIdErr") != null) { %>
						<%= request.getAttribute("loginIdErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" id="pwd" name="pwd"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("passwordErr") != null) { %>
						<%= request.getAttribute("passwordErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Confirm Password</td>
				<td><input type="password" id="cpwd" name="cpwd"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("confirmPasswordErr") != null) { %>
						<%= request.getAttribute("confirmPasswordErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Gender</td>
				<td><input type="radio" id="male" name="gender" value="male">
					<label for="male">Male</label><input type="radio"
					id="female" name="gender" value="female"> <label
					for="female">Female</label><input type="radio" id="other"
					name="gender" value="other"> <label for="other">Other</label></td>
					<th style="color:red">
					<%
						if (request.getAttribute("genderErr") != null) { %>
						<%= request.getAttribute("genderErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Date of Birth</td>
				<td><input type="date" id="dob" name="dob"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("dobErr") != null) { %>
						<%= request.getAttribute("dobErr") %> <%
					}
					%>
				</th>
			</tr>
			<tr>
				<td>Mobile Number</td>
				<td><input type="text" id="mobNo" name="mobNo"></td>
				<th style="color:red">
					<%
						if (request.getAttribute("MobileErr") != null) { %>
						<%= request.getAttribute("MobileErr") %> <%
					}
					%>
				</th>
				
			</tr>
			<tr>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td><input type="submit" value="<%=UserRegistrationCtl.OP_REGISTER%>" name="operation"></td>
				<td><input type="reset" value="Reset"></td>
			</tr>
		</table>
	</form>

	<%@ include file="ORSFooter.jsp"%>
</body>
</html>
