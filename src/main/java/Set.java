import java.util.*;

public class Set{
    public String setName;
    public List<String> neighbors;
    public List<Role> offCardRoles;
    public Card currentCard;
    //this is so we know whether it has not been flipped(0) or if it is flipped and people can move on it(1), or if it is re-flipped over(2)
    int flipStage;
    private int finalTakes;
    private int currentTakes; // field should be private if other classes use a getter method to access them, right?
    
    public int[] upgradeCostDollars;
    public int[] upgradeCostCredits;

    // regular set constructor
    Set(String s, List<String> n, List<Role> r, int t){
        this.setName = s;
        this.neighbors = n;
        this.offCardRoles = r;

        this.finalTakes = t;
    }

    // office constructor
    Set(String s, List<String> n, int[] upgradeD, int[] upgradeC){
        this.setName = s;
        this.neighbors = n;
        // this.finalTakes = 0;
        upgradeCostDollars = upgradeD;
        upgradeCostCredits = upgradeC;
    }

    // trailers constructor
    Set(String s, List<String> n){
        this.setName = s;
        this.neighbors = n;
        // this.finalTakes = 0;
    }

    // I'm setting up this implementation with adjacent/neighbors as a ArrayList List 
    // public void printInfo() {
    //     String setInfo = "";
    //     setInfo += ("\nSet name: " + setName);
    //     setInfo += "\nNeighbors: ";
    //     for (int i = 0; i < adjacent.size(); i++) {
    //         setInfo += ("\nSet name: " + adjacent[i]);
    //     }

<<<<<<< HEAD
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
=======
    //     if (setName == "Office") {
    //         setInfo += "\nUpgrade costs in dollars: ";
    //         for (int i = 0; i < upgradeCostDollars.length; i++) {
    //             setInfo += "\n\tRank " + (i+2) + ": " + upgradeCostDollars[i] + "dollars";
    //         }
    //         setInfo += "\nUpgrade costs in credits: ";
    //         for (int i = 0; i < upgradeCostCredits.length; i++) {
    //             setInfo += "\n\tRank " + (i+2) + ": " + upgradeCostCredits[i] + "credits";
    //         }
    //     } else if (setName == "Trailers") {
    //         setInfo = 
    //     } else {
    //         setInfo = 
    //     }
    //     System.out.println(setInfo);
    // }
>>>>>>> 6fdbf783f1de2d3dd62160a3ee7884390f6ce81c

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