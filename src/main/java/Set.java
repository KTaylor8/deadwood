import java.util.*;

public class Set{
    public String setName;
    public Stack<String> adjacent;
    public Stack<Role> offCardRoles;
    public Card currentCard;
    //this is so we know whether it has not been fliped(0) or if it is flipped and people can move on it(1), or if it is re-flipped over(2)
    int flipStage;
    private int finalTakes;
    public int currentTakes;

    Set(String s, Stack<String> a, Stack<Role> r, int t){
        this.setName = s;
        this.adjacent = a;
        this.offCardRoles = r;
        this.finalTakes = t;
    }

    public void resetSet(Card newCard){
        this.currentCard = newCard;
        this.currentTakes = finalTakes;
        flipStage = 0;
    }

    public void incTakes(){
        currentTakes--;
    }

    public void flipSet(){
        flipStage++;
    }

    public int getScene(){
        return currentTakes;
    }

}