<%@include file="include.jsp"%>

<html>
<head>
    <title><spring:message code="label.title.delete.user"/></title>
</head>
<body>
<form action="${pageContext.request.contextPath}/user-menu/delete" method="post">
    <spring:message code="label.user.menu.delete.email"/>:<p><input name="userEmail"></p>
    <p><input type="submit" value="<spring:message code="label.delete"/>"></p>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<c:set var="msg" value="${message}"/>
<c:if test="${msg != null}">
<p style="color:red"><c:out value="${msg}"/><p>
    </c:if>

</body>
</html>
