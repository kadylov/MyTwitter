<%-- 
    Replace this page with your own page.
    Replace main.css and main.jsp as well.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Forgot Password</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
        <script src="includes/main.js"></script>
    </head>

    <body>

        <!--Header-->
        <div class="header">
            <%@ include file="header.jsp" %>
        </div>


        <c:choose>
            <c:when test="${message=='found'}">

            <center><h2>email sent!</h2></center><br>
            
             </c:when>   
            <c:otherwise>
                <div class="formContainer">
                    <h3>Reset Password</h3><br>
                    <form action="membership" autocomplete="off" method="post">

                        <c:if test="${message!=null}">
                            <div id="errorMsg" name="errorMsg" class="errorContainer">${message}</div>
                        </c:if>
                        <div id="errorMsg" name="errorMsg" class="notVisible errorContainer"></div>


                        <input type="hidden" name="action" value="forgotPassword">



                        <!--Email-->
                        <label class="pad_top">Email:</label>
                        <input type='email' id='email' name='email' value="farsh03@gmail.com">
                        <span id='email_error' class="notVisible">*</span>
                        <br>

                        <!--Security Question-->
                        <label class="pad_top">Security question: </label>
                        <select  id='securityquestion' name='securityquestion' class="select" onchange="listenComboBox(this)" >
                            <option value="" id="">None</option>
                            <option value="0" > Your first pet</option>
                            <option value="1" >Your first car</option>
                            <option value="2" >Your first school</option>

                        </select><br><br>
                        <!--Answer to the security question-->
                        <div id='answerDiv' class="notVisible">
                            <label class="invAnswer pad_top">Answer:</label>
                            <input type="text" id ='answer' name='answer' value="${attr7}"><br>

                        </div>
                        <br>
                        <label>&nbsp;</label>
                        <input type="submit" name="submit" value="Submit" class="margin_left" >
                    </form>
                </div> 

            </c:otherwise>
        </c:choose>

        <!--Footer-->
        <div class="header">
            <%@ include file="footer.jsp" %>
        </div>

    </body>
</html>


