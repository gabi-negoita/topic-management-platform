let news = [];

$(document).ready(() => {
    $('#search-topics-input').on('keypress mouseover', () => {
        $.ajax({
            type: 'GET',
            url: '/navbar/searchbar-topics',
            success: (response) => {
                let content = [];
                response.forEach(element => content.push({
                    title: element.title,
                    description: element.description,
                    url: '/topics/' + element.id
                }));
                $('.ui.search').search({source: content});
            },
            error: (error) => {
                console.error("ERROR: could not retrieve topics: " + error);
            }
        });
    });

    $('#logout-button').click(() => $('#logout-modal').modal({inverted: true}).modal('show'));

    $('#notifs').popup({
        hoverable: true,
        position: 'bottom center',
        delay: {
            hide: 500
        }
    });


});

function updateNotifs() {
    $.ajax({
        type: 'GET',
        url: '/navbar/notifications/' + sessionUserId,
        success: function (response) {
            document.getElementById('notifDiv').innerHTML = "";
            let extraDivElement;
            news = [];
            response.forEach(function (r, i) {
                let status;
                let icon;

                let toastMessage;
                let notificationMessage;
                let link;
                switch (r.description) {
                    case "approved":
                        status = "green";
                        icon = "check circle";
                        toastMessage = `Your application for ${r.studentTopic.topic.title} has been ${r.description}`;
                        notificationMessage = `Your application for <span class="ui text"><b>${r.studentTopic.topic.title}</b></span> has been <span class="ui text"><b>${r.description}</b></span>`;
                        break;
                    case "canceled":
                        status = "red";
                        icon = "times circle";

                        let cancelTypeStudent = "";

                        if (r.type === "student-topic-request") {
                            cancelTypeStudent = "request";
                        } else if (r.type === "student-application-cancel") {
                            cancelTypeStudent = "application";
                        }

                        toastMessage = `${r.studentTopic.user.firstName} ${r.studentTopic.user.lastName} has canceled the ${cancelTypeStudent} for ${r.studentTopic.topic.title}`;
                        notificationMessage = `<span class="ui text"><b>${r.studentTopic.user.firstName} ${r.studentTopic.user.lastName}</b></span> has canceled the ${cancelTypeStudent} for <span class="ui text"><b>${r.studentTopic.topic.title}</b></span>`;
                        break;
                    case "waiting":
                        status = "orange";
                        icon = "exclamation circle";
                        toastMessage = `${r.studentTopic.user.firstName} ${r.studentTopic.user.lastName} has requested for your approval for ${r.studentTopic.topic.title}`;
                        notificationMessage = `<span class="ui text"><b>${r.studentTopic.user.firstName} ${r.studentTopic.user.lastName}</b></span> has requested for your approval for <span class="ui text"><b>${r.studentTopic.topic.title}</b></span>`;
                        break;
                    case "declined":
                        status = "red";
                        icon = "times circle";

                        if (r.type == "teacher-topic-response") {
                            notificationMessage = `Your application for <span class="ui text"><b>${r.studentTopic.topic.title}</b></span> has been <span class="ui text"><b>${r.description}</b></span>`;
                            toastMessage = `Your application for ${r.studentTopic.topic.title} has been ${r.description}`;
                        } else if (r.type == "teacher-application-cancel") {
                            notificationMessage = `<span class="ui text"><b>${r.studentTopic.topic.user.firstName} ${r.studentTopic.topic.user.lastName}</b></span> has cancelled your application for <span class="ui text"><b>${r.studentTopic.topic.title}</b></span>`;
                            toastMessage = `${r.studentTopic.topic.user.firstName} ${r.studentTopic.topic.user.lastName} has cancelled your application for ${r.studentTopic.topic.title}`;
                        }
                        break;
                    case null:
                        status = "blue"
                        icon = "pencil alternate";
                        break;
                }


                switch (r.type) {
                    case "teacher-topic-response":
                        if (userRoles.includes("student")) {
                            link = `/topics/${r.studentTopic.topic.id}`;
                            toastMessage = `Your application for ${r.studentTopic.topic.title} has been ${r.description}`;
                            extraDivElement = `
                                <div class="item" onmousedown="updateRead(${r.id}, ${r.isRead})">
                                    <div class="right floated content">
                                        ${!r.isRead ? '<i class="ui circle blue small icon"></i>' : ''}
                                    </div>
                                    <div class="content">
                                        <a href="${link}">
                                            <div>${notificationMessage}</div>
                                        </a>
                                        <div class="description">
                                            Received on <b>${formatDate(new Date(r.date))}</b>
                                        </div>
                                    </div>
                                </div>
                            `;
                        }
                        break;
                    case "student-topic-request":
                        link = `/topics/${r.studentTopic.topic.id}`;
                        extraDivElement = `
                            <div class="item" onmousedown="updateRead(${r.id}, ${r.isRead})">
                                <div class="right floated content">
                                    ${!r.isRead ? '<i class="ui circle blue small icon"></i>' : ''}
                                </div>
                                <div class="content">
                                    <a href="${link}">
                                        
                                        <div>${notificationMessage}</div>
                                    </a>
                                    <div class="description">
                                       Received on <b>${formatDate(new Date(r.date))}</b>
                                    </div>
                                </div>
                            </div>
                        `;
                        break;
                    case "student-application-cancel":
                        link = `/topics/${r.studentTopic.topic.id}`;
                        extraDivElement = `
                            <div class="item" onmousedown="updateRead(${r.id}, ${r.isRead})">
                                <div class="right floated content">
                                    ${!r.isRead ? '<i class="ui circle blue small icon"></i>' : ''}
                                </div>
                                <div class="content">
                                    <a href="${link}">
                                        
                                        <div>${notificationMessage}</div>
                                    </a>
                                    <div class="description">
                                       Received on <b>${formatDate(new Date(r.date))}</b>
                                    </div>
                                </div>
                            </div>
                        `;
                        break;
                    case "teacher-application-cancel":
                        link = `/topics/${r.studentTopic.topic.id}`;
                        extraDivElement = `
                            <div class="item" onmousedown="updateRead(${r.id}, ${r.isRead})">
                                <div class="right floated content">
                                    ${!r.isRead ? '<i class="ui circle blue small icon"></i>' : ''}
                                </div>
                                <div class="content">
                                    <a href="${link}">
                                        
                                        <div>${notificationMessage}</div>
                                    </a>
                                    <div class="description">
                                       Received on <b>${formatDate(new Date(r.date))}</b>
                                    </div>
                                </div>
                            </div>
                        `;
                        break;
                    case "comment":
                        link = `/topics/${r.comment.topic.id}`;
                        toastMessage = `A new comment on ${r.comment.topic.title} has been posted`;
                        extraDivElement = `
                            <div class="item" onmousedown="updateRead(${r.id}, ${r.isRead})">
                                <div class="right floated content">
                                    ${!r.isRead ? '<i class="ui circle blue small icon"></i>' : ''}
                                </div>
                                <div class="content">
                                    <a href="${link}">
                                        <div><span class="ui text"><b>${r.comment.user.firstName} ${r.comment.user.lastName}</b></span> has commented on <span class="ui text"><b>${r.comment.topic.title}</b></span></div>
                                    </a>
                                    <div class="description">
                                       Received on <b>${formatDate(new Date(r.date))}</b>
                                    </div>
                                </div>
                            </div>
                        `;
                }

                if (!r.isSeen) {
                    $('body')
                        .toast({
                            position: 'bottom right',
                            showProgress: 'top',
                            classProgress: status,
                            newestOnTop: true,
                            message: toastMessage,
                            displayTime: 30000,
                            classActions: 'horizontal bottom attached',
                            actions: [
                                {
                                    text: 'Close'
                                },
                                {
                                    text: 'More details',
                                    class: status,
                                    click: function () {
                                        window.location.replace(link);
                                    }
                                }
                            ]
                        })
                    ;
                    updateSeen(r.id);
                }


                if (i < 5) {
                    document.getElementById('notifDiv').innerHTML += extraDivElement;
                }

                news.push(extraDivElement);
            });

            if (response.length === 0) {
                let defaultItem = `
                    <div class="ui center aligned icon header">
                        <i class="bell small slash icon"></i> 
                        <div class="content">
                        No notifications
                            <div class="sub header">
                                You don't have any notifications yet
                            </div>
                        </div>
                    </div>
                `;

                document.getElementById('notifDiv').innerHTML = defaultItem;
                document.getElementById('notifOuterDiv').style.height = '6rem';
                document.getElementById('notifDiv').style.removeProperty("overflow");
            } else {
                extraDivElement = `
                    <div class="ui hidden divider"></div>
                    <a class="ui fluid circular inverted blue button" id="show-more">Show more</a>
                `;
                document.getElementById('notifDiv').innerHTML += extraDivElement;
            }

            $('#show-more').click(() => {
                $('#notifs').popup('hide');

                let newsElements = "";
                news.forEach(n => {
                    newsElements += `<div class="item">${n}</div>`;
                });

                newsElements = `
                            <div class="content scrolling content">
                                <div class="ui selection relaxed list">
                                    ${newsElements}
                                </div>
                            </div>`

                let actions = `
                            <div class="actions">
                                <a class="ui blue cancel inverted button">
                                    Close
                                </a>
                            </div>`;
                $('#news-modal').html('<div class="header">' +
                    '                       <span class="ui blue text">' +
                    '                           <i class="bell icon"></i>' +
                    '                           Notifications</span>' +
                    '                   </div>');
                $('#news-modal').append(newsElements);
                $('#news-modal').append(actions);
                $('#news-modal').modal({inverted: true, observeChanges: true}).modal('show');
            });
        },
        error: function () {
            console.error("ERROR: could not retrieve topics");
        }
    });
}


function updateRead(id, isRead) {
    if (!isRead) {
        $.ajax({
            url: "/navbar/read/" + id,
            type: "PUT",
            success: function () {
                isRead = true;
                updateNotifs();
            }
        });
    }
}

function updateSeen(id) {
    $.ajax({
        url: "/navbar/seen/" + id,
        type: "PUT"
    });
}


function formatDate(dateObj) {
    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"];
    const month = monthNames[dateObj.getMonth()].toString().slice(0, 3);
    const day = String(dateObj.getDate()).padStart(2, '0');
    const year = dateObj.getFullYear();
    return month + ' ' + day + ', ' + year;
}


