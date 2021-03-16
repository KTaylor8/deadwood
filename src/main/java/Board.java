import java.util.*;
import org.w3c.dom.Document;

// uses singleton with lazy initialization b/c of args
public class Board{
    private List<Card> cardDeck = new Stack<Card>();
    private List<Set> boardSets = new ArrayList<Set>();
    private View view;

    private static Board uniqueInstance;

    /**
     * Constructor for the board that creates the board based off of the xml files
     * @param boardPath
     * @param cardPath
     */
    private Board(String boardPath, String cardPath){
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
            view.showErrorPopUp("Error = " + e);
            return;
        } catch (Exception e) {
            view.showErrorPopUp("Error parsing cards: ");
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
            view.showErrorPopUp("Error = " + e);
            return;
        } catch (Exception e) {
            view.showErrorPopUp("Error parsing sets: ");
            e.printStackTrace();
            return;
        }

        /* Assign cards to sets */
        Collections.shuffle(cardDeck);

    }

    /**
     * initializer (w/ args)
     * @param boardPath
     * @param cardPath
     * @return
     */
    public static synchronized Board getInstance(String boardPath, String cardPath) {
        if (uniqueInstance == null) {
            uniqueInstance = new Board(boardPath, cardPath);
        }
        return uniqueInstance;
    }
    
    /**
     * accessor (no args)
     * @return
     */
    public static synchronized Board getInstance() {
        return uniqueInstance;
    }

    public List<Set> getAllSets(){
        return boardSets;
    }

    /**
     * Resets the views cards and shots, as well as sets new cards down in the places of sets
     */
    public void resetBoard(){
        view.clearCard();
        view.clearShot();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            boardSets.get(i).resetSet(cardDeck.remove(i-2)); // get from shuffled, not remove
            view.resetCard(boardSets.get(i));
            view.resetShot(boardSets.get(i));
        }
    }
    
    /**
     * Reloads the images of the board without setting down new cards, to update in between turns
     */
    public void reloadImgs(){
        view.clearCard();
        view.clearShot();
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            view.resetCard(boardSets.get(i));
            view.resetShot(boardSets.get(i));
        }
    }

    /**
     * refreshes the cards on the board incase they were flipped or removed.
     */
    public void refreshCards(){
        view.clearCard();
        for(int i = 2; i < boardSets.size(); i++){
            view.resetCard(boardSets.get(i));
        }
    }

    /**
     * refreshes the takes on the board in case they were removed or added
     */
    public void refreshShots(){
        view.clearShot();
        for(int i = 2; i < boardSets.size(); i++){
            view.resetShot(boardSets.get(i));
        }
    }

    /**
     * returns the budget of the set that has the name equal to the string given
     * @param pos
     * @return
     */
    public int getBudget(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getName())){
                return Integer.valueOf((s.getCard()).getBudget());
            }
        }
        return 0;
    }

    

    /**
     * returns the set with the given string name
     * @param pos
     * @return
     */
    public Set getSet(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getName())){
                return s;
            }
        }
        return null;
    }

    /**
     * returns a boolean if the given name string is equal to a role that is on card for the set given
     * @param st
     * @param s
     * @return
     */
    public boolean isOnCard(String st, Set s){
        List<Role> r = s.getOffCardRoles();
        for(int i = 0; i < r.size(); i++){
            if(r.get(i).getName().equals(st)){
                return true;
            }
        }
        return false;
    }    

    
    /**
     * gets all of the neighbors of the set that has the name equal to the name string given
     * @param pos
     * @return
     */
    public List<String> getNeighbors(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getName()))
            {
                return s.getNeighbors();
            }
        }
        return null;
    }

    /**
     * returns the number of scenes remaining on the board that have not been wrapped up
     * @return
     */
    public int getSceneNum(){
        int numScene = 0;
        for(Set s: boardSets){
            if(s.getFlipStage() != 2){
                numScene++;
            }
        }
        return numScene - 2; // office & trailers will never wrap so subtract those
    }
   
}