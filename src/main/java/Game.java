import java.util.*;

public class Game{
    private int numDays;
    private Queue<Player> players = new LinkedList<Player>();
    private int numPlayers;
    private Board board;
    private UI ui;
    private String input;
    private Controller controller;

    private Player currentPlayer;

    public Game (String[] args) {
        numPlayers = Integer.valueOf(args[2]); 
        board = new Board(args[0], args[1]);
        ui = new UI();
        controller = new Controller();
        controller.run();
    }

    public void run(){

        

        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            controller.popUp("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        //  intro statement
        //controller.popUp("Welcome to Deadwood!");
        
        //creates the player queue with diff values according to num players
        players = initPlayers();
        
        //iterates through the day
        while(numDays != 0){
            //ui.print("Placing all players in trailers");
            while(board.sceneNum() > 1){
                currentPlayer = players.peek();
                players.add(players.remove());
                controller.updateCurrentPlayer(currentPlayer.getName());
                ui.interact(currentPlayer, board, players);
            }
            //decrement days and reset the roles and board
            numDays--;
            ui.print("Its the end of the day! " + numDays + " days remain");
            board.resetBoard();
            for(int i = 0; i < players.size(); i++){
                (players.peek()).resetRole();
                (players.peek()).setLocation(board.getSet("trailer"));
                players.add(players.remove());
            }
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

        // Create players
        for(int i = 1; i <= numPlayers; i++){
            if (numPlayers >= 5) {
                p = new Player(startLocation, ("player" + i), startRank, startCredits);
            } else {
                p = new Player(startLocation, ("player" + i));
            }
            players.add(p);
        }

        return players;
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
            controller.popUp(winnersString);
        } else {
            controller.popUp(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
    }

}
