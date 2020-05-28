<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html,width=device-width,height=device-height,initial-scale=2.0; charset=UTF-8"/>
        <title>Log in</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>

    </head>
    <body>
        <!--Header-->
        <div class="header">
            <%@ include file="header.jsp" %>

        </div>

        <div class="formContainer">
            <form action="membership" method="post" autocomplete="on" onsubmit="return validateForm();">

                <c:if test="${message!=null}">
                    <div id="errorMsg" name="errorMsg" class="errorContainer">${message}</div>
                </c:if>
                <div id="errorMsg" name="errorMsg" class="notVisible errorContainer"></div>      

                <input type="hidden" name="action" value="login">

                <label>Log In</label><br>

                <!--Username and password textfield-->
                <input type="text" id="username_email" name="username_email" value="${cookie.email.value}" placeholder="username or email" required><br>
                <input type="text" id="password" name="password" placeholder="Password" value="wW2" required><br><br>

                <!--Log in button-->
                <input type="submit" value="Log in" class="margin_left"> 

                <!--Remember me checkbox-->
                <span class="rememb_me">
                    <input type="checkbox" class="center" checked name="rememberMe"></span>Remember me


                <!--Link for forgot password-->
                <a href="forgotpassword.jsp"><span class="forgot">Forgot Password</span></a><br>


                <!--Link for sign up-->
                <p class="pad_top margin_left">New? <a class="pad_top" href="signup.jsp">Sign up now</a></p>

            </form>
        </div>
        <div>

        </div>

        <!--Footer-->
        <div class="header">
            <%@ include file="footer.jsp" %>
        </div>
    </body>
</html>
