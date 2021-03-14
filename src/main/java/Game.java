import java.util.*;

// uses singleton with lazy initialization b/c of args
public class Game{
    private int numDays;
    private Queue<Player> players = new LinkedList<Player>();
    private int numPlayers;
    private Board board;
    private View view;
    private Player[] playerArray;

    private Player currentPlayer;

    private static Game uniqueInstance;

    public Game (String[] args) {
        numPlayers = Integer.valueOf(args[2]); 
        board = Board.getInstance(args[0], args[1]);
        view = View.getInstance();
    }

    // initializer (w/ args)
    public static synchronized Game getInstance(String[] args) {
        if (uniqueInstance == null) {
            uniqueInstance = new Game(args);
        }
        return uniqueInstance;
    }
    
    // accessor (no args)
    public static synchronized Game getInstance() {
        return uniqueInstance;
    }

    // obsoleted by setting view = View.getInstance() in constructor after board since view is singleton now
    // public void registerObserver(View view) { 
    //     this.view = view;

    public void run(){
        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            view.showPopUp("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        //  intro statement
        //controller.showPopUp("Welcome to Deadwood!");
        
        //creates the player queue with diff values according to num players
        players = initPlayers();

        // init and show board and currentPlayer
        board.reloadBoardImgs();
        startNewTurn();
        view.show(); // show view as last step of run()
    }

    private Queue<Player> initPlayers() {
        Queue<Player> players = new LinkedList<Player>();
        Player p;
        // set default values
        // Set startLocation = board.getSet("trailer"); // UNCOMMENT AFTER DONE TESTING
        Set startLocation = board.getSet("Saloon"); // FOR TESTING ONLY; DELETE AFTERWARD
        // int startRank = 1;  // UNCOMMENT AFTER DONE TESTING
        int startRank = 6; // FOR TESTING ONLY; DELETE AFTERWARD
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
                p = new Player(startLocation, tempName, startRank, startCredits, dieImgPaths);
                view.resetPlayerDie(p, i);
            } else {
                p = new Player(startLocation, tempName, startRank, dieImgPaths);
                view.resetPlayerDie(p, i);
            }
            players.add(p);
        }
        playerArray = new Player[players.size()];
        for(int i = 0; i < players.size(); i++){
            playerArray[i] = players.peek();
            players.add(players.remove());
        }

        return players;
    }

    public String chooseNeighbor() {
        String[] neighbors = currentPlayer.getLocation().getNeighborStrings();
        String result = view.showMovePopUp(neighbors);
        return result;
    }


    public void tryMove() {
        if(!currentPlayer.getHasPlayed()){
            if (!currentPlayer.isEmployed()) {
                String destStr = chooseNeighbor();
                //currentPlayer.setAreaData(getBoardSet(destStr).getArea());
                currentPlayer.moveTo(destStr, getBoardSet(destStr));
                if(currentPlayer.getLocation().getFlipStage() == 0){
                    currentPlayer.getLocation().flipSet();
                }
                refreshPlayerPanel();
            }
            else {
                view.showPopUp("Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
            }
        }
        else{                    
            view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
        }
    }

    public String chooseRole() {
        String[] roles = currentPlayer.getLocation().getRoleStrings();
        String result = view.showRolePopUp(roles);
        return result;
    }

    public void tryTakeRole() {
        if (currentPlayer.isEmployed() == false) {
            if(currentPlayer.getLocation().getName() == "office" || currentPlayer.getLocation().getName() == "trailer"){
                view.showPopUp("You are currently in the " + currentPlayer.getLocation().getName() + " please move to a tile that has a role");
            }
            else{
                String chosenRole = chooseRole();
                currentPlayer.takeRole(chosenRole);
                currentPlayer.getRole().occupy();
                if(!board.isOnCard(currentPlayer.getRole().getName(), currentPlayer.getLocation())){
                    currentPlayer.setOnCardAreaData(currentPlayer.getRole().getArea());
                    System.out.println("on card");
                }
                else{
                    currentPlayer.setAreaData(currentPlayer.getRole().getArea());
                }
                refreshPlayerPanel();
            }
        } else {
            view.showPopUp("You're already employed, so you can't take another role until you finish this one");
        }   
    }

    public void tryUpgrade() {
        if(currentPlayer.getLocation().getName().equals("office")){
            String[] upgrades = currentPlayer.getLocation().getUpgradeStrings(currentPlayer.getRank());
            if(upgrades.length != 0){
                String n = view.showUpgradePopUp(upgrades);
                String[] splited = n.split("\\s+");
                if(splited[4].equals("dollars")){
                    if(Integer.valueOf(splited[3]) > currentPlayer.getDollars()){
                        view.showPopUp("You don't have enough dollars for this upgrade");
                    }
                    else{
                        currentPlayer.incDollars(-1*(Integer.valueOf(splited[3])));
                        currentPlayer.setRank(Integer.valueOf(splited[1]));
                    }
                }
                else{
                    if(Integer.valueOf(splited[3]) > currentPlayer.getCredits()){
                        view.showPopUp("You don't have enough credits for this upgrade");
                    }
                    else{
                        currentPlayer.incCredits(-1*(Integer.valueOf(splited[3])));
                        currentPlayer.setRank(Integer.valueOf(splited[1]));
                    }
                }
                refreshPlayerPanel();
                view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath());
            }
            else{
                view.showPopUp("You are already max level");
            }
            
        }
        else{
            view.showPopUp("You are not located in the office, move to the office");
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
                currentPlayer.act(onCardPlayers, offCardPlayers);
                // DONT reset board after every act
            }
            else{
                view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
            }
        }
        else {
            view.showPopUp("You're not employed, so you need to take a role before you can act.");
        }
    }

    public void startNewTurn(){
        currentPlayer = players.peek();
        players.add(players.remove());
        view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath());
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
            startNewTurn();
            view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath());
            view.showPopUp("It is now " + currentPlayer.getName() + "'s turn");
        }
        else { // day ends
            if (numDays > 0) { // game continues
                //decrement days and reset the roles and board
                numDays--;
                view.showPopUp("Its the end of the day! " + numDays + " days remain");
                board.reloadBoardImgs();

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
            
            view.setDie(curPlayer);
            players.add(players.remove());
            
        }
        board.reloadBoardImgs();
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
