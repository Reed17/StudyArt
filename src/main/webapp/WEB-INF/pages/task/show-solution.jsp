<%@include file="../main/include.jsp"%>

<html>
<head>
    <title><spring:message code="solution"/></title>
</head>
<body>

<c:set var="task" value="${task}"/>

<h1>${task.title}</h1>
<p>${task.groupName}</p>
<br>
<p>${task.description}</p>
<br>
<p>${task.examples}</p>
<br>

    <textarea readonly rows="24" cols="80" name="userCode">${template}</textarea>

</body>
</html>
