// import java.util.*;

public class Player{

    private String playerName;
    private int rank = 1;
    private int dollars = 0;
    private int credits = 0;
    private boolean employed = false; 
    private int rehearseTokens = 0;
    private String position = "";
    private String roleName;

    public Player(String p){
        playerName = p;
        position = "trailer";
    }

    public Player(String p, int sl, int sc){
        playerName = p;
        rank = sl;
        position = "trailer";
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

    public String getPosition() {
        return position;
    }
    public void setPosition(String newPos){
        this.position = newPos;
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