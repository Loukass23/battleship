let url = 'http://localhost:8080/api/games'
fetchJson(url);


function getHeadersHtml(data) {
    return "<tr><th>Game</th><th>Date</th></tr>";
}

function renderHeaders(data) {
  var html = getHeadersHtml(data);
  document.getElementById("table-headers").innerHTML = html;
}

function getColumnsHtml(row) {
  return row.map(function(element) {
    return "<td>" + element.id + "</td><td>"+ element.created + "</td>";
  }).join("")
}
function getRowsHtml(data) {
  return data.map(element => {
    return "<tr><td>" + element.id + "</td><td>"+ element.created + "</td></tr>"
    })
}

function renderRows(data) {
  var html = getRowsHtml(data);
  document.getElementById("table-rows").innerHTML = html;
}

function renderTable(data) {
  renderHeaders(data);
  renderRows(data);
}

function fetchJson(url) {

    return fetch(url).then(function (response) {
            if (response.ok) {
                return response.json();
            }
        }).then(function (json) {

            console.log(json)
            renderTable(json)



        })
        .catch(console.error("Server Error"));

}
/*
renderTable({"destination_addresses":["San Francisco, CA, USA","Victoria, BC, Canada"],"origin_addresses":["Vancouver, BC, Canada","Seattle, WA, USA"],
"rows":[{"elements":[{"distance":{"text":"1,528 km","value":1528361},"duration":{"text":"14 hours 47 mins","value":53236},"status":"OK"},{"distance":
{"text":"114 km","value":114166},"duration":{"text":"3 hours 10 mins","value":11415},"status":"OK"}]},{"elements":[{"distance":{"text":"1,300 km",
"value":1299975},"duration":{"text":"12 hours 21 mins","value":44447},"status":"OK"},{"distance":{"text":"172 km","value":171688},"duration":
{"text":"4 hours 37 mins","value":16602},"status":"OK"}]}],"status":"OK"});*/