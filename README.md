# :uk: Tablut Challenge 2019

**Project for the "Foundations of Artificial Intelligence" course, Master Degree in Computer Engineering, University of Bologna**


This application has been developed for an AI challenge undertaken during the course.
It is a client implementation of an Ashton Tablut game player, based on a minmax algorithm and tree search.
The algorithm is an Iterative Deepening Search with Alpha-Beta cuts (from Aima 3.0.0 library).


The main class is src/it/unibo/ai/clientImplementation/MyTablutClient. It accepts two arguments:
- the colour you want to play (black or white)
- the maximum time for searching in the space of states (in seconds)


Create the jar archive and run the player after having started the game server.
```
java -jar MyTablutClient.jar white 60
```


The server can be found [here](https://github.com/AGalassi/TablutCompetition).


Additional client versions can be found in src/it/unibo/ai/didattica/competition/tablut/client:
- random player
- human player (insert moves from command line)


Project developed by [L. Perugini](https://github.com/leonardoperu), [M. Rossi](https://github.com/smartisrossi), [M. Minerva](https://github.com/mminerva), D. Altena. :it:


# :it: Tablut Challenge 2019

**Progetto per il corso "Fondamenti di Intelligenza Artificiale", Laurea Magistrale in Ingegneria Informatica, Università di Bologna**


Questa applicazione è stata sviluppata per una competizione di IA svolta durante il corso.
Il progetto è un client che implementa un giocatore di Ashton Tablut, basato sulla ricerca nello spazio degli stati con algoritmo minmax. L'algoritmo utilizzato è una ricerca iterativa in profondità (Iterative Deepening Search) con tagli Alfa-Beta, presente nella libreria Aima 3.0.0.


La classe main è src/it/unibo/ai/clientImplementation/MyTablutClient. Accetta due argomenti:
- il colore con cui si vuole giocare (black o white)
- il tempo massimo per la ricerca nello spazio degli stati (in secondi)


Creare il jar ed eseguire dopo aver avviato il server:
```
java -jar MyTablutClient.jar white 60
```


Il server può essere trovato [qui](https://github.com/AGalassi/TablutCompetition).


Sono disponibili altri client in src/it/unibo/ai/didattica/competition/tablut/client:
- random player
- human player (mosse selezionabili da linea di comando)


Progetto sviluppato da [L. Perugini](https://github.com/leonardoperu), [M. Rossi](https://github.com/smartisrossi), [M. Minerva](https://github.com/mminerva), D. Altena. :it:


