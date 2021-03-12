import java.util.*;

public class Player{

    private String playerName;
    private String[] playerDiePaths;
    private int playerDieCurrentNum;
    private AreaData playerDieArea;
    private int rank;
    private int dollars;
    private int credits;
    private boolean hasPlayed; 
    private boolean employed; 
    private int rehearseTokens;
    private Set location;
    private Role role;
    // private String roleName;
    // private Controller controller = new Controller();
    private View view = View.getInstance();;

    public Player(Set s, String p, String[] paths, View view){
        location = s;
        playerName = p;
        playerDiePaths = paths;
        // this.view = view;
        
        rank = 1;
        credits = 0;
        dollars = 0;
        playerDieCurrentNum = 0;
        employed = false;
        rehearseTokens = 0;
        playerDieArea = new AreaData(0, 0, 46, 46);
        // roles' area w/h = 46
        // player die position gets set when Game calls view.resetPlayerDie(); I don't think player should be able to call view
        hasPlayed = false;
    }

    public Player(Set s, String p, int sr, int sc, String[] paths, View view){
        location = s;
        playerName = p;
        rank = sr;
        credits = sc;
        playerDiePaths = paths;
        // this.view = view;

        dollars = 0;
        playerDieCurrentNum = 0;
        employed = false;
        rehearseTokens = 0;
        playerDieArea = new AreaData(0, 0, 46, 46); // roles' area w/h = 46
        hasPlayed = false;
    } 
    // player die position gets set when Game calls view.resetPlayerDie(); I don't think player should be able to call view
    // trailerX + (i*46)

    public String getName(){
        return playerName;
    }
    public void setName(String s){
        this.playerName = s;
    }

    public String getPlayerDiePath() {
        return playerDiePaths[playerDieCurrentNum];
    }

    public AreaData getPlayerDieArea() {
        return playerDieArea;
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
        if(location.getName().equals("office")){
            //make sure the level is greater than current level and in the upgrades list
            
            //get the upgrade prices
            if(desiredLevel > rank && desiredLevel <= costs.length + 1 )
            {
                if(costs[desiredLevel-2] > currency){
                    view.showPopUp("You do not have enough dollars for this upgrade");
                }
                else{
                    //change level
                    view.showPopUp("You are now level " + desiredLevel);
                    this.setRank(desiredLevel);
                    this.incDollars((-1*costs[desiredLevel-2]));
                    view.showPopUp("And you have " + currency + " remaining");
                }
            }
            else{
                view.showPopUp("Not a valid level!!!!");
            }
        }
        else{
            view.showPopUp("You are not on the casting office, so you can't upgrade");
        }
    }

