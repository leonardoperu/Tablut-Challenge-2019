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


Project developed by [L. Perugini](https://github.com/leonardoperu), [M. Rossi](https://github.com/smartisrossi), M. Minerva, D. Altena.
