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
        },

        updateBlueprint: function (author, blueprint, callback) {
            return $.ajax({
                url: `/blueprints/${author}/${blueprint.name}`,
                type: 'PUT',
                data: JSON.stringify(blueprint),
                contentType: "application/json"
            })
            .done(function () {
                console.info("Blueprint updated successfully");
                if (callback) callback();
            })
            .fail(function (xhr, status, error) {
                console.error("Error updating blueprint:", status, error);
                alert("Error updating blueprint");
            });
        },

        createBlueprint: function (blueprint, callback) {
            return $.ajax({
                url: `/blueprints`,
                type: 'POST',
                data: JSON.stringify(blueprint),
                contentType: "application/json"
            })
            .done(function () {
                console.info("Blueprint created successfully");
                if (callback) callback();
            })
            .fail(function (xhr, status, error) {
                console.error("Error creating blueprint:", status, error);
                alert("Error creating new blueprint");
            });
        },

        deleteBlueprint: function (author, bpname, callback) {
            return $.ajax({
                url: `/blueprints/${author}/${bpname}`,
                type: 'DELETE',
                contentType: "application/json"
            })
            .done(function () {
                console.info(`Blueprint ${bpname} deleted successfully`);
                if (callback) callback();
            })
            .fail(function (xhr, status, error) {
                console.error("Error deleting blueprint:", status, error);
                alert("Error deleting blueprint.");
            });
        },

    };

})();
