<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page isErrorPage = "true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Error Handler</title>
</head>
<body>
<%@ include file="ORSHeader.jsp"%>

<% Exception e = (Exception) request.getAttribute("exception") ;
String msg = e.getMessage();

%>


<div style="text-align: center; color: red; font-size: xx-large; padding-top: 30px;">
<%= msg %>
</div>
<div style="text-align: center; color: blue; font-size: x-large; padding-top: 40px;">
 Please <a href="<%=ORSView.WELCOME_CTL%>">Click Here</a> to go to Home Page !!
</div>
<%@ include file="ORSFooter.jsp"%>
</body>
</html>