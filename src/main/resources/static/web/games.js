let url = '/api/games'
fetchJson(url);

function fetchJson(url) {
  return fetch(url).then(function (response) {
      if (response.ok) {
        return response.json();
      }
    }).then(function (json) {

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






function getHeadersHtml() {
  return "<tr><th>Game</th><th>Date</th><th>Player 1</th><th>Player 2</th></tr>";
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