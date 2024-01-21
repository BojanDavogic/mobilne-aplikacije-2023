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
server.listen(port, '192.168.0.19', () => {
    console.log(`Server running on port ${port}`);
});

class KoZnaZnaAnswer {
    constructor(correct, timeOf, player) {
        this.correct = correct;
        this.timeOf = timeOf;
        this.player = player;
    }
}

// podaci za igru
let igraci = [];
let igraciSocket = {};
let rezultati = {};
let trenutniPotez = 0;
let koZnaZnaOdogovori = [];
let switcher = 0;
let inverter = 0;

app.use(express.static('public'));

io.on('connection', (socket) => {
    console.log('A user connected with socket id: ' + socket.id);

    socket.on('joinGame', (playerName) => {
        const isPlayerAlreadyJoined = igraci.includes(playerName);

        if (!isPlayerAlreadyJoined && igraci.length < 2) {
            igraci.push(playerName);
            igraciSocket[socket.id] = playerName;
            rezultati[playerName] = 0;
            console.log('Player joined: ' + playerName + '. Total players: ' + igraci.length);
            if (igraci.length === 2) {
                io.emit('playerJoined', igraci[0], igraci[1]);
            }

            if (igraci.length === 2) {
                io.emit('startGame', igraciSocket);
            }
        } else {
            socket.emit('cannotJoinGame', 'You are already in the game.');
            console.log('You are already in the game.');
        }
    });


    // kod za sve igre
    socket.on('getTurn', () => {
        const currentPlayerIndex = (trenutniPotez + inverter) % 2;
        const currentPlayer = igraci[currentPlayerIndex];

        io.emit('turn', currentPlayer);
        console.log(currentPlayer + ' turn');

        trenutniPotez++;
    });

    socket.on('resetTurn', () => {
        trenutniPotez = 0;
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

    // koZnaZna

    socket.on('koZnaZnaAnswer', (correct, timeOf, player) => {
        let answer = new KoZnaZnaAnswer(correct, timeOf, player);
        koZnaZnaOdogovori.push(answer);
    });

    socket.on('koZnaZnaAnswerCheck', async () => {
        console.log("Usli smo u check");
        console.log(JSON.stringify(koZnaZnaOdogovori));
        if (koZnaZnaOdogovori.length === 2) {
            console.log("ODGOVORI: " + JSON.stringify(koZnaZnaOdogovori));
            let answer1 = koZnaZnaOdogovori[0];
            let answer2 = koZnaZnaOdogovori[1];

            if ((answer1.correct && answer2.correct && answer1.timeOf < answer2.timeOf)
                || (answer1.correct && !answer2.correct)) {
                rezultati[answer1.player] += 10;
            } else if (answer2.correct && answer1.correct && answer2.timeOf < answer1.timeOf
                || (answer2.correct && !answer1.correct)) {
                rezultati[answer2.player] += 10;
            }

            if (!answer1.correct) {
                rezultati[answer1.player] -= 5;
            }

            if (!answer2.correct) {
                rezultati[answer2.player] -= 5;
            }

            console.log("REZ. 2 igraca");
            console.log("REZULTATI: " + JSON.stringify(rezultati));
            koZnaZnaOdogovori = [];
        } else if (koZnaZnaOdogovori.length === 1) {
            console.log("ODGOVOR: " + JSON.stringify(koZnaZnaOdogovori));
            let answer1 = koZnaZnaOdogovori[0];

            if (answer1.correct) {
                rezultati[answer1.player] += 10;
            }

            if (!answer1.correct) {
                rezultati[answer1.player] -= 5;
            }

            console.log("REZ. 1 igrac");
            console.log("REZULTATI: " + JSON.stringify(rezultati));
            koZnaZnaOdogovori = [];
        }
        io.emit('scoreUpdate', rezultati);
    });

    // spojnice

    socket.on('spojniceParovi', (prvoDugme, drugoDugme, tacnost) => {
        console.log(prvoDugme);
        console.log(drugoDugme);
        console.log(tacnost);
        io.emit('spojniceAzuriraj', prvoDugme, drugoDugme, tacnost);
    });

    socket.on("zavrsenPotez", (tacnost) => {
        io.emit("trenutnoStanjeIgre", tacnost);
    });

    socket.on('spojniceSolution', (solved) => {
        io.emit('spojniceUpdate', solved);
    });

    socket.on('spojniceScoreUpdate', (player1, score1, player2, score2) => {
        rezultati[player1] = score1;
        rezultati[player2] = score2;

        io.emit('scoreUpdate', rezultati);
        console.log(rezultati);
    });

    socket.on('endSpojnice', () => {
        io.emit('endSpojnice');
        console.log('End of spojnice');
    });

    // asocijacije

    socket.on('asocijacijeOpened', (field) => {
        io.emit('asocijacijeOpen', field);
        console.log("Field opened: " + field);
    });

    socket.on('asocijacijeSolution', (polje, resenjeKolone) => {
        io.emit('asocijacijeSolved', polje, resenjeKolone);
    });

    socket.on('asocijacijeKonacnoResenje', (field) => {
        io.emit('asocijacijeKonacno', field);
    });

    socket.on('asocijacijeScoreUpdate', (igrac1, rezultat1, igrac2, rezultat2) => {
        rezultati[igrac1] = rezultat1;
        rezultati[igrac2] = rezultat2;

        io.emit('scoreUpdate', rezultati);
        console.log(rezultati);
    });

    socket.on('endAsocijacije', () => {
        io.emit('endAsocijacije');
        console.log('End of asocijacije');
    });

    // diskonekcija
    socket.on('disconnect', () => {
        console.log('A user disconnected');

        const name = igraciSocket[socket.id];
        const index = igraci.indexOf(name);
        igraci.splice(index, 1);
        delete rezultati[name];

        io.emit('playerLeft', name);
    });
});