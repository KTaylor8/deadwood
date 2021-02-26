import java.util.*;

public class Deadwood{
    static private int numDays;
    
    static private Queue<Player> players = new LinkedList<Player>();
    static private int numPlayers;
    static private Board board;
    static private UI ui;
    static private String input;
    static private Set startLocation;
    static private int startRank;
    static private int startCredits;

    static private Player currentPlayer;
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
        
        //calc final Score
        
        numPlayers = Integer.valueOf(args[2]); 
        boardPath = args[0];
        cardPath = args[1];
        board = new Board(boardPath, cardPath);
        ui = new UI();

        //make sure user enters valid number
        while(!(numPlayers > 1) && !(numPlayers < 9)){
            ui.print("Invalid input, please enter a player number from 2 to 8");
            System.exit(0);
        }

        //  intro statement
        ui.print("Welcome to Deadwood! Start with naming your characters");
        
        //creates the player queue with diff values according to num players
        startLocation = board.getSet("trailer");
        startRank = 1;
        startCredits = 0;
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

        players = initPlayers(startLocation, startRank, startCredits, numPlayers, numDays);

        
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
        winners = calcWinner(players);
        if (winners.size() > 1) {
            ui.print("There's a tie with " + winners.get(0).calcFinalScore() + " points. The following players tied:");
            for (Player p : winners) {
                ui.print(p.getName());
            }
        } else {
            ui.print(winners.get(0).getName() + " wins with " + winners.get(0).calcFinalScore() + "!");
        }
        ui.closeScanner();
    }

    private static Queue<Player> initPlayers(Set sl, int sr, int sc, int np,  int nd) {
        Queue<Player> players = new LinkedList<Player>();
        Player p;

        for(int i = 1; i <= np; i++){
            ui.print("What is the name of player " + i +"?");
            input = ui.readInput();
            if (np >= 5) {
                p = new Player(sl, input, sr, sc);
            } else {
                p = new Player(sl, input);
            }
            players.add(p);
        }

        return players;
    }

    //to calculate winner
    private static List<Player> calcWinner(Queue<Player> players){
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

}
