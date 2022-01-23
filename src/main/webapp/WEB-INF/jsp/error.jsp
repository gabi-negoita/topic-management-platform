<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <title>MTAPO | Error</title>
</head>
<body>
<div class="ui stackable internally grid">
    <%--Menu--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui fitted hidden divider"></div>
            <jsp:include page="navbar.jsp"/>
        </div>
        <div class="two wide column"></div>
    </div>
    <div class="doubling row">
        <div class="five wide column"></div>
        <div class="six wide column">
            <div class="ui center aligned floating negative message">
                <i class="icon massive ${formResponse.status == 404 ? 'tired outline ' : 'surprise outline'}"></i>
                <div class="content">
                    <div class="header">
                        <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#${formResponse.status}"
                           target="_blank" class="ui red statistic">
                            <div class="value">${formResponse.status}</div>
                            <div class="label">${formResponse.message}</div>
                        </a>
                    </div>
                    <p><b>${formResponse.description}</b></p>
                    <a class="ui buttons" href="/home">
                        <div class="ui active yellow icon button">
                            <i class="star loading icon"></i>
                        </div>
                        <div class="ui active yellow button">
                            Just get me home
                        </div>
                        <div class="ui active yellow icon button">
                            <i class="star loading icon"></i>
                        </div>
                    </a>
                </div>
            </div>
        </div>
        <div class="five wide column"></div>
    </div>
</div>
</body>
</html>
