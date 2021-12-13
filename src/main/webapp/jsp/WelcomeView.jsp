<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ORS Welcomes You</title>
<style type="text/css">
.ORS {
	background-color: black;
	color: white;
	width: 100%;
}
</style>
</head>
<body>
	<div>
		<%@ include file="ORSHeader.jsp"%>
	</div>


	<div class="ORS">
		<center>
			<h1>
				<%=guest%>
			</h1>
		</center>
	</div>

	<div>
		<%@ include file="ORSFooter.jsp"%>
	</div>
</body>
</html>