    public boolean getHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean newStatus) {
        hasPlayed = newStatus;
    }

    public boolean isEmployed() {
        return employed;
    }
    // public void giveRole(String rn){ -- obsoleted by takeRole()
    //     this.roleName = rn;
    //     this.employed = true;
    // }

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
    // public static boolean canMoveTo(String dest, List<String> neighbors){
    //     for(int i = 0; i < neighbors.size(); i++){
    //         //if the designated neighbor exists return true
    //         if(dest.equals(neighbors.get(i)))
    //         {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    public boolean moveTo(String destName, Set dest) {
        boolean successfulMove = false;
        // String destName;
        // Set dest = board.getSet(destination);

        // if player hasn't played yet
        // if (!hasPlayed) {
            // if player is not employed
            // if(!employed){
                try { // this might not be the best way to handle this error...
                    destName = dest.getName(); // null if not a valid Set name
                } catch (NullPointerException e){
                    view.showPopUp("The place you tried to move to isn't a valid set name. Try again..or don't.");
                    return successfulMove;
                }

                if (location.checkNeighbor(destName) ) {
                    location = dest;
                    view.movePlayerPosition(this, dest.getArea().getX(), dest.getArea().getY());
                    view.showPopUp("You're now located in " + destName);
                    hasPlayed = true;
                    successfulMove = true;
                }
                else
                {
                    view.showPopUp("You can't move to that location; it's not a neighbor of the current location. (View neighbors with the command `neighbors`.)");
                }
            // }
            // else{
            //     view.showPopUp("Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
            // }
        // }

        return successfulMove;
    }
    
    public Role getRole() {
        return role;
    }
    public void resetRole(){
        role = null;
        this.rehearseTokens = 0;
        this.employed = false;
    }

    public void takeRole(String roleName) {
        // or could pass 2 board-referenced values in as args instead of the whole board
        // Role role; 
        int roleLevel;
        if (!location.getName().equals("office") && !location.getName().equals("trailer")) {
            Role desiredRole = location.getRole(roleName);

            // if (role != null) {
                roleLevel = Integer.parseInt(desiredRole.getLevel());
                if(!location.isClosed()){
                    if(rank >= roleLevel)
                    {
                        role = desiredRole;
                        view.showPopUp("You are now employed as: " + roleName + ". If you just moved, you'll be able to rehearse or act in this role on your next turn");
                    }
                    else{
                        view.showPopUp("Your rank isn't high enough to take this role. Your rank is " + rank + " while the role level is " + roleLevel);
                    }
                }
                else{
                    view.showPopUp("This set was already finished!");
                }
                // } else {
                //     view.showPopUp("Role " + roleName + " not found at this location. Your role options are" + board.freeRoles(location.getName()));
                // }
            // }
        }
        else {
            view.showPopUp("This location doesn't have any roles.");
        }
    }

    public boolean act(List<Player> onCardPlayers, List<Player> offCardPlayers) {
        int dieVal;
        int budget;
        boolean successfulActing = false;
        if(employed){
            //make and roll a die
            budget = Integer.valueOf((location.getCard()).getBudget());
            dieVal = 1 + (int)(Math.random() * 6);

            view.showPopUp("The budget of your current job is: " + budget + ", you rolled a: " + dieVal + ", and you have " + rehearseTokens + " rehearsal tokens");
            // view.changePlayerDieVal(this, dieVal);
            //if the die is higher than the budget
            if(budget > (dieVal + rehearseTokens)){
                //for acting failures on card and off card
                view.showPopUp("Acting failure!");
                if(location.getCard().hasRole(role)){
                    view.showPopUp("Since you had an important role you get nothing!");
                }
                else{
                    view.showPopUp("Since you weren't that important you will get 1 dollar, out of pity");
                    this.incDollars(1);
                }
            }
            else{
                //for acting successes on card and off card
                view.showPopUp("Acting success!");
                if(location.getCard().hasRole(role)){
                    view.showPopUp("Since you were important, you get 2 credits");
                    this.incCredits(2);
                }
                else{
                    view.showPopUp("Since you weren't that important you get 1 credit and 1 dollar");
                    this.incCredits(1);
                    this.incDollars(1);
                }
                //increment takes
                location.decTakesLeft();
            }

            //if it was the last scene and someone was on card hand out bonuses
            if(location.getTakesLeft() == 0){
                
                if(location.canBonus()){
                    view.showPopUp("Oh no! That was the last scene! Everyone gets money!");
                    location.bonuses(onCardPlayers, offCardPlayers);
                }
                else{
                    view.showPopUp("That was the last scene, but no ones on card so no one gets money! Aha!");
                }
                location.wrapUp(onCardPlayers, offCardPlayers);
            }
            else{
                view.showPopUp("There are only " + location.getTakesLeft() + " takes left in this scene!");
            }
            hasPlayed = true;
            successfulActing = true;
        }
        else{
            view.showPopUp("You are currently not employed");
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
                view.showPopUp("You've rehearsed! You gain a rehearsal token (adds +1 to your subsequent dice rolls while acting on role only)");
                hasPlayed = true;
                successfulRehearsal = true;
            } else {
                view.showPopUp("You have reached the max for rehearsal and only have the option to act. Its a guaranteed success though!");
            }
        } else {
            view.showPopUp("You're not employed? What are you going to rehearse for??");
        }

        return successfulRehearsal;
    }

    public int calcFinalScore(){
        return (dollars + credits + (5 * rank));
    }
}