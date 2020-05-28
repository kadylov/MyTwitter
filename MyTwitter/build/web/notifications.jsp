<%-- 
    Replace this page with your own page.
    Replace main.css and main.jsp as well.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html,width=device-width,height=device-height,initial-scale=2.0; charset=UTF-8"/>
        <title>JSP Page</title>
        <link rel="stylesheet" href="styles/home.css" type="text/css"/>
    </head>


    <c:if test="${sessionScope.user==null}">
        <c:redirect url="/login.jsp"></c:redirect>
    </c:if>  

    <c:set var="currUser" value="${sessionScope.user}" />

    <body>

        <!--Header-->
        <div class="header">
            <%@ include file="header.jsp" %>
        </div>

        <h2>User Last Login: &nbsp; ${sessionScope.user.last_login_time}</h2>


        <b>List of new tweets</b><br>
        <p>All the tweets in which this user is mentioned, since her last visit (login).</p>
        <c:forEach items="${lastMentions}" var="twit">
            <dl>
                <dt>
                    <img src="${pageContext.request.contextPath}/lib/${twit.ownerPicture}" class="avatar" align="left" />
                    <p><b>${twit.ownerFullname}</b> &nbsp; <a class="mention">@${twit.ownerUsername}</a>&nbsp;
                        <a>${twit.twitDate}</a>

                        <c:if test="${sessionScope.user.username==twit.ownerUsername}">
                        </p>
                    </c:if>

                    <p>${twit.twitMessage}</p><br>
                </dt>
            </dl>
        </c:forEach>


        <b>List of new followers</b>
        <p>All users who started following this user, since her last visit (login).</p>
        <c:forEach items="${sessionScope.followers}" var="follower">

            <div>
                <!--Display new followers since last login time of the current user--> 
                <c:if test="${follower.last_login_time > currUser.last_login_time}">
                    <img src="${pageContext.request.contextPath}/lib/${follower.profilePicture}" class="smallAvatar" align="left" />
                    <p id="small">
                        ${follower.fullName} &nbsp;
                        @<a class="mention">${follower.username}</a>
                    <p>Follow Date: &nbsp; ${follower.last_login_time}</p>
                    </p>

                </c:if>


            </div><br>

        </c:forEach>


        <!--Footer-->
        <div class="header">
            <%@ include file="footer.jsp" %>
        </div>

    </body>
</html>


