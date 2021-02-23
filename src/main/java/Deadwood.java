import java.util.*;
import java.util.Scanner;

public class Deadwood{
    //make queue??
    static int numDays;
    
    static Queue<Player> players = new LinkedList<Player>();
    static Board board;
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
        
        Player currentPlayer;    
        int numPlayers = Integer.valueOf(args[2]);
        String boardPath = args[0];
        String cardPath = args[1];
        //  intro statement
        board = new Board(boardPath, cardPath);
        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            System.out.println("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        System.out.println("baller");
        //creates the player queue with diff values according to num players
        //fix to not make ugly ?
        players = addPlayers(numPlayers);

        
        //figure out how to create board here

        while(numDays != 0){
            System.out.println("Placing all players in trailers");
            while(board.sceneNum() > 1){
                currentPlayer = players.peek();
                players.add(players.remove());
                playerTurn(currentPlayer);
            }
            numDays--;
            System.out.println("Its the end of the day! " + numDays + " days remain");
        }
    System.out.println("Calculating winner...");
    Player winner = calcWinner();
    System.out.println(winner.playerName + " wins with " + winner.calcFinalScore() + "!");
        

    }
    //figure out something for ties
    public static Player calcWinner(){
        Player winner = new Player("Big Loser", -100, 0);
        for(Player p: players){
            if(p.calcFinalScore() > winner.calcFinalScore()){
                winner = p;
            }
        }
        return winner;
    }

    public static Queue<Player> addPlayers(int numPlayers){
        
        Queue<Player> p = new LinkedList<Player>();
        Scanner scan = new Scanner(System.in);
        String input = "";

        if(numPlayers > 6){
            numDays = 4;
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(input, 2, 0));
            }
        }
        else if (numPlayers == 6) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(input, 0, 4));
            }
            numDays = 4;
        }
        else if (numPlayers == 5) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(input, 0, 2));
            }
            numDays = 4;
        }
        else if (numPlayers == 4) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(input));
            }
            numDays = 4;
        }
        else {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(input));
            }
            numDays = 3;
        }
        return p;
    }

    public static boolean moveTo(Player p, String place){
        Stack<String> adj = board.getAdjacent(p.position);
        for(int i = 0; i < adj.size(); i++){
            if(place.equals(adj.pop()))
            {
                return true;
            }
        }
        return false;
    }

    public static void playerTurn(Player currentPlayer){
                
        boolean hasPlayed = false; 
        Scanner scan = new Scanner(System.in);
        String input = "";

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
                        System.out.println("Current player " + currentPlayer.playerName + " position: " + currentPlayer.position);
                        //print the remaining players 
                        for(int i = 0; i < players.size() - 1; i++){
                            System.out.println((players.peek()).playerName + " is located at: " + (players.peek()).position);
                            players.add(players.remove());
                        }
                        //make sure current player is place back at last in queue
                        players.add(players.remove());
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
                    else if(input.contains("move")){
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
                                    System.out.println("invalid place to move to");
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
                        System.out.println(board.freeRoles(currentPlayer.position));
                    }
                   
                    //if player wants to take roll and are not employed, let them
                    else if(input.contains("take role") && currentPlayer.employed == false){
                        if(board.employ(currentPlayer.position, input.substring(10))) //also make return bool
                        {
                            currentPlayer.giveRole(input.substring(10));
                        }
                        else{
                            System.out.println("This role does not exist where you are currently, other options are " + board.freeRoles(currentPlayer.position));
                        }
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
                            act(currentPlayer);
                            hasPlayed = true;
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

    public static void act(Player curPlayer){
        Set sett = board.getSet(curPlayer.position);
        int budget = Integer.valueOF((sett.currentCard).budget);
        int die = 1 + (int)(Math.random() * ((6 - 1) + 1));

        System.out.println("The budget of your current job is: " + budget + ", you rolled a: " + die + ", and you have " + curPlayer.rehearseToken + " rehearsal tokens");
        if(budget > (die + curPlayer.rehearseToken)){
            System.out.println("Acting failure!");
            if(onCard(curPlayer.roleName, sett)){
                System.out.println("Since you had an important role you get nothing!");
            }
            else{
                System.out.println("Since you weren't that important you will get 1 dollar, out of pity");
            }
        }
        else{
            System.out.println("Acting success!");
            if(onCard(curPlayer.roleName, sett)){
                System.out.println("Since you were important, you get 2 credits");
                curPlayer.incCred(2);
            }
            else{
                System.out.println("Since you weren't that important you get 1 credit and 1 dollar");
                curPlayer.incCred(1);
                curPlayer.incDollar(1);
            }
            sett.incTakes();
        }
        if(sett.getScene() == 0){
            if(canBonus(sett)){
                System.out.println("Oh no! That was the last scene! Everyone gets money!");
                bonuses(sett);
            }
            else{
                System.out.println("That was the last scene, but no ones on card so no one gets money! Aha!");
            }
            wrapUp(sett);
        }
        else{
            System.out.println("There are only " + sett.getScene() + " scenes left!");
        }

    }

    public static void canBonus(Set s){
        for(int i = 0; i < ((s.currentCard).roles).size(); i ++){
            if(((s.currentCard).roles).occupied){
                return true;
            }
        }
        return false;
    }

    public static void bonuses(Set s){
        int[] dice = new int[Integer.valueOf((sett.currentCard).budget)];
        System.out.println("Rolling " + (sett.currentCard).budget + " dice");
        for(int d: dice){
            d = 1 + (int)(Math.random() * ((6 - 1) + 1));
        }
        Player[] onCardPeople = findOnCardPeople(s);
        Player[] offCardPeopel = findOffCardPeople(s);

    }

    public static Player[] findOnCardPeople(Set s){

    }

    public static boolean onCard(String role, Set s){
        Stack<Role> tester = s.offCardRoles;
        for(int i = 0; i < tester.size(); i++){
            if(role.equals(tester.name)){
                return false;
            }
            tester.add(tester.pop());
        }
        return true;
    }
}