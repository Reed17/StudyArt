<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>

<body>
<div style="float:right;">
    <form action="registration" method="post">
        user name:<p><input name="userName"></p>
        email:<p><input name="email"></p>
        password:<p><input type="password" name="password"></p>
        <p><input type="checkbox" name="role" value="teacher"> Teacher</p>
        <p><input type="submit" value="register"></p>
    </form>
</div>
<%
    String regist_msg =(String)request.getAttribute("error");
    if(regist_msg !=null){
%>
<p style="color:red"><%=regist_msg%></p>
<%
    }
%>
</body>
</html>