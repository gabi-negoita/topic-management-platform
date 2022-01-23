<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="navbar.jsp"/>
</head>

<body>
<h1>TEST</h1>
<button type="button" class="btn btn-primary">Primary</button>
<c:if test="1 -eq 2">
    <p>1 egal cu 2</p>
</c:if>

<c:if test="2 -eq 2">
    <p>2 este egal cu 2</p>
</c:if>


<c:forEach var="i" begin="1" end="5">
    <button><c:out value="${i}"/></button>
</c:forEach>

<c:forTokens items="Zara,nuha,roshy" delims="," var="name">
    <c:out value="${name}"/>
</c:forTokens>
<br>
<br>
<br>
<c:forEach items="${names}" var="name">
    <c:out value="${name}"/>
</c:forEach>

<br>
<br>
<br>
<c:forEach items="${users}" var="user">
    <c:out value="${user.email}"/>
</c:forEach>


</body>
</html>