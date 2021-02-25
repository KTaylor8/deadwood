import java.util.*;

public class Set{
    private String setName;
    private List<String> neighbors;
    private List<Role> offCardRoles = new ArrayList<Role>();
    private Card currentCard;
    //this is so we know whether it has not been flipped(0) or if it is flipped and people can move on it(1), or if it is re-flipped over(2)
    private int flipStage;
    private int finalTakes = 0;
    private int takesLeft; // field should be private if other classes use a getter method to access them, right?
    
    private int[] upgradeCostDollars;
    private int[] upgradeCostCredits;

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
        upgradeCostDollars = upgradeD;
        upgradeCostCredits = upgradeC;
    }

    // trailers constructor
    Set(String s, List<String> n){
        this.setName = s;
        this.neighbors = n;
    }

    public String getSetName() {
        return setName;
    }

    public void resetSet(Card newCard){
        for(int i = 0; i < offCardRoles.size(); i++ ){
            (offCardRoles.get(i)).occupied = false;
        }
        this.currentCard = newCard;
        this.takesLeft = finalTakes;
        flipStage = 0;
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public List<Role> getOffCardRoles() {
        return offCardRoles;
    }

    public Card getCard() {
        return currentCard;
    }

    public int getFlipStage() {
        return flipStage;
    }

    public void flipSet(){
        flipStage = 2;
    }

    public int getTakesLeft(){
        return takesLeft;
    }

    public void decTakesLeft(){
        takesLeft--;
    }

    public int[] getUpgradeCD(){
        return upgradeCostDollars;
    }

    public int[] getUpgradeCC(){
        return upgradeCostCredits;
    }

    public void printInfo() {
        String setInfo = "";
        setInfo += ("\nSet name: " + setName);
        setInfo += "\n\tNeighbors: ";
        for (int i = 0; i < neighbors.size(); i++) {
            setInfo += ("\n\t\tNeighbor name: " + neighbors.get(i));
        }
        if (setName == "office") {
            setInfo += "\n\tUpgrade costs in dollars: ";
            for (int i = 0; i < upgradeCostDollars.length; i++) {
                setInfo += "\n\t\tRank " + (i+2) + ": $" + upgradeCostDollars[i];
            }
            setInfo += "\n\tUpgrade costs in credits: ";
            for (int i = 0; i < upgradeCostCredits.length; i++) {
                setInfo += "\n\t\tRank " + (i+2) + ": " + upgradeCostCredits[i] + " credits";
            }
        } else if (setName != "trailer") {
            for (int i = 0; i < offCardRoles.size(); i++) {
                setInfo += "\n\tOff-card role #" + (i+1) + ":";
                setInfo += "\n\t\tOff-card role name: " + offCardRoles.get(i).name;
                setInfo += "\n\t\tOff-card role level: " + offCardRoles.get(i).level;
                setInfo += "\n\t\tOff-card role line: " + offCardRoles.get(i).line;
            }
        } 
        System.out.println(setInfo);
    }
}