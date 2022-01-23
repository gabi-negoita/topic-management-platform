function updateRequests() {
    const json = JSON.stringify({...sortArray});

    $.ajax({
        url: "/topic-requests/requests",
        type: "PUT",
        data: json,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (response) {
            requests = [];
            let request;
            let i = 0;
            response.forEach(function () {
                request = {};
                request.id = response[i].id;
                request.idTopic = response[i].topic.id;
                request.title = response[i].topic.title;
                request.firstName = response[i].user.firstName;
                request.lastName = response[i].user.lastName;
                request.email = response[i].user.email;
                request.date = response[i].date;
                requests.push(request);
                i++;
            });
            var div = document.getElementById('contentDiv');
            div.innerHTML = null;

            for (let i = 0; i < requests.length; i++) {
                div.innerHTML += `
                <div class="ui floating message" id="div_${requests[i].id}">
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
                                <a href="/topics/${requests[i].id}"><i class="file alternate icon"></i>
                                    <b>${requests[i].title}</b>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td class="two wide right marked yellow"><b>Student</b></td>
                            <td class="warning">
                                <i class="user graduate icon"></i>
                                <b>${requests[i].firstName} ${requests[i].lastName}</b>
                                <div class="ui basic circular label">
                                    <i class="envelope icon"></i>${requests[i].email}
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="two wide right marked olive"><b>Date of application</b></td>
                            <td class="positive">
                                <i class="calendar alternate outline icon"></i>
                                <b>${formatDate(new Date(requests[i].date))}</b>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="ui hidden divider"></div>
                    <div class="ui grid">
                        <div class="doubling row">
                            <div class="sixteen wide column">
                                <div class="ui right floated green button approve-button" data-value="${requests[i].id}">
                                    <i class="check circle icon"></i>
                                    Approve
                                </div>
                                <div class="ui right floated red button decline-button" data-value="${requests[i].id}">
                                    <i class="times circle icon"></i>
                                    Decline
                                </div>
                            </div>
                        </div>
                    </div>
                </div>    
                `;
            }

            if (requests.length === 0) {
                div.innerHTML += `
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
                `;
                document.getElementById('pagination').style.display = 'none';
            }

            $('#count').html(`<b>Page ` + (sortArray[3] + 1) + ` of ` + Math.ceil(numberOfElements / numberOfElementsPerPage) + `</b>`);

            if (sortArray[3] > 0 && sortArray[3] < (Math.ceil(numberOfElements / numberOfElementsPerPage) - 1)) {
                document.getElementById('aMin').className = 'item';
                document.getElementById('aDec').className = 'item';
                document.getElementById('aInc').className = 'item';
                document.getElementById('aMax').className = 'item';
            } else if (sortArray[3] == 0) {
                if (numberOfElements > numberOfElementsPerPage) {
                    document.getElementById('aMin').className = 'item disabled';
                    document.getElementById('aDec').className = 'item disabled';
                    document.getElementById('aInc').className = 'item';
                    document.getElementById('aMax').className = 'item';
                } else {
                    document.getElementById('aMin').className = 'item disabled';
                    document.getElementById('aDec').className = 'item disabled';
                    document.getElementById('aInc').className = 'item disabled';
                    document.getElementById('aMax').className = 'item disabled';
                }
            } else if (sortArray[3] == (Math.ceil(numberOfElements / numberOfElementsPerPage) - 1)) {
                document.getElementById('aMin').className = 'item';
                document.getElementById('aDec').className = 'item';
                document.getElementById('aInc').className = 'item disabled';
                document.getElementById('aMax').className = 'item disabled';
            }
            approve();
            decline();
        }
    });

}


function approve() {
    // Approve request
    $('.approve-button').on('click', function () {
        let requestElement = $(this);
        let requestId = requestElement.data().value;

        $('#approve-request-modal').modal({
            inverted: true,
            closable: false,
            observeChanges: true,
            onApprove: () => {
                $.ajax({
                    url: "/topic-requests/approve-request/" + requestId,
                    type: "PUT",
                    success: function () {
                        requestElement.closest('.message').transition('fade down');
                        checkResults();
                        setTimeout(function () {
                            updateRequests();
                        }, 500);
                    },
                    error: (error) => {
                        console.error('ERROR: could not approve request: ' + error);
                    }
                });
            }
        }).modal('show');
    });
}

function decline() {
    // Decline request
    $('.decline-button').on('click', function () {
        let requestElement = $(this);
        let requestId = requestElement.data().value;

        $('#decline-request-modal').modal({
            inverted: true,
            closable: false,
            observeChanges: true,
            onApprove: () => {
                $.ajax({
                    url: "/topic-requests/deny-request/" + requestId,
                    type: "PUT",
                    success: function () {
                        requestElement.closest('.message').transition('fade down');
                        checkResults();
                        setTimeout(function () {
                            updateRequests();
                        }, 500);
                    },
                    error: (error) => {
                        console.error('ERROR: could not decline request: ' + error);
                    }
                });
            }
        }).modal('show');
    });
}

function checkResults() {
    numberOfElements--;

    if (numberOfElements !== 0) {
        return;
    }

    $('#contentDiv').html(`
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
                `);
}


$(document).ready(() => {

    $('#role-dropdown').dropdown({
        onChange: function (value) {
            if (value == '') {
                sortArray[0] = 'id';
                sortArray[1] = 'desc';
                updateRequests();
            }
        }
    });

    approve();
    decline();

});

function formatDate(dateObj) {
    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"];
    const month = monthNames[dateObj.getMonth()].toString().slice(0, 3);
    const day = String(dateObj.getDate()).padStart(2, '0');
    const year = dateObj.getFullYear();
    const hour = dateObj.getHours();
    const minute = dateObj.getMinutes();
    return month + ' ' + day + ', ' + year + ' ' + hour + ':' + (minute < 10 ? '0' : '') + minute;
}