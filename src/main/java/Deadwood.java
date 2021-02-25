import java.util.*;

public class Deadwood{
    static private int numDays;
    
    static private Queue<Player> players = new LinkedList<Player>();
    static private Board board;

    static private Player currentPlayer;
    static private int numPlayers;
    static private String boardPath;
    static private String cardPath;
    static private List<Player> winners = new ArrayList<Player>();

    static private Scanner scan = new Scanner(System.in);
    public static void main(String[] args){

        //Ask how many players
        //create that many players
        //create board

        //while numDays > 0
        //set board 

        //while set > 1
        //iterate through players

        //numDays--
        
        //calc final Score
        
        numPlayers = Integer.valueOf(args[2]); 
        boardPath = args[0];
        cardPath = args[1];
        //  intro statement
        board = new Board(boardPath, cardPath);
        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            System.out.println("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        System.out.println("Welcome to Deadwood! Start with naming your characters");
        //creates the player queue with diff values according to num players
        //fix to not make ugly ?
        players = addPlayers(numPlayers);

        
        //iterates through the day
        while(numDays != 0){
            System.out.println("Placing all players in trailers");
            while(board.sceneNum() > 1){
                currentPlayer = players.peek();
                players.add(players.remove());
                playerTurn(currentPlayer);
            }
            //decrement days and reset the roles and board
            numDays--;
            System.out.println("Its the end of the day! " + numDays + " days remain");
            board.resetBoard();
            for(int i = 0; i < players.size(); i++){
                (players.peek()).resetRole();
                (players.peek()).setLocation(board.getSet("trailer"));
                players.add(players.remove());
            }
        }

        scan.close();

        //calculate winner
        System.out.println("Calculating winner...");
        winners = calcWinner(players);
        if (winners.size() > 1) {
            System.out.println("There's a tie with " + winners.get(0).calcFinalScore() + " points. The following players tied:");
            for (Player p : winners) {
                System.out.println(p.getName());
            }
        } else {
            System.out.println(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
        
    }

    //to add players to the game based on input and returns queue of players
    static private Queue<Player> addPlayers(int numPlayers){
        
        Queue<Player> p = new LinkedList<Player>();
        String input = "";

        //changes player values according to number of players
        if(numPlayers > 6){
            numDays = 4;
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(board.getSet("trailer"), input, 2, 0));
            }
        }
        else if (numPlayers == 6) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(board.getSet("trailer"), input, 0, 4));
            }
            numDays = 4;
        }
        else if (numPlayers == 5) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(board.getSet("trailer"), input, 0, 2));
            }
            numDays = 4;
        }
        else if (numPlayers == 4) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(board.getSet("trailer"), input));
            }
            numDays = 4;
        }
        else {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                p.add(new Player(board.getSet("trailer"), input));
            }
            numDays = 3;
        }
        return p;
    }

    //to calculate winner
    static private List<Player> calcWinner(Queue<Player> players){
        //for finding ties
        class ScoreSorter implements Comparator<Player> {
            public int compare(Player p1, Player p2) {
                return p2.calcFinalScore() - p1.calcFinalScore();
            }
        }

        winners = new ArrayList<Player>();
        while (players.size() > 0) {
            winners.add(players.remove());
        } // don't think we need "players" at this point?

        // scoreSorter's compare() should sort in descending order by calcFinalScore()
        // Arrays.sort(winnersPre, new scoreSorter()); // only works w/ normal arrays :(
        Collections.sort(winners, new ScoreSorter());

        int winningScore = winners.get(0).calcFinalScore();
        for (int i = winners.size()-1; i > 0; i--) { // remove non-ties starting from end, excluding 0
            if (winners.get(i).calcFinalScore() < winningScore) {
                winners.remove(i);
            }
        }
        return winners;
    }

    //returns list of people who are on card for a set
    private static List<Player> findOnCardPlayers(){
        List<Player> pl = new ArrayList<Player>();
        for(Player p: players){
            for(Role r: (currentPlayer.getLocation().getOnCardRoles())){
                if((r.getName()).equals(p.getRoleName())){
                    pl.add(0, p);
                }
            }
        }
        return pl;
    }

    //returns list of people who are off card for a set
    private static List<Player> findOffCardPlayers(){
        List<Player> pl = new ArrayList<Player>();
        for(Player p: players){
            for(Role r: (currentPlayer.getLocation().getOffCardRoles())){
                if((r.getName()).equals(p.getRoleName())){
                    pl.add(0, p);
                }
            }
        }
        return pl;
    }

    //big boi method for each players turn
    private static void playerTurn(Player currentPlayer){
                
        boolean hasPlayed = false; 
        String input = "";
        System.out.println("What would you like to do, " + currentPlayer.getName() + "?");
        
        //while the player doesn't say they want their turn to end
        while(!input.equals("end")){
            
            input = scan.nextLine().trim();

            if (input.equals("end")) {
                break;
            }
            //prints whose turn and dollar and credits
            else if(input.equals("who")){
                System.out.println(currentPlayer.getName() + "($" + currentPlayer.getDollars() + ", " + currentPlayer.getCredits() + "cr) working as a " + currentPlayer.getRoleName() + " with " + currentPlayer.getRehearseTokens() + " rehearsal tokens.");
            }
            //prints where current player is
            else if(input.equals("where")){
                System.out.println(currentPlayer.getLocation().getName());
            }
            //prints where all players are
            else if(input.equals("where all")){
                //print current player first
                System.out.println("Current player " + currentPlayer.getName() + " location: " + currentPlayer.getLocation().getName());
                //print the remaining players 
                for(int i = 0; i < players.size() - 1; i++){
                    System.out.println((players.peek()).getName() + " is located at: " + (players.peek()).getLocation().getName());
                    //make sure current player is place back at last in queue
                    players.add(players.remove());
                }
                //make sure current player is place back at last in queue
                players.add(players.remove());
            }
            //prints adjacent tiles to player
            else if(input.equals("neighbors")){
                List<String> n = board.getNeighbors(currentPlayer.getLocation().getName());
                System.out.println("Your neighbors are: ");
                for(int i = 0; i < n.size(); i++){
                    System.out.println("- " + n.get(i));
                }
            }
            else if(input.equals("available roles")){
                System.out.println(board.freeRoles(currentPlayer.getLocation().getName()));
            }
            
            //if player wants to take role and are not employed, let them
            else if(input.contains("take role") && currentPlayer.isEmployed() == false){
                if(!board.alreadyDone(currentPlayer.getLocation().getName())){
                    if(currentPlayer.getRank() >= board.employ(currentPlayer.getLocation().getName(), input.substring(10))) //also make return bool
                    {
                        currentPlayer.giveRole(input.substring(10));
                        System.out.println("You are now employed as: " + currentPlayer.getRoleName() + ". You can rehearse or act in this role on your next turn");
                    }
                    else{
                        System.out.println("This role does not exist where you are currently (check your command for typos) or you are not a high enough level, other role options are " + board.freeRoles(currentPlayer.getLocation().getName()));
                    }
                }
                else{
                    System.out.println("This set was already finished!");
                }
                
            }
            //if player wants to upgrade using dollars
            else if(input.contains("upgrade d")){
                //check to make sure player is in office
                if((currentPlayer.getLocation().getName()).equals("office")){
                    int desiredLevel = Integer.valueOf(input.substring(10));
                    //make sure the level is greater than current level and in the upgrades list
                    
                        //get the upgrade prices
                        int[] d = board.getDollarC();
                        if(desiredLevel > currentPlayer.getRank() && desiredLevel <= d.length + 1 )
                    {
                        if(d[desiredLevel-2] > currentPlayer.getDollars()){
                            System.out.println("You do not have enough dollars for this upgrade");
                        }
                        else{
                            //change level
                            System.out.println("You are now level " + desiredLevel);
                            currentPlayer.setLevel(desiredLevel);
                            currentPlayer.incDollar((-1*d[desiredLevel-2]));
                            System.out.println("And you have " + currentPlayer.getDollars() + " remaining");
                        }
                    }
                    else{
                        System.out.println("Not a valid level!!!!");
                    }
                }
                else{
                    System.out.println("You are not on the casting office");
                }

            }
            //if player wants to upgrade using credits
            else if(input.contains("upgrade c ")){
                if((currentPlayer.getLocation().getName()).equals("office")){
                    int desiredLevel = Integer.valueOf(input.substring(10));
                    //make sure desired level is above current level and in the upgrade lists
                    
                        //get list of upgrades
                        int[] c = board.getCreditC();
                        if(desiredLevel > currentPlayer.getRank() && desiredLevel  <= c.length +1 )
                    {
                        //make sure they have enough
                        if(c[desiredLevel-2] > currentPlayer.getCredits()){
                            System.out.println("You do not have enough credits for this upgrade");
                        }
                        else{
                            //upgrade
                            System.out.println("You are now level " + desiredLevel);
                            currentPlayer.setLevel(desiredLevel);
                            currentPlayer.incCred((-1*c[desiredLevel-2]));
                            System.out.println("And you have " + currentPlayer.getCredits() + " remaining");
                        }
                    }
                    else{
                        System.out.println("Not a valid level!!!!");
                    }
                }
                else{
                    System.out.println("You are not on the casting office");
                }

            }
            //gets the cost of every upgrade
            else if(input.equals("upgrade costs")){
                int[] dd = board.getDollarC();
                int[] cc = board.getCreditC();
                System.out.println("Upgrade costs if using dollars:");
                for(int i = 0; i < dd.length; i++){
                    System.out.println("Level " + (i+2) + ": $" + dd[i]);
                }
                System.out.println("Upgrade costs if using credits:");
                for(int i = 0; i < cc.length; i++){
                    System.out.println("Level " + (i+2) +": " + cc[i] + " credits");
                }
            }
            // 
            // else if (!hasPlayed) {

            // }
            //if player wants to move
            else if(input.contains("move")){
                if (hasPlayed) {
                    System.out.println("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
                    continue;
                }
                else if(input.equals("move")){
                    System.out.println("Please enter a place you want to move after \"move\"");
                } else {
                    hasPlayed = currentPlayer.moveTo(board.getSet(input.substring(5)));
                }
            }
            //if player wants to act
            else if(input.equals("act")){
                if(!hasPlayed){
                    hasPlayed = currentPlayer.act(findOnCardPlayers(), findOffCardPlayers());
                }
                else{
                    System.out.println("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
                }
            }
            //if player wants to rehearse 
            else if(input.equals("rehearse")){
                //if player hasn't played yet this turn
                if(!hasPlayed){
                    hasPlayed = currentPlayer.rehearse();
                }
                else{
                    
                    System.out.println("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
                }
            }

            //catch bad things
            else{
                System.out.println("unknown command, try again");
            }
        }
    }

}
