<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="url" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/init-accordion.js"></script>
    <script src="/resources/init-dropdown.js"></script>
    <script src="/resources/init-table.js"></script>
    <script src="/resources/table-sort.js"></script>
    <script src="/resources/topics.js"></script>
    <title>MTAPO | Topics</title>
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
    <%--Page title--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <h1 class="ui blue header">
                <i class="icons">
                    <i class="file alternate outline icon"></i>
                </i>
                <div class="content">
                    Topics
                    <div class="sub header">Here you can see all the topics</div>
                </div>
            </h1>
            <div class="ui clearing divider"></div>
        </div>
        <div class="two wide column"></div>
    </div>
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
                                <div class="ui two statistics">
                                    <div class="olive statistic">
                                        <div class="value">${topicStatistic.licenseTopics}</div>
                                        <div class="label">License topics</div>
                                    </div>
                                    <div class="green statistic">
                                        <div class="value">${topicStatistic.masterTopics}</div>
                                        <div class="label">Master topics</div>
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
    <%--Filter--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="four wide column">
            <form action="/topics" method="get" id="filter-form"></form>
            <%--Tags--%>
            <div class="ui icon fluid input">
                <div class="ui fluid multiple search selection two column dropdown" id="tag-input">
                    <input type="hidden" name="tags" form="filter-form" value="${param['tags']}">
                    <i class="dropdown icon"></i>
                    <div class="default text">Tags</div>
                    <div class="menu"></div>
                </div>
            </div>
        </div>
        <%--Teacher--%>
        <div class="four wide column">
            <div class="ui selection clearable fluid dropdown">
                <input type="hidden" name="teacher" form="filter-form" id="teacher-input" value="${param['teacher']}">
                <i class="dropdown icon"></i>
                <div class="default text">
                    <i class="user tie icon"></i>
                    Teacher
                </div>
                <div class="menu">
                    <c:forEach items="${teachers}" var="teacher">
                        <div class="item" data-value="${teacher.firstName} ${teacher.lastName}">
                                    <span class="text"> <i class="user tie icon"></i>
                                        ${teacher.firstName} ${teacher.lastName}
                                    </span>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <%--Category--%>
        <div class="two wide column">
            <div class="ui selection clearable fluid dropdown">
                <input type="hidden" name="category" form="filter-form" id="category-input"
                       value="${param['category']}">
                <i class="dropdown icon"></i>
                <div class="default text">Category</div>
                <div class="menu">
                    <div class="item" data-value="license">
                        <a class="ui olive empty circular label"></a>
                        License
                    </div>
                    <div class="item" data-value="master">
                        <a class="ui green empty circular label"></a>
                        Master
                    </div>
                </div>
            </div>
        </div>
        <%--Apply--%>
        <div class="two wide column">
            <button type="submit" form="filter-form" class="ui blue labeled icon circular fluid button">
                <i class="filter icon"></i>
                Apply
            </button>
        </div>
        <div class="two wide column"></div>
    </div>
    <c:choose>
    <c:when test="${topicPagination.totalElements <= 0}">
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
    </c:when>
    <c:when test="${topicPagination.totalElements > 0}">
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <table class="ui sortable blue striped padded table">
                <thead>
                <tr>
                    <th class="two wide">Category</th>
                    <th class="sorted ascending">Topic</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${topicPagination.elements}" var="topic">
                    <tr class="left marked ${topic.category.name == 'license' ? 'olive' : 'green'}">
                        <td class="${topic.category.name == 'license' ? 'olive' : 'green'}">
                            <div class="ui circular fluid label ${topic.category.name == 'license' ? 'olive' : 'green'}">
                                    ${topic.category.name}
                            </div>
                        </td>
                        <td>
                                <%--Title--%>
                            <div class="ui medium header">
                                <div class="ui doubling equal width grid">
                                    <div class="column">
                                        <a href="/topics/${topic.id}">
                                            <i class="linkify blue disabled icon"></i>
                                            <b>${topic.title}</b>
                                        </a>
                                    </div>
                                    <div class="three wide column">
                                        <div class="ui circular fluid label ${assignedUsers[topic.id] == 0 ? 'grey' : 'yellow'}">
                                            <c:if test="${assignedUsers[topic.id] == 1}">
                                                1 student
                                            </c:if>
                                            <c:if test="${assignedUsers[topic.id] > 1}">
                                                ${assignedUsers[topic.id]} students
                                            </c:if>
                                            <c:if test="${assignedUsers[topic.id] == 0}">
                                                No students
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                                <%--Teacher--%>
                            <div class="ui selection list">
                                <div class="item">
                                    <div class="ui blue horizontal circular label">
                                        <i class="user tie icon"></i>${topic.user.firstName} ${topic.user.lastName}
                                    </div>
                                    <span><i class="envelope icon"></i>${topic.user.email}</span>
                                </div>
                            </div>
                                <%--Tags--%>
                            <div class="ui circular basic gray labels">
                                <c:forEach items="${topic.tags}" var="tag">
                                    <div class="ui label">
                                        <span class="ui grey text">
                                            <i class="hashtag grey icon"></i>${tag.name}
                                        </span>
                                    </div>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot class="full-width">
                <tr>
                    <th colspan="5">
                        <div class="ui five item pagination borderless inverted blue menu">
                            <a class="item ${param['page'] == 0 || param['page'] == null ? 'disabled' : ''}"
                               href="<url:replaceParam name='page' value='0'/>">
                                <i class="angle double left icon"></i>
                            </a>
                            <a class="item ${param['page'] == 0 || param['page'] == null? 'disabled' : ''}"
                               href="/topics<url:replaceParam name='page' value='${topicPagination.pageNumber - 1}'/>">
                                <i class="angle left icon"></i>
                            </a>
                            <div class="active item">
                                <b>Page ${topicPagination.pageNumber + 1} of ${topicPagination.totalPages}</b>
                            </div>
                            <a class="item ${topicPagination.pageNumber + 1 == topicPagination.totalPages ? 'disabled' : ''}"
                               href="<url:replaceParam name='page' value='${topicPagination.pageNumber + 1}'/>">
                                <i class="angle right icon"></i>
                            </a>
                            <a class="item ${topicPagination.pageNumber + 1 == topicPagination.totalPages ? 'disabled' : ''}"
                               href="<url:replaceParam name='page' value='${topicPagination.totalPages - 1}'/>">
                                <i class="angle double right icon"></i>
                            </a>
                        </div>
                    </th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
</c:when>
</c:choose>
</div>
</body>
</html>