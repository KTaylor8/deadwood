import java.util.*;

public class Set{
    public String setName;
    public Stack<String> adjacent;
    public Stack<Role> offCardRoles;
    public Card currentCard;
    //this is so we know whether it has not been fliped(0) or if it is flipped and people can move on it(1), or if it is re-flipped over(2)
    public int flipStage;
    private int finalTakes;
    private int currentTakes; // field should be private if other classes use a getter method to access them, right?
    
    public int[] upgradeCostDollars;
    public int[] upgradeCostCredits;

    // regular set constructor
    Set(String s, Stack<String> a, Stack<Role> r, int t){
        this.setName = s;
        this.adjacent = a;
        this.offCardRoles = r;

        this.finalTakes = t;
    }

    // office constructor
    Set(String s, Stack<String> a, int[] upgradeD, int[] upgradeC){
        this.setName = s;
        this.adjacent = a;
        // this.finalTakes = 0;
        upgradeCostDollars = upgradeD;
        upgradeCostCredits = upgradeC;
    }

    // trailers constructor
    Set(String s, Stack<String> a){
        this.setName = s;
        this.adjacent = a;
        // this.finalTakes = 0;
    }

    public void printInfo() {
        String setInfo = "";
        setInfo += ("\nSet name: " + setName);
        setInfo += "\nNeighbors: ";
        for (int i = 0; i < adjacent.size(); i++) {
            setInfo += ("\nSet name: " + adjacent[i]);
        }

        if (setName == "Office") {
            setInfo += "\nUpgrade costs in dollars: ";
            for (int i = 0; i < upgradeCostDollars.length; i++) {
                setInfo += "\n\tRank " + (i+2) + ": " + upgradeCostDollars[i] + "dollars";
            }
            setInfo += "\nUpgrade costs in credits: ";
            for (int i = 0; i < upgradeCostCredits.length; i++) {
                setInfo += "\n\tRank " + (i+2) + ": " + upgradeCostCredits[i] + "credits";
            }
        } 
        else if (setName == "Trailers") {
            setInfo = 
        } 
        else {
            setInfo = 
        }
        System.out.println(setInfo);
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