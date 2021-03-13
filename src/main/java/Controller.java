// import java.util.*;

public class Controller{

    // private Scanner scan;
    View view;
    Game game;

    public Controller(String[] args) {
        // scan = new Scanner(System.in);
        View view = View.getInstance(this);
        game = Game.getInstance(args);
        game.registerObserver(view);
    }

    // public String readInput() {
    //     return scan.nextLine().trim();
    // }

    // public void print(String info) {
    //     System.out.println(info);
    // }

    // public void closeScanner() {
    //     scan.close();
    // }

    public String chooseRole() {
        String roleChosen = "";
        // lets user choose role from interface
        return roleChosen;
    }

    public String[] chooseUpgrade() {
        String[] upgradeChosen = {"dollars", "2"};
        // lets user choose upgrade from interface
        return upgradeChosen;
    }

    public void process(String res) {
        if (res.equals("move")) {
            // currentPlayer.moveTo(destStr, game.getBoardSet(destStr));
            game.tryMove();
        } else if (res.equals("take role")) {
            // showPopUp("dont take that role, trust me");
           // chooseRole(); // need to make function that lets user choose role from those available
            game.tryTakeRole();
        } else if (res.equals("upgrade")) {
            // showPopUp("upgrades people, upgrades");

            String[] upgradeChoices = chooseUpgrade(); // have a way for user to choose upgrade type and rank, should return a String[] w/ the 2 values
            game.tryUpgrade(upgradeChoices);

        } else if (res.equals("rehearse")) {
            // showPopUp("oh honey, you're gonna need something a lil more than rehearsing");
            game.tryRehearse();
        } else if (res.equals("act")) {
            // showPopUp("ha, yea right");
            game.tryAct();
        } else if (res.equals("end")) {
            // controller.endTurn();
            game.endTurn();
        }
    }


    // public void tryMove(String destination) {
    //     // if (game.getCurrentPlayer().getHasPlayed()) {
    //     //     view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
    //     //     continue;
    //     // }
    //     // else if(input.equals("move")){
    //     //     view.showPopUp("Please enter a place you want to move after \"move\"");
    //     // } else {
    //     //      hasPlayed = currentPlayer.moveTo(board.getSet(input.substring(5)), view);
    //     // }
    //     currentPlayer.moveTo(destination, game.getBoardSet(destination));
    //     currentPlayer.setHasPlayed(true);
    // }

    // //if player wants to take role and are not employed, let them take the role
    // public void tryTakeRole(String desiredRoleName) {
    //     if (currentPlayer.isEmployed() == false) {
    //         // LATER: THE METHOD CALLED NEEDS REFACTORING
    //         currentPlayer.takeRole(desiredRoleName);
    //         if (currentPlayer.isEmployed() == true) {
    //             // board.fillRole(location, role); // <-- this method needs to be simplified later; it's hard to follow currently
    //             currentPlayer.getRole().occupy();
    //         }
    //     } else {
    //         view.showPopUp("You're already employed, so you can't take another role until you finish this one");
    //     }   
    // }

    // public void tryUpgrade(String[] choices) { // change args based on View stuffs
    //     // let use pick dollars or credits somehow
    //     // boolean isDollars = choice; // set to always dollars for now

    //     if (choices[0].equals("Dollars")) {
    //         currentPlayer.upgrade(
    //             game.getDollarCost(), 
    //             currentPlayer.getDollars(), 
    //             Integer.valueOf(choices[1])
    //         );
    //     } 
    //     else {
    //         currentPlayer.upgrade(
    //             game.getCreditCost(), 
    //             currentPlayer.getCredits(), 
    //             Integer.valueOf(choices[1])
    //         );
    //     }
    // }

    // public void tryRehearse() {
    //     //if player hasn't played yet this turn
    //     if(!currentPlayer.getHasPlayed()){
    //         currentPlayer.rehearse();
    //         currentPlayer.setHasPlayed(true);
    //     }
    //     else{                    
    //         view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
    //     }
    // }

    // public void tryAct() {
    //     List<Role> onCardPlayers = new ArrayList<Role>();
    //     List<Role> offCardPlayers = new ArrayList<Role>();

    //     game.findPlayers(onCardPlayers);
    //     game.findPlayers(offCardPlayers);

    //     if(!game.getCurrentPlayer().getHasPlayed()){
    //         currentPlayer.act(onCardPlayers, offCardPlayers); //passing in find...CardPlayers b/c otherwise I'd have to pass in the queue of all the players and that seems like too much info
    //         currentPlayer.setHasPlayed(true);
    //     }
    //     else{
    //         view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
    //     }
    // }

    // public void endTurn() {
    //     game.changeTurn();
    //     // game.;
    // }

    // public void run() {

