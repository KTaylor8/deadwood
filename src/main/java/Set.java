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

    private UI ui = new UI();

    // regular set constructor
    public Set(String s, List<String> n, List<Role> r, int t){
        this.setName = s;
        this.neighbors = n;
        this.offCardRoles = r;
        this.finalTakes = t;
    }

    // office constructor
    public Set(String s, List<String> n, int[] upgradeD, int[] upgradeC){
        this.setName = s;
        this.neighbors = n;
        upgradeCostDollars = upgradeD;
        upgradeCostCredits = upgradeC;
    }

    // trailers constructor
    public Set(String s, List<String> n){
        this.setName = s;
        this.neighbors = n;
    }

    public String getName() {
        return setName;
    }

    public void resetSet(Card newCard){
        for(int i = 0; i < offCardRoles.size(); i++ ){
            (offCardRoles.get(i)).unoccupy();
        }
        this.currentCard = newCard;
        this.takesLeft = finalTakes;
        flipStage = 0;
    }

    public List<String> getNeighbors() {
        return neighbors;
    }
    public boolean checkNeighbor(String s) {
        boolean isNeighbor = false;
        for (int i = 0; i < neighbors.size(); i++) {
            if (s.equals(neighbors.get(i))) {
                isNeighbor = true;
            }
        }
        return isNeighbor;
    }

    public List<Role> getOnCardRoles() {
        return currentCard.getOnCardRoles();
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

    // returns boolean stating if THIS set is closed
    public boolean isClosed() {
        return (flipStage == 2);
    }

    //returns true if someone is on card
    public boolean canBonus(){
        for(int i = 0; i < ((this.getCard()).getOnCardRoles()).size(); i ++){
            if((((this.getCard()).getOnCardRoles()).get(i)).isOccupied()){
                return true;
            }
        }
        return false;
    }

    //returns Role object
    public Role getRole(String roleName){
        for(Role r: offCardRoles){
            if(roleName.equals(r.getName())){
                return r;
            }
        }
        
        for(Role r: currentCard.getOnCardRoles()){
            if(roleName.equals(r.getName())){
                return r;
            }
        }
        
        return null;
    }

    //hands out bonuses based on on card and off card people
    public void bonuses(List<Player> onCardPlayers, List<Player> offCardPlayers){

        int[] dice = new int[Integer.valueOf(this.currentCard.getBudget())];
        ui.print("Rolling " + (this.currentCard.getBudget()) + " dice");
        for (int i = 0; i < dice.length; i++) {
            dice[i] = 1 + (int)(Math.random() * ((6 - 1) + 1));
        }

        // sort array in ascending order and then reverse it
        Arrays.sort(dice);
        int[] newDice = new int[dice.length];
        for(int i = 0; i < dice.length; i++) {
            newDice[dice.length-1-i] = dice[i];
        }

        //hand out bonuses of randomized dice to on card people
        for(int i = 0; i < dice.length; i++){
            (onCardPlayers.get(i%(onCardPlayers.size()))).incDollar(dice[i]);
            ui.print((onCardPlayers.get(i%(onCardPlayers.size()))).getName() + " gets $" + dice[i]);
        }

        //hand out bonuses of rank to off card people
        for(Player p: offCardPlayers){
            // SORRY THIS IS CONVOLUTED. NEED TO MAKE (BOARD?) METHOD THAT RETURNS ROLE OBJ/ROLE RANK GIVEN PLAYER
            int playerRoleRank = Integer.parseInt(p.getLocation().getRole(p.getRoleName()).getLevel());
            p.incDollar(playerRoleRank);
            ui.print(p.getName() + " gets $" + playerRoleRank);
        }
    }

    //wraps up a set and resets the roles,
    public void wrapUp(List<Player> onCardPlayers, List<Player> offCardPlayers){
        for(Player p: onCardPlayers){
            p.resetRole();
        }
        for(Player p: offCardPlayers){
            p.resetRole();
        }

        //make sure the set isn't used again for this day
        this.flipSet();
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
                setInfo += "\n\t\tOff-card role name: " + offCardRoles.get(i).getName();
                setInfo += "\n\t\tOff-card role level: " + offCardRoles.get(i).getLevel();
                setInfo += "\n\t\tOff-card role line: " + offCardRoles.get(i).getLine();
            }
        } 
        ui.print(setInfo);
    }
}