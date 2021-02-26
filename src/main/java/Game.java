import java.util.*;

public class Game{
    private int numDays;
    private Queue<Player> players = new LinkedList<Player>();
    private int numPlayers;
    private Board board;
    private UI ui;
    private String input;

    private Player currentPlayer;

    public Game (String[] args) {
        numPlayers = Integer.valueOf(args[2]); 
        board = new Board(args[0], args[1]);
        ui = new UI();
    }

    public void run(){
        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            ui.print("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        //  intro statement
        ui.print("Welcome to Deadwood! Start with naming your characters");
        
        //creates the player queue with diff values according to num players
        players = initPlayers();
        
        //iterates through the day
        while(numDays != 0){
            ui.print("Placing all players in trailers");
            while(board.sceneNum() > 1){
                currentPlayer = players.peek();
                players.add(players.remove());
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
        ui.print("Calculating winner...");
        calcWinner(players);
        ui.closeScanner();
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
            ui.print("What is the name of player " + i +"?");
            input = ui.readInput();
            if (numPlayers >= 5) {
                p = new Player(startLocation, input, startRank, startCredits);
            } else {
                p = new Player(startLocation, input);
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
        
        // Announce winners
        if (winners.size() > 1) {
            ui.print("There's a tie with " + winners.get(0).calcFinalScore() + " points. The following players tied:");
            for (Player p : winners) {
                ui.print(p.getName());
            }
        } else {
            ui.print(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
    }

}
