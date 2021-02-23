import java.util.*;

public class Player{

    public String playerName;
    public int level = 1;
    public int dollar = 0;
    public int credit = 0;
    public boolean employed = false; 
    public int rehearseToken = 0;
    public String position = "trailer";
    public String roleName;

    public void Player(String p){
        playerName = p;
    }

    public void Player(int sl, String p){
        playerName = p;
        level = sl;
    }

    public void Player(String p, int sc){
        playerName = p;
        credit = sc;
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
        this.employed = !this.employed;
    }

    public void incToken(){
        this.rehearseToken++;
    }

    public void resetRole(){
        roleName = "";
        this.rehearseToken = 0;
        this.employed = !this.employed;
    }

    public void changePos(String newPos){
        this.position = newPos;
    }

    public int calcFinalScore(){
        return (dollar + credit + (5 * level));
    }
}