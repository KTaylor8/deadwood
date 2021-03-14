// import java.lang.invoke.VolatileCallSite;
import java.util.*;
import org.w3c.dom.Document;

// uses singleton with lazy initialization b/c of args
public class Board{
    private List<Card> cardDeck = new Stack<Card>();
    private List<Set> boardSets = new ArrayList<Set>();
    private View view;

    private static Board uniqueInstance;

    public Board(String boardPath, String cardPath){
        view = View.getInstance();
        view.init();  // view needs to be initialized before XMLParser is used

        /* Parse XML */
        Document doc = null;
        XMLParser parsing = XMLParser.getInstance();

        try {
            doc = parsing.getDocFromFile(cardPath); // static path: "src/main/resources/xml/cards.xml"
            cardDeck = parsing.parseCardData(doc);
            // for (Card card: cardDeck) { // uncomment to test that all the Cards are there
            //     card.printInfo();
            // }

        } catch (NullPointerException e) {
            view.showPopUp("Error = " + e);
            return;
        } catch (Exception e) {
            view.showPopUp("Error parsing cards: ");
            e.printStackTrace();
            return;
        }

        try {
            doc = parsing.getDocFromFile(boardPath); // static path: "src/main/resources/xml/board.xml"
            boardSets = parsing.parseBoardData(doc);
            // for (Set set: boardSets) { // uncomment to test that all the Sets are there
            //     set.printInfo();
            // }

        } catch (NullPointerException e) {
            view.showPopUp("Error = " + e);
            return;
        } catch (Exception e) {
            view.showPopUp("Error parsing sets: ");
            e.printStackTrace();
            return;
        }

        /* Assign cards to sets */
        Collections.shuffle(cardDeck);

    }

    // initializer (w/ args)
    public static synchronized Board getInstance(String boardPath, String cardPath) {
        if (uniqueInstance == null) {
            uniqueInstance = new Board(boardPath, cardPath);
        }
        return uniqueInstance;
    }
    
    // accessor (no args) -- not currently used anywhere, but we can keep it if we want
    public static synchronized Board getInstance() {
        return uniqueInstance;
    }

    public void resetBoard(){
        view.clearCard();
        view.clearShotPanel();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            boardSets.get(i).resetSet(cardDeck.get(i)); // get from shuffled, not remove
            // System.out.println("Set name: " + boardSets.get(i).getName() + "; flip stage: " + boardSets.get(i).getFlipStage());
            view.resetCard(boardSets.get(i));
            view.resetShotPanel(boardSets.get(i));
        }
    }

    public void resetBoardTest(){ //TEST
        // view.clearCard();
        view.clearShotPanelTest();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            // boardSets.get(i).resetSet(cardDeck.get(i)); // get from shuffled, not remove  -
            // System.out.println("Set name: " + boardSets.get(i).getName() + "; flip stage: " + boardSets.get(i).getFlipStage());
            // view.resetCard(boardSets.get(i));
            // view.resetShotPanel(boardSets.get(i));
        }
    }

    // TEST
    public void reloadImgsTest(Player p, int j){ // WE MIGHT WANT TO MAKE SEPARATE METHODS FOR AFTER MOVE/TAKEROLE/ACT INSTEAD OF RELOADING THE WHOLE BOARD'S IMGS EACH TIME, BUT THIS IS OK FOR NOW
    // view.clearCard();
    view.clearShotPanelTest2();

    // view.refreshShotPanelTest(p, j);
    
    // for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
    //     view.resetCard(boardSets.get(i));
    //     // view.resetShotPanel(boardSets.get(i));
    // }
    
    }
    
    public void reloadImgs(){ // WE MIGHT WANT TO MAKE SEPARATE METHODS FOR AFTER MOVE/TAKEROLE/ACT INSTEAD OF RELOADING THE WHOLE BOARD'S IMGS EACH TIME, BUT THIS IS OK FOR NOW
        view.clearCard();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            view.resetCard(boardSets.get(i));
            view.resetShotPanel(boardSets.get(i));
        }
    }

    public void refreshCards(){
        view.clearCard();
        for(int i = 2; i < boardSets.size(); i++){
            view.resetCard(boardSets.get(i));
        }
    }

    public void refreshShots(){
        view.clearShotPanel();
        for(int i = 2; i < boardSets.size(); i++){
            view.resetShotPanel(boardSets.get(i));
        }
    }

    public void refreshDice(){
        view.clearDice();
        for(int i = 2; i < boardSets.size(); i++){
            //view.resetPlayerDie(boardSets.get(i));
        }
    }

    //returns in of the budget of designated set
    public int getBudget(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getName())){
                return Integer.valueOf((s.getCard()).getBudget());
            }
        }
        return 0;
    }

    public List<Set> getAllSets(){
        return boardSets;
    }

    //returns set with the string name given
    public Set getSet(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getName())){
                return s;
            }
        }
        return null;
    }

    public boolean isOnCard(String st, Set s){
        List<Role> r = s.getOffCardRoles();
        for(int i = 0; i < r.size(); i++){
            if(r.get(i).getName().equals(st)){
                return true;
            }
        }
        return false;
    }    

    //returns list of strings of the neighbors of a given set
    public List<String> getNeighbors(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getName()))
            {
                return s.getNeighbors();
            }
        }
        return null;
    }

    //occupies a designated role -- obsoleted by assigning Role obj to Player obj
    // public void fillRole(Set location, Role role){
    //     if( !(role.isOccupied()) ){
    //         role.occupy();
    //     } else {
    //         view.showPopUp("Can't fill role: role not empty.");
    //     }
    // }

    //returns a string that is a list of free roles card and off card
    /*
    public String[] freeRoles(String pos){
        String[] free = "";
        for(Set s: boardSets){
            //if the set name is equal to the name given
            if((s.getName()).equals(pos)){
                //add off card roles to the string
                for(int i = 0; i < (s.getOffCardRoles()).size(); i++){
                    if(!((s.getOffCardRoles()).get(i)).isOccupied()){
                        free += "\nOff card role: " + ((s.getOffCardRoles()).get(i)).getName() +" must be level: " + ((s.getOffCardRoles()).get(i)).getLevel();
                    }
                }
                //add the on card roles to the string
                if (s.getCard() != null) {
                    for(int i = 0; i < ((s.getCard()).getOnCardRoles()).size(); i++){
                        if(!(((s.getCard()).getOnCardRoles()).get(i)).isOccupied()){
                            free += "\nOn card role: " + (((s.getCard()).getOnCardRoles()).get(i)).getName() +" must be level: " + (((s.getCard()).getOnCardRoles()).get(i)).getLevel() + "";
                            
                        }
                    }
                }  
            }
        }
        //check to see if the string is empty and then return the string
        if(free.equals("")){
            free += "There are no open roles at this location.";
        }
        return free;
    } */

    //returns the number of sets that have not been finished in one day
    public int getSceneNum(){
        int numScene = 0;
        for(Set s: boardSets){
            if(s.getFlipStage() != 2){
                numScene++;
            }
        }
        return numScene - 2;
    }
   
}