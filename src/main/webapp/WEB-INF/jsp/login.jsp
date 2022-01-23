<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="semantic-ui.jsp"/>
    <jsp:include page="meta.jsp"/>
    <title>MTAPO | Login</title>
    <link rel="icon" href="/resources/ugal-logo.png">
    <link href="/resources/login.css" rel="stylesheet">
</head>
<body>

<div class="background-color"></div>
<div class="background-image"></div>
<div class="page-content">
    <form action="/login" method="post" class="ui form">
        <div class="ui left aligned raised segment">
            <h2 class="ui blue header">
                <div class="content">
                    Log in into your account
                </div>
            </h2>
            <div class="ui clearing divider"></div>
            <c:if test="${param['error'] != null}">
                <div class="ui icon negative message">
                    <i class="inbox icon"></i>
                    <div class="content">
                        <div class="header">
                            Invalid credentials
                        </div>
                        <p>Username or password is incorrect</p>
                    </div>
                </div>
            </c:if>
            <div class="field">
                <div class="ui left icon input">
                    <i class="user icon"></i>
                    <input type="text" name="email" value="${sessionScope.email != null ? sessionScope.email : ''}"
                           placeholder="Email" required autofocus>
                </div>
            </div>
            <div class="field">
                <div class="ui left icon input">
                    <i class="lock icon"></i>
                    <input type="password" name="password" placeholder="Password" required>
                </div>
            </div>
            <button type="submit" class="ui fluid large blue submit button">Login</button>
        </div>
    </form>
</div>
</body>
</html>