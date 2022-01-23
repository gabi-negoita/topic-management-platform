<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="url" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/init-accordion.js"></script>
    <script src="/resources/init-dropdown.js"></script>
    <script src="/resources/init-table.js"></script>
    <script src="/resources/table-sort.js"></script>
    <script src="/resources/uploaded-topics.js"></script>
    <title>MTAPO | Uploaded Topics</title>
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
            <div class="ui stackable grid">
                <div class="doubling row">
                    <div class="fourteen wide column">
                        <h1 class="ui blue header">
                            <i class="icons">
                                <i class="file alternate outline icon"></i>
                                <i class="bottom right corner upload icon"></i>
                            </i>
                            <div class="content">
                                Uploaded topics
                                <div class="sub header">Here you can see all the topics you've uploaded</div>
                            </div>
                        </h1>
                    </div>
                    <div class="two wide column">
                        <div class="ui green labeled icon circular right floated button" id="create-topic-button">
                            <i class="plus circle icon"></i>
                            New
                        </div>
                        <div class="ui small modal create-topic-modal">
                            <div class="ui icon left aligned header">
                                <span class="ui blue text">
                                    <i class="plus circle green icon"></i>
                                    Create new topic
                                </span>
                            </div>
                            <div class="content">
                                <form action="/uploaded-topics/create-topic" method="post" class="ui form" id="create-topic-form"></form>
                                <div class="ui stackable grid">
                                    <%--Title & category--%>
                                    <div class="doubling row">
                                        <div class="ten wide column">
                                            <div class="ui form">
                                                <div class="required field">
                                                    <label>Title</label>
                                                    <input placeholder="Title" name="title" type="text" form="create-topic-form" required>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="six wide column">
                                            <div class="ui form">
                                                <div class="required field">
                                                    <label>Category</label>
                                                    <div class="ui selection fluid dropdown">
                                                        <input type="hidden" name="category" value="license" form="create-topic-form">
                                                        <i class="dropdown icon"></i>
                                                        <div class="text">License</div>
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
                                            </div>
                                        </div>
                                    </div>
                                    <%--Tags--%>
                                    <div class="doubling row">
                                        <div class="sixteen wide column">
                                            <div class="ui form">
                                                <div class="field">
                                                    <label>Tags (select up to 5 items or create your own by pressing Enter)</label>
                                                    <div class="ui fluid multiple search selection four column dropdown" id="create-topic-tag-input">
                                                        <input type="hidden" name="tags" form="create-topic-form" required>
                                                        <i class="dropdown icon"></i>
                                                        <div class="default text">Tags</div>
                                                        <div class="menu"></div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <%--Description--%>
                                    <div class="doubling row">
                                        <div class="sixteen wide column">
                                            <div class="ui form">
                                                <div class="required field">
                                                    <label>Description</label>
                                                    <textarea placeholder="Description"
                                                              name="description" form="create-topic-form" required></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%--Modal actions--%>
                            <div class="actions">
                                <a class="ui red cancel inverted button button">
                                    <i class="remove icon"></i>
                                    Cancel
                                </a>
                                <button type="submit" form="create-topic-form" class="ui green ok inverted button" id="create-topic-button">
                                    <i class="checkmark icon"></i>
                                    Create topic
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="ui clearing divider"></div>
        </div>
        <div class="two wide column"></div>
    </div>
    <%--Form response message--%>
    <c:if test="${formResponse != null}">
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
    </c:if>
    <%--Filter & create topic--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <%--Tags--%>
        <div class="seven wide column">
            <form action="/uploaded-topics" method="get" id="filter-results-form"></form>
            <div class="ui icon fluid input">
                <div class="ui fluid multiple search selection four column dropdown" id="tag-input">
                    <input type="hidden" name="tags" form="filter-results-form" value="${param['tags']}">
                    <i class="dropdown icon"></i>
                    <div class="default text">Tags</div>
                    <div class="menu"></div>
                </div>
            </div>
        </div>
        <%--Category--%>
        <div class="three wide column">
            <div class="ui selection clearable fluid dropdown">
                <input type="hidden" name="category" id="category-input" value="${param['category']}" form="filter-results-form">
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
            <button type="submit" form="filter-results-form" class="ui blue labeled icon circular fluid button">
                <i class="filter icon"></i>
                Apply
            </button>
        </div>
        <div class="two wide column"></div>
    </div>
    </form>
    <c:if test="${topics.size() == 0}">
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
    <c:if test="${topics.size() != 0}">
        <div class="doubling row">
            <div class="two wide column"></div>
            <div class="twelve wide column">
                <table class="ui sortable blue striped padded table">
                    <thead>
                    <tr>
                        <th class="two wide">Category</th>
                        <th class="sorted ascending">Topic</th>
                        <th class="two wide">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${topics}" var="topic">
                        <tr class="left marked ${topic.category.name == 'license' ? 'olive' : 'green'}">
                            <td class="${topic.category.name == 'license' ? 'olive' : 'green'}">
                                <div class="ui circular fluid label ${topic.category.name == 'license' ? 'olive' : 'green'}">
                                        ${topic.category.name}
                                </div>
                            </td>
                            <td>
                                    <%--Title--%>
                                <div class="ui medium header">
                                    <a href="/topics/${topic.id}">
                                        <i class="linkify blue disabled icon"></i>
                                        <b>${topic.title}</b>
                                    </a>
                                </div>
                                    <%--Assigned students--%>
                                <div class="ui styled fluid accordion">
                                    <div class="title">
                                        <i class="dropdown icon"></i>
                                        <b>
                                            <c:if test="${topicIdToUsersMap[topic.id].size() == 1}">
                                                <span class="ui green text">1 student</span> has been assigned to this topic
                                            </c:if>
                                            <c:if test="${topicIdToUsersMap[topic.id].size() != 1}">
                                                <span class="ui green text">${topicIdToUsersMap[topic.id].size()} students</span> have been assigned to this topic
                                            </c:if>
                                        </b>
                                    </div>
                                    <div class="content">
                                        <c:if test="${topicIdToUsersMap[topic.id].size() == 0}">
                                            <div class="ui info message">
                                                <i class="frown face outline large icon"></i>
                                                <b>No students yet</b>
                                            </div>
                                        </c:if>
                                        <c:if test="${topicIdToUsersMap[topic.id].size() != 0}">
                                            <div class="ui middle aligned selection blue list">
                                                <c:forEach items="${topicIdToUsersMap[topic.id]}" var="user">
                                                    <div class="item">
                                                        <div class="right floated">
                                                            <div class="ui compact icon red labeled circular button remove-student-button" data-value="${topic.id}-${user.id}">
                                                                Remove
                                                                <i class="times circle icon"></i>
                                                            </div>
                                                        </div>
                                                        <i class="large user graduate middle aligned icon"></i>
                                                        <div class="content">
                                                            <div class="header">${user.firstName} ${user.lastName}</div>
                                                            <div class="description">${user.email}</div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                                <br>
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
                            <td>
                                <div class="ui compact primary labeled icon fluid circular button edit-topic-button" data-value="${topic.id}">
                                    <i class="edit icon"></i>
                                    Edit
                                </div>
                                <div class="ui fitted hidden divider"></div>
                                <c:if test="${topicIdToUsersMap[topic.id].size() == 0}">
                                    <div class="ui compact negative labeled icon fluid circular button delete-topic-button"
                                         data-value="${topic.id}">
                                        <i class="trash alternate icon"></i>
                                        Delete
                                    </div>
                                </c:if>
                                <c:if test="${topicIdToUsersMap[topic.id].size() != 0}">
                                    <div class="disabled-delete-button">
                                        <div class="ui compact negative labeled icon fluid circular button disabled">
                                            <i class="trash alternate icon"></i>
                                            Delete
                                        </div>
                                    </div>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="two wide column"></div>
        </div>
        <%--Edit topic modal--%>
        <div class="ui small modal" id="edit-topic-modal">
            <div class="header">
                <span class="ui blue text">
                    <i class="edit icon"></i>
                    Edit topic
                </span>
            </div>
            <div class="content">
                <form action="" class="ui form" method="post" id="edit-topic-form"></form>
                <div class="ui stackable grid">
                        <%--Title & category--%>
                    <div class="doubling row">
                        <div class="ten wide column">
                            <div class="ui form">
                                <div class="required field">
                                    <label>Title</label>
                                    <input placeholder="Title" name="title" type="text" id="edit-topic-title" form="edit-topic-form" required>
                                </div>
                            </div>
                        </div>
                        <div class="six wide column">
                            <div class="ui form">
                                <div class="required field">
                                    <label>Category</label>
                                    <div class="ui selection fluid dropdown" id="edit-topic-category">
                                        <input type="hidden" name="category" value="license" form="edit-topic-form">
                                        <i class="dropdown icon"></i>
                                        <div class="text">License</div>
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
                            </div>
                        </div>
                    </div>
                        <%--Tags--%>
                    <div class="doubling row">
                        <div class="sixteen wide column">
                            <div class="ui form">
                                <div class="field">
                                    <label>Tags (select up to 5 items or create your own by pressing Enter)</label>
                                    <div class="ui fluid multiple search selection four column dropdown" id="edit-topic-tag-input">
                                        <input type="hidden" name="tags" form="edit-topic-form" required>
                                        <i class="dropdown icon"></i>
                                        <div class="default text">Tags</div>
                                        <div class="menu"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                        <%--Description--%>
                    <div class="doubling row">
                        <div class="sixteen wide column">
                            <div class="ui form">
                                <div class="required field">
                                    <label>Description</label>
                                    <textarea placeholder="Description"
                                              class="popup"
                                              name="description"
                                              id="edit-topic-description"
                                              form="edit-topic-form" required></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="actions">
                <div class="ui red cancel inverted button button">
                    <i class="remove icon"></i>
                    Cancel
                </div>
                <button type="submit" class="ui green ok inverted button" form="edit-topic-form" id="edit-topic-submit-button">
                    <i class="checkmark icon"></i>
                    Save changes
                </button>
            </div>
        </div>
        <%--Delete topic modal--%>
        <div class="ui small modal" id="delete-topic-modal">
            <div class="header">
                <span class="ui blue text">
                    <i class="trash alternate red icon"></i>
                    Delete topic
                </span>
            </div>
            <div class="content">
                <form action="" class="ui form" method="post" id="delete-topic-form">
                    <h3>If you delete this topic, then</h3>
                    <ul>
                        <i class="exclamation red icon"></i>
                        <b>All students assigned to this topic will be <span class="ui red text">removed</span></b>
                        <br>
                        <i class="exclamation red icon"></i>
                        <b>All comments on this topic will be <span class="ui red text">removed</span></b>
                        <br>
                        <i class="exclamation red icon"></i>
                        <b>This topic won't be accessible anymore</b>
                    </ul>
                    <div class="ui divider"></div>
                    <div class="ui toggle checkbox" id="delete-topic-checkbox">
                        <input type="checkbox">
                        <label><b>I understand and I want to proceed</b></label>
                    </div>
                </form>
            </div>
            <div class="actions">
                <div class="ui red cancel inverted button button">
                    <i class="remove icon"></i>
                    Cancel
                </div>
                <button type="submit" class="ui green ok inverted button disabled" form="delete-topic-form" id="delete-topic-submit-button">
                    <i class="checkmark icon"></i>
                    Delete
                </button>
            </div>
        </div>
        <%--Remove student modal--%>
        <div class="ui small modal" id="remove-student-modal">
            <div class="header">
                <span class="ui blue text">
                    <i class="times circle red icon"></i>
                    Remove student
                </span>
            </div>
            <div class="content">
                <form action="" class="ui form" method="post" id="remove-student-form">
                    <h3>If you remove this student, then</h3>
                    <ul>
                        <i class="exclamation red icon"></i>
                        <b>The student won't have assigned this topic anymore</b>
                        <br>
                        <i class="exclamation red icon"></i>
                        <b>The student won't be able to apply to this topic again</b>
                    </ul>
                    <div class="ui divider"></div>
                    <div class="ui toggle checkbox" id="remove-student-checkbox">
                        <input type="checkbox">
                        <label><b>I understand and I want to proceed</b></label>
                    </div>
                </form>
            </div>
            <div class="actions">
                <div class="ui red cancel inverted button button">
                    <i class="remove icon"></i>
                    Cancel
                </div>
                <button type="submit" class="ui green ok inverted button disabled" form="remove-student-form" id="remove-student-submit-button">
                    <i class="checkmark icon"></i>
                    Remove
                </button>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>