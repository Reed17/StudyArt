<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><spring:message code="label.title.group.list"/></title>
</head>
<body>
<c:forEach var="group" items="${groupList}">
    <li><a href="${pageContext.request.contextPath}/tasks-menu/show-group/${group}"> <p>${group}</p> </a></li>
</c:forEach>
</body>
</html>
