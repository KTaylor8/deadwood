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

    // regular set constructor
    public Set(String s, List<String> n, List<Role> r, List<ShotToken> shotTokens, AreaData areaData){
        this.setName = s;
        this.neighbors = n;
        this.offCardRoles = r;
        this.shotTokens = shotTokens;
        this.area = areaData;

        this.totalTakes = shotTokens.size();
    }

    // office constructor
    public Set(String s, List<String> n, int[] upgradeD, int[] upgradeC, AreaData[]upgradeDAreas, AreaData[] upgradeCAreas, AreaData areaData){
        this.setName = s;
        this.neighbors = n;
        upgradeCostDollars = upgradeD;
        upgradeCostCredits = upgradeC;
        upgradeDollarsAreas = upgradeDAreas;
        upgradeCreditsAreas = upgradeCAreas;
        this.area = areaData;
    }

    // trailers constructor
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

    public String[] getUpgradeStrings(int j){
        int test = (upgradeCostCredits.length*2) - ((j)*2);
        if(test < 0){
            test = 0;
        }
        String[] u = new String[test];
        int k = 0;
        for(int i = j; i < u.length; i++){
            u[k] = "Level " + (i+1) + " = " + upgradeCostCredits[i] + " credits";
            k++;
            u[k] = "Level " + (i+1) + " : " + upgradeCostDollars[i] + " dollars";
            k++;
        }
        
        return u;
    }


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

    public String[] getNeighborStrings() {
        String[] n = new String[neighbors.size()];
        for(int i = 0; i < neighbors.size(); i++){
            n[i] = (neighbors.get(i)) ;
        }
        return n;
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
        view.showPopUp("Rolling " + (this.currentCard.getBudget()) + " dice");
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
            view.showPopUp((onCardPlayers.get(i%(onCardPlayers.size()))).getName() + " gets $" + dice[i]);
        }

        //hand out bonuses of rank to off card people
        for(Player p: offCardPlayers){
            int playerRoleRank = Integer.parseInt(p.getRole().getLevel());
            p.incDollars(playerRoleRank);
            view.showPopUp(p.getName() + " gets $" + playerRoleRank);
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
}