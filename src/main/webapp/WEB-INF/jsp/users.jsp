<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="url" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/init-accordion.js"></script>
    <script src="/resources/init-dropdown.js"></script>
    <script src="/resources/users.js"></script>
    <title>MTAPO | Users</title>
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
                <i class="icons">
                    <i class="users cog icon"></i>
                </i>
                <div class="content">
                    Users
                    <div class="sub header">Here you can see all the users and also you can assign roles to them</div>
                </div>
            </h1>
            <div class="ui clearing divider"></div>
        </div>
        <div class="two wide column"></div>
    </div>
    <c:if test="${formResponse != null}">
        <div class="doubling row">
            <div class="two wide column"></div>
            <div class="twelve wide column">
                <div class="doubling row">
                    <div class="two wide column"></div>
                    <div class="twelve wide column">
                        <div class="ui ${formResponse.status} floating message">
                            <div class="header">${formResponse.message}</div>
                            <p>${formResponse.description}</p>
                        </div>
                    </div>
                    <div class="two wide column"></div>
                </div>
            </div>
            <div class="two wide column"></div>
        </div>
    </c:if>
    <%--Actual content--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui styled fluid accordion">
                <div class="title">
                    <i class="dropdown icon"></i>
                    <i class="chart bar icon"></i>
                    Statistics
                </div>
                <div class="content">
                    <div class="ui stackable grid">
                        <div class="doubling row">
                            <div class="sixteen wide column">
                                <div class="ui three statistics">
                                    <div class="yellow statistic">
                                        <div class="value">${userRoleStatistic.students}</div>
                                        <div class="label">Students</div>
                                    </div>
                                    <div class="orange statistic">
                                        <div class="value">${userRoleStatistic.teachers}</div>
                                        <div class="label">Teachers</div>
                                    </div>
                                    <div class="red statistic">
                                        <div class="value">${userRoleStatistic.admins}</div>
                                        <div class="label">Admins</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="two wide column"></div>
    </div>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <form action="/users" method="get" id="filter-form">
                <div class="ui stackable grid">
                    <div class="doubling row">
                        <div class="twelve wide column">
                            <div class="ui icon fluid input">
                                <i class="search icon"></i>
                                <input type="text" placeholder="Search by name or email..." name="keywords"
                                       id="search-input" value="${param['keywords']}">
                            </div>
                        </div>
                        <div class="four wide column">
                            <div class="ui clearable selection fluid dropdown">
                                <input type="hidden" name="sort" id="sort-input" value="${param['sort']}">
                                <i class="dropdown icon"></i>
                                <div class="default text">
                                    <i class="sort icon"></i>
                                    Sort by
                                </div>
                                <div class="menu">
                                    <div class="item" data-value="first-name-asc">
                                        <i class="sort alphabet up alternate icon"></i>
                                        Sort by <span class="ui blue text"><b>first name</b></span>
                                    </div>
                                    <div class="item" data-value="first-name-desc">
                                        <i class="sort alphabet down alternate icon"></i>
                                        Sort by <span class="ui blue text"><b>first name</b></span>
                                    </div>
                                    <div class="item" data-value="last-name-asc">
                                        <i class="sort alphabet up alternate icon"></i>
                                        Sort by <span class="ui blue text"><b>last name</b></span>
                                    </div>
                                    <div class="item" data-value="last-name-desc">
                                        <i class="sort alphabet down alternate icon"></i>
                                        Sort by <span class="ui blue text"><b>last name</b></span>
                                    </div>
                                    <div class="item" data-value="email-asc">
                                        <i class="sort alphabet up alternate icon"></i>
                                        Sort by <span class="ui blue text"><b>email</b></span>
                                    </div>
                                    <div class="item" data-value="email-desc">
                                        <i class="sort alphabet down alternate icon"></i>
                                        Sort by <span class="ui blue text"><b>email</b></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="two wide column"></div>
    </div>
    <c:if test="${userPagination.totalElements <= 0}">
        <div class="doubling row">
            <div class="three wide column"></div>
            <div class="ten wide column">
                <div class="ui section hidden divider"></div>
                <h2 class="ui center aligned icon header">
                    <i class="frown outline icon"></i>
                    <div class="content">
                        Oops!
                        <div class="sub header">
                            Seems like we couldn't find any results
                        </div>
                    </div>
                </h2>
            </div>
            <div class="three wide column"></div>
        </div>
    </c:if>
    <c:if test="${userPagination.totalElements > 0}">
        <div class="doubling row">
            <div class="two wide column"></div>
            <div class="twelve wide column">
                <div class="ui three doubling raised blue cards" id="user-cards">
                    <c:forEach items="${userPagination.elements}" var="user">
                        <div class="ui card">
                            <div class="content">
                                <div class="ui right floated icon blue inverted button change-roles-button change-roles-button"
                                     data-value="${user.id}">
                                    <i class="edit icon"></i>
                                </div>
                                <div class="header">
                                    <i class="linkify blue disabled icon"></i>
                                    <a class="header" href="/activity/${user.id}">${user.firstName} ${user.lastName}</a>
                                </div>
                                <div class="meta"><i class="envelope icon"></i> ${user.email}</div>
                                <div class="description">
                                    <table class="ui very compact very basic table">
                                        <tbody>
                                        <tr>
                                            <td><b><span class="ui blue text">Roles</span></b></td>
                                            <td>
                                                <c:forEach items="${user.roles}" var="role">
                                                    <c:if test="${role.name.toLowerCase().equals('student')}">
                                                        <div class="ui circular yellow label">
                                                            student
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${role.name.toLowerCase().equals('teacher')}">
                                                        <div class="ui circular orange label">
                                                            teacher
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${role.name.toLowerCase().equals('admin')}">
                                                        <div class="ui circular red label">
                                                            admin
                                                        </div>
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                        <c:if test="${user.category != null}">
                                            <tr>
                                                <td><b><span class="ui blue text">Studies</span></b></td>
                                                <td>
                                                    <c:if test="${user.category.name.toLowerCase().equals('license')}">
                                                        <div class="ui circular olive label">
                                                            license
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${user.category.name.toLowerCase().equals('master')}">
                                                        <div class="ui circular green label">
                                                            master
                                                        </div>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <c:if test="${userIdToTopicMap[user.id] != null}">
                                            <tr>
                                                <td><b><span class="ui blue text">Assigned topic</span></b></td>
                                                <td>
                                                    <i class="linkify blue disabled icon"></i>
                                                    <a href="/topics/${userIdToTopicMap[user.id].id}">
                                                        <b>${userIdToTopicMap[user.id].title}</b>
                                                    </a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><b><span class="ui blue text">Mentor</span></b></td>
                                                <td>
                                                    <i class="linkify blue disabled icon"></i>
                                                    <a href="/activity/${userIdToTopicMap[user.id].user.id}">
                                                        <b>${userIdToTopicMap[user.id].user.firstName} ${userIdToTopicMap[user.id].user.lastName}</b>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="two wide column"></div>
        </div>
        <div class="doubling row">
            <div class="five wide column"></div>
            <div class="six wide column">
                <div class="ui five item pagination borderless inverted blue menu">
                    <a class="item ${param['page'] == 0 || param['page'] == null ? 'disabled' : ''}"
                       href="<url:replaceParam name='page' value='0'/>">
                        <i class="angle double left icon"></i>
                    </a>
                    <a class="item ${param['page'] == 0 || param['page'] == null? 'disabled' : ''}"
                       href="/users<url:replaceParam name='page' value='${userPagination.pageNumber - 1}'/>">
                        <i class="angle left icon"></i>
                    </a>
                    <div class="active item">
                        <b>Page ${userPagination.pageNumber + 1} of ${userPagination.totalPages}</b>
                    </div>
                    <a class="item ${userPagination.pageNumber + 1 == userPagination.totalPages ? 'disabled' : ''}"
                       href="<url:replaceParam name='page' value='${userPagination.pageNumber + 1}'/>">
                        <i class="angle right icon"></i>
                    </a>
                    <a class="item ${userPagination.pageNumber + 1 == userPagination.totalPages ? 'disabled' : ''}"
                       href="<url:replaceParam name='page' value='${userPagination.totalPages - 1}'/>">
                        <i class="angle double right icon"></i>
                    </a>
                </div>
            </div>
            <div class="five wide column"></div>
        </div>
        <%--Modal--%>
        <div class="ui small modal" id="change-roles-modal">
            <div class="header">
                <span class="ui blue text">
                    <i class="edit blue icon"></i>
                    Change roles
                </span>
            </div>
            <div class="content">
                <h3 class="ui header">
                    <span id="user-name"></span>
                    <div class="sub header"><i class="envelope icon"></i><span id="user-email"></span></div>
                </h3>
                <div class="ui clearing hidden divider"></div>
                <form action="" class="ui form" method="post" id="change-roles-form">
                    <div class="required field">
                        <label>Roles</label>
                        <div class="ui fluid multiple search selection dropdown" id="roles-input">
                            <input type="hidden" name="roles">
                            <i class="dropdown icon"></i>
                            <div class="default text">
                                <i class="sort icon"></i>
                                Roles
                            </div>
                            <div class="menu">
                                <div class="item">Student</div>
                                <div class="item">Teacher</div>
                                <div class="item">Admin</div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="actions">
                <div class="ui red cancel inverted button button">
                    <i class="remove icon"></i>
                    Cancel
                </div>
                <button type="submit" class="ui green ok inverted button" form="change-roles-form"
                        id="change-roles-submit-button">
                    <i class="checkmark icon"></i>
                    Save changes
                </button>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>