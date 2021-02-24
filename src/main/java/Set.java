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

    public void printInfo() {
        String setInfo = "";
        setInfo += ("\nSet name: " + setName);
        setInfo += "\n\tNeighbors: ";
        for (int i = 0; i < neighbors.size(); i++) {
            setInfo += ("\n\t\tNeighbor name: " + neighbors.get(i));
        }
        if (setName == "Office") {
            setInfo += "\n\tUpgrade costs in dollars: ";
            for (int i = 0; i < upgradeCostDollars.length; i++) {
                setInfo += "\n\t\tRank " + (i+2) + ": $" + upgradeCostDollars[i];
            }
            setInfo += "\n\tUpgrade costs in credits: ";
            for (int i = 0; i < upgradeCostCredits.length; i++) {
                setInfo += "\n\t\tRank " + (i+2) + ": " + upgradeCostCredits[i] + " credits";
            }
        } else if (setName != "Trailer") {
            for (int i = 0; i < offCardRoles.size(); i++) {
                setInfo += "\n\tOff-card role #" + (i+1) + ":";
                setInfo += "\n\t\tOff-card role name: " + offCardRoles.get(i).name;
                setInfo += "\n\t\tOff-card role level: " + offCardRoles.get(i).level;
                setInfo += "\n\t\tOff-card role line: " + offCardRoles.get(i).line;
            }
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

    public int[] getUpgradeCD(){
        return upgradeCostDollars;
    }

    public int[] getUpgradeCC(){
        return upgradeCostCredits;
    }

    public int getScene(){
        return currentTakes;
    }

}