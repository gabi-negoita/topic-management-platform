$(document).ready(() => {
    $('.delete-comment-button').on('click', function () {
        const topicId = $(this).data().value.split('-')[0];
        const commentId = $(this).data().value.split('-')[1];
        $('#delete-comment-form').attr('action', '/topics/' + topicId + '/delete-comment/' + commentId);

        $('#delete-comment-modal').modal({
            inverted: true,
            observeChanges: true,
            onApprove: () => false
        }).modal('show');
    });

    $('#apply-request-button').on('click', function () {
        $('#apply-request-modal').modal({
            inverted: true,
            observeChanges: true,
            onApprove: () => false
        }).modal('show');
    });

    $('#cancel-request-button').on('mouseover', function () {
        $(this)
            .removeClass('orange')
            .addClass('red');

        $('#cancel-request-button-icon')
            .removeClass('spinner loading')
            .addClass('remove circle');
    }).on('mouseout', function () {
        $(this).removeClass('red');
        $(this).addClass('orange');

        $('#cancel-request-button-icon')
            .removeClass('remove circle')
            .addClass('spinner loading');
    }).on('click', function () {
        $('#cancel-request-modal').modal({
            inverted: true,
            observeChanges: true,
            onApprove: () => false
        }).modal('show');
    });

    $('#cancel-application-button').on('mouseover', function () {
        $(this)
            .removeClass('green')
            .addClass('red');

        $('#cancel-application-button-icon')
            .removeClass('check circle')
            .addClass('remove circle');
    }).on('mouseout', function () {
        $(this).removeClass('red');
        $(this).addClass('green');

        $('#cancel-application-button-icon')
            .removeClass('remove circle')
            .addClass('check circle');
    }).on('click', function () {

        const cancelApplicationSubmitButton = $('#cancel-application-submit-button');
        $('#cancel-application-checkbox').checkbox({
            onChecked: () => cancelApplicationSubmitButton.removeClass('disabled'),
            onUnchecked: () => cancelApplicationSubmitButton.addClass('disabled')
        });

        $('#cancel-application-modal').modal({
            inverted: true,
            observeChanges: true,
            onApprove: () => false
        }).modal('show');
    });

    $('.edit-comment-button').on('click', function () {
        const topicId = $(this).data().value.split('-')[0];
        const commentId = $(this).data().value.split('-')[1];
        $('#edit-comment-form').attr('action', '/topics/' + topicId + '/edit-comment/' + commentId);

        $.ajax({
            type: 'POST',
            url: '/topics/get-comment/' + commentId,
            success: function (response) {
                $('#edit-comment-textarea').html(response);

                $('#edit-comment-modal').modal({
                    inverted: true,
                    closable: false,
                    observeChanges: true,
                    onApprove: () => false
                }).modal('show');
            },
            error: (error) => {
                console.error('ERROR: could not retrieve comment: ' + error);
            }
        });
    });
});