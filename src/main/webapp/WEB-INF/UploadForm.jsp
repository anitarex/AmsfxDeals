
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <link href="<c:url value="/resources/theme1/ani_theme.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload the File</title>
</head>
<body  class="form-style-1" >
		<h1  >Bloomberg - Data Warehouse Project</h1>
		<h2>Import File</h2>
		<c:forEach var="message" items="${messages}">
		<table  cellpadding="0" align="left" >
		<tr><td>	
		<p style="color:Tomato; font-size:14 ; text-align:left">
		<span class="alert ${message.key}">${message.value}</span></p><BR>
		</td>
		</tr>
		</table><BR><BR><BR><BR><BR><BR>
			</c:forEach>
	
	
		
		<form  method="post" action="uploadFile.do" enctype="multipart/form-data">
<table  cellpadding="0" align="left" >
		
				<tr>
					<td><p>Upload a CSV file</p></td>
					<td><p><input type="file" name="fileUpload" size="100" /></p></td>
				</tr><tr>
					<td></td><td><p><input type="submit" value="Upload" /></td>
				</tr>
			</table>
		</form>
<BR>
<BR><BR>
<BR><BR><BR>
<BR>


	<h2 align="left">Import summary  </h1>
	
	<c:forEach var="dmessage" items="${dmessages}"><table  cellpadding="0" align="left" >
		<tr><td>
	<p style="color:Tomato; font-size:14 ;  font-weight:bold  text-align:left">	
	<span class="alert ${dmessage.key}">${dmessage.value}</span> 	</p>	

		</td>
		</tr>
		</table>
<BR><BR><BR><BR>
		</c:forEach>
</p>	
<form  method="post" action="results.do" enctype="multipart/form-data">
<table  cellpadding="0" align="left">
		
				<tr>
					<td><p>Enter the existing CSV file name</p></td>
					<td><p><input type="text" name="sourceFileName" size="100" /></p></td>
				</tr><tr>
					<td></td><td><p><input type="submit" value="Submit" /></td>
				</tr>
			</table>
		</form>
</body>
</html>