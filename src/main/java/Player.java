import java.util.*;

public class Player{

    private String playerName;
    private String[] playerDiePaths;
    private AreaData playerDieArea;
    private int rank;
    private int dollars;
    private int credits;
    private boolean hasPlayed; 
    private boolean employed; 
    private int rehearseTokens;
    private Set location;
    private Role role;
    private View view = View.getInstance();

    public Player(Set s, String p, int sr, String[] paths){
        location = s;
        playerName = p;
        playerDiePaths = paths;
        
        rank = sr;
        credits = 0;
        dollars = 0;
        employed = false;
        rehearseTokens = 0;
        playerDieArea = new AreaData(0, 0, 46, 46);
        // roles' area w/h = 46
        // player die position gets set when Game calls view.resetPlayerDie(); I don't think player should be able to call view
        hasPlayed = false;
    }

    public Player(Set s, String p, int sr, int sc, String[] paths){
        location = s;
        playerName = p;
        rank = sr;
        credits = sc;
        playerDiePaths = paths;

        dollars = 0;
        employed = false;
        rehearseTokens = 0;
        playerDieArea = new AreaData(0, 0, 46, 46); // roles' area w/h = 46
        hasPlayed = false;
    } 
    

    public String getName(){
        return playerName;
    }
    public void setName(String s){
        playerName = s;
    }

    public String getPlayerDiePath() {
        return playerDiePaths[rank- 1];
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
    public void incCredits(int credits){
        this.credits += credits;
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

    public int getRehearseTokens() {
        return rehearseTokens;
    }
    public void incTokens(){
        rehearseTokens++;
    }

    public Set getLocation() {
        return location;
    }
    public void setLocation(Set newPos){ // I don't think this is used anywhere
        location = newPos;
    }

    public void setAreaData(AreaData a){
        playerDieArea = a;
    }

    public void setOnCardAreaData(AreaData a){
        AreaData b = new AreaData((a.getX() + location.getArea().getX()) - 2, (a.getY() + location.getArea().getY())  - 2, playerDieArea.getW(), playerDieArea.getH());
        
        playerDieArea = b;
    }
    public AreaData getAreaData(){
        return playerDieArea;
    }
    
    public boolean moveTo(String destName, Set dest) {
        boolean successfulMove = false;

        try { // this might not be the best way to handle this error...
            destName = dest.getName(); // null if not a valid Set name
        } catch (NullPointerException e){
            view.showPopUp("The place you tried to move to isn't a valid set name. Try again...or don't.");
            return successfulMove;
        }

        if (location.checkNeighbor(destName) ) {
            location = dest;
            setAreaData(dest.getArea());
            //view.movePlayerPosition(this, dest.getArea().getX(), dest.getArea().getY());
            view.showPopUp("You're now located in " + destName);
            hasPlayed = true;
            successfulMove = true;
        }
        else
        {
            view.showPopUp("You can't move to that location; it's not a neighbor of the current location. (View neighbors with the command `neighbors`.)");
        }

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
        int roleLevel;
        if (!location.getName().equals("office") && !location.getName().equals("trailer")) {
            Role desiredRole = location.getRole(roleName);

            roleLevel = Integer.parseInt(desiredRole.getLevel());
            if(!location.isClosed()){
                if(rank >= roleLevel)
                {
                    employed = true;
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

        }
        else {
            view.showPopUp("This location doesn't have any roles.");
        }
    }

    //Returns boolean
    public boolean act(List<Player> onCardPlayers, List<Player> offCardPlayers) {
        int dieVal;
        int budget;
        boolean successfulActing = false;

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
                incDollars(1);
            }
        }
        else{
            //for acting successes on card and off card
            view.showPopUp("Acting success!");
            //decrement takesLeft
            location.decTakesLeft();
            // determine pay for sucessful shot
            if(location.getCard().hasRole(role)){
                view.showPopUp("Since you were important, you get 2 credits");
                incCredits(2);
            }
            else{
                view.showPopUp("Since you weren't that important you get 1 credit and 1 dollar");
                incCredits(1);
                incDollars(1);
            }
        }

        //if it was the last scene and someone was on card hand out bonuses
        if(location.getTakesLeft() <= 0){
            
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
            view.showPopUp("There are " + location.getTakesLeft() + " takes left in this scene.");
        }
        hasPlayed = true;
        successfulActing = true;

        return successfulActing;
    }

    // Returns boolean
    // determines if the player needs to rehearse, and if they can then they are given a rehearsal token
    public boolean rehearse() {
        boolean successfulRehearsal = false;

        //if the player does not have the max number of tokens already
        if(rehearseTokens < Integer.valueOf(getLocation().getCard().getBudget()) - 1) {
            this.incTokens();
            view.showPopUp("You've rehearsed! You gain a rehearsal token (adds +1 to your subsequent dice rolls while acting on role only)");
            hasPlayed = true;
            successfulRehearsal = true;
        } else {
            view.showPopUp("You have reached the max for rehearsal and only have the option to act. Its a guaranteed success though!");
        }

        return successfulRehearsal;
    }

    // Returns int
    // returns the score of the player based on their dollar, credit, and rank
    public int calcFinalScore(){
        return (dollars + credits + (5 * rank));
    }
}