import java.util.*;

public class Player{

    private String playerName;
    private String[] playerDiePaths;
    private int rank = 1;
    private int dollars = 0;
    private int credits = 0;
    private boolean employed = false; 
    private int rehearseTokens = 0;
    private Set location;
    private String roleName;
    private UI ui = new UI();

    public Player(Set s, String p, String[] paths){
        playerName = p;
        location = s;
        playerDiePaths = paths;
    }

    public Player(Set s, String p, int sr, int sc, String[] paths){
        playerName = p;
        rank = sr;
        location = s;
        playerDiePaths = paths;
    }

    public String getName(){
        return playerName;
    }
    public void setName(String s){
        this.playerName = s;
    }

    public String[] getPlayerDiePath() {
        return playerDiePaths;
    }

    public int getRank(){
        return rank;
    }
    public void setRank(int level){
        this.rank = level;
    }

    public int getDollars(){
        return dollars;
    }
    public void incDollars(int dollar){
        this.dollars += dollar;
    }

    public int getCredits(){
        return credits;
    }
    public void incCredits(int credit){
        this.credits += credit;
    }

    public void upgrade(int[] costs, int currency, int desiredLevel) {
        //check to make sure player is in office
        if((location.getName()).equals("office")){
            //make sure the level is greater than current level and in the upgrades list
            
            //get the upgrade prices
            if(desiredLevel > rank && desiredLevel <= costs.length + 1 )
            {
                if(costs[desiredLevel-2] > currency){
                    ui.print("You do not have enough dollars for this upgrade");
                }
                else{
                    //change level
                    ui.print("You are now level " + desiredLevel);
                    this.setRank(desiredLevel);
                    this.incDollars((-1*costs[desiredLevel-2]));
                    ui.print("And you have " + currency + " remaining");
                }
            }
            else{
                ui.print("Not a valid level!!!!");
            }
        }
        else{
            ui.print("You are not on the casting office, so you can't upgrade");
        }
    }

    public boolean isEmployed() {
        return employed;
    }
    public void giveRole(String rn){
        this.roleName = rn;
        this.employed = true;
    }

    public int getRehearseTokens() {
        return rehearseTokens;
    }
    public void incTokens(){
        this.rehearseTokens++;
    }

    public Set getLocation() {
        return location;
    }
    public void setLocation(Set newPos){ // I don't think this is used anywhere
        this.location = newPos;
    }
    //to move a player to a neighbor
    public static boolean canMoveTo(String dest, List<String> neighbors){
        for(int i = 0; i < neighbors.size(); i++){
            //if the designated neighbor exists return true
            if(dest.equals(neighbors.get(i)))
            {
                return true;
            }
        }
        return false;
    }
    public boolean moveTo(Set dest) {
        boolean successfulMove = false;
        String destName;

        //if player is not employed
        if(!employed){
            try { // this might not be the best way to handle this error...
                destName = dest.getName(); // null if not a valid Set name
            } catch (NullPointerException e){
                ui.print("The place you tried to move to isn't a valid set name. Try again..or don't.");
                return successfulMove;
            }

            if (location.checkNeighbor(destName) ) {
                location = dest;
                ui.print("You're now located in " + destName);
                successfulMove = true;
            }
            else
            {
                ui.print("You can't move to that location; it's not a neighbor of the current location. (View neighbors with the command `neighbors`.)");
            }
        }
        else{
            ui.print("Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
        }

        return successfulMove;
    }
    
    public String getRoleName() {
        return roleName;
    }
    public void resetRole(){
        roleName = "";
        this.rehearseTokens = 0;
        this.employed = false;
    }

    public void employ(Board board, String roleName) {
        // or could pass 2 board-referenced values in as args instead of the whole board
        Role role; 
        int roleLevel;

        if(!location.isClosed()){
            role = location.getRole(roleName);
            if (role != null) {
                roleLevel = Integer.parseInt(role.getLevel());
                if(rank >= roleLevel)
                {
                    this.giveRole(roleName);
                    board.fillRole(location, role); // <-- this method needs to be simplified later; it's hard to follow currently
                    ui.print("You are now employed as: " + roleName + ". If you just moved, you'll be able to rehearse or act in this role on your next turn");
                }
                else{
                    ui.print("Your rank isn't high enough to take this role. Your rank is " + rank + " while the role level is " + roleLevel);
                }
            } else {
                ui.print("Role " + roleName + " not found at this location. Your role options are" + board.freeRoles(location.getName()));
            }
        }
        else{
            ui.print("This set was already finished!");
        }
    }

    public boolean act(List<Player> onCardPlayers, List<Player> offCardPlayers) {
        boolean successfulActing = false;
        if(employed){
            //make and roll a die
            int budget = Integer.valueOf((location.getCard()).getBudget());
            int die = 1 + (int)(Math.random() * 6);

            ui.print("The budget of your current job is: " + budget + ", you rolled a: " + die + ", and you have " + rehearseTokens + " rehearsal tokens");
            //if the die is higher than the budget
            if(budget > (die + rehearseTokens)){
                //for acting failures on card and off card
                ui.print("Acting failure!");
                if(location.getCard().hasRole(roleName)){
                    ui.print("Since you had an important role you get nothing!");
                }
                else{
                    ui.print("Since you weren't that important you will get 1 dollar, out of pity");
                    this.incDollars(1);
                }
            }
            else{
                //for acting successes on card and off card
                ui.print("Acting success!");
                if(location.getCard().hasRole(roleName)){
                    ui.print("Since you were important, you get 2 credits");
                    this.incCredits(2);
                }
                else{
                    ui.print("Since you weren't that important you get 1 credit and 1 dollar");
                    this.incCredits(1);
                    this.incDollars(1);
                }
                //increment takes
                location.decTakesLeft();
            }

            //if it was the last scene and someone was on card hand out bonuses
            if(location.getTakesLeft() == 0){
                
                if(location.canBonus()){
                    ui.print("Oh no! That was the last scene! Everyone gets money!");
                    location.bonuses(onCardPlayers, offCardPlayers);
                }
                else{
                    ui.print("That was the last scene, but no ones on card so no one gets money! Aha!");
                }
                location.wrapUp(onCardPlayers, offCardPlayers);
            }
            else{
                ui.print("There are only " + location.getTakesLeft() + " takes left in this scene!");
            }

            successfulActing = true;
        }
        else{
            ui.print("You are currently not employed");
        }

        return successfulActing;
    }

    public boolean rehearse() {
        boolean successfulRehearsal = false;

        // if player is employed
        if (employed) {
            //and if the player does not have the max number of tokens already
            if(rehearseTokens < 5) {
                this.incTokens();
                ui.print("You've rehearsed! You gain a rehearsal token (adds +1 to your subsequent dice rolls while acting on role only)");
                successfulRehearsal = true;
            } else {
                ui.print("You have reached the max for rehearsal and only have the option to act. Its a guaranteed success though!");
            }
        } else {
            ui.print("You're not employed? What are you going to rehearse for??");
        }

        return successfulRehearsal;
    }

    public int calcFinalScore(){
        return (dollars + credits + (5 * rank));
    }
}