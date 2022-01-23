<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/topic.js"></script>
    <title>MTAPO | View topic</title>
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
            <div class="ui grid">
                <%--Title--%>
                <div class="ten wide left floated column">
                    <h1 class="ui blue header">
                        <i class="icons">
                            <i class="file alternate outline icon"></i>
                            <i class="bottom right corner eye icon"></i>
                        </i>
                        <div class="content">
                            Topic
                            <div class="sub header">Here you can see a topic's detail and also leave comments to clear
                                things out
                            </div>
                        </div>
                    </h1>
                </div>
                <%--Apply button--%>
                <div class="four wide right floated column">
                    <c:if test="${sessionUser.category.name == topic.category.name and studentTopic == null and hasApprovedTopic == false}">
                        <div class="ui green labeled icon circular right floated button" id="apply-request-button"
                             data-value="${topic.id}">
                            <i class="check circle icon"></i>
                            Apply
                        </div>
                    </c:if>
                    <c:if test="${sessionUser.category.name == topic.category.name and studentTopic == null and hasApprovedTopic == true}">
                        <div class="ui circular labeled icon active right floated yellow button"
                             data-tooltip="You are already assigned to another topic"
                             data-variation="basic">
                            <i class="check circle icon"></i>
                            Already assigned
                        </div>
                    </c:if>
                    <%--Cancel button--%>
                    <c:if test="${studentTopic != null and studentTopic.status == 'waiting'}">
                        <div class="ui animated fade orange circular labeled icon right floated button"
                             id="cancel-request-button">
                            <i class="spinner loading icon" id="cancel-request-button-icon"></i>
                            <div class="visible content">
                                Waiting for approval
                            </div>
                            <div class="hidden content">
                                Cancel request
                            </div>
                        </div>
                    </c:if>
                    <%--Status--%>
                    <c:if test="${studentTopic != null and studentTopic.status == 'approved'}">
                        <div class="ui animated fade green circular labeled icon right floated button"
                             id="cancel-application-button">
                            <i class="check circle icon" id="cancel-application-button-icon"></i>
                            <div class="visible content">
                                Assigned
                            </div>
                            <div class="hidden content">
                                Cancel
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${studentTopic != null and studentTopic.status != 'waiting' and studentTopic.status != 'approved'}">
                        <div class="ui circular labeled icon active right floated red button"
                             data-tooltip="${studentTopic.reason}"
                             data-variation="basic">
                            <i class="times circle icon"></i>
                            Request ${studentTopic.status}
                        </div>
                    </c:if>

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
    <%--Actual content--%>
    <div class="doubling row">
        <div class="two wide column"></div>
        <div class="twelve wide column">
            <div class="ui raised blue segment">
                <div class="ui grid">
                    <div class="doubling row">
                        <div class="fourteen wide column">
                            <div class="ui blue header">
                                <div class="content">
                                    <i class="linkify blue disabled icon"></i>
                                    <a href="/topics/${topic.id}">${topic.title}</a>
                                </div>
                            </div>
                        </div>
                        <div class="two wide column">
                            <div class="ui circular fluid label ${topic.category.name == 'license' ? 'olive' : 'green'}">
                                ${topic.category.name}
                            </div>
                        </div>
                        <div class="ui divider"></div>
                        <div class="sixteen wide column">
                            <div class="ui selection list">
                                <div class="item">
                                    <div class="ui blue horizontal circular label">
                                        <i class="user tie icon"></i>${topic.user.firstName} ${topic.user.lastName}
                                    </div>
                                    <span><i class="envelope icon"></i>${topic.user.email}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ui clearing divider"></div>
                ${topic.description}
                <%--Comments--%>
                <h4 class="ui horizontal divider header">
                    <i class="comments icon"></i>
                    Comments
                </h4>
                <div class="ui hidden divider"></div>
                <c:if test="${comments.size() == 0}">
                    <div class="ui center aligned icon header">
                        <i class="comment slash icon"></i>
                        <div class="content">
                            No comments yet
                        </div>
                    </div>
                </c:if>
                <c:if test="${comments.size() != 0}">
                    <div class="ui comments">
                        <c:forEach items="${comments}" var="comment">
                            <div class="ui raised segment ${comment.user.category == null ? 'orange' : 'yellow'}">
                                <div class="comment">
                                    <div class="avatar">
                                        <i class="user big icon ${comment.user.category == null ? 'tie' : 'graduate'}"></i>
                                    </div>
                                    <div class="content">
                                        <a class="author">${comment.user.firstName} ${comment.user.lastName}</a>
                                        <div class="metadata">
                                            <span class="date">Posted on <b><fmt:formatDate value="${comment.date}"
                                                                                            pattern="MMM d, yyyy"/></b></span>
                                        </div>
                                        <div class="text">${comment.comment}</div>
                                        <div class="actions">
                                            <c:if test="${sessionUser.id == comment.user.id}">
                                                <a class="edit edit-comment-button"
                                                   data-value="${topic.id}-${comment.id}">
                                                    <i class="edit icon"></i>
                                                    <b>Edit</b>
                                                </a>
                                            </c:if>
                                            <c:if test="${sessionUser.id == comment.user.id or sessionUser.id == topic.user.id}">
                                                <a class="delete delete-comment-button"
                                                   data-value="${topic.id}-${comment.id}">
                                                    <i class="trash alternate icon"></i>
                                                    <b>Delete</b>
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                <form action="/topics/${topic.id}/post-comment" method="post" class="ui form">
                    <div class="ui grid">
                        <div class="sixteen wide column">
                            <div class="required field">
                                <label>Leave a comment</label>
                                <textarea name="comment"
                                          placeholder="Leave a comment to ask for any clarifications regarding this topic"
                                          required></textarea>
                            </div>
                        </div>
                        <div class="sixteen wide column">
                            <button type="submit" class="ui blue labeled right floated icon circular button">
                                <i class="paper plane icon"></i>
                                Post comment
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%--Edit comment modal--%>
<div class="ui small modal" id="edit-comment-modal">
    <form action="" method="post" id="edit-comment-form"></form>
    <div class="header">
        <span class="ui blue text">
            <i class="edit blue icon"></i>
            Edit comment
        </span>
    </div>
    <div class="center aligned content">
        <form action="" method="post" class="ui form">
            <div class="ui grid">
                <div class="sixteen wide column">
                    <textarea id="edit-comment-textarea"
                              name="comment"
                              form="edit-comment-form"
                              placeholder="Leave a comment to ask for any clarifications regarding this topic"
                              required></textarea>
                </div>
            </div>
        </form>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <button type="submit" form="edit-comment-form" class="ui green ok inverted button">
            <i class="checkmark icon"></i>
            Save changes
        </button>
    </div>
