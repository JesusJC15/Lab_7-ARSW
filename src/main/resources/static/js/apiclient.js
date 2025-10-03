const apiclient = (function () {

    return {

        getBlueprintsByAuthor: function (authname, callback) {
            $.get(`/blueprints/${authname}`, function (data) {
                callback(data);
            }).fail(function (xhr, status, error) {
                console.error("Error fetching blueprints:", status, error);
                callback([]); 
            });
        },

        getBlueprintsByNameAndAuthor: function (authname, bpname, callback) {
            $.get(`/blueprints/${authname}/${bpname}`, function (data) {
                callback(data);
            }).fail(function (xhr, status, error) {
                console.error("Error fetching blueprint:", status, error);
                callback(null);
            });
        }
    };

})();
