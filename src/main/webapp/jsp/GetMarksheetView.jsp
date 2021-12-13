<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.co.sunrays.beans.MarksheetBean"%>
<%@page import="in.co.sunrays.controller.GetMarksheetCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Marksheet</title>
<style type="text/css">


table.getMarksheetTableCSS1{
background-color : #FFFFE0 ;
width:100%;
height: 50px;
border: 1px solid black;
margin-top: 30px;
}

table.getMarksheetTableCSS2{
background-color : #FFFFE0 ;
 width:100%; 
 height: 50px;
 border: 1px solid black;
 text-align: center;
 font-size: large;
}


table.getMarksheetTableCSS3{
background-color: #FFFFE0 ;
 width: 100%; 
 border: 1px solid black;
 text-align: center;

 
}


table.getMarksheetTableCSS3 td {

border: 1px solid black;
height: 40px;

}
table.getMarksheetTableCSS3 th {

border: 1px solid black;
height: 40px;

}


</style>
</head>
<body>
<body>
	<%@ include file="ORSHeader.jsp"%>

	

	<form action="<%=ORSView.GET_MARKSHEET_CTL%>" method="post">

		<table align="center">
			<tr>
				<td>
					<h1>Get MARKSHEET</h1>
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
						<td>Roll No <input type="text" name="rollNo"
							value="${bean.getRollNo() }"> &nbsp;&nbsp;&nbsp;<input
							type="submit" value="<%=GetMarksheetCtl.OP_GO%>" name="operation"
							class="">&nbsp;&nbsp;&nbsp;<input type="submit"
							value="Cancel" name="operation" class=""></td>
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

				</table>
			</div>
		</div>
		
		
		


		<c:if test="${bean.getId() >0}">	
			
		<table class="getMarksheetTableCSS1">
			<tr>
				<th><h1>Marksheet</h1></th>
			</tr>			
		</table>

		<table class="getMarksheetTableCSS2">
			<tr>
				<td>Roll Number: ${bean.getRollNo() }</td>
				<td>Student Name:${bean.getName() }</td>
				<td>College Name:${collegeName}</td>
			</tr>
		</table>


		<table class="getMarksheetTableCSS3">
			<tr>
				<th>Subject</th>
				<th>Maximum Marks</th>
				<th>Passing Marks</th>
				<th>Marks Obtained</th>
				<th>Remark</th>
			<tr>
				<td>Mathematics</td>
				<td>100</td>
				<td>33</td>
				<td>${bean.getMathMarks() }</td>
				<c:choose>
					<c:when test="${bean.getMathMarks() >33}">
						<td style="color: green">Pass</td>
					</c:when>
					<c:otherwise>
						<td style="color: red;">Fail</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>Physics</td>
				<td>100</td>
				<td>33</td>
				<td>${bean.getPhyMarks()}</td>
				<c:choose>
					<c:when test="${bean.getPhyMarks() >33}">
						<td style="color: green">Pass</td>
					</c:when>
					<c:otherwise>
						<td style="color: red;">Fail</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>Chemistry</td>
				<td>100</td>
				<td>33</td>
				<td>${bean.getChemMarks()}</td>
				<c:choose>
					<c:when test="${bean.getChemMarks() >33}">
						<td style="color: green">Pass</td>
					</c:when>
					<c:otherwise>
						<td style="color: red;">Fail</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<th>Total</th>
				<th><%=300%></th>
				<th>99</th>
				<th>${bean.getPhyMarks() + bean.getChemMarks() + bean.getMathMarks()}</th>
				<c:choose>
					<c:when test="${bean.getPhyMarks() + bean.getChemMarks() + bean.getMathMarks()>99}">
						<th style="color: blue">Pass</th>
					</c:when>
					<c:otherwise>
						<th style="color: red;">Fail</th>
					</c:otherwise>
				</c:choose>				
			</tr>
		</table>
</c:if>

<%-- <div style="margin: auto;">
			<table align="center">
				<tr>
					<td><input type="submit"
			value="<%=GetMarksheetCtl.OP_BACK%>" name="operation"></td>
					
				</tr>
			</table>
		</div> --%>		
	</form>

	<%@ include file="ORSFooter.jsp"%>
</body>
</html>