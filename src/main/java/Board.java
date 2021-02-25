import java.util.*;
import org.w3c.dom.Document;

public class Board{
    private List<Card> cardDeck = new Stack<Card>();
    private List<Set> boardSets = new ArrayList<Set>();

    public Board(String boardPath, String cardPath){
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
            if(pos.equals(s.getSetName())){
                if(s.getFlipStage() == 2){
                    return true;
                }
            }
        }
        return false;
    }

    //returns in of the budget of designated set
    public int getBudget(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getSetName())){
                return Integer.valueOf((s.getCard()).budget);
            }
        }
        return 0;
    }

    //returns set with the string name given
    public Set getSet(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getSetName())){
                return s;
            }
        }
        return null;
    }

    //returns list of strings of the neighbors of a given set
    public List<String> getNeighbors(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.getSetName()))
            {
                return s.getNeighbors();
            }
        }
        return null;
    }

    //returns the dollar cost of upgrades from the office
    public int[] getDollarC(){
        for(Set s: boardSets){
            if((s.getSetName()).equals("office")){
                return (s.getUpgradeCD());
            }
        }
        return null;
    }

    //returns the credit cost of upgrades from the office
    public int[] getCreditC(){
        for(Set s: boardSets){
            if((s.getSetName()).equals("office")){
                return (s.getUpgradeCC());
            }
        }
        return null;
    }

    //returns the int of the value of the given role and occupies a designated role
    public int employ(String pos, String roll){
        for(Set s: boardSets){
            //finds the set
            if((s.getSetName()).equals(pos)){
                //checks if role is in off cards
                for(int i = 0; i < (s.getOffCardRoles()).size(); i++){
                    //occupy and return level if equals
                    if(roll.equals(((s.getOffCardRoles()).get(i)).name) && !((s.getOffCardRoles()).get(i)).occupied){
                        ((s.getOffCardRoles()).get(i)).occupy();
                        return Integer.valueOf(((s.getOffCardRoles()).get(i)).level);
                    }
                }
                //checks if role is on card
                for(int i = 0; i < ((s.getCard()).roles).size(); i++){
                    //occupy and return level if equals
                    if(roll.equals((((s.getCard()).roles).get(i)).name) && !((s.getCard()).roles).get(i).occupied){
                        
                        ((s.getCard().roles).get(i)).occupy();
                        return Integer.valueOf((((s.getCard()).roles).get(i)).level);
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
            if((s.getSetName()).equals(pos)){
                //add off card roles to the string
                for(int i = 0; i < (s.getOffCardRoles()).size(); i++){
                    if(!((s.getOffCardRoles()).get(i)).occupied){
                        free += "\nOff card role: " + ((s.getOffCardRoles()).get(i)).name +" must be level: " + ((s.getOffCardRoles()).get(i)).level;
                    }
                }
                //add the on card roles to the string
                if (s.getCard() != null) {
                    for(int i = 0; i < ((s.getCard()).roles).size(); i++){
                        if(!(((s.getCard()).roles).get(i)).occupied){
                            free += "\nOn card role: " + (((s.getCard()).roles).get(i)).name +" must be level: " + (((s.getCard()).roles).get(i)).level + "";
                            
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
            if(s.getFlipStage() != 2){
                numScene++;
            }
        }
        return numScene - 2;
    }
   
}