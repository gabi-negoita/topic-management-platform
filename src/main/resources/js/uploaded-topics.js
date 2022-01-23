$(document).ready(() => {
    let createTopicButton = $('#create-topic-button');
    let deleteTopicButton = $('.delete-topic-button');
    let editTopicButton = $('.edit-topic-button');
    let removeStudentButton = $('.remove-student-button');
    let allTags = [];

    // Get current user tags
    $.ajax({
        type: 'POST',
        url: '/uploaded-topics/get-current-user-tags',
        success: response => {
            const queryString = window.location.search;
            const urlParams = new URLSearchParams(queryString);
            const hasTags = urlParams.has('tags');
            let tags = hasTags ? urlParams.get('tags').split(',') : undefined;
            let values = [];

            response.forEach(item => {
                values.push({
                    name: item.name,
                    value: item.name,
                    selected: (tags !== undefined && tags.includes(item.name))
                });
            });

            $('#tag-input').dropdown({values: values});
        },
        error: (error) => {
            console.error('ERROR: could not retrieve tags: ' + error);
        }
    });

    // Get all tags
    $.ajax({
        type: 'POST',
        url: '/uploaded-topics/get-all-tags',
        success: response => {
            response.forEach(tag => {
                allTags.push(tag.name);
            });
        },
        error: (error) => {
            console.error('ERROR: could not retrieve tags: ' + error);
        }
    });

    createTopicButton.on('click', () => {
        let tagDropdownValues = [];

        allTags.forEach(tag => {
            tagDropdownValues.push({
                name: tag,
                value: tag
            });
        });

        $('#create-topic-tag-input').dropdown({
            allowAdditions: true,
            forceSelection: false,
            maxSelections: 5,
            values: tagDropdownValues
        });

        $('.create-topic-modal').modal({
            inverted: true,
            observeChanges: true,
            closable: false,
            onApprove: () => false
        }).modal('show');
    });

    deleteTopicButton.on('click', function () {
        const topicId = $(this).data().value;
        $('#delete-topic-form').attr('action', '/uploaded-topics/delete-topic/' + topicId);

        const deleteTopicSubmitButton = $('#delete-topic-submit-button');
        $('#delete-topic-checkbox').checkbox({
            onChecked: () => deleteTopicSubmitButton.removeClass('disabled'),
            onUnchecked: () => deleteTopicSubmitButton.addClass('disabled')
        });

        $('#delete-topic-modal').modal({
            inverted: true,
            observeChanges: true,
            closable: false,
            onApprove: () => false
        }).modal('show');
    });

    editTopicButton.on('click', function () {
        const topicId = $(this).data().value;
        $('#edit-topic-form').attr('action', '/uploaded-topics/edit-topic/' + topicId);

        $.ajax({
            type: 'POST',
            url: '/uploaded-topics/get-topic/' + topicId,
            success: response => {
                let editTopicTagInputDropdown = $('#edit-topic-tag-input');
                let topicTagNames = [];
                let tagDropdownValues = [];

                response.tags.forEach(tag => {
                    topicTagNames.push(tag.name);
                });

                allTags.forEach(tag =>
                    tagDropdownValues.push({
                        name: tag,
                        value: tag,
                        selected: topicTagNames.includes(tag)
                    })
                );

                console.log(tagDropdownValues);

                $('#edit-topic-title').val(response.title);
                $('#edit-topic-category').dropdown('set selected', response.category.name);
                $('#edit-topic-description').val(response.description);

                editTopicTagInputDropdown.dropdown('clear');
                editTopicTagInputDropdown.dropdown({
                    allowAdditions: true,
                    forceSelection: false,
                    maxSelections: 5,
                    values: tagDropdownValues
                });

                $('#edit-topic-modal').modal({
                    inverted: true,
                    observeChanges: true,
                    closable: false,
                    onApprove: () => false
                }).modal('show');
            },
            error: (error) => {
                console.error('ERROR: could not retrieve topic: ' + error);
            }
        });
    });

    removeStudentButton.on('click', function () {
        const data = $(this).data().value;
        const topicId = data.split('-')[0];
        const studentId = data.split('-')[1];

        $('#remove-student-form').attr('action', '/uploaded-topics/remove-student/' + topicId + "/" + studentId);

        const removeStudentSubmitButton = $('#remove-student-submit-button');
        $('#remove-student-checkbox').checkbox({
            onChecked: () => removeStudentSubmitButton.removeClass('disabled'),
            onUnchecked: () => removeStudentSubmitButton.addClass('disabled')
        });

        $('#remove-student-modal').modal({
            inverted: true,
            observeChanges: true,
            closable: false,
            onApprove: () => false
        }).modal('show');
    });

    $('.disabled-delete-button').popup({
        position: 'top center',
        content: 'This topic has students assigned to it and hence it cannot be deleted',
        delay: {
            show: 25,
            hide: 25
        }
    });
});