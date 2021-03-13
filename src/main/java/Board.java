// import java.lang.invoke.VolatileCallSite;
import java.util.*;
import org.w3c.dom.Document;

public class Board{
    private List<Card> cardDeck = new Stack<Card>();
    private List<Set> boardSets = new ArrayList<Set>();
    private View view;

    private static Board uniqueInstance;

    public Board(String boardPath, String cardPath){
        this.view = View.getInstance();
        view.init();

        /* Parse XML */
        Document doc = null;
        XMLParser parsing = new XMLParser();

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
    
    // accessor (no args)
    public static synchronized Board getInstance() {
        return uniqueInstance;
    }

    public void setBoard(){
        view.clearCard();
        view.clearShot();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            view.resetCard(boardSets.get(i));
            view.resetShot(boardSets.get(i));
        }
    }
    
    public void resetBoard(){
        view.clearCard();
        view.clearShot();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            boardSets.get(i).resetSet(cardDeck.get(i)); // get from shuffled, not remove
            //System.out.println("hello");
            view.resetCard(boardSets.get(i));
            view.resetShot(boardSets.get(i));
        }
    }

    public void refreshCard(){
        view.clearCard();
        for(int i = 2; i < boardSets.size(); i++){
            view.resetCard(boardSets.get(i));
        }
    }

    public void refreshShot(){
        view.clearShot();
        for(int i = 2; i < boardSets.size(); i++){
            view.resetShot(boardSets.get(i));
        }
    }

    public void refreshDice(View view){
        view.clearDice();
        for(int i = 2; i < boardSets.size(); i++){
            //view.resetPlayerDie(boardSets.get(i));
        }
    }

    // //returns boolean if the set has already been done
    // // obsoleted by Set's isClosed()
    // public boolean setClosed(String pos){
    //     for(Set s: boardSets){
    //         if(pos.equals(s.getName())){
    //             if(s.getFlipStage() == 2){
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

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