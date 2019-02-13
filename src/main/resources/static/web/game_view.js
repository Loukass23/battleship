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
        opponentSalvoesLocations: [],
        readytoPlay: false,
        fleetReady: false,
        shipFleet: {
            destroyerS: {
                type: "Destroyer",
                set: false,
                origine: "",
                locations: "",
                horizontal: true,
                size: 2
            },
            carrierS: {
                type: "Carrier",
                set: false,
                origine: "",
                locations: "",
                horizontal: true,
                size: 5
            },
            submarineS: {
                type: "Submarine",
                set: false,
                origine: "",
                locations: "",
                horizontal: true,
                size: 3
            },
            cruiserS: {
                type: "Cruiser",
                set: false,
                origine: "",
                locations: "",
                horizontal: true,
                size: 3
            },
            battleshipS: {
                type: "Battleship",
                set: false,
                origine: "",
                locations: "",
                horizontal: true,
                size: 4
            },

        }
    },
    methods: {
        fetchJson(url) {
            axios
                .get(url)
                .then(response => {
                    console.log(response.data)
                    response.data.ships.forEach(e => this.shipLocations = this.shipLocations.concat(e.locations))
                    this.setShips(response.data.ships)
                    response.data.salvoes.forEach(e => this.salvoesLocations = this.salvoesLocations.concat(e))
                    response.data.opponentSalvoes.forEach(e => this.opponentSalvoesLocations = this.opponentSalvoesLocations.concat(e))
                    //this.buildGameGrid(this.gameGrid, 'fleet-grid')
                    //this.buildGameGrid(this.gameGrid, 'fired-grid')
                    this.loggedUser = response.data.player
                    this.gamePlayerId = response.data.id
                    console.log(this.shipLocations)
                    console.log(this.salvoesLocations)
                    //this.showShips()

                })
                .catch(error => console.log(error))
        },
        setShips(ships) {
            ships.forEach(ship => {

                switch (ship.type) {
                    case "Destroyer":
                        this.shipFleet.destroyerS.set = true
                        this.shipFleet.destroyerS.locations = ship.locations
                        this.shipFleet.destroyerS.origine = ship.locations[0]

                        break;
                    case "Submarine":
                        this.shipFleet.submarineS.set = true
                        this.shipFleet.submarineS.origine = ship.locations[0]
                        this.shipFleet.submarineS.locations = ship.locations
                        break;
                    case "Carrier":
                        this.shipFleet.carrierS.set = true
                        this.shipFleet.carrierS.origine = ship.locations[0]
                        this.shipFleet.carrierS.locations = ship.locations
                        break;
                    case "Battleship":
                        this.shipFleet.battleshipS.set = true
                        this.shipFleet.battleshipS.origine = ship.locations[0]
                        this.shipFleet.battleshipS.locations = ship.locations
                        break;
                    case "Cruiser":
                        this.shipFleet.cruiserS.set = true
                        this.shipFleet.cruiserS.origine = ship.locations[0]
                        this.shipFleet.cruiserS.locations = ship.locations
                        break;
                }

            })
            //if (ships.length = 5) this.readytoPlay = true

        },
        logout() {

            $.post("/api/logout")
                .done(() => this.loggedUser = null)
                .fail();
        },
        addShip() {
            for (let key in this.shipFleet) {
                $.post({
                        url: "/api/games/players/" + this.gamePlayerId + "/ships",
                        data: JSON.stringify(
                            [{
                                "type": this.shipFleet[key].type,
                                "locations": this.shipFleet[key].locations
                            }]
                        ),
                        dataType: "text",
                        contentType: "application/json"
                    })
                    .done(function () {
                        this.readytoPlay = true
                        location.reload();

                    })
                    .fail(function () {
                        console.log("fail to add ship")
                    })
            }
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


                    // if (gridType == 'fleet-grid') {

                    //     this.isShip(cellId) ? column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0 bg-primary' : column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0'
                    //     if (this.isHit(cellId)) {
                    //         column.innerHTML = this.isHit(cellId)
                    //         column.className = 'col-sm-1 border px-0 py-0 mx-0 my-0 bg-danger'
                    //     }
                    // }
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
        },
        allowDrop: function (event) {
            event.preventDefault();
        },

        drag: function (event) {
            event.dataTransfer.setData("text", event.target.id);

        },
        drop: function (event, row, cell) {
            event.preventDefault();

            var data = event.dataTransfer.getData("text");
            event.preventDefault();
            event.target.appendChild(document.getElementById(data));

            switch (data) {
                case "Destroyer":
                    this.shipFleet.destroyerS.locations = this.setLocations(row, cell, this.shipFleet.destroyerS.size, this.shipFleet.destroyerS.horizontal)
                    this.shipFleet.destroyerS.set = true
                    break;
                case "Submarine":
                    this.shipFleet.submarineS.locations = this.setLocations(row, cell, this.shipFleet.submarineS.size, this.shipFleet.submarineS.horizontal)
                    this.shipFleet.submarineS.set = true
                    break;
                case "Carrier":
                    this.shipFleet.carrierS.locations = this.setLocations(row, cell, this.shipFleet.carrierS.size, this.shipFleet.carrierS.horizontal)
                    this.shipFleet.carrierS.set = true
                    break;
                case "Battleship":
                    this.shipFleet.battleshipS.locations = this.setLocations(row, cell, this.shipFleet.battleshipS.size, this.shipFleet.battleshipS.horizontal)
                    this.shipFleet.battleshipS.set = true
                    break;
                case "Cruiser":
                    this.shipFleet.cruiserS.locations = this.setLocations(row, cell, this.shipFleet.cruiserS.size, this.shipFleet.cruiserS.horizontal)
                    this.shipFleet.cruiserS.set = true
                    break;
            }
            //this.addShip(data, position)
            let count = 0;
            for (let key in this.shipFleet) {
                if (this.shipFleet[key].set == true) count++
            }
            if (count == 5) this.fleetReady = true

        },
        setLocations(row, cell, size, horizontal) {
            let origineIndex
            let position = []
            if (horizontal) {
                origineIndex = this.gameGrid.horizontal.indexOf(cell)
                for (let i = 0; i < size; i++) {
                    position.push(row + this.gameGrid.horizontal[origineIndex + i])

                }



            } else {
                origineIndex = this.gameGrid.vertical.indexOf(row)
                for (let i = 0; i < size; i++) {
                    position.push(this.gameGrid.vertical[origineIndex + i] + cell)

                }
            }

            position.forEach(e => {
                if (!document.getElementById(e)) alert("Invalid ship position")
                this.isShip(e) ? document.getElementById(e).style.backgroundColor = "red" :
                    document.getElementById(e).style.backgroundColor = "blue"
            })
            return position
        },
        rotateDestroyer() {
            const des = document.getElementById('Destroyer')
            if (this.shipFleet.destroyerS.horizontal) {
                this.shipFleet.destroyerS.horizontal = false
                des.style.webkitTransform = "rotate(90deg) translateY(20px) translateX(25px)"
            } else {
                this.shipFleet.destroyerS.horizontal = true
                des.style.webkitTransform = "rotate(0deg) translateY(0px) translateX(0px)"
            }
        },
        rotateCarrier() {
            const des = document.getElementById('Carrier')
            if (this.shipFleet.carrierS.horizontal) {
                this.shipFleet.carrierS.horizontal = false
                des.style.webkitTransform = "rotate(90deg) translateY(75px) translateX(80px)"
            } else {
                this.shipFleet.carrierS.horizontal = true
                des.style.webkitTransform = "rotate(0deg) translateY(0px) translateX(0px)"
            }
        },
        rotateSubmarine() {
            const des = document.getElementById('Submarine')
            if (this.shipFleet.submarineS.horizontal) {
                this.shipFleet.submarineS.horizontal = false
                des.style.webkitTransform = "rotate(90deg) translateY(30px) translateX(40px)"
            } else {
                this.shipFleet.submarineS.horizontal = true
                des.style.webkitTransform = "rotate(0deg) translateY(0px) translateX(0px)"
            }
        },
        rotateCruiser() {
            const des = document.getElementById('Cruiser')

            if (this.shipFleet.cruiserS.horizontal) {
                des.style.webkitTransform = "rotate(90deg) translateY(30px) translateX(40px)"
                this.shipFleet.cruiserS.horizontal = false
            } else {
                this.shipFleet.cruiserS.horizontal = true
                des.style.webkitTransform = "rotate(0deg) translateY(0px) translateX(0px)"
            }
        },
        rotateBattleship() {
            const des = document.getElementById('Battleship')

            if (this.shipFleet.battleshipS.horizontal) {
                des.style.webkitTransform = "rotate(90deg) translateY(60px) translateX(60px)"
                this.shipFleet.battleshipS.horizontal = false
            } else {
                this.shipFleet.battleshipS.horizontal = true
                des.style.webkitTransform = "rotate(0deg) translateY(0px) translateX(0px)"
            }
        }

    },

    created() {
        const gpId = new URLSearchParams(window.location.search).get("gp")
        this.fetchJson(this.url + gpId)

    }
});