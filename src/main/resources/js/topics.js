$(document).ready(() => {
    $('#category-input').on('click', () => $('#filter-form').submit());


    $.ajax({
        type: 'POST',
        url: '/topics/get-all-tags',
        success: response => {
            let values = [];

            response.forEach(item => {
                values.push({
                    name: item.name,
                    value: item.name
                });
            });

            $('#tag-input').dropdown({
                allowAdditions: true,
                forceSelection: false,
                maxSelections: 5,
                values: values
            });
        },
        error: (error) => {
            console.error('ERROR: could not retrieve tags: ' + error);
        }
    });
});