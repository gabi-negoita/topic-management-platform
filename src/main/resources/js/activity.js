$(document).ready(() => {

    const url = window.location.href;
    let currentUserId = parseInt(url.substring(url.lastIndexOf('/') + 1));
    let currentSessionUserId = $('#activity-chart').data().value;

    // Get user activity
    $.ajax({
        type: 'POST',
        url: '/activity/get-activity/' + (isNaN(currentUserId) ? currentSessionUserId : currentUserId),
        success: (response) => {
            let data = [];

            response.forEach(item => {
                data.push({
                    x: new Date(item.date),
                    y: item.actions
                });
            });

            renderActivityChart(data);
        },
        error: (error) => {
            console.error("ERROR: could not retrieve user activity: ");
            console.error(error);
        }
    });

    function renderActivityChart(data) {
        let options = {
            chart: {
                type: 'area',
                height: '100%'
            },
            series: [{
                name: 'Actions',
                data: data,
            }],
            xaxis: {
                type: 'datetime',
                labels: {
                    datetimeUTC: false
                }
            }
        };

        new ApexCharts(document.querySelector("#activity-chart"), options).render();
    }
});