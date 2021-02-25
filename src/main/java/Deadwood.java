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
                (players.peek()).setPosition("trailer");
                players.add(players.remove());
            }
        }

        //calculate winner
        System.out.println("Calculating winner...");
        winners = calcWinner();
        if (winners.size() > 1) {
            System.out.println("There's a tie with " + winners.get(0).calcFinalScore() + " points. The following players tied:");
            for (Player p : winners) {
                System.out.println(p.getName());
            }
        } else {
            System.out.println(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
        
    }
    
    //for finding ties
    static private class ScoreSorter implements Comparator<Player> {
        public int compare(Player p1, Player p2) {
            return p2.calcFinalScore() - p1.calcFinalScore();
        }
    }

    //to calculate winner
    static private List<Player> calcWinner(){
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

    //to add players to the game based on input and returns queue of players
    static private Queue<Player> addPlayers(int numPlayers){
        
        Queue<Player> p = new LinkedList<Player>();
        Scanner scan = new Scanner(System.in);
        String input = "";

        //changes player vals according to number of players
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

    //to move a player to a neighbor
    private static boolean moveTo(Player p, String place){
        //get list of neighbors
        List<String> adj = board.getNeighbors(p.getPosition());
        for(int i = 0; i < adj.size(); i++){
            //if the designated neighbor exists return true
            if(place.equals(adj.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    //big boi method for each players turn
    private static void playerTurn(Player currentPlayer){
                
        boolean hasPlayed = false; 
        Scanner scan = new Scanner(System.in);
        String input = "";
        System.out.println("What would you like to do " + currentPlayer.getName() + "?");
        
        //while the player doesn't say they want their turn to end
                while(!input.equals("end")){
                    
                    input = scan.nextLine();

                    //prints whose turn and dollar and credits
                    if(input.equals("who")){
                        System.out.println(currentPlayer.getName() + "($" + currentPlayer.getDollars() + ", " + currentPlayer.getCredits() + "cr) working as a " + currentPlayer.getRoleName() + " with " + currentPlayer.getRehearseTokens() + " rehersal tokens.");
                    }
                    //prints where current player is
                    else if(input.equals("where")){
                        System.out.println(currentPlayer.getPosition());
                    }
                    //prints where all players are
                    else if(input.equals("where all")){
                        //print current player first
                        System.out.println("Current player " + currentPlayer.getName() + " position: " + currentPlayer.getPosition());
                        //print the remaining players 
                        for(int i = 0; i < players.size() - 1; i++){
                            System.out.println((players.peek()).getName() + " is located at: " + (players.peek()).getPosition());
                            players.add(players.remove());
                        }
                        //make sure current player is place back at last in queue
                        players.add(players.remove());
                    }
                    //prints adjacent tiles to player
                    else if(input.equals("neighbors")){
                        List<String> temp = board.getNeighbors(currentPlayer.getPosition());
                        System.out.println("Your neighbors are: ");
                        for(int i = 0; i < temp.size(); i++){
                            System.out.println("- " + temp.get(i));
                        }
                    }
                    //if player wants to move
                    else if(input.equals("move")){
                        System.out.println("Please enter a place you want to move after \"move\"");
                    }
                    else if(input.contains("move ")){
                        //if player is not employed
                        if(!currentPlayer.isEmployed()){
                            //if player hasnt moved or acted
                            if(!hasPlayed){
                                String split = input.substring(5);
                                if(moveTo(currentPlayer, split)) 
                                {
                                    currentPlayer.setPosition(split);
                                    //hasPlayed = true;
                                }
                                else
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
                        System.out.println(board.freeRoles(currentPlayer.getPosition()));
                    }
                   
                    //if player wants to take roll and are not employed, let them
                    else if(input.contains("take role") && currentPlayer.isEmployed() == false){
                        if(!board.alreadyDone(currentPlayer.getPosition())){
                            if(currentPlayer.getLevel() >= board.employ(currentPlayer.getPosition(), input.substring(10))) //also make return bool
                            {
                                currentPlayer.giveRole(input.substring(10));
                                System.out.println("You are now employed as: " + currentPlayer.getRoleName());
                            }
                            else{
                                System.out.println("This role does not exist where you are currently or you are not a high enough level, other options are " + board.freeRoles(currentPlayer.getPosition()));
                            }
                        }
                        else{
                            System.out.println("This set was already finished!");
                        }
                        
                    }
                    //if player wants to upgrade using dollars
                    else if(input.contains("upgrade d")){
                        //check to make sure player is in office
                        if((currentPlayer.getPosition()).equals("office")){
                            int desiredLevel = Integer.valueOf(input.substring(10));
                            //make sure the level is creater than current level and in the upgrades list
                            
                                //get the upgrade prices
                                int[] d = board.getDollarC();
                                if(desiredLevel > currentPlayer.getLevel() && desiredLevel  <= d.length +1 )
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
                        if((currentPlayer.getPosition()).equals("office")){
                            int desiredLevel = Integer.valueOf(input.substring(10));
                            //make sure desired level is above current level and in the upgrade lists
                            
                                //get list of upgrades
                                int[] c = board.getCreditC();
                                if(desiredLevel > currentPlayer.getLevel() && desiredLevel  <= c.length +1 )
                            {
                                //make usre they have enough
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
                            System.out.println("Level " + (i+2) + ": " + dd[i]);
                        }
                        System.out.println("Upgrade costs if using credits:");
                        for(int i = 0; i < cc.length; i++){
                            System.out.println("Level " + (i+2) +": " + cc[i]);
                        }
                    }
                    //if player wants to act
                    else if(input.equals("act")){
                        if(!hasPlayed){
                            if(currentPlayer.isEmployed()){
                                act(currentPlayer);
                                hasPlayed = true;
                            }
                            else{
                                System.out.println("You are currently not employed");
                            }
                        }
                        else{
                            System.out.println("You have already acted this");
                        }
                        

                    }
                    //if player wants to rehearse 
                    else if(input.equals("rehearse")){
                        //if player is employed
                        if(currentPlayer.isEmployed()){
                            //and if the player does not have the max number of tokens already
                            if(!hasPlayed){
                                if(currentPlayer.getRehearseTokens() == 5)
                                {
                                    System.out.println("You have reached the max for rehearsal and only have the option to act. Its a garunteed success though!");
                                }
                                else{
                                    currentPlayer.incToken();
                                    hasPlayed = true;
                                }
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

    //for letting the player act
    private static void act(Player curPlayer){
        //make and role a die
        Set sett = board.getSet(curPlayer.getPosition());
        int budget = Integer.valueOf((sett.currentCard).budget);
        int die = 1 + (int)(Math.random() * 6);


        System.out.println("The budget of your current job is: " + budget + ", you rolled a: " + die + ", and you have " + curPlayer.getRehearseTokens() + " rehearsal tokens");
        //if the die is higher than the budget
        if(budget > (die + curPlayer.getRehearseTokens())){
            //for acting failures on card and off card
            System.out.println("Acting failure!");
            if(onCard(curPlayer.getRoleName(), sett)){
                System.out.println("Since you had an important role you get nothing!");
            }
            else{
                System.out.println("Since you weren't that important you will get 1 dollar, out of pity");
                curPlayer.incDollar(1);
            }
        }
        else{
            //for acting successes on card and off card
            System.out.println("Acting success!");
            if(onCard(curPlayer.getRoleName(), sett)){
                System.out.println("Since you were important, you get 2 credits");
                curPlayer.incCred(2);
            }
            else{
                System.out.println("Since you weren't that important you get 1 credit and 1 dollar");
                curPlayer.incCred(1);
                curPlayer.incDollar(1);
            }
            //increment takes
            sett.incTakes();
        }
        //if it was the last scene and someone was on card hand out bonuses
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

    //returns true if someone is on card
    private static boolean canBonus(Set s){
        for(int i = 0; i < ((s.currentCard).roles).size(); i ++){
            if((((s.currentCard).roles).get(i)).occupied){
                return true;
            }
        }
        return false;
    }

    //hands out bonuses based on on card and off card people
    private static void bonuses(Set s){
        int[] dice = new int[Integer.valueOf((s.currentCard).budget)];
        System.out.println("Rolling " + ((s.currentCard).budget) + " dice");
        for (int i = 0; i < dice.length; i++) {
            dice[i] = 1 + (int)(Math.random() * ((6 - 1) + 1));
        }

        // sort array in ascending order and then reverse it
        Arrays.sort(dice);
        int[] newDice = new int[dice.length];
        for(int i = 0; i < dice.length; i++) {
            newDice[dice.length-1-i] = dice[i];
        }


        List<Player> onCardPeople = findOnCardPeople(s);
        List<Player> offCardPeople = findOffCardPeople(s);

        //hand out bonuses of randomized dice to on card people
        for(int i = 0; i < dice.length; i++){
            (onCardPeople.get(i%(onCardPeople.size()))).incDollar(dice[i]);
            System.out.println((onCardPeople.get(i%(onCardPeople.size()))).getName() + " gets $" + dice[i]);
        }

        //hand out bonuses of rank to off card people
        for(Player p: offCardPeople){
            p.incDollar(getRoleRank(p.getRoleName(), s));
            System.out.println(p.getName() + " gets $" + getRoleRank(p.getRoleName(), s));
        }
    }

    //returns int of the level of the role
    private static int getRoleRank(String roleName, Set s){
        for(Role r: s.offCardRoles){
            if(roleName.equals(r.name)){
                return Integer.valueOf(r.level);
            }
        }
        return 0;
    }

    //returns list of people who are on card for a set
    private static List<Player> findOnCardPeople(Set s){
        List<Player> pl = new ArrayList<Player>();
        for(Player p: players){
            for(Role r: ((s.currentCard).roles)){
                if((r.name).equals(p.getRoleName())){
                    pl.add(0, p);
                }
            }
        }
        return pl;

    }

    //returns list of people who are off card for a set
    private static List<Player> findOffCardPeople(Set s){
        List<Player> pl = new ArrayList<Player>();
        for(Player p: players){
            for(Role r: (s.offCardRoles)){
                if((r.name).equals(p.getRoleName())){
                    pl.add(0, p);
                }
            }
        }
        return pl;

    }

    //wraps up a set and resets the roles,
    private static void wrapUp(Set s){
        List<Player> onCardPeople = findOnCardPeople(s);
        List<Player> offCardPeople = findOffCardPeople(s);

        for(Player p: onCardPeople){
            p.resetRole();
        }
        for(Player p: offCardPeople){
            p.resetRole();
        }

        //make sure the set isnt used again for this day
        s.flipSet();

    }

    //returns boolean if the role is on card of a set
    private static boolean onCard(String role, Set s){
        List<Role> tester = s.offCardRoles;
        for(int i = 0; i < tester.size(); i++){
            if(role.equals((tester.get(i)).name)){
                return false;
            }
        }
        return true;
    }
}
