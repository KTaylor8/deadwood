import java.util.*;

public class UI{

    private Scanner scan;

    public UI() { scan = new Scanner(System.in); }

    public String readInput() {
        return scan.nextLine().trim();
    }

    public void print(String info) {
        System.out.println(info);
    }

    public void closeScanner() {
        scan.close();
    }

    //returns list of people who are on card for a set
    private List<Player> findPlayers(Queue<Player> players, List<Role> roles){
        List<Player> pl = new ArrayList<Player>();
        for(Player p: players){
            for(Role r: roles){
                if((r.getName()).equals(p.getRoleName())){
                    pl.add(0, p);
                }
            }
        }
        return pl;
    }

    //big boi method for each players turn
    public void interact(Player currentPlayer, Board board, Queue<Player> players){
                
        boolean hasPlayed = false; 
        String input = "";
        this.print("What would you like to do, " + currentPlayer.getName() + "?");
        
        //while the player doesn't say they want their turn to end
        while(!input.equals("end")){
            
            input = this.readInput();

            if (input.equals("end")) {
                break;
            }
            //prints whose turn and dollar and credits
            else if(input.equals("who")){
                this.print(currentPlayer.getName() + "($" + currentPlayer.getDollars() + ", " + currentPlayer.getCredits() + "cr) working as a " + currentPlayer.getRoleName() + " with " + currentPlayer.getRehearseTokens() + " rehearsal tokens.");
            }
            //prints where current player is
            else if(input.equals("where")){
                this.print(currentPlayer.getLocation().getName());
            }
            //prints where all players are
            else if(input.equals("where all")){
                //print current player first
                this.print("Current player " + currentPlayer.getName() + " location: " + currentPlayer.getLocation().getName());
                //print the remaining players 
                for(int i = 0; i < players.size() - 1; i++){
                    this.print((players.peek()).getName() + " is located at: " + (players.peek()).getLocation().getName());
                    //make sure current player is place back at last in queue
                    players.add(players.remove());
                }
                //make sure current player is place back at last in queue
                players.add(players.remove());
            }
            //prints adjacent tiles to player
            else if(input.equals("neighbors")){
                List<String> n = board.getNeighbors(currentPlayer.getLocation().getName());
                this.print("Your neighbors are: ");
                for(int i = 0; i < n.size(); i++){
                    this.print("- " + n.get(i));
                }
            }
            else if(input.equals("available roles")){
                this.print(board.freeRoles(currentPlayer.getLocation().getName()));
            }
            
            //if player wants to take role and are not employed, let them
            else if(input.contains("take role")){
                if (currentPlayer.isEmployed() == false) {
                    // LATER: THE METHOD CALLED NEEDS REFACTORING
                    currentPlayer.employ(board, input.substring(10));
                } else {
                    this.print("You're already employed, so you can't take another role until you finish this one");
                }                
            }
            //if player wants to upgrade using dollars
            else if(input.contains("upgrade d")){
                currentPlayer.upgrade(
                    board.getDollarCost(), 
                    currentPlayer.getDollars(), 
                    Integer.valueOf(input.substring(10))
                );
            }
            //if player wants to upgrade using credits
            else if(input.contains("upgrade c ")){
                currentPlayer.upgrade(
                    board.getCreditCost(), 
                    currentPlayer.getCredits(), 
                    Integer.valueOf(input.substring(10))
                );
            }
            //gets the cost of every upgrade
            else if(input.equals("upgrade costs")){
                int[] dd = board.getDollarCost();
                int[] cc = board.getCreditCost();
                this.print("Upgrade costs if using dollars:");
                for(int i = 0; i < dd.length; i++){
                    this.print("\tLevel " + (i+2) + ": $" + dd[i]);
                }
                this.print("Upgrade costs if using credits:");
                for(int i = 0; i < cc.length; i++){
                    this.print("\tLevel " + (i+2) +": " + cc[i] + " credits");
                }
            }
            //if player wants to move
            else if(input.contains("move")){
                if (hasPlayed) {
                    this.print("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
                    continue;
                }
                else if(input.equals("move")){
                    this.print("Please enter a place you want to move after \"move\"");
                } else {
                    hasPlayed = currentPlayer.moveTo(board.getSet(input.substring(5)));
                }
            }
            //if player wants to act
            else if(input.equals("act")){
                if(!hasPlayed){
                    hasPlayed = currentPlayer.act(
                        findPlayers(players, currentPlayer.getLocation().getOnCardRoles()), findPlayers(players, currentPlayer.getLocation().getOffCardRoles())
                    ); //passing in find...CardPlayers b/c otherwise I'd have to pass in the queue of all the players and that seems like too much info
                }
                else{
                    this.print("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
                }
            }
            //if player wants to rehearse 
            else if(input.equals("rehearse")){
                //if player hasn't played yet this turn
                if(!hasPlayed){
                    hasPlayed = currentPlayer.rehearse();
                }
                else{                    
                    this.print("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
                }
            }

            //catch bad things
            else{
                this.print("unknown command, try again");
            }
        }
    }
}