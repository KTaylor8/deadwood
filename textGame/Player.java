import java.util.*;

public class Player{

    public String playerName;
    public int level;
    public int dollar;
    public int credit;
    public boolean employed; 
    public int rehearseToken;

    public Player(String p){
        playerName = p;
        level = 1;
        dollar = 0;
        credit = 0;
        employed = false;
        rehearseToken = 0;
    }

    public Player(String p, int sd, int sc){
        playerName = p;
        level = 1;
        dollar = sd;
        credit = sc;
        employed = false;
        rehearseToken = 0;
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

    public changeEmployed(){
        this.employed = !this.employed;
    }

    public incToken(int token){
        this.rehearseToken += token;
    }

    public resetToken(){
        this.rehearseToken = 0;
    }

    public int calcFinalScore(){
        return (dollar + credit + (5 * level));
    }
}