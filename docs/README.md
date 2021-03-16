# How to Use This Code

## How to Run Deadwood

    1. While in team_bunny_345-21wi compile with:  
        ./compile.sh
    2. Then decide player number run with:
        ./run.sh src/main/resources/xml/board.xml src/main/resources/xml/cards.xml {number of total players} {number of computer players}

## How to Play Deadwood

Note: computer players suppress ALL popups EXCEPT for errors and those at the day's end and at the game's end, but even though popups are suppressed, the board and player side panels will still update and the game will otherwise continue as normal.

    1. Once code is running, the current player will be displayed on the top of the screen
    2. During a turn you can pick from these options:
        - "end turn" to end the turn
        - "move"
            - You will then be prompted to pick from the surrounding areas with a pop upgrade
            - Pick where you would like to move
        - "take role" 
            - You will then be prompted to pick a role from the set you are act
            - Look at the board and see the rank you must be for the roles (indicated by the dice above it)
            - Look at the names that are printed on the role and pick one, select the same name on the popUp that appeared
        - "upgrade"
            - If you are located in the office, you have the option to upgrade
            - When you press upgrade, you will be prompted to pick an upgrade based on either the currency dollars or credits
            - If you can't pay for it you will be told
            - You will then lose that corresponding amount of currency and become the corresponding level
        - "act" 
            - If you are employed you will either have a successful or failed act attempt, and given the corresponding amount of money
            - Scene tokens will decrement until none remain and the set will be wrapped up, bonuses will be distributed if necessary
        - "rehearse"
            - If you are employed you can rehearse once per turn to increase your odds of a successful act until its guaranteed
        Note: You are allowed to many of these things any amount of times, but you can only do one of the following (once) per turn:
            act
            move
            rehearse
    3. After each day, the board will reset and move the players back to the trailers
    4. After the max days it will calculate the scores and display which player is the winner
