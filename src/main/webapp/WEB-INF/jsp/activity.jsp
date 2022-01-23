<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <script src="/resources/activity.js"></script>
    <title>MTAPO | Activity</title>
</head>
<body>
<div class="ui stackable grid">
    <%--Menu--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui fitted hidden divider"></div>
            <jsp:include page="navbar.jsp"/>
        </div>
        <div class="two wide column"></div>
    </div>
    <%--Page title--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <h1 class="ui blue header">
                <i class="tasks icon"></i>
                <div class="content">
                    ${currentUser.firstName}'s Activity
                    <div class="sub header">Here you can review your activity within the application</div>
                </div>
            </h1>
            <div class="ui clearing divider"></div>
        </div>
        <div class="two wide column"></div>
    </div>
    <%--Actual content--%>
    <%--Topic assignment messages--%>
    <c:if test="${currentUser.category.name != null and approvedStudentTopic != null}">
        <div class="doubling row">
            <div class="two wide column"></div>
            <div class="twelve wide column">
                <div class="ui green raised message">
                    <div class="header">
                        <i class="check circle icon"></i>
                            ${currentUser eq sessionScope.sessionUser ? 'You are' : 'User is'} currently assigned to a topic
                    </div>
                    <table class="ui very basic table">
                        <tbody>
                        <tr>
                            <td><b>Topic</b></td>
                            <td class="positive left marked blue">
                                <a href="/topics/${approvedStudentTopic.topic.id}"><i class="file alternate icon"></i>
                                    <b>${approvedStudentTopic.topic.title}</b>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td><b>Mentor</b></td>
                            <td class="warning left marked orange">
                                <i class="user tie icon"></i>
                                <b>${approvedStudentTopic.topic.user.firstName} ${approvedStudentTopic.topic.user.lastName}</b>
                                <div class="ui basic circular label">
                                    <i class="envelope icon"></i>${approvedStudentTopic.topic.user.email}
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="two wide column"></div>
        </div>
    </c:if>
    <c:if test="${currentUser.category.name != null and approvedStudentTopic == null}">
        <div class="doubling row">
            <div class="two wide column"></div>
            <div class="twelve wide column">
                <div class="ui orange raised message">
                    <div class="header">
                        <i class="exclamation triangle icon"></i>
                            ${currentUser eq sessionScope.sessionUser ? 'You are' : 'User is'} currently not assigned to a topic
                    </div>
                    <p>
                        <i class="external alternate icon"></i>
                        Visit the <b><a href="/topics">topics</a></b> page if you want apply
                    </p>
                </div>
            </div>
            <div class="two wide column"></div>
        </div>
    </c:if>
    <%--Activity--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui raised blue segment">
                <h3 class="header">
                    <i class="chart area blue icon"></i>
                    <span class="ui blue text">Activity</span>
                </h3>
                <div id="activity-chart" data-value="${sessionScope.sessionUser.id}"></div>
            </div>
        </div>
        <div class="two wide column"></div>
    </div>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui two column equal width stackable grid">
                <div class="doubling stretched row">
                    <%--[Student only] Topic applications--%>
                    <c:if test="${currentUser.category.name != null and currentUser eq sessionScope.sessionUser}">
                        <div class="column">
                            <div class="ui raised blue segment">
                                <h3 class="header">
                                    <i class="file alternate outline blue icon"></i>
                                    <span class="ui blue text">Topic applications</span>
                                </h3>
                                <c:if test="${not empty studentTopics}">
                                    <div class="ui selection divided list">
                                        <c:forEach items="${studentTopics}" var="studentTopic">
                                            <div class="item">
                                                <div class="right floated content">
                                                    <div class="ui circular label ${studentTopic.status == 'approved' ? 'green' : studentTopic.status == 'waiting' ? 'orange' : 'red'}">
                                                            ${studentTopic.status}
                                                    </div>
                                                </div>
                                                <div class="content">
                                                    <a href="/topics/${studentTopic.topic.id}" class="header">
                                                        <i class="linkify disabled blue icon"></i>
                                                            ${studentTopic.topic.title}
                                                    </a>
                                                    <div class="description">Applied on <b><fmt:formatDate value="${studentTopic.date}" pattern="MMM d, yyyy"/></b></div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:if>
                                <c:if test="${empty studentTopics}">
                                    <h2 class="ui center aligned icon header">
                                        <div class="content">
                                            No topic applications
                                            <div class="sub header">
                                                You didn't apply to a topic yet
                                            </div>
                                        </div>
                                    </h2>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                    <div class="column">
                        <div class="ui raised yellow segment">
                            <h3 class="header">
                                <i class="comments blue icon"></i>
                                <span class="ui blue text">Posted comments</span>
                            </h3>
                            <c:if test="${not empty comments}">
                                <div class="ui selection divided list">
                                    <c:forEach items="${comments}" var="comment">
                                        <div class="item">
                                            <div class="content">
                                                <a href="/topics/${comment.topic.id}" class="header">
                                                    <i class="linkify disabled blue icon"></i>
                                                        ${comment.topic.title}
                                                </a>
                                                <div class="description">Applied on <b><fmt:formatDate value="${comment.date}" pattern="MMM d, yyyy"/></b></div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>
                            <c:if test="${empty comments}">
                                <h2 class="ui center aligned icon header">
                                    <div class="content">
                                        No comments
                                        <div class="sub header">
                                            You didn't post any comments yet
                                        </div>
                                    </div>
                                </h2>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="two wide column"></div>
    </div>
</div>
</body>
</html>