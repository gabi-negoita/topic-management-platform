<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="url" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/init-accordion.js"></script>
    <script src="/resources/init-dropdown.js"></script>
    <script src="/resources/init-table.js"></script>
    <script src="/resources/topic-requests.js"></script>
    <script src="/resources/table-sort.js"></script>
    <script src="/resources/logs.js"></script>
    <title>MTAPO | Logs</title>
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
                    <i class="file icon"></i>
                    <i class="bottom right corner shield alternate icon"></i>
                </i>
                <div class="content">
                    Logs
                    <div class="sub header">Here you can visualize all system logs</div>
                </div>
            </h1>
            <div class="ui clearing divider"></div>
        </div>
        <div class="two wide column"></div>
    </div>
    <%--Actual content--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="three wide column">
            <form action="/logs" method="get" id="filter-form"></form>
            <%--Operation--%>
            <div class="ui icon fluid input">
                <div class="ui selection clearable fluid dropdown" id="operation-input">
                    <input type="hidden" name="operation" form="filter-form" value="${param['operation']}">
                    <i class="dropdown icon"></i>
                    <div class="default text">Operation</div>
                    <div class="menu">
                        <div class="item" data-value="insert">
                            <i class="plus circle green icon"></i>
                            Insert
                        </div>
                        <div class="item" data-value="update">
                            <i class="edit blue icon"></i>
                            Update
                        </div>
                        <div class="item" data-value="delete">
                            <i class="trash alternate red icon"></i>
                            Delete
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--Status--%>
        <div class="three wide column">
            <div class="ui icon fluid input">
                <div class="ui selection clearable fluid dropdown" id="status-input">
                    <input type="hidden" name="status" form="filter-form" value="${param['status']}">
                    <i class="dropdown icon"></i>
                    <div class="default text">Status</div>
                    <div class="menu">
                        <div class="item" data-value="success">
                            <i class="check circle green icon"></i>
                            Success
                        </div>
                        <div class="item" data-value="failed">
                            <i class="times circle red icon"></i>
                            Failed
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--Sort--%>
        <div class="four wide column">
            <div class="ui clearable selection fluid dropdown">
                <input type="hidden" name="order" id="sort-input" form="filter-form" value="${param['order']}">
                <i class="dropdown icon"></i>
                <div class="default text">
                    <i class="sort icon"></i>
                    Sort by
                </div>
                <div class="menu">
                    <div class="item" data-value="asc">
                        <i class="sort amount down alternate icon"></i>
                        Sort by <span class="ui blue text"><b>date</b></span>
                    </div>
                    <div class="item" data-value="desc">
                        <i class="sort amount down icon"></i>
                        Sort by <span class="ui blue text"><b>date</b></span>
                    </div>
                </div>
            </div>
        </div>
        <%--Apply--%>
        <div class="two wide column">
            <button type="submit" form="filter-form" class="ui blue right labeled icon circular fluid button">
                <i class="filter icon"></i>
                Apply
            </button>
        </div>
        <div class="two wide column"></div>
    </div>
    <c:choose>
    <c:when test="${logsPagination.totalElements <= 0}">
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
            <div class="two wide column"></div>
        </div>
    </c:when>
    <c:when test="${logsPagination.totalElements > 0}">
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <table class="ui sortable blue padded table">
                <thead>
                <tr>
                    <th class="two wide">Operation</th>
                    <th>Details</th>
                    <th class="three wide">User</th>
                    <th class="two wide">Date</th>
                    <th class="two wide ">Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${logsPagination.elements}" var="log">
                    <tr class="${log.status == 'success' ? 'positive' : 'negative'}">
                        <c:choose>
                            <c:when test="${log.operation == 'insert'}">
                                <td class="left marked green">
                                    <div class="ui circular fluid green label">
                                        <i class="plus circle icon"></i>${log.operation}
                                    </div>
                                </td>
                            </c:when>
                            <c:when test="${log.operation == 'update'}">
                                <td class="left marked blue">
                                    <div class="ui circular fluid blue label">
                                        <i class="edit icon"></i>${log.operation}
                                    </div>
                                </td>
                            </c:when>
                            <c:when test="${log.operation == 'delete'}">
                                <td class="left marked red">
                                    <div class="ui circular fluid red label">
                                        <i class="trash alternate icon"></i>${log.operation}
                                    </div>
                                </td>
                            </c:when>
                        </c:choose>
                        <td><b>${log.details}</b></td>
                        <td><a href="/activity/${log.user.id}"><b>${log.user.firstName} ${log.user.lastName}</b></a></td>
                        <td>
                            <b><fmt:formatDate value="${log.getDate()}" pattern="MMM d, yyyy"/></b>
                        </td>
                        <td>
                            <div class="ui circular fluid label ${log.status == 'success' ? 'green' : 'red'}">
                                <i class="${log.status == 'success' ? 'check' : 'times'} circle icon"></i>${log.status}
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
                               href="/logs<url:replaceParam name='page' value='${logsPagination.pageNumber - 1}'/>">
                                <i class="angle left icon"></i>
                            </a>
                            <div class="active item">
                                <b>Page ${logsPagination.pageNumber + 1} of ${logsPagination.totalPages}</b>
                            </div>
                            <a class="item ${logsPagination.pageNumber + 1 == logsPagination.totalPages ? 'disabled' : ''}"
                               href="<url:replaceParam name='page' value='${logsPagination.pageNumber + 1}'/>">
                                <i class="angle right icon"></i>
                            </a>
                            <a class="item ${logsPagination.pageNumber + 1 == logsPagination.totalPages ? 'disabled' : ''}"
                               href="<url:replaceParam name='page' value='${logsPagination.totalPages - 1}'/>">
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