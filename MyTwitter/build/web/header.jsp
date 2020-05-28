<%-- 
    Document   : header.jsp
    Created on : Sep 24, 2015, 6:47:09 PM
    Author     : xl
--%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html,width=device-width,height=device-height,initial-scale=2.0; charset=UTF-8"/>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>

        <title>JSP Page</title>
    </head>
    <body>

        <!--<h1>Header JSP</h1>-->

        <c:if test="${sessionScope.user!=null}">
            <div class="topnav">
                <a href="membership?action=home">Home</a>
                <a href="membership?action=notifications">Notification</a>
                <a href="membership?action=profile">Profile</a>
                <span class="signout"> <a href="membership?action=signout">Sign out</a></span></div>
            </c:if>

        <%--<c:if test="${cookie.containsKey('fullname')}">--%>
        <!--<span class="signout"> <a href="membership?action=signout">Sign out</a></span>-->
        <%--</c:if>--%>
    </body>
</html>
