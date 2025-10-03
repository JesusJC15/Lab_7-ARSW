const AppController = (function () {
    
    let author = null;
    let blueprints = [];

    let client = apimock;

    function setBlueprints(bps) {
        blueprints = bps.map(bp => ({
            name: bp.name,
            pointsCount: bp.points.length
        }));
    }

    function updateTable() {
        let tbody = $("#blueprintsTable tbody");
        tbody.empty();

        blueprints.forEach(bp => {
            let row = $("<tr></tr>");
            row.append(`<td>${bp.name}</td>`);
            row.append(`<td>${bp.pointsCount}</td>`);

            let btn = $(`<button class="btn btn-primary btn-sm">Open</button>`);
            btn.click(function () {
                AppController.drawBlueprintByName(author, bp.name);
            });
            let tdBtn = $("<td></td>").append(btn);
            row.append(tdBtn);

            tbody.append(row);
        });

        let total = blueprints.reduce((sum, bp) => sum + bp.pointsCount, 0);
        $("#pointsSum").text(total);
    }


    function drawBlueprint(points) {
        const canvas = $("#blueprintCanvas")[0];
        if (!canvas) return;
        const ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height); 

        if (!points || points.length === 0) return;

        ctx.beginPath();
        ctx.moveTo(points[0].x, points[0].y);

        points.slice(1).forEach(p => {
            ctx.lineTo(p.x, p.y);
        });

        ctx.strokeStyle = "blue";
        ctx.lineWidth = 2;
        ctx.stroke();
    }
    
    return {
        
        setClient: function (newClient) {
            client = newClient;
        }, 
        
        setAuthor: function (newAuthor) {
            author = newAuthor;
        },

        getAuthor: function () {
            return author;
        },

        setBlueprints: function (bps) {
            setBlueprints(bps);
        },

        getBlueprints: function () {
            return [...blueprints]; 
        },

        getTotalPoints: function () {
            return blueprints.reduce((sum, bp) => sum + bp.pointsCount, 0);
        },

        updateBlueprintsFromAuthor: function (authname) {
            author = authname;
            $("#authorName").text(authname); 

            apimock.getBlueprintsByAuthor(authname, function (bps) {
                setBlueprints(bps); 
                updateTable();      
            });
        },

        drawBlueprintByName: function (authname, bpname) {
            apimock.getBlueprintsByNameAndAuthor(authname, bpname, function (bp) {
                if (!bp) return;

                if ($("#currentBlueprint").length === 0) {
                    $("<h3 id='currentBlueprint'></h3>").insertBefore("#blueprintCanvas");
                }
                $("#currentBlueprint").text(`Drawing blueprint: ${bp.name}`);

                drawBlueprint(bp.points);
            });
        },

        reset: function () {
            author = null;
            blueprints = [];
        }
    };
})();
