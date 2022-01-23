<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/init-accordion.js"></script>
    <script src="/resources/init-dropdown.js"></script>
    <script src="/resources/topic-requests.js"></script>
    <title>MTAPO | Topic Requests</title>
</head>
<body>
<script>
    let sortArray = ['id', 'desc', null, 0];
    let requests = [];
    let numberOfElements = ${numberOfElements};
    let request;
    let numberOfElementsPerPage = 10;
    <c:forEach var="request" items="${requestsBatch}">
    request = {};
    request.id = '${request.id}';
    request.idTopic = '${request.topic.id}';
    request.title = '${request.topic.title}';
    request.firstName = '${request.user.firstName}';
    request.lastName = '${request.user.lastName}';
    request.email = '${request.user.email}';
    request.date = '${request.date}';
    requests.push(request);
    </c:forEach>

    $(document).ready(() => {
        if (numberOfElements > numberOfElementsPerPage) {
            document.getElementById('aInc').classList.remove('disabled');
            document.getElementById('aMax').classList.remove('disabled');
        }
    });

</script>
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
                    <i class="file alternate outline icon"></i>
                    <i class="bottom right corner check circle icon"></i>
                </i>
                <div class="content">
                    Topic Requests
                    <div class="sub header">Here you can approve or decline the requests you received from your
                        students
                    </div>
                </div>
            </h1>
            <div class="ui clearing divider"></div>
        </div>
        <div class="two wide column"></div>
    </div>
    <%--Actual content--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui stackable grid">
                <div class="doubling row">
                    <div class="twelve wide column">
                        <div class="ui icon fluid input" id="search-input-container">
                            <i class="search icon"></i>
                            <input type="text" placeholder="Search by user name or email..." name="keywords"
                                   id="search-input"
                                   onkeyup="sortArray[2] = $('#search-input').val(); updateRequests(sortArray, requests)">
                        </div>
                    </div>
                    <div class="four wide column">
                        <div class="ui clearable selection fluid dropdown" id="role-dropdown">
                            <input type="hidden">
                            <i class="dropdown icon"></i>
                            <div class="default text">
                                <i class="sort icon"></i>
                                Sort by
                            </div>
                            <div class="menu">
                                <div class="item"
                                     onclick="{sortArray[0] = 'id'; sortArray[1] = 'asc'; updateRequests(sortArray, requests);}">
                                    <i class="sort amount up alternate icon"></i>
                                    Sort by <span class="ui blue text"><b>date</b></span>
                                </div>
                                <div class="item"
                                     onclick="{sortArray[0] = 'id'; sortArray[1] = 'desc'; updateRequests(sortArray, requests);}">
                                    <i class="sort amount down alternate icon"></i>
                                    Sort by <span class="ui blue text"><b>date</b></span>
                                </div>
                                <div class="item"
                                     onclick="{sortArray[0] = 'email'; sortArray[1] = 'asc'; updateRequests(sortArray, requests);}">
                                    <i class="sort alphabet up alternate icon"></i>
                                    Sort by <span class="ui blue text"><b>user email</b></span>
                                </div>
                                <div class="item"
                                     onclick="{sortArray[0] = 'email'; sortArray[1] = 'desc'; updateRequests(sortArray, requests);}">
                                    <i class="sort alphabet down alternate icon"></i>
                                    Sort by <span class="ui blue text"><b>user email</b></span>
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
        <div class="twelve wide column" id="contentDiv">
            <c:forEach items="${requestsBatch}" var="request">
            <div class="ui floating message" id="div_${request.id}">
                <div class="content">
                    <div class="header">
                        <i class="spinner loading icon"></i>
                        Pending request
                    </div>
                </div>
                <table class="ui very basic table">
                    <tbody>
                    <tr>
                        <td class="two wide right marked blue"><b>Topic</b></td>
                        <td class="violet">
                            <a href="/topics/${request.id}"><i class="file alternate icon"></i>
                                <b>${request.topic.title}</b>
                            </a>
            </div>
            </td>
            </tr>
            <tr>
                <td class="two wide right marked yellow"><b>Student</b></td>
                <td class="warning">
                    <i class="user graduate icon"></i>
                    <b>${request.user.firstName} ${request.user.lastName}</b>
                    <div class="ui basic circular label">
                        <i class="envelope icon"></i>${request.user.email}
                    </div>
                </td>
            </tr>
            <tr>
                <td class="two wide right marked olive"><b>Date of application</b></td>
                <td class="positive">
                    <i class="calendar alternate icon"></i>
                    <b><fmt:formatDate value="${request.date}" pattern="MMM d, yyyy HH:mm"/></b>
                </td>
            </tr>
            </tbody>
            </table>
            <div class="ui hidden divider"></div>
            <div class="ui grid">
                <div class="doubling row">
                    <div class="sixteen wide column">
                        <div class="ui right floated green button approve-button" data-value="${request.id}">
                            <i class="check circle icon"></i>
                            Approve
                        </div>
                        <div class="ui right floated red button decline-button" data-value="${request.id}">
                            <i class="times circle icon"></i>
                            Decline
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </c:forEach>
        <c:if test="${requestsBatch.size() eq 0}">
            <div class="doubling row">
                <div class="three wide column"></div>
                <div class="ten wide column">
                    <div class="ui section hidden divider"></div>
                    <h2 class="ui center aligned icon header">
                        <i class="grin beam outline icon"></i>
                        <div class="content">
                            Yay!
                            <div class="sub header">
                                Everything has been taken cared of
                            </div>
                        </div>
                    </h2>
                </div>
                <div class="three wide column"></div>
            </div>
        </c:if>
    </div>
