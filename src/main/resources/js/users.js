$(document).ready(() => {
    $('#sort-input').on('change', () => $('#filter-form').submit());

    $('.change-roles-button').on('click', function () {
        $.ajax({
            url: '/users/get-user/' + $(this).data().value,
            type: 'POST',
            success: function (response) {
                initializeChangeRolesModal(response);
            },
            error: (error) => {
                console.error('ERROR: could not retrieve user: ' + error);
            }
        });
    });

    function initializeChangeRolesModal(user) {
        $.ajax({
            url: '/users/get-roles/',
            type: 'POST',
            success: function (roles) {
                $('#change-roles-form').attr('action', '/users/change-roles/' + user.id);
                $('#user-name').html(user.firstName + ' ' + user.lastName);
                $('#user-email').html(user.email);
                let values = [];
                let userRoles = user.roles.map(item => item.name);

                roles.forEach(role => {
                    const isSelected = userRoles.includes(role.name);
                    values.push({
                        name: role.name,
                        value: role.name,
                        selected: isSelected
                    });
                });

                console.log(values)

                $('#roles-input').dropdown({
                    values: values,
                    onChange: function (value) {
                        if (value === '') {
                            $('#change-roles-submit-button').addClass('disabled');
                        } else {
                            $('#change-roles-submit-button').removeClass('disabled')
                        }
                    }
                });

                // Show modal
                $('#change-roles-modal').modal({
                    inverted: true,
                    observeChanges: true,
                    closable: false,
                    onApprove: () => false
                }).modal('show');
            },
            error: (error) => {
                console.error('ERROR: could not retrieve user roles: ' + error);
            }
        });

    }
});
