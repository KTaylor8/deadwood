import java.util.*;

public class Player{

    private String playerName;
    private int rank = 1;
    private int dollars = 0;
    private int credits = 0;
    private boolean employed = false; 
    private int rehearseTokens = 0;
    private Set location;
    private String roleName;

    public Player(Set s, String p){
        playerName = p;
        location = s;
    }

    public Player(Set s, String p, int sl, int sc){
        playerName = p;
        rank = sl;
        location = s;
    }

    public String getName(){
        return playerName;
    }
    public void setName(String s){
        this.playerName = s;
    }

    public int getRank(){
        return rank;
    }
    public void setLevel(int level){
        this.rank = level;
    }

    public int getDollars(){
        return dollars;
    }
    public void incDollar(int dollar){
        this.dollars += dollar;
    }

    public int getCredits(){
        return credits;
    }
    public void incCred(int credit){
        this.credits += credit;
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
    public void incToken(){
        this.rehearseTokens++;
    }

    public Set getPosition() {
        return location;
    }
    public void setPosition(Set newPos){ // I don't think this is used anywhere
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
    public void moveTo(Set dest) {
        //if player is not employed
        if(!employed){
            if (location.checkNeighbor(dest.getName()) ) {
                location = dest;
            }
            else
            {
                System.out.println("invalid place to move to");
            }
        }
        else{
            System.out.println("Since you are employed in a role, you cannot move but you can act or rehearse if you have not already");
        }
    }
    
    public String getRoleName() {
        return roleName;
    }
    public void resetRole(){
        roleName = "";
        this.rehearseTokens = 0;
        this.employed = false;
    }

    public int calcFinalScore(){
        return (dollars + credits + (5 * rank));
    }
}