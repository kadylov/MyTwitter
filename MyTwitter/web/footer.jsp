<%-- 
    Document   : footer.jsp
    Created on : Sep 24, 2015, 6:47:16 PM
    Author     : xl
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.GregorianCalendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="gregTime" scope="session" value="<%= new GregorianCalendar().getTime()%>"></c:set>

    <!DOCTYPE html>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html,width=device-width,height=device-height,initial-scale=2.0; charset=UTF-8"/>

            <title>JSP Page</title>
        </head>
        <body>
        <center><p>${gregTime}</p> </center>

</body>
</html>
