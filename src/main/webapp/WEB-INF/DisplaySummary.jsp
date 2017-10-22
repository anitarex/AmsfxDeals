<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 
 <%@page import="amsfx.*"%>
<jsp:useBean id="displayDao" type="amsfx.DealDao" scope="request" />
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>Import Summary</title>
          <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
       <link href="<c:url value="/resources/theme1/ani_theme.css" />" rel="stylesheet">
      
    </head>
 <body class="form-style-1">
   <h1>Import Summary</h1>
 
        <hr> 
        <table class="form-style-2" cellpadding="0" align="left">
		
				
        <% for (ImportDetails impDetail  : displayDao.getImportSummary(request.getParameter("sourceFileName"))) { %>
           
           <tr><td><label>Source File Name</td><td><p><%=impDetail.getSourceFileName() %> </td></tr>
           <tr><td><label>Start Time</td><td><p> <%=impDetail.getStartTime() %></td></tr>
          <tr><td><label>End Time</td><td><p><%=impDetail.getEndTime() %> </td></tr>
          <tr><td><label>Total time taken</td><td><p><%=(impDetail.getEndTime().getTime()-impDetail.getStartTime().getTime())/1000 %>secs</td></tr>
            <tr> <td><label>Number of Valid Deals</td><td><p> <%=impDetail.getValidDealCount() %> </td></tr>
           <tr><td><label>Number of InValid Deals</td><td> <p><%=impDetail.getInvalidDealCount() %> </td></tr>
        <tr> <td><label>Total Records </td><td><p> <%=impDetail.getValidDealCount()+impDetail.getInvalidDealCount()%> </td></tr>
          
        
           
        <% } %>
  

   <tr><td></td><td><form action="uploadFile.do" >
 <p><input type="submit" value="Back"/> 
</form></td></tr>
 </table>
        <iframe 
            frameborder="0" scrolling="no" width="100%" height="30"> </iframe>
     </body>
 </html>