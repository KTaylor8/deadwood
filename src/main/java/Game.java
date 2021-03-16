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

    /**
     * Creates the elements of the game
     * @param args
     */
    private Game (String[] args) {
        numPlayers = Integer.valueOf(args[2]); 
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
        //make sure user enters valid number
        if((numPlayers < 1) || (numPlayers > 8)){
            view.showPopUp("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        
        //creates the player queue with diff values according to num players
        players = initPlayers();

        // init and show board and currentPlayer
        board.resetBoard();
        startNewTurn();
        view.show(); // show view as last step of run()
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
        numDays = 4;

        // managing the image filepaths for the die assigned to each player
        int numImgs = 6;
        String[] dieImgPaths;
        String dieImgPathStub = "src/main/resources/img/dice_";
        String[] playerDieLetters = {"b", "c", "g", "o", "p", "r", "v", "y"};
        String imgExtension = ".png";

        //changes player values according to number of players
        if((numPlayers < 2) || (numPlayers > 8)){
            System.out.println("Please enter a number from 2 to 8");
            System.exit(0);
        }
        if (numPlayers >= 7){
            startRank = 2;
            startCredits = 0;
        }
        else if (numPlayers == 6){
            startRank = 1;
            startCredits = 4;
        }
        else if (numPlayers == 5){
            startRank = 1;
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
            String tempName = "player" + (i+1); 
            if (numPlayers >= 5) {
                p = new Player(startLocation, tempName, startRank, startCredits, dieImgPaths);
                p.setAreaData(startLocation.getArea());
                view.setDie(p);
            } else {
                p = new Player(startLocation, tempName, startRank, dieImgPaths);
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
     * Grabs the available neighbors and creates a popup with the options, returns the chosen option
     * @return String
     */
    public String chooseNeighbor() {
        String[] neighbors = currentPlayer.getLocation().getNeighborStrings();
        String result = view.showMovePopUp(neighbors);
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
                //currentPlayer.setAreaData(getBoardSet(destStr).getArea());
                currentPlayer.moveTo(destStr, getBoardSet(destStr));
                if(currentPlayer.getLocation().getFlipStage() == 0){
                    currentPlayer.getLocation().flipSet();
                }
                refreshPlayerPanel(); // this is layering more components over the others but if it works it works
            }
            else {
                view.showPopUp("Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
            }
        }
        else{                    
            view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or click `end` to end your turn.");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * Grabs the available roles and gives a popup with the options that the player can choose from. Returns the resulting string.
     * @return String
     */
    public String chooseRole() {
        String[] roles = currentPlayer.getLocation().getRoleStrings();
        if(roles.length <= 0){
            view.showPopUp("There are no more available roles on this card!");
            return "";
        }
        else{
            String result = view.showRolePopUp(roles);
            return result;
        }
    }

    /**
     * If the player is not employed, and theyre in a location that has roles, prompt the roles that they could take,
     * once a role is chosen, give the player that role and occupy it so future players cannot take it, updates the view
     * in the position of the role.
     */
    public void tryTakeRole() {
        if (currentPlayer.isEmployed() == false) {
            if(currentPlayer.getLocation().getName() == "office" || currentPlayer.getLocation().getName() == "trailer"){
                view.showPopUp("You are currently in the " + currentPlayer.getLocation().getName() + " please move to a tile that has a role");
            }
            else{
                if(currentPlayer.getLocation().getFlipStage() == 2){
                    view.showPopUp("This scene has already finished! You're too late!!");
                }
                else{
                    String chosenRole = chooseRole();
                    if (chosenRole.equals("")) {
                        
                        return;
                    }
                    currentPlayer.takeRole(chosenRole);
                    currentPlayer.getRole().occupy();

                    if(!board.isOnCard(currentPlayer.getRole().getName(), currentPlayer.getLocation())){
                        currentPlayer.setOnCardAreaData(currentPlayer.getRole().getArea());
                    }
                    else{
                        currentPlayer.setAreaData(currentPlayer.getRole().getArea());
                    }
                    refreshPlayerPanel();
                    }
            }
        } else {
            view.showPopUp("You're already employed, so you can't take another role until you finish this one");
        }   
        view.updateSidePanel(playerArray);
    }

    /**
     * If the player is located in the office and are not already max level, give a popup with the upgrade options and their prices
     * check if the player can afford the one that theyve chosen, and assign the player their new rank, update their die on the board
     */
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
                        view.showPopUp("You are now level " + splited[1]);
                    }
                }
                else{
                    if(Integer.valueOf(splited[3]) > currentPlayer.getCredits()){
                        view.showPopUp("You don't have enough credits for this upgrade");
                    }
                    else{
                        currentPlayer.incCredits(-1*(Integer.valueOf(splited[3])));
                        currentPlayer.setRank(Integer.valueOf(splited[1]));
                        view.showPopUp("You are now level " + splited[1]);
                        
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
        view.updateSidePanel(playerArray);
    }

    /**
     * If the player is employed and they are not already at rehearsal tokens needed to succeed, give the player a rehearsal token, refreshe the board
     */
    public void tryRehearse() {
        if (currentPlayer.isEmployed() == true) {
            if(!currentPlayer.getHasPlayed()){
                currentPlayer.rehearse();
            }
            else{                    
                view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or click `end` to end your turn.");
            }
        }
        else {
            view.showPopUp("You're not employed, so you need to take a role before you can rehearse.");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * if the player is employed, let them attempt to act. If they are successful, pay them the amount that they are owed and remove a shot token
     * if that was the last shot, then wrapup the scene and pay dues if there are players on the card. release the actors from their roles
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
                //board.reloadImgs();
                refreshPlayerPanel();
            }
            else{
                view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or click `end` to end your turn.");
            }
        }
        else {
            view.showPopUp("You're not employed, so you need to take a role before you can act.");
        }
        view.updateSidePanel(playerArray);
    }

    /**
     * change who is next in the queue and place the old current back in the queue, update the view with who is currently playing
     */
    public void startNewTurn(){
        currentPlayer = players.peek();
        players.add(players.remove());
        view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath());
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

    /**
     * When the player wants to end their turn, check to see if there is 1 scene remaining, if there is, end the day
     * If that day was the last one, calculate who is the winner and display it, end the game.
     */
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
                
                view.clearDice();
                board.resetBoard();
                // reset players
                resetPlayers();
            }
            else { // game ends
                view.showPopUp("Calculating winner...");
                calcWinner();
                System.exit(0);
                // do something to freeze the screen and not allow 
            }
            startNewTurn();
            view.changeCurrentPlayer(currentPlayer.getName(), currentPlayer.getPlayerDiePath());
            view.showPopUp("It is now " + currentPlayer.getName() + "'s turn");
        }
        view.updateSidePanel(playerArray);

    }

    
    /**
     * Returns a lsit of all of the people who are employed in a list of roles, this is used to see who else works in a scene 
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
     * Goes through each of the players and compares the final values with eachother to see who got the most amount of points
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
