<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.sunrays.controller.ORSView"%>
<%@page import="in.co.sunrays.controller.BaseCtl"%>
<%@page import="in.co.sunrays.beans.UserBean"%>
<%@page import="in.co.sunrays.controller.LoginCtl"%>
<%@page import="in.co.sunrays.beans.RoleBean"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
<link rel="stylesheet" href="../css/common.css">
<style>
ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
	overflow: hidden;
	background-color: #7cb26e;
}

li {
	float: left;
}

li a {
	display: block;
	color: white;
	text-align: center;
	padding: 16px;
	text-decoration: none;
}

li a:hover {
	background-color: #111111;
}
</style>

</head>
<body>
	<%
		UserBean user = (UserBean) session.getAttribute("user");

		String guest = "Guest";
		boolean isUserLoggedIn = true;
		if (user == null) {
			guest = "Hi Guest";
			isUserLoggedIn = false;
		} else {
			guest = "Hi " + user.getFirstName().toUpperCase();
		}
	%>
<div>
	<div style="width: 100%; display: inline-block;">

		<div class="" style="width: 92%; height: 10%; float: left;">
			<ul>

				<%
					if (!isUserLoggedIn) {
				%>
				<li><a href="<%=ORSView.MY_PROFILE_CTL%>"><%=guest%></a></li>
				<li><a href="<%=ORSView.LOGOUT_CTL%>">Log In</a></li>
				<li><a
					href="<%=ORSView.LOGOUT_CTL%>?operation=<%=LoginCtl.OP_SIGN_UP%>">Sign
						Up</a></li>

				<%
					} else {
						if (user.getRoleId() == RoleBean.ADMIN) {
				%>
				<li><a href="<%=ORSView.WELCOME_CTL%>">Home</a></li>				

				<li><a href="<%=ORSView.USER_CTL%>">Add User</a></li>
				<li><a href="<%=ORSView.USER_LIST_CTL%>">User List</a></li>

				<li><a href="<%=ORSView.COLLEGE_CTL%>">Add College</a></li>
				<li><a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a></li>

				<li><a href="<%=ORSView.STUDENT_CTL%>">Add Student</a></li>
				<li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>

				<li><a href="<%=ORSView.ROLE_CTL%>">Add Role</a></li>
				<li><a href="<%=ORSView.ROLE_LIST_CTL%>">Role List</a></li>

				<li><a href="<%=ORSView.COURSE_CTL%>">Add Course</a></li>
				<li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>

				<li><a href="<%=ORSView.SUBJECT_CTL%>">Add Subject</a></li>
				<li><a href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a></li>

				<li><a href="<%=ORSView.TIMETABLE_CTL%>">Add Time Table</a></li>
				<li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">Time Table List</a></li>

				<li><a href="<%=ORSView.FACULTY_CTL%>">Add Faculty</a></li>
				<li><a href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a></li>				
				
				<li><a href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</a></li>
				<li><a href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a></li>
				
				<li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Merit List</a></li>
				<li><a href="<%=ORSView.GET_MARKSHEET_CTL%>">Get Marksheet</a></li>
				
				<li><a href="<%=ORSView.JAVA_DOC_VIEW%>">Java Doc</a></li>
				<li><a href="<%=ORSView.MY_PROFILE_CTL%>">My Profile</a></li>
				<li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
				<li><a href="<%=ORSView.LOGOUT_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</a></li>
			
				<%
					 }if (user.getRoleId() == RoleBean.STUDENT){
 %>
 	         <li><a href="<%=ORSView.WELCOME_CTL%>">Home</a></li>
 	
 			 <li><a href="<%=ORSView.GET_MARKSHEET_CTL%>">Get Marksheet</a></li>
			 <li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Merit List</a></li>				
			 
			 <li><a href="<%=ORSView.MY_PROFILE_CTL%>">My Profile</a></li>
			 <li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
			 <li><a href="<%=ORSView.LOGOUT_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</a></li>		
			
			 <%	}if (user.getRoleId() == RoleBean.FACULTY) {
 %>
			<li><a href="<%=ORSView.WELCOME_CTL%>">Home</a></li>
			<li><a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a></li>
			
			<li><a href="<%=ORSView.STUDENT_CTL%>">Add Student</a></li>
			<li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>
				
			 <li><a href="<%=ORSView.GET_MARKSHEET_CTL%>">Get Marksheet</a></li>
			 <li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Merit List</a></li>				
		
		    <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>	 
		    
		    <li><a href="<%=ORSView.SUBJECT_CTL%>">Add Subject</a></li>
			<li><a href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a></li>
			
			 <li><a href="<%=ORSView.MY_PROFILE_CTL%>">My Profile</a></li>
			 <li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
			 <li><a href="<%=ORSView.LOGOUT_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</a></li>
			
			 <%	}if (user.getRoleId() == RoleBean.COLLEGE_SCHOOL) {
 %>
 
		 <li><a href="<%=ORSView.WELCOME_CTL%>">Home</a></li>
		 <li><a href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a></li>
		 
		 <li><a href="<%=ORSView.STUDENT_CTL%>">Add Student</a></li>
		 <li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>
				
		<li><a href="<%=ORSView.FACULTY_CTL%>">Add Faculty</a></li>
		<li><a href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a></li>	
								
		 <li><a href="<%=ORSView.COURSE_CTL%>">Add Course</a></li>
		 <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>
		
		<li><a href="<%=ORSView.SUBJECT_CTL%>">Add Subject</a></li>
		<li><a href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a></li>		
					 
		 <li><a href="<%=ORSView.TIMETABLE_CTL%>">Add Time Table</a></li>
		<li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">Time Table List</a></li>
					 		
		<li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Merit List</a></li>
		
		<li><a href="<%=ORSView.MY_PROFILE_CTL%>">My Profile</a></li>
		<li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
		<li><a href="<%=ORSView.LOGOUT_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</a></li>		 
	
	<%} if (user.getRoleId() == RoleBean.KIOSK) { %>
 
       <li><a href="<%=ORSView.WELCOME_CTL%>">Home</a></li>
       <li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Merit List</a></li>
	   <li><a href="<%=ORSView.GET_MARKSHEET_CTL%>">Get Marksheet</a></li>
		<li><a href="<%=ORSView.MY_PROFILE_CTL%>">My Profile</a></li>
		<li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
		<li><a href="<%=ORSView.LOGOUT_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</a></li>	
			
			 <%	} 
	}	%>
			</ul>
		</div> <div
			style="width: 8%;  float: left; background-color: #7cb26e ; text-align: right;">
			
			<%
					if (isUserLoggedIn) {
				if(user.getRoleId() == RoleBean.ADMIN ||  user.getRoleId() == RoleBean.COLLEGE_SCHOOL){ %>
			<img alt="Rays Technology" src="../image/raysLogo.png" 
				height="98" width="100" style=" margin-right: 5%">	
		<%	}else{ %>
		<img alt="Rays Technology" src="../image/raysLogo.png" 
		height="46" width="100" style=" margin-right: 5%">	
<%}
		
		}else{ %>
			<img alt="Rays Technology" src="../image/raysLogo.png" 
				height="46" width="100" style=" margin-right: 5%">	
		<%} %>
		</div>
		
	</div>
</div>	
</body>
</html>