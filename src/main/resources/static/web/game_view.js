new Vue({
    el: '#app',
    data: {
        url: '/api/game_view/',
        loggedUser: null,
        gamePlayerId: null,
        gameGrid: {
            horizontal: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]
        },
        shipLocations: [],
        salvoesLocations: [],
        opponentSalvoesLocations: []

    },
    methods: {
        fetchJson(url) {
            axios
                .get(url)
                .then(response => {
                    console.log(response.data)
                    response.data.ships.forEach(e => this.shipLocations = this.shipLocations.concat(e.locations))
                    response.data.salvoes.forEach(e => this.salvoesLocations = this.salvoesLocations.concat(e))
                    response.data.opponentSalvoes.forEach(e => this.opponentSalvoesLocations = this.opponentSalvoesLocations.concat(e))
                    this.buildGameGrid(this.gameGrid, 'fleet-grid')
                    this.buildGameGrid(this.gameGrid, 'fired-grid')
                    this.loggedUser = response.data.player
                    this.gamePlayerId = response.data.id

                })
                .catch(error => console.log(error))
        },
        test() {
            console.log('test')
        },
        logout() {

            $.post("/api/logout")
                .done(() => this.loggedUser = null)
                .fail();
        },
        addShip() {
            console.log('ici')
            $.post({
                    url: "/api/games/players/" + this.gamePlayerId + "/ships",
                    data: JSON.stringify(
                        [{
                                "type": "test",
                                "locations": ["A1", "B1", "C1"]
                            },
                            {
                                "type": "test boat",
                                "locations": ["H5", "H6"]
                            }
                        ]
                    ),
                    dataType: "text",
                    contentType: "application/json"
                })
                .done(function () {
                    console.log("ship added ")
                })
                .fail(function () {
                    console.log("fail to add ship")
                })
        },
        buildGameGrid(gameGrid, gridType) {
            let fleetGrid = document.getElementById(gridType)

            let headerTop = document.createElement('div')
            headerTop.className = 'row text-center '

            let keyCell = document.createElement('div')
            keyCell.className = 'col-sm-1 border'
            keyCell.innerHTML = '#'
            headerTop.appendChild(keyCell)

            gameGrid.horizontal.forEach(element => {
                let cellTop = document.createElement('div')
                cellTop.className = 'col-sm-1 border'
                cellTop.innerHTML = '<b>' + element + '</b>'
                headerTop.appendChild(cellTop)
            })
            fleetGrid.appendChild(headerTop)

            for (let i in gameGrid.vertical) {
                let row = document.createElement('div')
                row.className = 'row text-center my-0'

                let headerLeft = document.createElement('div')
                headerLeft.className = 'col-sm-1 border '
                headerLeft.innerHTML = gameGrid.vertical[i]
                row.appendChild(headerLeft)

                for (let j in gameGrid.horizontal) {
                    let column = document.createElement('div')
                    let cellId = gameGrid.vertical[i] + gameGrid.horizontal[j]
                    column.setAttribute('id', cellId)

                    if (gridType == 'fleet-grid') {

                        this.isShip(cellId) ? column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0 bg-primary' : column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0'
                        if (this.isHit(cellId)) {
                            column.innerHTML = this.isHit(cellId)
                            column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0 bg-danger'
                        }
                    }
                    if (gridType == 'fired-grid') {
                        column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0'
                        if (this.isFired(cellId) != null) {
                            column.innerHTML = this.isFired(cellId)
                            column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0 bg-info'
                        }
                    }

                    row.appendChild(column)
                }
                fleetGrid.appendChild(row)

            }
        },
        isShip(cell) {
            return this.shipLocations.includes(cell)
        },
        isFired(cell) {
            let turn = null
            this.salvoesLocations.forEach(e => {
                if (e.salvoesLocations.includes(cell))
                    turn = e.turn
            })
            return turn
        },
        isHit(cell) {
            let turn = null
            this.opponentSalvoesLocations.forEach(e => {
                if (e.salvoesLocations.includes(cell))
                    turn = e.turn
            })
            return this.shipLocations.includes(cell) ? turn : null
        }

    },

    created() {
        const gpId = new URLSearchParams(window.location.search).get("gp")
        this.fetchJson(this.url + gpId)

    }
});