    // }


    //big boi method for each players turn
    // public void interact(Player currentPlayer, Board board, Queue<Player> players){
                
        // boolean hasPlayed = false; 
        // String input = "";
        // view.showPopUp("What would you like to do, " + currentPlayer.getName() + "?");
        
        //while the player doesn't say they want their turn to end
        // while(!input.equals("end")){
        //     // input = this.readInput();

        //     if (input.equals("end")) {
        //         break;
        //     }
 
            //if player wants to move
            // else if(input.contains("move")){

            // }

            //if player wants to take role and are not employed, let them
            // else if(input.contains("take role")){
            //     if (currentPlayer.isEmployed() == false) {
            //         // LATER: THE METHOD CALLED NEEDS REFACTORING
            //         currentPlayer.takeRole(board, input.substring(10));
            //     } else {
            //         view.showPopUp("You're already employed, so you can't take another role until you finish this one");
            //     }                
            // }
            //if player wants to upgrade using dollars
            // else if(input.contains("upgrade d")){
            //     currentPlayer.upgrade(
            //         board.getDollarCost(), 
            //         currentPlayer.getDollars(), 
            //         Integer.valueOf(input.substring(10))
            //     );
            // }
            //if player wants to upgrade using credits
            // else if(input.contains("upgrade c ")){
            //     currentPlayer.upgrade(
            //         board.getCreditCost(), 
            //         currentPlayer.getCredits(), 
            //         Integer.valueOf(input.substring(10))
            //     );
            // }
            //gets the cost of every upgrade -- I don't think this is needed in the GUI version b/c the board show it, right?
            // else if(input.equals("upgrade costs")){
            //     int[] dd = board.getDollarCost();
            //     int[] cc = board.getCreditCost();
            //     view.showPopUp("Upgrade costs if using dollars:");
            //     for(int i = 0; i < dd.length; i++){
            //         view.showPopUp("\tLevel " + (i+2) + ": $" + dd[i]);
            //     }
            //     view.showPopUp("Upgrade costs if using credits:");
            //     for(int i = 0; i < cc.length; i++){
            //         view.showPopUp("\tLevel " + (i+2) +": " + cc[i] + " credits");
            //     }
            // }
 
            //if player wants to act
            // else if(input.equals("act")){
            //     if(!game.getCurrentPlayer().getHasPlayed()){
            //         hasPlayed = currentPlayer.act(
            //             view, findPlayers(players, currentPlayer.getLocation().getOnCardRoles()), findPlayers(players, currentPlayer.getLocation().getOffCardRoles())
            //         ); //passing in find...CardPlayers b/c otherwise I'd have to pass in the queue of all the players and that seems like too much info
            //     }
            //     else{
            //         view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
            //     }
            // }
            //if player wants to rehearse 
            // else if(input.equals("rehearse")){
            //     //if player hasn't played yet this turn
            //     if(!hasPlayed){
            //         hasPlayed = currentPlayer.rehearse();
            //     }
            //     else{                    
            //         view.showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
            //     }
            // }

            //catch bad things
            // else{
            //     view.showPopUp("unknown command, try again");
            // }


                       //prints whose turn and dollar and credits
            // else if(input.equals("who")){
            //     view.showPopUp(currentPlayer.getName() + "($" + currentPlayer.getDollars() + ", " + currentPlayer.getCredits() + "cr) working as a " + currentPlayer.getRoleName() + " with " + currentPlayer.getRehearseTokens() + " rehearsal tokens.");
            // }
            // //prints where current player is
            // else if(input.equals("where")){
            //     view.showPopUp(currentPlayer.getLocation().getName());
            // }
            //prints where all players are
            // else if(input.equals("where all")){
            //     //print current player first
            //     view.showPopUp("Current player " + currentPlayer.getName() + " location: " + currentPlayer.getLocation().getName());
            //     //print the remaining players 
            //     for(int i = 0; i < players.size() - 1; i++){
            //         view.showPopUp((players.peek()).getName() + " is located at: " + (players.peek()).getLocation().getName());
            //         //make sure current player is place back at last in queue
            //         players.add(players.remove());
            //     }
            //     //make sure current player is place back at last in queue
            //     players.add(players.remove());
            // }
            //prints adjacent tiles to player
            // else if(input.equals("neighbors")){
            //     List<String> n = board.getNeighbors(currentPlayer.getLocation().getName());
            //     view.showPopUp("Your neighbors are: ");
            //     for(int i = 0; i < n.size(); i++){
            //         view.showPopUp("- " + n.get(i));
            //     }
            // }
            // else if(input.equals("available roles")){
            //     view.showPopUp(board.freeRoles(currentPlayer.getLocation().getName()));
            // }
        // }
    // }
}