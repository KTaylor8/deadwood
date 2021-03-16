import java.util.*;

// uses singleton with lazy initialization b/c of args
public class Game{
    private int numDays;
    private Queue<Player> players = new LinkedList<Player>();
    private int numTotalPlayers;
    private int numComputerPlayers;
    private int numHumanPlayers;
    private Board board;
    private View view;
    private Player[] playerArray;

    private Player currentPlayer;

    private static Game uniqueInstance;

    /**
     * Creates the elements of the game
     * @param args
     */
    private Game (String[] args) {
        numTotalPlayers = Integer.valueOf(args[2]); 
        if (args[3] == null) {
            numComputerPlayers = 0;
        } else {
            numComputerPlayers = Integer.parseInt(args[3]);
        }
        numHumanPlayers = numTotalPlayers - numComputerPlayers;
        board = Board.getInstance(args[0], args[1]);
        view = View.getInstance();
    }

    /**
     * initializer (w/ args)
     * @param args
     * @return Game
     */
    public static synchronized Game getInstance(String[] args) {
        if (uniqueInstance == null) {
            uniqueInstance = new Game(args);
        }
        return uniqueInstance;
    }
    
    /**
     * accessor (no args)
     * @return
     */
    public static synchronized Game getInstance() {
        return uniqueInstance;
    }

    /**
     * runs the elements of game to create the board and let the player play it
     */
    public void run(){
        //make sure user enters valid number of players
        if((numTotalPlayers < 1) || (numTotalPlayers > 8)){
            view.showErrorPopUp("Invalid input, please enter a player number from 2 to 8");
            System.out.println("Please enter a number from 2 to 8");
            System.exit(0);
        }
        
        //creates the player queue with diff values according to num players
        players = initPlayers();

        // init and show board and currentPlayer
        board.resetBoard();
        startNewTurn();
        view.show(); // show view as last step of run()

        if (numTotalPlayers == numComputerPlayers) { // if all players are computers, trigger doComputerTurn()
            view.showErrorPopUp("Warning: do not do this option if you are epileptic as the colors of the game are about to flash on-screen for the duration of this computer-player-only game play.");
            doComputerTurn();
        }
    }

    /**
     * Initializes the queue of players, as well as the array of players (for the side panels of the view)
     * based on the number of players that are in the game 
     * @return Queue<Player>
     */
    private Queue<Player> initPlayers() {
        Queue<Player> players = new LinkedList<Player>();
        Player p;
        // set default values
        Set startLocation = board.getSet("trailer"); 
        int startRank = 1; 
        int startCredits = 0;
        boolean playerIsComputer = false;
        numDays = 4;

        // managing the image filepaths for the die assigned to each player
        int numImgs = 6;
        String[] dieImgPaths;
        String dieImgPathStub = "src/main/resources/img/dice_";
        String[] playerDieLetters = {"b", "c", "g", "o", "p", "r", "v", "y"};
        String imgExtension = ".png";

        //changes player values according to number of players
        if (numTotalPlayers >= 7){
            startRank = 2;
            startCredits = 0;
        }
        else if (numTotalPlayers == 6){
            startRank = 1;
            startCredits = 4;
        }
        else if (numTotalPlayers == 5){
            startRank = 1;
            startCredits = 2;
        }
        else if (numTotalPlayers == 4){
        }
        else {
            numDays = 3;
        }

        // Make players
        for(int i = 0; i < numTotalPlayers; i++){
            // determine if this player should be a computer
            // human players get generated first
            if (i >= numHumanPlayers) { 
                playerIsComputer = true;
            } else {
                playerIsComputer = false;
            }

            // Create dieImgPaths
            dieImgPaths = new String[numImgs];
            for (int j = 0; j < numImgs; j++) {
                dieImgPaths[j] = dieImgPathStub + playerDieLetters[i] + (j+1) + imgExtension;
            }
            // Create players
            String tempName;
            if (playerIsComputer == false) { // name human players
                tempName = "player" + (i+1);
            } else { // name computer players
                tempName = "player" + (i+1) + " (computer)";
            }
            if (numTotalPlayers >= 5) {
                p = new Player(startLocation, tempName, startRank, startCredits, dieImgPaths, playerIsComputer);
                p.setAreaData(startLocation.getArea());
                view.setDie(p);
            } else {
                p = new Player(startLocation, tempName, startRank, dieImgPaths, playerIsComputer);
                p.setAreaData(startLocation.getArea());
                view.setDie(p);
            }
            players.add(p);
        }
        playerArray = new Player[players.size()];
        for(int i = 0; i < players.size(); i++){
            playerArray[i] = players.peek();
            players.add(players.remove());
        }
            view.updateSidePanel(playerArray);
        return players;
    }

