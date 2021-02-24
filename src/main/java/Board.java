import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Board{
    String boardPath, cardPath;
    Stack<Card> cardDeck = new Stack<Card>();
    List<Set> boardSets = new ArrayList<Set>();

    public Board(String b, String c){
        boardPath = b;
        cardPath = c;
        setBoard();
    }

    public void setBoard(){
        Document doc = null;
        XMLParser parsing = new XMLParser();
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

    public boolean employ(String pos, String roll){
        for(Set s: boardSets){
            if((s.setName).equals(pos)){
                for(int i = 0; i < (s.offCardRoles).size(); i++){
                    if(pos.equals(((s.offCardRoles).get(i)).name) && !((s.offCardRoles).get(i)).occupied){
                        ((s.offCardRoles).get(i)).occupy();
                        return true;
                    }
                }
                for(int i = 0; i < ((s.currentCard).roles).size(); i++){
                    if(pos.equals((((s.currentCard).roles).get(i)).name) && !((s.currentCard).roles).get(i).occupied){
                        ((s.currentCard.roles).get(i)).occupy();
                        return true;
                    }
                }
            }
        }
        return false;
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
            numScene += s.getScene();
        }
        return numScene;
    }
   
}