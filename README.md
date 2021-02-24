To run game:
    1. While in team_bunny_345-21wi compile with:  
        ./compile.sh
    2. Then decide player number run with:
        ./run.sh src/main/resources/xml/board.xml src/main/resources/xml/cards.xml {player number}

To play game:
    (WARNING: ALL THINGS ARE CASE SENSITIVE, COMMANDS ARE LOWERCASE AND LOCATIONS SHOWN HAVE EXACT SAME CAPITALIZATION
        ex1: to move:  
            - "move Secret Hideout"
            -" take role Sleeping Drunkard
    Get exact names of roles and neighboring places if you are unsure of capitalization)
    
    1. Once code is running, you will be prompted to enter names of all of your players, this will also be in the same order that they play.
    2. During a turn you can pick from these options:
        - "end" to end the turn
        - "who" to see current character and their currency
        - "where" to see the current character position
        - "where all" to see every characters position
        - "neighbors" to get a list of the surrounding spots
        - "move {neighbor to move to}" to move your character to a spot if not working
        - "available roles" to get a list of the available roles where current player is at
        - "take role {role you want to take}" to become employed in a role if you are not already employed
        - "upgrade d {level you want to upgrade to}" to try and upgrade to that level using dollars
        - "upgrade c {level you want to upgrade to}" to try and upgrade to that level using credits
        - "upgrade costs" to get the cost of each upgrade from both credits and dollars
        - "act" when employed to act
        - "rehearse" to add a rehearse token to the current player
        Note: You are allowed to do any of these things any amount of times, but you can only do one of:
            act
            move
            rehearse
    3. After a day board will reset
    4. After the max days it will calc and give winner