</div>
<%--Apply request modal--%>
<div class="ui mini modal" id="apply-request-modal">
    <form action="/topics/${topic.id}/apply" method="post" id="apply-request-form"></form>
    <div class="ui icon left aligned header">
            <span class="ui blue text">
                <i class="check circle green icon"></i>
                Apply to topic
            </span>
    </div>
    <div class="center aligned content">
        <h4>Are you sure you want to apply to this topic?</h4>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            No
        </a>
        <button type="submit" form="apply-request-form" class="ui green ok inverted button">
            <i class="checkmark icon"></i>
            Yes
        </button>
    </div>
</div>
<%--Cancel request modal--%>
<div class="ui mini modal" id="cancel-request-modal">
    <form action="/topics/${topic.id}/cancel-request" method="post" id="cancel-request-form"></form>
    <div class="ui icon left aligned header">
            <span class="ui blue text">
                <i class="remove circle red icon"></i>
                Cancel topic request
            </span>
    </div>
    <div class="center aligned content">
        <h4>Are you sure you want to cancel the request?</h4>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <button type="submit" form="cancel-request-form" class="ui green ok inverted button">
            <i class="checkmark icon"></i>
            Cancel request
        </button>
    </div>
</div>
<%--Cancel application modal--%>
<div class="ui small modal" id="cancel-application-modal">
    <form action="/topics/${topic.id}/cancel-application" method="post" id="cancel-application-form"></form>
    <div class="ui icon left aligned header">
            <span class="ui blue text">
                <i class="remove circle red icon"></i>
                Cancel topic application
            </span>
    </div>
    <div class="content">
        <h3>If you cancel your topic application, then</h3>
        <ul>
            <i class="exclamation red icon"></i>
            <b>You won't be able to apply to this topic again</b>
        </ul>
        <div class="ui divider"></div>
        <div class="ui toggle checkbox" id="cancel-application-checkbox">
            <input type="checkbox">
            <label><b>I understand and I want to proceed</b></label>
        </div>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <button type="submit" id="cancel-application-submit-button" form="cancel-application-form"
                class="ui green ok inverted disabled button">
            <i class="checkmark icon"></i>
            Cancel application
        </button>
    </div>
</div>
<%--Delete comment modal--%>
<div class="ui mini modal" id="delete-comment-modal">
    <form action="" method="post" id="delete-comment-form"></form>
    <div class="header">
            <span class="ui blue text">
                <i class="trash alternative red icon"></i>
                Delete comment
            </span>
    </div>
    <div class="center aligned content">
        <h4>Are you sure you want to delete this comment?</h4>
    </div>
    <div class="actions">
        <a class="ui red cancel inverted button button">
            <i class="remove icon"></i>
            Cancel
        </a>
        <button type="submit" form="delete-comment-form" class="ui green ok inverted button">
            <i class="checkmark icon"></i>
            Yes
        </button>
    </div>
</div>
</body>
</html>
        