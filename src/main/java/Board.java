import java.util.*;
import org.w3c.dom.Document;

public class Board{
    String boardPath, cardPath;
    List<Card> cardDeck = new Stack<Card>();
    List<Set> boardSets = new ArrayList<Set>();

    public Board(String b, String c){
        boardPath = b;
        cardPath = c;
        setBoard();
    }

    //sets the board acording to the xml file
    public void setBoard(){
        /* Parse XML */
        Document doc = null;
        XMLParser parsing = new XMLParser();

        try {
            doc = parsing.getDocFromFile(cardPath); // static path: "src/main/resources/xml/cards.xml"
            cardDeck = parsing.convertDocToCardDeck(doc);
            // for (Card card: cardDeck) { // uncomment to test that all the Cards are there
            //     card.printInfo();
            // }

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }

        try {
            doc = parsing.getDocFromFile(boardPath); // static path: "src/main/resources/xml/board.xml"
            boardSets = parsing.parseBoardData(doc);
            // for (Set set: sets) { // uncomment to test that all the Sets are there
            //     set.printInfo();
            // }

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }

        /* Assign cards to sets */
        Collections.shuffle(cardDeck);

        resetBoard();
    }
    
    public void resetBoard(){
        for(int i = 2; i < boardSets.size(); i++){ // exclude first 2 sets, which are office and trailers
            boardSets.get(i).resetSet(cardDeck.get(i)); // get from shuffled, not remove
        }
    }

    //returns boolean if the set has already been done
    public boolean alreadyDone(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName)){
                if(s.flipStage == 2){
                    return true;
                }
            }
        }
        return false;
    }

    //returns in of the budget of designated set
    public int getBudget(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName)){
                return Integer.valueOf((s.currentCard).budget);
            }
        }
        return 0;
    }

    //returns set with the string name given
    public Set getSet(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName)){
                return s;
            }
        }
        return null;
    }

    //reutnrs list of strings of the neighbors of a given set
    public List<String> getNeighbors(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName))
            {
                return s.neighbors;
            }
        }
        return null;
    }

    //returns the dollar cost of upgrades from the office
    public int[] getDollarC(){
        for(Set s: boardSets){
            if((s.setName).equals("office")){
                return (s.getUpgradeCD());
            }
        }
        return null;
    }

    //reutnrs the credit cost of upgrades from the office
    public int[] getCreditC(){
        for(Set s: boardSets){
            if((s.setName).equals("office")){
                return (s.getUpgradeCC());
            }
        }
        return null;
    }

    //returns the int of the value of the given role and occupies a designated role
    public int employ(String pos, String roll){
        for(Set s: boardSets){
            //finds the set
            if((s.setName).equals(pos)){
                //checks if role is in off cards
                for(int i = 0; i < (s.offCardRoles).size(); i++){
                    //occupy and return level if equals
                    if(roll.equals(((s.offCardRoles).get(i)).name) && !((s.offCardRoles).get(i)).occupied){
                        ((s.offCardRoles).get(i)).occupy();
                        return Integer.valueOf(((s.offCardRoles).get(i)).level);
                    }
                }
                //checks if role is on card
                for(int i = 0; i < ((s.currentCard).roles).size(); i++){
                    //occupy and return level if equals
                    if(roll.equals((((s.currentCard).roles).get(i)).name) && !((s.currentCard).roles).get(i).occupied){
                        
                        ((s.currentCard.roles).get(i)).occupy();
                        return Integer.valueOf((((s.currentCard).roles).get(i)).level);
                    }
                }
            }
        }
        //returns really high level just incase of failure
        return 1000;
    }

    //returns a string that is a list of free roles card and off card
    public String freeRoles(String pos){
        String free = "";
        for(Set s: boardSets){
            //if the set name is equal to the name given
            if((s.setName).equals(pos)){
                //add off card roles to the string
                for(int i = 0; i < (s.offCardRoles).size(); i++){
                    if(!((s.offCardRoles).get(i)).occupied){
                        free += "\nOff card role: " + ((s.offCardRoles).get(i)).name +" must be level: " + ((s.offCardRoles).get(i)).level;
                    }
                }
                //add the on card roles to the string
                if (s.currentCard != null) {
                    for(int i = 0; i < ((s.currentCard).roles).size(); i++){
                        if(!(((s.currentCard).roles).get(i)).occupied){
                            free += "\nOn card role: " + (((s.currentCard).roles).get(i)).name +" must be level: " + (((s.currentCard).roles).get(i)).level + "";
                            
                        }
                    }
                }  
            }
        }
        //check to see if the string is empty and then return the string
        if(free.equals("")){
            free += "There are no open roles on this card.";
        }
        return free;
    }

    //returns the number of sets that have not been finished in one day
    public int sceneNum(){
        int numScene = 0;
        for(Set s: boardSets){
            if(s.flipStage != 2){
                numScene++;
            }
        }
        return numScene - 2;
    }
   
}