<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="header.jsp"/>
    <script src="/resources/init-accordion.js"></script>
    <title>MTAPO | Home</title>
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
                <i class="child icon"></i>
                <div class="content">
                    Welcome, ${sessionScope.sessionUser.firstName}!
                    <div class="sub header">This is an educational site for choosing and discussing topics for the final
                        projects for license and master!
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
            <div class="ui raised segment">
                <h3>
                    <div class="header">
                        <i class="info circle blue icon"></i>
                        <span class="ui blue text">General information</span>
                    </div>
                </h3>
                <div class="ui basic segment">
                    <b>The <span class="ui blue text">Department of Computer Science Engineering</span>
                        provides a tool for choosing topics for theses and facilitates discussions between
                        students and mentors (teachers).</b>
                </div>
            </div>
        </div>
        <div class="two wide column"></div>
    </div>
    <div class="doubling stretched row">
        <div class="two wide column"></div>
        <div class="six wide column">
            <div class="ui raised segment">
                <h3>
                    <div class="header">
                        <i class="list alternate blue icon"></i>
                        <span class="ui blue text">How to use</span>
                    </div>
                </h3>
                <div class="ui basic segment">
                    <div class="ui selection list">
                        <div class="item">
                            <i class="angle right blue icon"></i>
                            <div class="content">
                                <div class="header">
                                    When a teacher <span class="ui blue text">refuses</span> to be a mentor for a
                                    student for a particular topic,
                                    the student will <span class="ui blue text">no longer be able to apply</span> for
                                    that topic
                                    A student can change their topic
                                </div>
                            </div>
                        </div>
                        <div class="item">
                            <i class="angle right blue icon"></i>
                            <div class="content">
                                <div class="header">
                                    Both students and teachers can <span class="ui blue text">cancel</span> an already
                                    approved topic
                                </div>
                            </div>
                        </div>
                        <div class="item">
                            <i class="angle right blue icon"></i>
                            <div class="content">
                                <div class="header">
                                    Anyone involved in a topic (mentor or student) can <span class="ui blue text">add comments</span>
                                    on that specific topic
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="six wide column">
            <div class="ui raised segment">
                <h3>
                    <div class="header">
                        <i class="cogs blue icon"></i>
                        <span class="ui blue text">System description and instructions for use</span>
                    </div>
                </h3>
                <div class="ui basic segment">
                    <div class="ui selection list">
                        <div class="item">
                            <i class="angle right blue icon"></i>
                            <div class="content">
                                <div class="header">
                                    The system for choosing thesis topics will allow students to <span
                                        class="ui blue text">communicate</span> with teachers
                                    in order to choose or apply to a topic and more
                                </div>
                            </div>
                        </div>
                        <div class="item">
                            <i class="angle right blue icon"></i>
                            <div class="content">
                                <div class="header">
                                    If you are a teacher, this system allows you to <span
                                        class="ui blue text">view</span> your topics,
                                    to <span class="ui blue text">accept</span> or <span
                                        class="ui blue text">refuse</span> to coordinate a student,
                                    but also to communicate with the students you are mentoring
                                </div>
                            </div>
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
