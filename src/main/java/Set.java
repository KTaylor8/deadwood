import java.util.*;

public class Set{
    String setName;
    Stack<String> adjacent;
    Stack<Role> offCardRoles;
    Card currentCard;
    //this is so we know whether it has not been fliped(0) or if it is flipped and people can move on it(1), or if it is re-flipped over(2)
    int flipStage;
    int finalTakes;
    int currentTakes;

    Set(String s, Stack<String> a, Stack<Role> r, int t){
        this.setName = s;
        this.adjacent = a;
        this.offCardRoles = r;
        this.finalTakes = t;
    }

    public resetSet(Card newCard){
        this.currentCard = newCard;
        this.currentTakes = finalTakes;
        flipStage = 0;
    }

    public incTakes(){
        currentTakes--;
    }

    public flipSet(){
        flipStage++;
    }


}