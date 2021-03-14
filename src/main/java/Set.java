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

        this.finalTakes = shotTokens.size();
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

    public void resetSet(Card newCard){
        for(int i = 0; i < offCardRoles.size(); i++ ){
            (offCardRoles.get(i)).unoccupy();
        }
        this.currentCard = newCard;
        this.takesLeft = finalTakes;
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
        String[] n = new String[onR.size() + offR.size()];
        int i = 0;
        for(int j = 0; j < onR.size(); j++){
            if(!onR.get(j).isOccupied())
            {
                n[i] = onR.get(j).getName();
                i++;
            }
        }
        for(int j = 0; j < offR.size(); j++){
            if(!offR.get(j).isOccupied())
            {
                n[i] = offR.get(j).getName();
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

    public void printInfo() {
        String setInfo = "";
        Role role = new Role();
        setInfo += ("\nSet name: " + setName);
        setInfo += "\n\tNeighbors: ";
        for (int i = 0; i < neighbors.size(); i++) {
            setInfo += ("\n\t\tNeighbor name: " + neighbors.get(i));
        }
        setInfo += "\n\tSet area: x = " + area.getX() + 
                    ", y = " + area.getY() +
                    ", w = " + area.getW() +
                    ", h = " + area.getH();
        if (setName == "office") {
            setInfo += "\n\tUpgrade costs in dollars: ";
            for (int i = 0; i < upgradeCostDollars.length; i++) {
                setInfo += "\n\t\tRank " + (i+2) + ": $" + upgradeCostDollars[i];
                setInfo += "\n\t\t\tUpgrade area: x = " + upgradeDollarsAreas[i].getX() + 
                ", y = " + upgradeDollarsAreas[i].getY() +
                ", w = " + upgradeDollarsAreas[i].getW() +
                ", h = " + upgradeDollarsAreas[i].getH();
            }
            setInfo += "\n\tUpgrade costs in credits: ";
            for (int i = 0; i < upgradeCostCredits.length; i++) {
                setInfo += "\n\t\tRank " + (i+2) + ": " + upgradeCostCredits[i] + " credits";
                setInfo += "\n\t\t\tUpgrade area: x = " + upgradeCreditsAreas[i].getX() + 
                ", y = " + upgradeCreditsAreas[i].getY() +
                ", w = " + upgradeCreditsAreas[i].getW() +
                ", h = " + upgradeCreditsAreas[i].getH();
            }
        } else if (setName != "trailer") {
            setInfo += "\n\tTakes: ";
            for (int i = 0; i < shotTokens.size(); i++) {
                ShotToken take = shotTokens.get(i);
                setInfo += ("\n\t\tTake #" + (i+1));
                setInfo += "\n\t\t\tTake area: x = " + take.getArea().getX() + 
                ", y = " + take.getArea().getY() +
                ", w = " + take.getArea().getW() +
                ", h = " + take.getArea().getH();
            }
            for (int i = 0; i < offCardRoles.size(); i++) {
                role = offCardRoles.get(i);
                setInfo += "\n\tOff-card role #" + (i+1) + ":";
                setInfo += "\n\t\tOff-card role name: " + role.getName();
                setInfo += "\n\t\tOff-card role level: " + role.getLevel();
                setInfo += "\n\t\tOff-card role line: " + role.getLine();
                setInfo += "\n\t\tCard area: x = " + role.getArea().getX() + 
                ", y = " + role.getArea().getY() +
                ", w = " + role.getArea().getW() +
                ", h = " + role.getArea().getH();
            }
        } 
        View.getInstance().showPopUp(setInfo);
    }
}