    /**
     * Grabs the available neighbors and creates a popup with the options or chooses an option randomly, returns the chosen option
     * @return String chosen neighbor
     */
    public String chooseNeighbor() {
        String[] neighbors = currentPlayer.getLocation().getNeighborStrings();
        String result;
        if (currentPlayer.isComputer()) {
            // choose random neighbor
            Random rand = new Random();
            int randInt = rand.nextInt(neighbors.length);
            result = neighbors[randInt];
        }
        else { // if current player is human
            // choose upgrade manually
            result = view.showMovePopUp(neighbors);
        }
        return result;
    }

    /**
     * If the player has not already played, and if they are not employed, prompt the location they would like to move to,
     * set that as their location and refresh the view with the players dice
     */
    public void tryMove() {
        if(!currentPlayer.getHasPlayed()){
            if (!currentPlayer.isEmployed()) {
                String destStr = chooseNeighbor();

                currentPlayer.moveTo(destStr, getBoardSet(destStr));
                if(currentPlayer.getLocation().getFlipStage() == 0){
                    currentPlayer.getLocation().flipSet();
                }
                refreshPlayerPanel();
            }
            else {
                view.showPopUp(currentPlayer.isComputer(), "Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
            }
        }
        else{                    
            view.showPopUp(currentPlayer.isComputer(), "You've already moved, rehearsed or acted this turn. Try a different command or click `end turn` to end turn your turn.");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * Grabs the available roles and gives a popup with the options that the player can choose from or chooses one option at random. Returns the resulting string.
     * @return String
     */
    public String chooseRole() {
        String[] roles = currentPlayer.getLocation().getRoleStrings();
        String result;
        if (currentPlayer.isComputer()) {
            // choose random role
            Random rand = new Random();
            int randInt = rand.nextInt(roles.length);
            result = roles[randInt];
        }
        else { // if current player is human
            // choose upgrade manually
            if(roles.length <= 0){
                view.showPopUp(currentPlayer.isComputer(), "There are no more available roles on this card!");
                result = "";
            }
            else{
                result = view.showRolePopUp(roles);
            }
        }


        return result;
    }

    /**
     * If the player is not employed, and they're in a location that has roles, prompt the roles that they could take,
     * once a role is chosen, give the player that role and occupy it so future players cannot take it, updates the view
     * in the position of the role.
     */
    public void tryTakeRole() {
        if (currentPlayer.isEmployed() == false) {
            if(currentPlayer.getLocation().getName() == "office" || currentPlayer.getLocation().getName() == "trailer"){
                view.showPopUp(currentPlayer.isComputer(), "You are currently in the " + currentPlayer.getLocation().getName() + " please move to a tile that has a role");
            }
            else{
                if(currentPlayer.getLocation().getFlipStage() == 2){
                    view.showPopUp(currentPlayer.isComputer(), "This scene has already finished! You're too late!!");
                }
                else{
                    String chosenRole = chooseRole();
                    if (chosenRole.equals("")) {
                        return;
                    }
                    currentPlayer.takeRole(chosenRole);

                    if (currentPlayer.isEmployed()) {
                        if(
                            !board.isOnCard(
                                currentPlayer.getRole().getName(), currentPlayer.getLocation())
                            )
                        {
                            currentPlayer.setOnCardAreaData(currentPlayer.getRole().getArea());
                        }
                        else
                        {
                            currentPlayer.setAreaData(currentPlayer.getRole().getArea());
                        }
                        refreshPlayerPanel();
                        
                    }
                }
            }
        } else {
            view.showPopUp(currentPlayer.isComputer(), "You're already employed, so you can't take another role until you finish this one");
        }   
        view.updateSidePanel(playerArray);
    }

    /**
     * Grabs the available roles and gives a popup with the options that the player can choose from or tries to upgrade to rank 1 greater than player's current rank. Returns the resulting string.
     * @return String
     */
    private void upgrade() {
        String[] upgradeOptions = currentPlayer.getLocation().getUpgradeStrings(currentPlayer.getRank());
        // String[] upgradeTypes = {"credits", "dollars"}; // credits come before dollars for each rank in getUpgradeStrings()
        // String upgradeCostChosen;
        // String upgradeTypeChosen;
        String upgradeChoice;

        // choose upgrade
        if (currentPlayer.isComputer()) {
            // get next rank above player's rank
            // String upgradeRankChosen = Integer.toString(currentPlayer.getRank()+1);
    
            // randomly choose between the credit option or the dollar option for the next rank 1 above the player's current rank
            Random rand = new Random();
            int randInt = rand.nextInt(2); 
            upgradeChoice = upgradeOptions[randInt];
        }
        else { // if current player is human
            // choose upgrade manually
            upgradeChoice = view.showUpgradePopUp(upgradeOptions);
        }

        // parse upgradeChosen and do upgrade
        String[] upgradesSplit = upgradeChoice.split("\\s+");
        if(upgradesSplit[4].equals("dollars")){ // upgrade in dollars
            if(Integer.valueOf(upgradesSplit[3]) > currentPlayer.getDollars()){ // upgrade fails
                view.showPopUp(currentPlayer.isComputer(), "You don't have enough dollars for this upgrade");
            }
            else{ // upgrade succeeds
                currentPlayer.incDollars(-1*(Integer.valueOf(upgradesSplit[3])));
                currentPlayer.setRank(Integer.valueOf(upgradesSplit[1]));
                view.showPopUp(currentPlayer.isComputer(), "You are now level " + upgradesSplit[1]);
            }
        }
        else { // upgrade in credits
            if(Integer.valueOf(upgradesSplit[3]) > currentPlayer.getCredits()){ // upgrade fails
                view.showPopUp(currentPlayer.isComputer(), "You don't have enough credits for this upgrade");
            }
            else{ // upgrade succeeds
                currentPlayer.incCredits(-1*(Integer.valueOf(upgradesSplit[3])));
                currentPlayer.setRank(Integer.valueOf(upgradesSplit[1]));
                view.showPopUp(currentPlayer.isComputer(), "You are now level " + upgradesSplit[1]);
                
            }
        }
    }

    /**
     * If the player is located in the office and are not already max level, give a popup with the upgrade options and their prices
     * check if the player can afford the one that they've chosen, and assign the player their new rank, update their die on the board
     */
    public void tryUpgrade() {
        int maxLevel = 6;
        if(currentPlayer.getLocation().getName().equals("office")){
            if(currentPlayer.getRank() < maxLevel){ // player not at max level, allow upgrade
                upgrade();
                
                refreshPlayerPanel();
                view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath(), numDays);
            }
            else{
                view.showPopUp(currentPlayer.isComputer(), "You are already max level: " + maxLevel);
            }
            
        }
        else{
            view.showPopUp(currentPlayer.isComputer(), "You are not located in the office, move to the office");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * If the player is employed and they are not already at rehearsal tokens needed to succeed, give the player a rehearsal token, refresh the board
     */
    public void tryRehearse() {
        if (currentPlayer.isEmployed() == true) {
            if(!currentPlayer.getHasPlayed()){
                currentPlayer.rehearse();
            }
            else{                    
                view.showPopUp(currentPlayer.isComputer(), "You've already moved, rehearsed or acted this turn. Try a different command or click `end turn` to end your turn.");
            }
        }
        else {
            view.showPopUp(currentPlayer.isComputer(), "You're not employed, so you need to take a role before you can rehearse.");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * if the player is employed, let them attempt to act. If they are successful, pay them the amount that they are owed and remove a shot token
     * if that was the last shot, then wrap up the scene and pay dues if there are players on the card. release the actors from their roles
     * and update the board with the changes that were made.
     */
    public void tryAct() {

        List<Player> onCardPlayers = new ArrayList<Player>();
        List<Player> offCardPlayers = new ArrayList<Player>();
        if (currentPlayer.isEmployed() == true) {
            if(!currentPlayer.getHasPlayed()){
                onCardPlayers = findPlayers(currentPlayer.getLocation().getOnCardRoles());
                offCardPlayers = findPlayers(currentPlayer.getLocation().getOffCardRoles());
                currentPlayer.act(onCardPlayers, offCardPlayers);
                refreshPlayerPanel();
            }
            else{
                view.showPopUp(currentPlayer.isComputer(), "You've already moved, rehearsed or acted this turn. Try a different command or click `end turn` to end your turn.");
            }
        }
        else {
            view.showPopUp(currentPlayer.isComputer(), "You're not employed, so you need to take a role before you can act.");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * Plays turn for computer player by selecting random actions
     * @param none
     * @return void
     */
    public void doComputerTurn() {
        Random rand = new Random();
        int randInt;

        if (!currentPlayer.isEmployed()) { // player not employed
            // moves (but suppresses popups) at random and update of player panel
            tryMove();

            if (!(currentPlayer.getLocation().getName() == "trailer")) {
                if (currentPlayer.getLocation().getName() == "office") { // in office
                    // upgrade (but suppresses popups) at random and update of player panel
                    tryUpgrade();
                    
                } else { // in regular set
                    // take role (but suppresses popups) at random and update of player panel
                    tryTakeRole();
                }
            }
            endTurn();
        } else { // player is employed
            randInt = rand.nextInt(2); // get either 0 or 1
            if (randInt == 0) {
                // rehearse
                tryRehearse(); 
            } else {
                // act
                tryAct();
            }

            endTurn();
        }
    }

    /**
     * change who is next in the queue and place the old current back in the queue, update the view with who is currently playing
     */
    public void startNewTurn(){
        currentPlayer = players.peek();
        players.add(players.remove());
        view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath(), numDays);
        currentPlayer.setHasPlayed(false);
    }

    /**
     * for all of the players, remove them from any current roles, place them in the trailer and update the board
     */
    public void resetPlayers() {
        Player curPlayer;
        for(int i = 0; i < players.size(); i++){
            curPlayer = players.peek();
            curPlayer.resetRole();
            curPlayer.setLocation(board.getSet("trailer"));
            curPlayer.setAreaData(curPlayer.getLocation().getArea());
            view.setDie(curPlayer);
            players.add(players.remove());
        }

    }

    public void endTurn() {
        if (board.getSceneNum() <= 1) { // day ends
            if (numDays > 0) { // game continues
                //decrement days and reset the roles and board
                numDays--;
                if (currentPlayer.isComputer() == false) {
                    view.showPopUp(currentPlayer.isComputer(), "Its the end of the day! " + numDays + " days remain");
                }
                
                view.clearDice();
                board.resetBoard();
                // reset players
                resetPlayers();
            }
            else { // game ends
                view.showPopUp(currentPlayer.isComputer(), "Calculating winner...");
                calcWinner();
                System.exit(0);
            }
        }

        // set up for new turn
        startNewTurn();

        // rotate player
        view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath(), numDays);
        view.showPopUp(currentPlayer.isComputer(), "It is now " + currentPlayer.getName() + "'s turn");
        view.updateSidePanel(playerArray);
        
        if (currentPlayer.isComputer() == true) {
            doComputerTurn();
        }
    }
    
    /**
     * Returns a list of all of the people who are employed in a list of roles, this is used to see who else works in a scene 
     * to see who gets paid for a scene wrap up
     * @param rl
     * @return List<Player>
     */
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

    /**
     * returns the set with the name given
     * @param s
     * @return Set
     */
    public Set getBoardSet(String s){
        return board.getSet(s);
    }

    /**
     * clears the panel with the dice on is, and then iterates through the players to set them back down in their new positions if 
     * those positions were changed.
     */
    public void refreshPlayerPanel() {
        view.clearDice();
        Player curPlayer;
        
        for(int i = 0; i < players.size(); i++){
            curPlayer = players.peek();
            
            view.setDie(curPlayer);
            players.add(players.remove());
            
        }
        board.reloadImgs();
    }

    /**
     * Goes through each of the players and compares the final values with each other to see who got the most amount of points
     *then displays the winner(s) 
     */
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
        } 

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

        // Announce winners
        boolean hideCalculatingWinnerPopUp = false;
        String winnersString = "";
        if (winners.size() > 1) {
            winnersString = "There's a tie with " + winners.get(0).calcFinalScore() + " points. The following players tied: ";
            for (Player p : winners) {
                winnersString += p.getName() + " ";
            }
            view.showPopUp(hideCalculatingWinnerPopUp, winnersString);
        } else {
            view.showPopUp(hideCalculatingWinnerPopUp, winners.get(0).getName() + " wins with a score of " + winners.get(0).calcFinalScore() + " points! Clicking `ok` will end and close the game.");
        }
    }

}
