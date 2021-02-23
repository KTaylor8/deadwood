import java.util.*;

public class Board{
    String boardPath, cardPath;
    Stack<Card> cardDeck = new Stack<Card>();
    Set[] boardSets;

    public Board(String b, String, c){
        boardPath = b;
        cardPath = c;
        setBoard();
    }

    public void setBoard(){
        try {
            Document doc1 = parsing.getDocFromFile(cardPath);
            cardDeck = parsing.convertDocToCardDeck(doc);
            Collections.shuffle(cardDeck);
            for (Card card: cardDeck) {
                System.out.println(card);
            }

        } catch (NullPointerException e) {
            System.out.println("Null error with card = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error with card = " + e);
            return;
        }

        try{
            Document doc2 = parsing.getDocFromFile(boardPath);
            boardSets = parsing.createBoard(doc2);

        }
        catch(NullPointerException e){
            System.out.println("Null Error with board = " + e);
            return;
        }
        catch(Exception e){
            System.out.println("Error with board = " + e);
            return;
        }

        for(Set s: boardSets){
            s.resetSet(cardDeck.pop());
        }
    }

    public Set[] createBoard(Document d){
        Element root = d.getDocumentElement
        NodeList sets = root.getElementsByTagName("set");
        Set setObj;
        Role role = new Role();
        Set[] totalSets = new Set[sets.getLength];
        
        String setName;
        int takes = 0;

        for(int i = 0; i < sets.getLength(); i++){
            Node sset = sets.item(i);
            
            
            Stack<String> adjacentTiles = new Stack<String>;
            Stack<Role> offCardRoles = new Stack<Role>();
            setName = card.getAttributes().getNamedItem("name").getNodeValue();

            
            NodeList setList = sset.getChildNodes();
            for(int i = 0; i < setList.getLength(); i++){

                if ("neighbors".equals(setList.getNodeName())) {
                    Node adj = setList;
                    adjacentTiles.push(adj.getAttributes().getNamedItem("name").getNodeValue());
                }else if ("part".equals(setList.getNodeName())) {
                    role =  handlePartData(role, setList); // passing empty role in for now--wasn't sure if role should've been declared within this method
                    offCardRoles.push(role);
                    
                } //for part nodes

            }
            
            takes = setList.SelectNodes("takes").Count;

            totalSets[i] = new Set(setName, adjacentTiles, offCardRoles, takes);
        }
        return totalSets;

    }

    public Stack<Card> convertDocToCardDeck(Document d) {

        Element root = d.getDocumentElement();

        NodeList cards = root.getElementsByTagName("card");

        // declare Card, Role, and stacks of Card and Role
        Card cardObj;
        Role role = new Role(); // required to use vscode debugger

        Stack<Card> cardDeck = new Stack<Card>();

        // Declare vars for Role args
        String cardName;
        String budget;
        String sceneNumber = "";
        String sceneDescription = "";

        for (int i = 0; i < cards.getLength(); i++) {

            
            Stack<Role> cardRoles = new Stack<Role>();

            // System.out.println("Printing information for card " + (i + 1));
            System.out.println("\nParsing card " + (i + 1));

            //reads attributes from the cards/nodes
            Node card = cards.item(i);
            cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            // String imgName = card.getAttributes().getNamedItem("img").getNodeValue();
            budget = card.getAttributes().getNamedItem("budget").getNodeValue();
            // System.out.println("Name = " + cardName + ", budget = " + budget);

            //reads attributes and parts from the cards' children
            NodeList cardList = card.getChildNodes();

            for (int j = 0; j < cardList.getLength(); j++) {

                Node cardListSub = cardList.item(j);

                if ("scene".equals(cardListSub.getNodeName())) {
                    //read attributes and text in scene
                    Node scene = cardListSub;
                    sceneNumber = scene.getAttributes().getNamedItem("number").getNodeValue();
                    sceneDescription = scene.getTextContent();
                    // System.out.println(" scene description:" + sceneDescription);
                    // System.out.println(" scene number = " + sceneNumber);
                    // System.out.println("\n");
                    
                } else if ("part".equals(cardListSub.getNodeName())) {
                    role =  handlePartData(role, cardListSub); // passing empty role in for now--wasn't sure if role should've been declared within this method
                    cardRoles.push(role);
                    
                } //for part nodes

            } //for card childnodes
            // System.out.println("\n");

            // init card obj w/ parsed data, push it to deck, and clear stack of roles for this card
            cardObj = new Card(cardName, budget, sceneNumber, sceneDescription, cardRoles);
            cardDeck.push(cardObj);
            cardRoles.clear();

        }//for card nodes

        return cardDeck;
    }//readCardData() method

}