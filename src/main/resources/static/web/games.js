let url = '/api/games'
fetchJson(url);
let gameGrid = {
  horizontal: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
  vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]
}
buildGameTable(gameGrid)

function getHeadersHtml() {
  return "<tr><th>Game</th><th>Date</th></tr>";
}

function renderHeaders(data) {
  var html = getHeadersHtml(data);
  document.getElementById("table-headers").innerHTML = html;
}

function getColumnsHtml(row) {
  return row.map(function (element) {
    return "<td>" + element.id + "</td><td>" + element.created + "</td>";
  }).join("")
}

function getRowsHtml(data) {
  return data.map(element => {
    return "<tr><td>" + element.id + "</td><td>" + element.created + "</td></tr>"
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

      console.log('test')
      renderTable(json)
    })
    .catch(function (error) {
      console.log(error.message);
    });
}

function renderTable(data) {
  renderHeaders(data);
  renderRows(data);
}