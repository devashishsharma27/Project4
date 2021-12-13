<%@page import="in.co.sunrays.controller.ErrorCtl"%>
<%@page import="in.co.sunrays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<center>
<form action="<%=ORSView.ERROR_CTL%>" method="post">
<br>
<br>
<br>
<br>
<img alt="" src="<%=ORSView.APP_CONTEXT%>/jsp/error.jpg">
<!-- <h1 style="color: red;">CONNECTION TIME OUT ERROR</h1>
<br><br>
<h1 style="color: red;">CAUSE AND FIX</h1> -->
<br><br>
<input type="submit" name="operation" value="<%=ErrorCtl.OP_BACK%>" style="height:30px; width:150px;">

</form>
</center>

</body>
</html>