<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="textarea" uri="http://www.springframework.org/tags/form" %>
<%@include file="include.jsp" %>

<html>
<head>
    <title><spring:message code="label.title.add.lesson"/></title>
</head>
<body>

<h1><h1><spring:message code="label.title.create.new.lesson"/></h1></h1>

<form:form action="${pageContext.request.contextPath}/lesson-menu/create-lesson" modelAttribute="lesson"
           method="post">

    <spring:message code="label.create.course.lesson.title"/>:<br>
    <label>
        <form:input path="title" size="47"/>
            <%--<form:textarea path="title" rows="1" cols="50" />--%>
        <form:errors path="title"/>
    </label><br>

    <spring:message code="label.create.course.lesson.description"/>:<br>
    <label>
        <form:textarea path="description" rows="10" cols="50"/>
        <form:errors path="description"/>
    </label><br>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <p><input type="submit" value="<spring:message code="label.create.course.add.lesson"/>"></p>

</form:form>
</body>
</html>
