# Botnet Wars

A terminal-based STRATEGY game. Your goal is to control the largest possible number of bots and launch attacks on institutions to obtain points. Plan your moves carefully, take control of powerful bots and execute chain attacks.

### Bots

Bots are represented by cards in the field. Players can take control of bots to grow their army and launch more powerful attacks. Players can only take control of bots by infecting them through the 'infect' command. 

##### System Types
Each bot is a different type of device. Devices can be PC [P], Mobile [M], IoT [I] or Server [S]. The device type is written in brackets []. When infecting another bot, the system type of the bot to be infected must be included in the 'can infect:' list of at least one of the bots owned by the player. The systems that can be infected are represented by the (-P-M-I-S-) letters on the bot.

##### Institutions
Institutions can be Users [U], Business [B], Financial Institutions [F] or Emergency Institutions [E]. The institutions that can be attacked are represented by the (U-B-F-E) letters on the bot.

###### Bot Structure

```sh
 ---------- 
|    02     |  --> Id
|   <P1>    |  --> Player who owns this bot
|           |
| [ P  C ]  |  --> System Type
|can attack:|
| -U-B-E-F- |  --> List of institutions this bot can attack
|    100    |  --> Attack value
|can infect:|
| -P-M-I-S- |  --> List of systems this bot can infect
 ---------- 
```

### Commands

The main menu shows the player commands. Commands are executed by entering the number of the option and pressing enter.

###### Main Menu

```sh
 Botmaster Commands:
  [1] Attack
  [2] Infect
  [3] My Bots
  [4] Show Botnet
  [5] Skip
  >
```

##### Attack
Launch an attack with your existing bots to a target institution. If institutions are attacked in the order specified by the chain attack, attacking more than once during a turn is possible. At least one bot owned by the player must have the initial letter of the institution written on it. The institutions that can be attacked are represented by the (U-B-F-E) letters on the bot. When attacking, all bots that can attack the target institution will attack simultaneously stacking their attack values.

###### Attack Menu

```sh
 Chain Attack ->(B)->(E)->(U)->(F)
      Attack:
        [1] Back
        [2] (U)User
        [3] (B)Business
        [4] (E)Emergency Services
        [5] (F)Financial Institution
        >
```

A chain attack allows the player to execute multiple attacks per turn. To successfully execute a chain attack the institutions must be attacked in the order shown by the arrows at the top.
  
##### Infect
Take control of more bots. To infect a bot on the field, enter the id of the bot (written at the top of each bot) and press ENTER. The bot can only be infected if the attack value is lower or equal than its attacker.  The attacking bot must have the initial letter of the target bot system written on it. The systems that can be infected are represented by the (P-M-I-S) letters on the bot.

###### Infect Menu

```sh
 Infect:
        [1] Back
        >
```
  
##### My Bots
Preview your own army of bots. Only owned bots will be shown when this command is selected.
  
##### Show Botnet
Displays the entire list of bots on the field. If a bot has an owner, it will be displayed under the id of the bot.
  
##### Skip
Skip your turn.

### Setting Up a New Game

When the program is executed, the player must enter the settings for a new game. The game supports up to n players (2-4 players is recommended). The total number of bots on the field can also be chosen (30-50 bots is recommended). Columns number is used to display the bots organized on a grid, it is recommended to choose a column number that is a divisor of the total amount of bots. The score limit is used to determine the end of the game, the player who reaches the score limit first wins.

###### New Game
```sh
(Big window size is recommended)

How many players? (2 recommended)
2
Number of bots: (30 recommended)
50
Columns: (10 recommended)
10
Score Limit: (1000 recommended)
5000
Successfully created 50 bots
Successfully created 2 players
```

### Installation
To execute the program copy the files in the same directory and write the command shown below on a terminal. Java is required to run the program. Since the binaries are already included, it is not necessary to have a java compiler.

###### Running the Program
```sh
java BotnetWars
```

Version
----

Beta
