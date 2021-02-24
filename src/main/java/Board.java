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

        for(Set s: boardSets){
            s.resetSet(cardDeck.remove(cardDeck.size()-1));
        }
    }
    
    public void resetBoard(){
        for(Set s: boardSets){
            s.resetSet(cardDeck.remove(cardDeck.size()-1));
        }
    }

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

    public int getBudget(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName)){
                return Integer.valueOf((s.currentCard).budget);
            }
        }
        return 0;
    }

    public Set getSet(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName)){
                return s;
            }
        }
        return null;
    }

    public List<String> getNeighbors(String pos){
        for(Set s: boardSets){
            if(pos.equals(s.setName))
            {
                return s.neighbors;
            }
        }
        return null;
    }

    public int[] getDollarC(){
        for(Set s: boardSets){
            if((s.setName).equals("office")){
                return (s.getUpgradeCD());
            }
        }
        return null;
    }

    public int[] getCreditC(){
        for(Set s: boardSets){
            if((s.setName).equals("office")){
                return (s.getUpgradeCC());
            }
        }
        return null;
    }

    public int employ(String pos, String roll){
        for(Set s: boardSets){
            if((s.setName).equals(pos)){
                for(int i = 0; i < (s.offCardRoles).size(); i++){
                    if(roll.equals(((s.offCardRoles).get(i)).name) && !((s.offCardRoles).get(i)).occupied){
                        ((s.offCardRoles).get(i)).occupy();
                        return Integer.valueOf(((s.offCardRoles).get(i)).level);
                    }
                }
                for(int i = 0; i < ((s.currentCard).roles).size(); i++){
                    if(roll.equals((((s.currentCard).roles).get(i)).name) && !((s.currentCard).roles).get(i).occupied){
                        
                        ((s.currentCard.roles).get(i)).occupy();
                        return Integer.valueOf((((s.currentCard).roles).get(i)).level);
                    }
                }
            }
        }
        return 1000;
    }

    public String freeRoles(String pos){
        String free = "";
        for(Set s: boardSets){
            if((s.setName).equals(pos)){
                for(int i = 0; i < (s.offCardRoles).size(); i++){
                    if(!((s.offCardRoles).get(i)).occupied){
                        free += "\nOff card role: " + ((s.offCardRoles).get(i)).name +" must be level: " + ((s.offCardRoles).get(i)).level;
                    }
                }
                for(int i = 0; i < ((s.currentCard).roles).size(); i++){
                    if(!(((s.currentCard).roles).get(i)).occupied){
                        free += "\nOn card role: " + (((s.currentCard).roles).get(i)).name +" must be level: " + (((s.currentCard).roles).get(i)).level + "";
                        
                    }
                }
            }
        }
        if(free.equals("")){
            free += "There are no open roles on this card.";
        }
        return free;
    }

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