<%-- 
    Document   : home.jsp
    Created on : Sep 24, 2015, 6:47:02 PM
    Author     : xl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html,width=device-width,height=device-height,initial-scale=2.0; charset=UTF-8"/>
        <!--<meta name="viewport" content="width=device-width,height=device-height,initial-scale=1.0"/>-->
        <title>Home Page</title>
        <link rel="stylesheet" href="styles/home.css" type="text/css"/>
        <script src="includes/main.js"></script>
    </head>


    <c:if test="${sessionScope.user==null}">
        <c:redirect url="/login.jsp"></c:redirect>
    </c:if>     




    <c:set var = "userImg" scope = "session" value = "${sessionScope.user.profilePicture}"/>

    <body>
        <div>
            <div class="header"><%@ include file="header.jsp" %>
            </div>

            <div class="row">
                <div class="column side">
                    <!--     First Column -->
                    <div class="leftDiv">
                        <img src="${pageContext.request.contextPath}/lib/${userImg}" class="cardImg"><br>


                        <p>${sessionScope.user.fullName} &nbsp; <a>@${sessionScope.user.username}</a></p>
                        <p></p>
                        <p>Tweets: &nbsp;&nbsp;&nbsp;&nbsp; ${sessionScope.numbTwits}</p> 
                        <p>Following:&nbsp; ${sessionScope.numbFollowing}</p>
                        <p>Followers:&nbsp; ${sessionScope.numbFollowers}</p>
                        <br>


                        <p>Trends</p>
                    </div>
                </div>


                <div class="column middle">
                    <div class="leftDiv">
                        <form action="membership" autocomplete="off" method="get">
                            <input type="hidden" name="action" value="tweet">
                            <img src="${pageContext.request.contextPath}/lib/${userImg}" class="avatar">
                            <textarea class="textArea" name="textArea"></textarea><br><br>
                            <label>&nbsp;</label>
                            <div class="twitBtn">
                                <span >
                                    <input type="submit" name="tweet" value="Tweet">

                                </span>
                            </div>
                        </form><br><br>


                        <div>

                            <c:forEach items="${twits}" var="twit">
                                <dl>
                                    <dt>
                                        <img src="${pageContext.request.contextPath}/lib/${twit.ownerPicture}" class="avatar" align="left" />
                                        <p><b>${twit.ownerFullname}</b> &nbsp; <a class="mention">@${twit.ownerUsername}</a>&nbsp;
                                            <a>${twit.twitDate}</a>

                                            <c:if test="${sessionScope.user.username==twit.ownerUsername}">
                                                <span><a href="membership?action=deleteTwit&amp;btn=${twit.twitId}">
                                                        <input type="submit" value="&times;" name="${twit.twitId}">
                                                    </a>
                                                </span>
                                            </p>
                                        </c:if>

                                        <p>${twit.twitMessage}</p><br>
                                    </dt>
                                </dl>
                            </c:forEach>


                        </div>

                    </div>
                </div>



                <div class="column side">
                    <div class="rightDiv">
                        <h2>List Of Users</h2>

                        <c:forEach items="${sessionScope.users}" var="user">

                            <c:if test="${sessionScope.user.username!=user.username}">

                                <!--Get avatar of the user-->
                                <div><img src="${pageContext.request.contextPath}/lib/${user.profilePicture}" class="smallAvatar" align="left" />
                                    <label id="small">
                                        ${user.fullName} &nbsp;
                                        @<a class="mention">${user.username}</a>
                                    </label>
                                    <c:set var="username">${user.username}</c:set>
                                    <c:set var="btnType" value="follow" />
                                        <!--<br><h3>${followedUsers.containsKey(username)} &nbsp; ${username}</h3>-->
                                    <span>
                                        <!--<a href="membership?action=follow&amp;username=${user.username}">-->
                                        <form action="membership" method="get">
                                            <input type="hidden" name="action" value="follow">
                                            <input type="hidden" name="username" value="${user.username}">
                                            <c:choose>
                                                <c:when test="${followedUsers.containsKey(username)}">
                                                    <input type="submit" name="unfollow" value="unfollow"> 
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="submit" name="follow" value="follow"> <br>

                                                </c:otherwise>

                                            </c:choose>
                                        </form>
                                            

                                        </a>
                                    </span>
                                </div><br>

                            </c:if>
                        </c:forEach>

                    </div>
                </div>
            </div>
        </div>
    </body>



</html>