</div>

<div class="doubling row">
    <div class="two wide column"></div>
    <div class="twelve wide column">

    </div>
</div>
<div class="two wide column"></div>

<%--    Pagination--%>
<c:if test="${numberOfElements != 0}">
    <div class="doubling row" id="pagination">
        <div class="five wide column"></div>
        <div class="six wide column">
            <div class="ui five item pagination borderless inverted blue menu">
                <a id="aMin" class="item disabled"
                   onclick="{sortArray[3] = 0; updateRequests(sortArray, requests);}">
                    <i class="angle double left icon"></i>
                </a>
                <a id="aDec" class="item disabled" onclick="{sortArray[3]--; updateRequests(sortArray, requests);}">
                    <i class="angle left icon"></i>
                </a>
                <div class="active item" id="count">
                </div>
                <a id="aInc" class="item disabled" onclick="{sortArray[3]++; updateRequests(sortArray, requests);}">
                    <i class="angle right icon"></i>
                </a>
                <a id="aMax" class="item disabled"
                   onclick="{sortArray[3] = (Math.ceil(numberOfElements/numberOfElementsPerPage) - 1); updateRequests(sortArray, requests);}">
                    <i class="angle double right icon"></i>
                </a>
            </div>
        </div>
        <div class="five wide column"></div>
    </div>
</c:if>
<script>
    $('#count').html(`<b>Page 1 of ` + Math.ceil(numberOfElements / numberOfElementsPerPage) + `</b>`);
</script>
</div>
<div class="ui mini modal" id="approve-request-modal">
    <div class="header">
        <span class="ui blue text">
            <i class="check circle green icon"></i>
            Approve request
        </span>
    </div>
    <div class="center aligned content">
        <h4>Are you sure you want to <span class="ui green text">approve</span> this request?</h4>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <div class="ui green ok inverted button">
            <i class="checkmark icon"></i>
            Approve
        </div>
    </div>
</div>
<div class="ui mini modal" id="decline-request-modal">
    <div class="header">
        <span class="ui blue text">
            <i class="times circle red icon"></i>
            Decline request
        </span>
    </div>
    <div class="center aligned content">
        <h4>Are you sure you want to <span class="ui red text">decline</span> this request?</h4>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <div class="ui green ok inverted button">
            <i class="checkmark icon"></i>
            Decline
        </div>
    </div>
</div>

</body>
</html>