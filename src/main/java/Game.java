import java.util.*;

public class Game{
    private int numDays;
    private Queue<Player> players = new LinkedList<Player>();
    private int numPlayers;
    private Board board;
    private View view;

    private Player currentPlayer;

    public Game (String[] args) {
        numPlayers = Integer.valueOf(args[2]); 
        board = new Board(args[0], args[1]);
        // controller = new Controller();
        // view  = new View(controller);
    }

    private static Game uniqueInstance;

    public static synchronized Game getInstance(String[] args) {
        if (uniqueInstance == null) {
            uniqueInstance = new Game(args);
        }
        return uniqueInstance;
    }
    public static synchronized Game getInstance() {
        // if (uniqueInstance == null) {
        //     uniqueInstance = new Game();
        // }
        return uniqueInstance;
    }

    public void registerObserver(View view) {
        this.view = view;
    }

    public void run(){
        // view.init(); // init view so other classes can show popUps and reset cards/sets, but don't show it until the board is set-up
        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            view.showPopUp("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        //  intro statement
        //controller.showPopUp("Welcome to Deadwood!");
        
        //creates the player queue with diff values according to num players
        players = initPlayers();

        // init and show board
        board.resetBoard();
        view.show();
        rotateTurn();
        //iterates through the day
        // while(numDays != 0){
        //     //view.showPopUp("Placing all players in trailers");
        //     if(board.getSceneNum() > 1){ // this if-statement will probably have to be moved
        //         //currentPlayer = players.peek();
        //         //players.add(players.remove());
        //         changeTurn();
        //         view.changeCurrentPlayer(currentPlayer.getName());
        //         //ui.interact(currentPlayer, board, players);
        //     }
        //     //decrement days and reset the roles and board
        //     numDays--;
        //     view.showPopUp("Its the end of the day! " + numDays + " days remain");
        //     board.resetBoard();

        //     // reset players
        //     resetPlayers();
        // }

        // //calculate winner
        // //view.showPopUp("Calculating winner...");
        
        // calcWinner();
        // //ui.closeScanner();
    }

    private Queue<Player> initPlayers() {
        Queue<Player> players = new LinkedList<Player>();
        Player p;
        // set default values
        Set startLocation = board.getSet("trailer");
        int startRank = 1;
        int startCredits = 0;
        numDays = 4;

        // managing the image filepaths for the die assigned to each player
        int numImgs = 6;
        String[] dieImgPaths;
        String dieImgPathStub = "src/main/resources/img/dice_";
        String[] playerDieLetters = {"b", "c", "g", "o", "p", "r", "v", "y"};
        String imgExtension = ".png";

        //changes player values according to number of players
        if (numPlayers >= 7){
            startRank = 2;
            startCredits = 0;
        }
        else if (numPlayers == 6){
            startRank = 0;
            startCredits = 4;
        }
        else if (numPlayers == 5){
            startRank = 0;
            startCredits = 2;
        }
        else if (numPlayers == 4){
        }
        else {
            numDays = 3;
        }

        // Make players
        for(int i = 0; i < numPlayers; i++){
            // Create dieImgPaths
            dieImgPaths = new String[numImgs];
            for (int j = 0; j < numImgs; j++) {
                dieImgPaths[j] = dieImgPathStub + playerDieLetters[i] + (j+1) + imgExtension;
            }
            // Create players
            String tempName = "player" + (i+1); // PROBABLY WILL LET USERS CHOOSE THEIR OWN NAMES LATER
            if (numPlayers >= 5) {
                p = new Player(startLocation, tempName, startRank, startCredits, dieImgPaths, view);
                view.resetPlayerDie(p, i);
            } else {
                p = new Player(startLocation, tempName, dieImgPaths, view);
                view.resetPlayerDie(p, i);
            }
            players.add(p);
        }

        return players;
    }

    public String chooseNeighbor() {
        String[] neighbors = currentPlayer.getLocation().getNeighborStrings();
        String result = view.moveShowPopUp(neighbors);
        return result;
    }

    public void tryMove() {
        if(!currentPlayer.getHasPlayed()){
            if (!currentPlayer.isEmployed()) {
                String destStr = chooseNeighbor();
                currentPlayer.moveTo(destStr, getBoardSet(destStr));
                refreshPlayerPanel();
            }
        }
        else{                    
            view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
        }
    }

    public void tryTakeRole(String desiredRole) {
        if (currentPlayer.isEmployed() == false) {
            // LATER: THE METHOD CALLED NEEDS REFACTORING
            currentPlayer.takeRole(desiredRole);
            if (currentPlayer.isEmployed() == true) {
                // board.fillRole(location, role); // <-- this method needs to be simplified later; it's hard to follow currently
                currentPlayer.getRole().occupy();
            }
        } else {
            view.showPopUp("You're already employed, so you can't take another role until you finish this one");
        }   
    }

    public void tryUpgrade(String[] upgradeChosen) {
        if (upgradeChosen[0].equals("dollars")) {
            currentPlayer.upgrade(
                getDollarCost(), 
                currentPlayer.getDollars(), 
                Integer.valueOf(upgradeChosen[1])
            );
        } 
        else {
            currentPlayer.upgrade(
                getCreditCost(), 
                currentPlayer.getCredits(), 
                Integer.valueOf(upgradeChosen[1])
            );
        }
    }

    public void tryRehearse() {
        if (currentPlayer.isEmployed() == true) {
            if(!currentPlayer.getHasPlayed()){
                currentPlayer.rehearse();
            }
            else{                    
                view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
            }
        }
        else {
            view.showPopUp("You're not employed, so you need to take a role before you can rehearse.");
        }
    }

    public void tryAct() {

        List<Player> onCardPlayers = new ArrayList<Player>();
        List<Player> offCardPlayers = new ArrayList<Player>();
        if (currentPlayer.isEmployed() == true) {
            if(!currentPlayer.getHasPlayed()){
                findPlayers(currentPlayer.getLocation().getOnCardRoles());
                findPlayers(currentPlayer.getLocation().getOffCardRoles());
                currentPlayer.act(onCardPlayers, offCardPlayers); //passing in find...CardPlayers b/c otherwise I'd have to pass in the queue of all the players and that seems like too much info
            }
            else{
                view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
            }
        }
        else {
            view.showPopUp("You're not employed, so you need to take a role before you can act.");
        }
    }

    public void rotateTurn(){
        currentPlayer = players.peek();
        players.add(players.remove());
        view.changeCurrentPlayer(currentPlayer.getName());
        currentPlayer.setHasPlayed(false);
    }

    // to reset players
    public void resetPlayers() {
        Player curPlayer;
        for(int i = 0; i < players.size(); i++){
            curPlayer = players.peek();
            curPlayer.resetRole();
            curPlayer.setLocation(board.getSet("trailer"));
            view.resetPlayerDie(curPlayer, i);
            players.add(players.remove());
        }
    }

    public void endTurn() {
        if (board.getSceneNum() > 1) { // day continues
            rotateTurn();
            view.changeCurrentPlayer(currentPlayer.getName());
        }
        else { // day ends
            if (numDays > 0) { // game continues
                //decrement days and reset the roles and board
                numDays--;
                view.showPopUp("Its the end of the day! " + numDays + " days remain");
                board.resetBoard();

                // reset players
                resetPlayers();
            }
            else { // game ends
                view.showPopUp("Calculating winner...");
                calcWinner();
                // do something to freeze the screen and not allow 
            }
        }
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public List<Player> findPlayers(List<Role> rl) {
        List<Player> pl = new ArrayList<Player>();
        for(Player p: players){
            for(Role r: rl){
                if(r.equals(p.getRole())){
                    pl.add(0, p);
                }
            }
        }
        return pl;
    }

    public Set getBoardSet(String s){
        return board.getSet(s);
    }


    //returns the dollar cost of upgrades from the office
    public int[] getDollarCost(){
        for(Set s: board.getAllSets()){
            if((s.getName()).equals("office")){
                return (s.getUpgradeCD());
            }
        }
        return null;
    }

    //returns the credit cost of upgrades from the office
    public int[] getCreditCost(){
        for(Set s: board.getAllSets()){
            if((s.getName()).equals("office")){
                return (s.getUpgradeCC());
            }
        }
        return null;
    }

    public void refreshPlayerPanel() {
        view.clearDice();
        Player curPlayer;
        for(int i = 0; i < players.size(); i++){
            curPlayer = players.peek();
            view.resetPlayerDie(curPlayer, i);
            players.add(players.remove());
        }
    }

    //to calculate winner
    private void calcWinner(){
        //local class for finding ties
        class ScoreSorter implements Comparator<Player> {
            public int compare(Player p1, Player p2) {
                return p2.calcFinalScore() - p1.calcFinalScore();
            }
        }

        List<Player> winners = new ArrayList<Player>();
        int winningScore;

        // convert queue of players into List<Player> for determining winner(s)
        while (players.size() > 0) {
            winners.add(players.remove());
        } // don't think we need "players" at this point?

        // scoreSorter's compare() should sort in descending order by calcFinalScore()
        // Arrays.sort(winnersPre, new scoreSorter()); // only works w/ normal arrays :(
        Collections.sort(winners, new ScoreSorter());

        // remove any players that don't have the winning score
        winningScore = winners.get(0).calcFinalScore();
        for (int i = winners.size()-1; i > 0; i--) { // remove non-ties starting from end, excluding 0
            if (winners.get(i).calcFinalScore() < winningScore) {
                winners.remove(i);
            }
        }
        String winnersString = "";
        // Announce winners
        if (winners.size() > 1) {
            winnersString = "There's a tie with " + winners.get(0).calcFinalScore() + " points. The following players tied: ";
            for (Player p : winners) {
                winnersString += p.getName() + " ";
            }
            view.showPopUp(winnersString);
        } else {
            view.showPopUp(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
    }

}
