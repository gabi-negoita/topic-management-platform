<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
    $(document).ready(() => {
        sessionUserId = ${sessionScope.sessionUser.id};
        userRoles = [];
        <c:forEach items="${userRoles}" var="role">
        userRoles.push('${role}');
        </c:forEach>
        updateNotifs();
        setInterval(function () {
            updateNotifs();
        }, 10000);
    });
</script>
<div class="ui stackable inverted borderless blue menu">
    <div class="vertically fitted item">
        <i class="university large icon"></i>
    </div>
    <a class="item ${uri == 'home' ? 'active' : ''}" href="/home">Home</a>
    <c:if test="${fn:contains(userRoles, 'teacher')}">
    <div class="ui dropdown simple item">
        </c:if>
        <c:if test="${not (fn:contains(userRoles, 'teacher'))}">
        <a class="item ${uri == 'topics' ? 'active' : ''}" href="/topics">
            </c:if>
            Topics
            <c:if test="${fn:contains(userRoles, 'teacher')}">
                <i class="icon dropdown"></i>
                <div class="menu">
                    <a class="item ${uri == 'topics' ? 'active' : ''}" href="/topics">View topics</a>
                    <c:if test="${fn:contains(userRoles, 'teacher')}">
                        <a class="item ${uri.contains('uploaded-topics') ? 'active' : ''}" href="/uploaded-topics">Uploaded
                            topics</a>
                    </c:if>
                    <c:if test="${fn:contains(userRoles, 'teacher')}">
                        <a class="item ${uri.contains('topic-requests') ? 'active' : ''}" href="/topic-requests">Topic
                            requests</a>
                    </c:if>
                </div>
            </c:if>
            <c:if test="${not (fn:contains(userRoles, 'teacher'))}"></a></c:if>
        <c:if test="${fn:contains(userRoles, 'teacher')}"></div>
    </c:if>

    <%--    Admin stuff--%>
    <c:if test="${fn:contains(userRoles, 'admin')}">
        <div class="ui dropdown simple item">
            Admin<i class="icon dropdown"></i>
            <div class="menu">
                <a class="item ${uri == 'users' ? 'active' : ''}" href="/users">Users</a>
                <a class="item ${uri == 'users' ? 'logs' : ''}" href="/logs">System logs</a>
            </div>
        </div>
    </c:if>
    <div class="right stackable menu">
        <div class="ui category search item">
            <div class="ui inverted transparent icon input">
                <input class="prompt" type="text" placeholder="Search topics..." id="search-topics-input">
                <i class="search icon"></i>
            </div>
            <div class="results"></div>
        </div>
        <a class="ui icon item" id="notifs"><i class="bell icon"></i></a>
        <div class="ui wide popup transition hidden" id="notifOuterDiv">
            <div class="ui selection relaxed list" id="notifDiv"></div>
        </div>
        <div class="ui dropdown simple item">
            <i class="user alternate icon"></i>
            ${sessionScope.sessionUser.firstName}
            <i class="icon dropdown"></i>
            <div class="menu">
                <a class="item ${uri.equals('activity') ? 'active' : ''}" href="/activity">Your activity</a>
                <c:if test="${fn:contains(userRoles, 'student') and not empty navbarTopic}">
                    <a class="item ${uri.contains('topics/') and fn:split(uri, '/')[1] eq navbarTopic.id ? 'active' : ''}" href="/topics/${navbarTopic.id}">Your topic</a>
                </c:if>
                <a class="item" id="logout-button">Logout</a>
            </div>
        </div>
        <div class="results"></div>
    </div>
</div>
<div class="ui mini modal" id="logout-modal">
    <div class="ui icon left aligned header">
        <span class="ui blue text">
            <i class="sign out red icon"></i>
            Logout
        </span>
    </div>
    <div class="center aligned content">
        <h4>Are you sure you want to log out?</h4>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <a class="ui green ok inverted button" href="/logout">
            <i class="checkmark icon"></i>
            Yes
        </a>
    </div>
</div>
<div class="ui small modal" id="news-modal">
</div>