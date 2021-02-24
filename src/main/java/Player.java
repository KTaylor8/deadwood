import java.util.*;

public class Player implements Comparable<Player>{

    public String playerName;
    public int level = 1;
    public int dollar = 0;
    public int credit = 0;
    public boolean employed = false; 
    public int rehearseToken = 0;
    public String position = "";
    public String roleName;

    public Player(String p){
        playerName = p;
        position = "trailer";
    }

    public Player(String p, int sl, int sc){
        playerName = p;
        level = sl;
        position = "trailer";
    }

    public int compareTo(Player p) {
        // sorts in descending order
        return Integer.compare(p.calcFinalScore(), this.calcFinalScore()); 
    }

    public void setName(String s){
        this.playerName = s;
    }
    public void setLevel(int level){
        this.level = level;
    }

    public void incDollar(int dollar){
        this.dollar += dollar;
    }

    public void incCred(int credit){
        this.credit += credit;
    }

    public void giveRole(String rn){
        this.roleName = rn;
        this.employed = true;
    }

    public void incToken(){
        this.rehearseToken++;
    }

    public void resetRole(){
        roleName = "";
        this.rehearseToken = 0;
        this.employed = false;
    }

    public void changePos(String newPos){
        this.position = newPos;
    }

    public int calcFinalScore(){
        return (dollar + credit + (5 * level));
    }
}