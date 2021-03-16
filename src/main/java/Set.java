import java.util.*;

public class Set{
    private String setName;
    private List<String> neighbors;
    private List<Role> offCardRoles = new ArrayList<Role>();
    private Card currentCard;
    //this is so we know whether it has not been flipped(0) or if it is flipped and people can move on it(1), or if it is re-flipped over(2)
    private int flipStage;
    private int totalTakes = 0;
    private int takesLeft; // field should be private if other classes use a getter method to access them, right?
    private List<ShotToken> shotTokens;
    private AreaData area;
    
    private int[] upgradeCostDollars, upgradeCostCredits;

    AreaData[] upgradeDollarsAreas, upgradeCreditsAreas;

    private View view = View.getInstance();

    /**
     * constructor for the sets that will have roles on them
     * @param s
     * @param n
     * @param r
     * @param shotTokens
     * @param areaData
     */
    public Set(String s, List<String> n, List<Role> r, List<ShotToken> shotTokens, AreaData areaData){
        this.setName = s;
        this.neighbors = n;
        this.offCardRoles = r;
        this.shotTokens = shotTokens;
        this.area = areaData;

        this.totalTakes = shotTokens.size();
    }

    /**
     * office constructor
     * @param s
     * @param n
     * @param upgradeD
     * @param upgradeC
     * @param upgradeDAreas
     * @param upgradeCAreas
     * @param areaData
     */
    public Set(String s, List<String> n, int[] upgradeD, int[] upgradeC, AreaData[]upgradeDAreas, AreaData[] upgradeCAreas, AreaData areaData){
        this.setName = s;
        this.neighbors = n;
        upgradeCostDollars = upgradeD;
        upgradeCostCredits = upgradeC;
        upgradeDollarsAreas = upgradeDAreas;
        upgradeCreditsAreas = upgradeCAreas;
        this.area = areaData;
    }

    /**
     * Set Constructor
     * @param s
     * @param n
     * @param areaData
     */
    public Set(String s, List<String> n, AreaData areaData){
        this.setName = s;
        this.neighbors = n;
        this.area = areaData;
    }


    public String getName() {
        return setName;
    }

    public AreaData getArea() {
        return area;
    }

    public Card getCard() {
        return currentCard;
    }

    public int getFlipStage() {
        return flipStage;
    }

    public void flipSet(){
        flipStage++;
    }

    public List<ShotToken> getShotTokens(){
        return shotTokens;
    }

    public int getTotalTakes(){
        return totalTakes;
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

    /**
     * returns an array of all of the available levels above a given level, as well as the costs
     * @param rank
     * @return String[]
     */
    public String[] getUpgradeStrings(int rank){
        rank = rank-1;
        int test = (upgradeCostCredits.length*2) - ((rank)*2);
        if(test < 0){
            test = 0;
        }
        String[] upgrades = new String[test];
        int index = 0;
        for(int i = rank; i < upgradeCostCredits.length; i++){
            upgrades[index] = "Level " + (i+2) + " = " + upgradeCostCredits[i] + " credits";
            index++;
            upgrades[index] = "Level " + (i+2) + " = " + upgradeCostDollars[i] + " dollars";
            index++;
        }
        
        return upgrades;
    }

    /**
     * gives the set a new card and resets the state of the card flip and resets the number of takes remaining
     * @param newCard
     */
    public void resetSet(Card newCard){
        for(int i = 0; i < offCardRoles.size(); i++ ){
            (offCardRoles.get(i)).unoccupy();
        }
        this.currentCard = newCard;
        this.takesLeft = totalTakes;
        flipStage = 0;
    }

    
    public List<String> getNeighbors(){
        return neighbors;
    }

    /**
     * returns the neighbors but as a string array so that they can be displayed in the view
     * @return String[]
     */
    public String[] getNeighborStrings() {
        String[] n = new String[neighbors.size()];
        for(int i = 0; i < neighbors.size(); i++){
            n[i] = (neighbors.get(i)) ;
        }
        return n;
    }
    
    /**
     * returns tha the given string is a neighbor of the set
     * @param s
     * @return boolean
     */
    public boolean checkNeighbor(String s) {
        boolean isNeighbor = false;
        for (int i = 0; i < neighbors.size(); i++) {
            if (s.equals(neighbors.get(i))) {
                isNeighbor = true;
            }
        }
        return isNeighbor;
    }

    /**
     * returns the roles of the set that are on the card
     * @return List<Role>
     */
    public List<Role> getOnCardRoles() {
        return currentCard.getOnCardRoles();
    }

    /**
     * returns the roles of the set that are off the card
     * @return List<Role>
     */
    public List<Role> getOffCardRoles() {
        return offCardRoles;
    }

    /**
     * returns all of the roles as a string array so that view can use it as options of a popup
     * @return String[]
     */
    public String[] getRoleStrings(){
        List<Role> onR= getOnCardRoles();
        List<Role> offR= getOffCardRoles();
        List<Role> allR = new ArrayList<Role>();
        for(int j = 0; j < onR.size(); j++){
            if(!onR.get(j).isOccupied())
            {
                allR.add(onR.get(j));
            }
        }
        for(int j = 0; j < offR.size(); j++){
            if(!offR.get(j).isOccupied())
            {
                allR.add(offR.get(j));
            }
        }
        String[] n = new String[allR.size()];
        int i = 0;
        for(int j = 0; j < allR.size(); j++){
            if(!allR.get(j).isOccupied())
            {
                n[i] = allR.get(j).getName();
                i++;
            }
        }
        return n;
    }


    /**
     * Returns if there is a player that has a role on card
     * @return boolean
     */
    public boolean canBonus(){
        for(int i = 0; i < ((this.getCard()).getOnCardRoles()).size(); i ++){
            if((((this.getCard()).getOnCardRoles()).get(i)).isOccupied()){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the role with a name that matches the string given
     * @param roleName
     * @return
     */
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

    /**
     * gives bonuses to players based on whether they were on card or off card and what the budget of the scene was
     * @param onCardPlayers
     * @param offCardPlayers
     */
    public void bonuses(boolean currentPlayerIsComputer, List<Player> onCardPlayers, List<Player> offCardPlayers){

        if (onCardPlayers.size() > 0) { // calculate special on-card-player bonuses if there were any on-card-players
            int[] dice = new int[Integer.valueOf(this.currentCard.getBudget())];
            view.showPopUp(currentPlayerIsComputer, "Rolling " + (this.currentCard.getBudget()) + " dice");
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
                (onCardPlayers.get(i%(onCardPlayers.size()))).incDollars(dice[i]);
                view.showPopUp(currentPlayerIsComputer, onCardPlayers.get(i%(onCardPlayers.size())).getName() + " gets $" + dice[i]);
            }
        }

        //hand out bonuses of rank to off card people
        for(Player p: offCardPlayers){
            int playerRoleRank = Integer.parseInt(p.getRole().getLevel());
            p.incDollars(playerRoleRank);
            view.showPopUp(currentPlayerIsComputer, p.getName() + " gets $" + playerRoleRank);
        }
    }

    /**
     * iterates through the different roles and makes sure that they are not occupied, and make sure the set can be reused for the day
     * @param onCardPlayers
     * @param offCardPlayers
     */
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
}