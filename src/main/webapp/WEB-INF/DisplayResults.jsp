<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 
 <%@page import="amsfx.*"%>
<jsp:useBean id="displayDealDao" type="amsfx.DealDao" scope="request" />
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>Listing Valid Deals</title>
          <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
       <link href="<c:url value="/resources/theme1/ani_theme.css" />" rel="stylesheet">
      
    </head>
 <body class="form-style-1">
   <h1>Listing Valid Deals</h1>
 
        <hr><ol> 
        <% for (ValidDeal validdeal  : displayDealDao.getValidDeals(request.getParameter("sourceFileName"))) { %>
            <t><%=validdeal.getDealID() %> </t>
            <t> <%=validdeal.getFromCurrency() %> </t>
            <t> <%=validdeal.getToCurrency() %> </t>
            <t> <%=validdeal.getDealAmount() %> </t>
            <t> <%=validdeal.getSourceFileName() %> </t>
           <BR> 
        <% } %>
        </ol><hr>
 
        <iframe 
            frameborder="0" scrolling="no" width="100%" height="30"> </iframe>
     </body>
 </html>