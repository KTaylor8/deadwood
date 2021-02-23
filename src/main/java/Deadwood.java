import java.util.*;
import java.util.Scanner;

public class DeadWood{
    //make queue??
    static Queue<Player> players = new PriorityQueue<Player>();
    static int numDays;
    public static void main(String[] args){

        //Ask how many players
        //create that many players
        //create board

        //while numDays > 0
        //set board 

        //while set > 1
        //iterate through players

        //numDays--
        
        //calcfinalScore
        Scanner scan = new Scanner(System.in);
        Player currentPlayer;    
        int numPlayers = (int)args[2];
        String boardPath = args[0];
        String cardPath = args[1];
        String input = "";
        //  intro statement

        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            System.out.println("Invalid input, please enter a player number from 2 to 8");
            System.exit();
        }

        //creates the player queue with diff values according to num players
        //fix to not make ugly ?
        addPlayers(numPlayers);

        
        //figure out how to create board here
        Board board = new board(boardPath, cardPath);

        while(numDays != 0){
            System.out.println("Placing all players in trailers");
            while(board.sceneNum() > 1){
                currentPlayer = players.peek();
                players.add(players.pop());
                playerTurn(currentPlayer);
            }
            numDays--;
            System.out.println("Its the end of the day! " + numDays + " days remain");
        }
    System.out.println("Calculating winner...");
    Player winner = calcWinner();
    System.out.println(winner.playerName + "wins with " + winner.calcFinalScore() + "!");
        

    }
    //figure out something for ties
    public static Player calcWinner(){
        Player winner = new Player(-100, "Big Loser ");
        for(Player p: players){
            if(p.calcFinalScore() > winner.calcFinalScore()){
                winner = p;
            }
        }
    }

    public static void addPlayers(int numPlayers){
        if(numPlayers > 6){
            numDays = 4;
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(2, input));
            }
        }
        else if (numPlayers == 6) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input, 4));
            }
            numDays = 4;
        }
        else if (numPlayers == 5) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input, 2));
            }
            numDays = 4;
        }
        else if (numPlayers == 4) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input));
            }
            numDays = 4;
        }
        else {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input));
            }
            numDays = 3;
        }
    }

    public static boolean moveTo(Player p, String place){
        Stack<String> adj = board.getAdjacent(p.position);
        for(int i = 0; i < adj.getLength(); i++){
            if(place.equals(adj.pop()))
            {
                return true;
            }
        }
        return false;
    }

    public static void playerTurn(Player currentPlayer){
                
        boolean hasPlayed = false; 

                while(!input.equals("end")){
                    System.out.println("What would you like to do " + currentPlayer.playerName + "?");
                    input = scan.nextLine();

                    //prints who's turn and dollar and credits
                    if(input.equals("who")){
                        System.out.println(currentPlayer.playerName + "($" + currentPlayer.dollar + ", " + currentPlayer.credit + "cr)");
                    }
                    //prints where current player is
                    else if(input.equals("where")){
                        System.out.println(currentPlayer.position);
                    }
                    //prints where all players are
                    else if(input.equals("where all")){
                        //print current player first
                        System.out.println("Current player " + currentPlayer.name + " position: " + currentPlayer.position);
                        //print the remaining players 
                        for(int i = 0; i < players.getLength() - 1; i++){
                            System.out.println((players.peek()).name + " is located at: " + (players.peek()).position);
                            players.add(players.pop());
                        }
                        //make sure current player is place back at last in queue
                        players.add(players.pop());
                    }
                    //prints adjacent tiles to player
                    else if(input.equals("adjacent")){
                        Stack<String> temp = board.getAdjacent(currentPlayer.position);
                        System.out.println("You are adjacent to: ");
                        while(temp.peek()!= null){
                            System.out.println("- " + temp.pop());
                        }
                    }
                    //if player wants to move
                    else if(input.includes("move")){
                        //if player is not employed
                        if(!currentPlayer.employed){
                            //if player hasnt moved or acted
                            if(!hasPlayed){
                                String split = input.substring(5);
                                if(moveTo(currentPlayer, split)) //make return a bool
                                {
                                    currentPlayer.changePos(split);
                                    hasPlayed = true;
                                }
                                //else
                                {
                                    System.out("invalid place to move to");
                                }
                            }
                            else{
                                System.out.println("You have already moved or acted this turn");
                            }

                        }
                        else{
                            System.out.println("Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
                        }
                    }
                    else if(input.equals("available roles")){
                        System.out.println(board.freeRoles(currentPlayer.pos));
                    }
                   
                    //if player wants to take roll and are not employed, let them
                    else if(input.includes("take role") && currentPlayer.employed == false){
                        if(board.employ(currentPlayer.pos, input.substring(10))) //also make return bool
                        {
                            currentPlayer.giveRole(substring(10));
                        }
                        else{
                            System.out.println("This role does not exist where you are currently, other options are " + board.getRoles(currentPlayer))
                        }*/
                    }
                    //if player wants to upgrade
                    else if(input.equals("upgrade")){
                        /*if(Board.inCastingOffice(currentPlayer)){
                            System.out.println("You are now level");
                        }
                        else{
                            System.out.println("You are not on the casting office");
                        }*/

                    }
                    //if player wants to act
                    else if(input.equals("act")){
                        if(currentPlayer.employed){
                            //act(player) //side note: did we have the act things in game or in board?????
                        }
                        else{
                            System.out.println("You are currently not employed");
                        }

                    }
                    //if player wants to rehearse 
                    else if(input.equals("rehearse")){
                        //if player is employed
                        if(currentPlayer.employed){
                            //and if the player does not have the max number of tokens already
                            if(currentPlayer.level + currentPlayer.rehearseToken == 6)
                            {
                                System.out.println("You have reached the max for rehearsal and only have the option to act. Its a garunteed success though!");
                            }
                            else{
                                currentPlayer.incToken();
                            }
                        }
                        else{
                            System.out.println("You're not employed? What are you going to rehearse for??");
                        }
                    }
                    //if the player wants to end, let them end
                    else if(input.equals("end")){
                    }
                    //catch bad things
                    else{
                        System.out.println("unknown command, try again");
                    }
                }

    }
}