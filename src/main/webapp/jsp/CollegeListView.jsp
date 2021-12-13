<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="in.co.sunrays.controller.CollegeListCtl"%>
<%@page import="in.co.sunrays.beans.CollegeBean"%>
<%@page import="in.co.sunrays.controller.CollegeCtl"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>College List</title>
<script type="text/javascript" src="../js/orsUtil.js"></script>
</head>
<body>
<%@ include file="ORSHeader.jsp"%>

	<%
		int pageNo = 0;
		/* ArrayList<CollegeBean> list = (ArrayList<CollegeBean>) request.getAttribute("list"); */

		if (request.getAttribute("pageNo") != null) {
			pageNo = (Integer) request.getAttribute("pageNo");
		} else {
			pageNo = 1;
		}
		int recordCount = (Integer) request.getAttribute("recordCount");
		int count = (pageNo == 1) ? 1 : (pageNo - 1) * 10 + 1;

		String collegeNameSearch = (String) request.getAttribute("collegeNameSearch");
		collegeNameSearch = (collegeNameSearch == null) ? "" : collegeNameSearch;
		
		String stateSearch = (String) request.getAttribute("stateSearch");
		stateSearch = (stateSearch == null) ? "" : stateSearch;
		
		String citySearch = (String) request.getAttribute("citySearch");
		citySearch = (citySearch == null) ? "" : citySearch;
	%>
	<div style="text-align: center;">

		<h1>College List</h1>
		
	</div>

	<div style="text-align: center;">

		<c:if test="${requestScope.error != null}">
			<font class="errorMsg"> ${requestScope.error }</font>
		</c:if>
		<c:if test="${requestScope.success != null}">
			<font class="successMsg"> ${requestScope.success }</font>
		</c:if>
		<c:if test="${requestScope.addRecordMsg != null}">
			<font class="addMsg"> ${requestScope.addRecordMsg }</font>
		</c:if>
		<c:if test="${requestScope.updateRecordMsg != null}">
			<font class="updateMsg"> ${requestScope.updateRecordMsg }</font>
		</c:if>
		<c:if test="${requestScope.deleteRecordMsg != null}">
			<font class="deleteMsg"> ${requestScope.deleteRecordMsg }</font>
		</c:if>		
	</div>

	<div style="text-align: center;">

		<form action="CollegeListCtl" method="post">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="recordCount" value="${recordCount}">

			<div style="width: 50%; padding-left: 18%;">
				<table class="functionTable">
					<tr>
						<td class="searchCriteria"><input type="text"
							value="${collegeNameSearch }" name="name"
							placeholder="Enter College Name"></td>							
							<td class="searchCriteria"><input type="text"
							value="${stateSearch }" name="state"
							placeholder="Enter State Name"></td>
							<td class="searchCriteria"><input type="text"
							value="${citySearch }" name="city"
							placeholder="Enter City Name"></td>							
							
						<td><input type="submit" name="operation" value="Search"
							class="commonSearchButton"></td>
						<td><input type="submit" name="operation" value="Delete"
							class="commonDeleteButton" /></td>
						<td><input type="submit" name="operation" value="Add"
							class="commonAddButton" /></td>
						<td><input type="reset" name="operation" value="Reset"
							class="commonAddButton" /></td>	
								
					</tr>
				</table>
			</div>
			<div style="overflow-x: auto;">
				<table class="orsCommonRecordTable" align="center"
					style="width: 80%">
					<tr>
						<td><input type="checkbox" id="orsSelectAll"
							onchange="selectAll(this.form)"></td>
						<td>Serial No</td>
						<td>College Name</td>
						<td>Address</td>
						<td>State</td>
						<td>City</td>
						<td>Phone</td>
						<td></td>
					</tr>
					<c:if test="${list != null && list.size() > 0}">
						<c:forEach items="${list}" var="bean">
							<tr>
								<td><input type="checkbox" name="id" id="${bean.getId()}"
									value="${bean.getId()}"></td>
								<td><%=count++%></td>
								<td>${bean.getName()}</td>
								<td>${bean.getAddress()}</td>								
								<td>${bean.getState()}</td>
								<td>${bean.getCity()}</td>								
								<td>${bean.getPhoneNo()}</td>								
								<td><a
									href="<%=ORSView.COLLEGE_CTL%>?id=${bean.getId()}&operation=<%=CollegeCtl.OP_EDIT%>">Edit</a></td>
							</tr>
						</c:forEach>
					</c:if>
					<%-- <c:if test="${list == null || list.size() == 0}">
						<font class="errorMsg">No Record Found</font>
					</c:if> --%>
				</table>
			</div>

			<%-- <div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><input type="submit" name="operation"
							value="${CollegeListCtl.OP_PREVIOUS}" class="commonPreviousButton"></td>
						<td><input type="submit" name="operation"
							value="${CollegeListCtl.OP_NEXT}" class="commonNextButton"></td>
					</tr>
				</table>
			</div> --%>
			
			<div class="bottomFunctionDiv">
				<table class="bottomFunctionTable">
					<tr>
						<td><c:choose>
								<c:when test="${pageNo==1}">
									<input type="submit" name="operation" disabled
										value="${CollegeListCtl.OP_PREVIOUS}"
										class="commonDisabledPreviousButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${CollegeListCtl.OP_PREVIOUS}"
										class="commonPreviousButton">
								</c:otherwise>
							</c:choose></td>
						<td><c:choose>
								<c:when test="${list.size()<pageSize}">
									<input type="submit" name="operation" disabled
										value="${CollegeListCtl.OP_NEXT}"
										class="commonDisabledNextButton">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation"
										value="${CollegeListCtl.OP_NEXT}" class="commonNextButton">
								</c:otherwise>
							</c:choose></td>
					</tr>
				</table>
			</div>
		</form>
		<div>
			<%@ include file="ORSFooter.jsp"%>
		</div>
	</div>
</body>
</html>