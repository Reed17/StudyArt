<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>

<body>
<div style="float:right;">
    <form action="login" method="post">
        id:<p><input name="id"></p>
        password:<p><input name="password"></p>
        <p><input type="submit" value="log in"></p>
    </form>
    <form method="get" action="/registration">
        <button type="submit">Registration</button>
    </form>
</div>
<%
    String login_msg = (String) request.getAttribute("error");
    if (login_msg != null) {
%>
<p style="color:red"><%=login_msg%>
</p>
<%
    }
%>
</body>
</html>