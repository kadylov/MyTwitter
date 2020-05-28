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
        <title>Sign Up Form</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
        <script src="includes/main.js"></script>
    </head>

    <body>

        <!--Header-->
        <div class="header">
            <%@ include file="header.jsp" %>
        </div>


        <c:set var="update" scope="session" value="submit"></c:set>
        <c:set var="readOnly" scope="session" value=""></c:set>
        <c:set var="actionType" scope="session" value="signup"></c:set>


            <!--check whether user wants to update his profile by checking session-->
        <c:if test="${sessionScope.user!=null}">
            <c:set var="readOnly" scope="session" value="readonly"></c:set>
            <c:set var="update" scope="session" value="update"></c:set>
            <c:set var="actionType" scope="session" value="update"></c:set>
        </c:if>


        <div class="formContainer">
            <h1>Registration</h1>
            <form action="membership" autocomplete="off" method="post" enctype='multipart/form-data' onsubmit="return validateForm();">

                <c:if test="${message!=null}">
                    <div id="errorMsg" name="errorMsg" class="errorContainer">${message}</div>
                </c:if>
                <div id="errorMsg" name="errorMsg" class="notVisible errorContainer"></div>


                <input type="hidden" name="action" value="<c:out value="${actionType}"></c:out>">


                    <!--Fullname-->
                    <label class="pad_top">Fullname:</label>
                    <input type="text" id='fullname' name='fullname' value="${attr0}" required>
                <span id="fullName_error" class="notVisible">*</span>
                <br>

                <!--Username-->
                <label class="pad_top">Username:</label>

                <input type="text" id='username' name='username' <c:out value="${readOnly}"></c:out>   value="${attr1}"   required>
                    <span id='username_error' class="notVisible">*</span>
                    <br>

                    <!--Email-->
                    <label class="pad_top">Email:</label>
                    <input type='email' id='email' name='email' value="${attr2}" <c:out value="${readOnly}"></c:out> required>
                    <span id='email_error' class="notVisible">*</span>
                    <br>

                    <!--Password-->
                    <label class='pad_top'>Password:</label>

                    <input type='password' id='password' name='password' value="${attr3}" required >
                <span id='password_error' class="notVisible">*</span>
                <br>

                <!--Confirm Password-->
                <label class="pad_top">Confirm Password:</label>
                <input type="password" id='confirmpassword' name='confirmpassword' value="${attr4}" required>
                <span id="confirmpassword_error" class="notVisible">*</span>
                <br>

                <!--Date of Birth-->
                <label class="pad_top">Date-of-birth:</label>
                <input type='date' id='dateofbirth'  name='dateofbirth' value="${attr5}" required>
                <span id="dateofbirth_error" class="notVisible">*</span>
                <br>


                <!--Security Question-->
                <label class="pad_top">Security question: </label>
                <%--<c:if var="${attr6!=null}"></c:if>--%>
                <select  id='securityquestion' name='securityquestion' class="select" onchange="listenComboBox(this)" required>
                    <option value="" id="">None</option>
                    <option value="0" > Your first pet</option>
                    <option value="1" >Your first car</option>
                    <option value="2" >Your first school</option>

                    <c:choose>
                        <c:when test="${attr6=='0'}">
                            <option value="0" selected> Your first pet</option>
                        </c:when>

                        <c:when test="${attr6=='1'}">
                            <option value="1" selected>Your first car</option>
                        </c:when>

                        <c:when test="${attr6=='2'}">
                            <option value="2" selected>Your first school</option>

                        </c:when>

                    </c:choose>




                </select><br><br>

                <!--Answer to the security question-->
                <div id='answerDiv' class="notVisible">
                    <label class="invAnswer pad_top">Answer:</label>
                    <input type="text" id ='answer' name='answer' value="${attr7}"><br>

                </div>
                <br>
                <label>&nbsp;</label>
                <input type="submit" name="submit" value="${update}" class="margin_left" >
                <input type="file" name="imgFile" value="upload" class="margin_left" >

            </form>
        </div> 

        <!--Footer-->
        <div class="header">
            <%@ include file="footer.jsp" %>
        </div>

    </body>
</html>


