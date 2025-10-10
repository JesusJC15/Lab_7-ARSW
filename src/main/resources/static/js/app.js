const AppController = (function () {
    let author = null;
    let blueprints = [];
    let client = apiclient;
    let canvas = null;
    let ctx = null;
    let currentBlueprint = null;
    let isNewBlueprint = false;

    function setBlueprints(bps) {
        blueprints = bps.map(bp => ({
            name: bp.name,
            pointsCount: bp.points.length,
            points: bp.points
        }));
    }

    function updateTable() {
        const tbody = $("#blueprintsTable tbody");
        tbody.empty();

        blueprints.forEach(bp => {
            const row = $("<tr></tr>");
            row.append(`<td>${bp.name}</td>`);
            row.append(`<td>${bp.pointsCount}</td>`);

            const btn = $(`<button class="btn btn-primary btn-sm">Open</button>`);
            btn.click(() => AppController.drawBlueprintByName(author, bp.name));
            const tdBtn = $("<td></td>").append(btn);
            row.append(tdBtn);

            tbody.append(row);
        });

        const total = blueprints.reduce((sum, bp) => sum + bp.pointsCount, 0);
        $("#pointsSum").text(total);
    }

    function drawBlueprint(points) {
        if (!ctx) return;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        if (!points || points.length === 0) return;

        ctx.beginPath();
        ctx.moveTo(points[0].x, points[0].y);
        points.slice(1).forEach(p => ctx.lineTo(p.x, p.y));
        ctx.strokeStyle = "blue";
        ctx.lineWidth = 2;
        ctx.stroke();
    }

    function addPointToCurrentBlueprint(x, y) {
        if (!currentBlueprint) return;
        currentBlueprint.points.push({ x, y });
        drawBlueprint(currentBlueprint.points);

        const bp = blueprints.find(b => b.name === currentBlueprint.name);
        if (bp) bp.pointsCount = currentBlueprint.points.length;

        $("#pointsSum").text(
            blueprints.reduce((sum, b) => sum + b.pointsCount, 0)
        );
    }

    function initCanvasEvents() {
        canvas = document.getElementById("blueprintCanvas");
        if (!canvas) return;

        ctx = canvas.getContext("2d");

        canvas.replaceWith(canvas.cloneNode(true));
        canvas = document.getElementById("blueprintCanvas");
        ctx = canvas.getContext("2d");

        if (window.PointerEvent) {
            canvas.addEventListener("pointerdown", handlePointerDown);
        } else {
            canvas.addEventListener("mousedown", handleMouseDown);
        }
    }

    function handlePointerDown(event) {
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        addPointToCurrentBlueprint(x, y);
    }

    function handleMouseDown(event) {
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        addPointToCurrentBlueprint(x, y);
    }

    function saveOrUpdateBlueprint() {
        if (!author || !currentBlueprint) {
            alert("Please select an author and a blueprint first.");
            return;
        }

        console.log("Saving or updating blueprint:", currentBlueprint);

        let promise;

        if (isNewBlueprint) {
            promise = client.createBlueprint(currentBlueprint)
                .then(() => {
                    return new Promise((resolve, reject) => {
                        client.getBlueprintsByAuthor(author, function (bps) {
                            if (bps) resolve(bps);
                            else reject("Error getting blueprints after POST.");
                        });
                    });
                })
                .then((bps) => {
                    setBlueprints(bps);
                    updateTable();
                    isNewBlueprint = false;
                    alert("New blueprint created successfully");
                })
                .catch((err) => {
                    console.error("Error creating new blueprint:", err);
                    alert("Error creating new blueprint.");
                });
        } else {
            promise = client.updateBlueprint(author, currentBlueprint)
                .then(() => {
                    return new Promise((resolve, reject) => {
                        client.getBlueprintsByAuthor(author, function (bps) {
                            if (bps) resolve(bps);
                            else reject("Error getting blueprints after PUT.");
                        });
                    });
                })
                .then((bps) => {
                    setBlueprints(bps);
                    updateTable();
                    alert("Blueprint updated successfully");
                })
                .catch((err) => {
                    console.error("Error updating blueprint:", err);
                    alert("Error updating blueprint.");
                });
        }

        return promise;
    }

    function createNewBlueprint() {
        if (!author) {
            alert("Please set an author first.");
            return;
        }

        const name = prompt("Enter the name of the new blueprint:");
        if (!name) {
            alert("You must enter a valid name.");
            return;
        }

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        currentBlueprint = {
            author: author,
            name: name,
            points: []
        };

        $("#currentBlueprint").text(`New blueprint: ${name}`);

        isNewBlueprint = true;
    }

    function deleteCurrentBlueprint() {
        if (!author || !currentBlueprint) {
            alert("No author or blueprint selected for deletion.");
            return;
        }

        const confirmDelete = confirm(`Are you sure you want to delete "${currentBlueprint.name}"?`);
        if (!confirmDelete) return;

        console.log(`Deleting blueprint: ${currentBlueprint.name}`);

        client.deleteBlueprint(author, currentBlueprint.name)
            .then(() => {
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                $("#currentBlueprint").text("Select a blueprint to draw");
                currentBlueprint = null;

                return new Promise((resolve, reject) => {
                    client.getBlueprintsByAuthor(author, function (bps) {
                        if (bps) resolve(bps);
                        else reject("Error fetching blueprints after deletion.");
                    });
                });
            })
            .then((bps) => {
                setBlueprints(bps);
                updateTable();
                alert("Blueprint deleted successfully");
            })
            .catch((err) => {
                console.error("Error deleting blueprint:", err);
                alert("Error deleting blueprint.");
            });
    }

    return {
        setClient(newClient) { client = newClient; },
        setAuthor(newAuthor) { author = newAuthor; },
        getAuthor() { return author; },
        getBlueprints() { return [...blueprints]; },
        getTotalPoints() { return blueprints.reduce((sum, bp) => sum + bp.pointsCount, 0); },
        updateBlueprintsFromAuthor(authname) {
            author = authname;
            $("#authorName").text(authname);
            client.getBlueprintsByAuthor(authname, function (bps) {
                setBlueprints(bps);
                updateTable();
            });
        },
        drawBlueprintByName(authname, bpname) {
            client.getBlueprintsByNameAndAuthor(authname, bpname, function (bp) {
                if (!bp) return;
                $("#currentBlueprint").text(`Drawing blueprint: ${bp.name}`);
                currentBlueprint = JSON.parse(JSON.stringify(bp));
                drawBlueprint(currentBlueprint.points);
            });
        },
        reset() {
            author = null;
            blueprints = [];
            currentBlueprint = null;
        },
        initCanvasEvents,
        saveOrUpdateBlueprint,
        createNewBlueprint,
        deleteCurrentBlueprint
    };
})();