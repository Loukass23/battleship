new Vue({
    el: '#app',
    data: {
      url: '/api/games',
      loggedUser: null,
      games: [],
      players:[]
    },
    computed: {
      test: function () {
        return this.loggedUser }
      },
    methods: {
      fetchJson(url) {
        axios
                .get(url)
                .then(response => {
                    console.log(response.data)
                    this.games = response.data.games
                    this.loggedUser = response.data.player
                    this.setPlayers();
                })
                .catch(error => console.log(error))
      },
      setPlayers(){
        this.games.forEach((element, index) => {
          let player1
          let player2;
          element.gamePlayers[0]? player1 = element.gamePlayers[0].player.name : player1 = '-'
          element.gamePlayers[1]? player2 = element.gamePlayers[1].player.name : player2 = '-'
          this.players.push({game:index , player_1 : player1 , player_2: player2 })
        });
       
      },
      selectGame(gameID){
        window.location.href = 'game.html?gp='+ gameID
      },
      logout() {

        $.post("/api/logout")
          .done(()=> this.loggedUser = null)
          .fail();
      },
      login() {

        var form = document.getElementById('login-form');
      
        $.post("/api/login", {
            username: form["username"].value,
            password: form["password"].value
          })
          .done(function () {
            console.log("logged in!");
      
      
      
          })
          .fail(function () {
            console.log("failed to log in!");
          });
        },
        addGame() {
        if(this.loggedUser){
          $.post("/api/games", {
              playerName: this.loggedUser.username
            })
            .done(function () {
              console.log("Game added");
              location.reload();
        
        
            })
            .fail(function () {
              console.log("failed to add player");
            });
          }
          else alert('You must be logged in to add games!')
        }
    },
  
    created() {
      this.fetchJson(this.url)
  
    }
  });