let url = '/api/game_view/'
let gamePlayerId = new URLSearchParams(window.location.search).get("gp");
let shipLocations = []
let gameGrid = {
    horizontal: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
    vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]
}
fetchJson(url);

function setPlayer(player) {
    document.getElementById("playerName").innerHTML = player
}

function fetchJson(url) {
    return fetch(url + gamePlayerId).then(function (response) {
            if (response.ok) {
                return response.json();
            }
        }).then(function (json) {

            console.log(json)
            json.ships.forEach(e => shipLocations = shipLocations.concat(e.locations))
            console.log(shipLocations)
            setPlayer(json.player.name)
            buildGameTable(gameGrid)

        })
        .catch(error => {
            console.log(error.message);
        });
}


function buildGameTable(gameGrid) {
    let thead = document.getElementById("game-table-header")
    let th0 = document.createElement('th')
    th0.innerHTML = '<b>#</b>'
    thead.appendChild(th0)
    gameGrid.horizontal.forEach(element => {
        let th = document.createElement('th')
        th.innerHTML = '<b>' + element + '</b>'
        thead.appendChild(th)
    });
    let tbody = document.getElementById("game-table")
    for (let i in gameGrid.vertical) {
        let tr = document.createElement('tr')
        let tdhead = document.createElement('td')
        tdhead.innerHTML = '<b>' + gameGrid.vertical[i] + '</b>'
        tr.appendChild(tdhead)
        for (let j in gameGrid.horizontal) {
            let td = document.createElement('td')
            if (shipLocations.includes(gameGrid.vertical[i] + gameGrid.horizontal[j])) {
                td.className = "text-danger bg-danger"
            }
            td.innerHTML = gameGrid.vertical[i] + gameGrid.horizontal[j]
            td.setAttribute('id', gameGrid.vertical[i] + gameGrid.horizontal[j])
            tr.appendChild(td)
        }
        tbody.appendChild(tr)
    }
}