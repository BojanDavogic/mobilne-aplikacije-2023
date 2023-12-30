const express = require('express');
const http = require('http');
const socket = require('socket.io');
const app = express();
const port = 3000;

const server = http.createServer(app);
const io = socket(server, {
    cors: {
        origin: '*',
        methods: ['GET', 'POST']
    }
});

app.get('/', function (req, res) {
    res.send('Server is running');
});

// pokretanje servera
server.listen(port, '192.168.170.96', () => {
    console.log(`Server running on port ${port}`);
});

// podaci za igru
let players = [];
let playerSocket = {};
let scores = {};
let currentTurn = 0;
let koZnaZnaAnswers = [];
let switcher = 0;
let inverter = 0;

app.use(express.static('public'));

io.on('connection', (socket) => {
    console.log('A user connected with socket id: ' + socket.id);

    socket.on('joinGame', (playerName) => {
        // Provjeri da li je korisnik već u igri
        const isPlayerAlreadyJoined = players.includes(playerName);

        if (!isPlayerAlreadyJoined && players.length < 2) {
            players.push(playerName);
            playerSocket[socket.id] = playerName;
            scores[playerName] = 0;
            console.log('Player joined: ' + playerName + '. Total players: ' + players.length);
            io.emit('playerJoined', playerName);

            if (players.length === 2) {
                io.emit('startGame', playerSocket);
            }
        } else {
            // Korisnik je već u igri, možete poslati odgovor da ne može ponovo pristupiti
            socket.emit('cannotJoinGame', 'You are already in the game.');
            console.log('You are already in the game.');
        }
    });


    // kod za sve igre
    socket.on('getTurn', () => {
        const currentPlayerIndex = currentTurn % 4 < 2 ?
            (currentTurn % 2 === 0 ? 0 + inverter : 1 - inverter) :
            (currentTurn % 4 === 2 ? 0 + inverter : 1 - inverter);
        const currentPlayer = players[currentPlayerIndex];

        io.emit('turn', currentPlayer);
        console.log(currentPlayer + ' turn');

        setTimeout(() => {
            currentTurn++;
        }, 1000);
    });

    socket.on('resetTurn', () => {
        currentTurn = 0;
    });

    socket.on('getSwitcher', () => {
        io.emit('switcher', ++switcher);
    });

    socket.on('resetSwitcher', () => {
        switcher = 0;
    });

    socket.on('invert', () => {
        if (inverter === 0)
            inverter = 1;
        else
            inverter = 0;
    });


    // asocijacije
    socket.on('asocijacijeOpened', (field) => {
        io.emit('asocijacijeOpen', field);
    });

    socket.on('asocijacijeSolution', (solved) => {
        io.emit('asocijacijeSolved', solved);
    });

    socket.on('asocijacijeScoreUpdate', (player1, score1, player2, score2) => {
        scores[player1] = score1;
        scores[player2] = score2;

        io.emit('scoreUpdate', scores);
        console.log(scores);
    });

    socket.on('endAsocijacije', () => {
        io.emit('endAsocijacije');
        console.log('End of asocijacije');
    });

    // diskonekcija
    socket.on('disconnect', () => {
        console.log('A user disconnected');

        const name = playerSocket[socket.id];
        const index = players.indexOf(name);
        players.splice(index, 1);
        delete scores[name];

        io.emit('playerLeft', name);
    });
});