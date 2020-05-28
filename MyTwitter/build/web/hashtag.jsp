<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html,width=device-width,height=device-height,initial-scale=2.0; charset=UTF-8"/>
        <title>Hashtags</title>
        <link rel="stylesheet" href="styles/home.css" type="text/css"/>
    </head>


    <c:if test="${sessionScope.user==null}">
        <c:redirect url="/login.jsp"></c:redirect>
    </c:if> 


    <c:set var = "userImg" scope = "session" value = "${sessionScope.user.profilePicture}"/>

    <body>

        <div class="header"><%@ include file="header.jsp" %></div>
        <h1>Hello World!</h1>
        <div class="">

                <c:forEach items="${twitsWithTags}" var="twit">
                    <dl>
                        <dt>
                            <img src="${pageContext.request.contextPath}/lib/${twit.ownerPicture}" class="avatar" align="left" />
                            <p><b>${twit.ownerFullname}</b> &nbsp; <a class="mention">@${twit.ownerUsername}</a>&nbsp;
                                <a>${twit.twitDate}</a>

                            <p>${twit.twitMessage}</p><br>
                        </dt>
                    </dl>
                </c:forEach>


            </div>

        </div>
        
    </body>
</html>
