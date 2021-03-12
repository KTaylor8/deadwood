import java.util.*;

public class Game{
    private int numDays;
    private Queue<Player> players = new LinkedList<Player>();
    private int numPlayers;
    private Board board;
    private UI ui;
    private String input;
    private View view;
    private boolean hasPlayed = false; 

    private Player currentPlayer;

    public Game (String[] args) {
        numPlayers = Integer.valueOf(args[2]); 
        board = new Board(args[0], args[1]);
        view  = new View(this);
        ui = new UI(view);
    }

    public void run(){
        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            view.popUp("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        //  intro statement
        //controller.popUp("Welcome to Deadwood!");
        
        //creates the player queue with diff values according to num players
        players = initPlayers();

        // init and show board
        board.resetBoard(view);
        view.show();
        changeTurn();
        //iterates through the day
        while(numDays != 0){
            //ui.print("Placing all players in trailers");
            while(board.sceneNum() > 1){
                //currentPlayer = players.peek();
                //players.add(players.remove());
                //view.changeCurrentPlayer(currentPlayer.getName());
                //ui.interact(currentPlayer, board, players);
            }
            //decrement days and reset the roles and board
            numDays--;
            view.popUp("Its the end of the day! " + numDays + " days remain");
            board.resetBoard(view);

            // reset players
            resetPlayers(players);
        }

        //calculate winner
        //ui.print("Calculating winner...");
        
        calcWinner(players);
        //ui.closeScanner();
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
                p = new Player(startLocation, tempName, startRank, startCredits, dieImgPaths);
                view.resetPlayerDie(p, i);
            } else {
                p = new Player(startLocation, tempName, dieImgPaths);
                view.resetPlayerDie(p, i);
            }
            players.add(p);
        }

        return players;
    }

    public void changeHasPlayed(Boolean b){
        hasPlayed = b;
    }

    public boolean haveTheyPlayed(){
        return hasPlayed;
    }

    public void changeTurn(){
        currentPlayer = players.peek();
        players.add(players.remove());
        view.changeCurrentPlayer(currentPlayer.getName());
        hasPlayed = false;
    }

    // to reset players
    public void resetPlayers(Queue<Player> players) {
        Player curPlayer;
        for(int i = 0; i < players.size(); i++){
            curPlayer = players.peek();
            curPlayer.resetRole();
            curPlayer.setLocation(board.getSet("trailer"));
            view.resetPlayerDie(curPlayer, i);
            players.add(players.remove());
        }
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Set getBoardSet(String s){
        return board.getSet(s);
    }

    public void refreshPlayerPanel(View view) {
        view.clearDice();
        Player curPlayer;
        for(int i = 0; i < players.size(); i++){
            curPlayer = players.peek();
            view.resetPlayerDie(curPlayer, i);
            players.add(players.remove());
        }
    }

    //to calculate winner
    private void calcWinner(Queue<Player> players){
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
            view.popUp(winnersString);
        } else {
            view.popUp(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
    }

}
