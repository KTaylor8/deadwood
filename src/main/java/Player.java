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

    public Player(String p){
        playerName = p;
    }

    public Player(int sl, String p){
        playerName = p;
        level = sl;
    }

    public Player(String p, int sc){
        playerName = p;
        credit = sc;
    }

    public setLevel(int level){
        this.level = level;
    }

    public incDollar(int dollar){
        this.dollar += dollar;
    }

    public incCred(int credit){
        this.credit += credit;
    }

    public giveRole(String rn){
        this.roleName = rn;
        this.employed = !this.employed;
    }

    public incToken(){
        this.rehearseToken++;
    }

    public resetRole(){
        roleName = "";
        this.rehearseToken = 0;
        this.employed = !this.employed;
    }

    public changePos(String newPos){
        this.position = newPos;
    }

    public int calcFinalScore(){
        return (dollar + credit + (5 * level));
    }
}