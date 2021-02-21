/**
 * Based on example code for parsing XML file
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {


    // building a document from the XML file
    // returns a Document object after loading the card.xml file.
    public Document getDocFromFile(String filename) throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;

            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    // class for Area, so then it's easier to store and access the dimensions
    private class AreaData{
        String x;
        String y;
        String h;
        String w;

        AreaData(String x, String y, String h, String w) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
        }
    }

    private AreaData handleAreaData(Node area) {
        // String areaX = area.getAttributes().getNamedItem("x").getNodeValue();
        // String areaY = area.getAttributes().getNamedItem("y").getNodeValue();
        // String areaH = area.getAttributes().getNamedItem("h").getNodeValue();
        // String areaW = area.getAttributes().getNamedItem("w").getNodeValue();
        // System.out.println("  area: ");
        // System.out.println("   x = " + areaX + ", y = " + areaY  + ", h = " + areaH + ", w = " + areaW);

        return new AreaData(
            area.getAttributes().getNamedItem("x").getNodeValue(),
            area.getAttributes().getNamedItem("y").getNodeValue(),
            area.getAttributes().getNamedItem("h").getNodeValue(),
            area.getAttributes().getNamedItem("w").getNodeValue()
        );
    }//handleAreaData() method

    private Role handlePartData(Role roleObj, Node part){
        
        // Part data for Role init
        AreaData partArea;
        String partName;
        String partLevel;
        String partLine;

        //read attributes and text in part
        partName = part.getAttributes().getNamedItem("name").getNodeValue();;
        partLevel = part.getAttributes().getNamedItem("level").getNodeValue();
        // System.out.println(" part name = " + partName + ", part level = " + partLevel);

        //reads attributes and text from parts' children
        NodeList partChildren = part.getChildNodes();

        for (int k = 0; k < partChildren.getLength(); k++) {

            Node partChildrenSub = partChildren.item(k);

            if ("area".equals(partChildrenSub.getNodeName())) {
                //reads attributes and text from part's area
                partArea = handleAreaData(partChildrenSub);
                // System.out.println("\n");
            } else if ("line".equals(partChildrenSub.getNodeName())) {
                //reads text from part's line
                partLine = partChildrenSub.getTextContent();
                // System.out.println("  Line = " + line);
            }

        } //for part childnodes

        roleObj = new Role(partName, partLevel, partArea, partLine);

        return roleObj;
    }//handlePartData() method

    // reads board data from XML file and prints data
    public void readBoardData(Document d) {

        Element root = d.getDocumentElement();

        NodeList sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {

            System.out.println("Printing information for set " + (i + 1));

            //reads attributes from the sets/nodes
            Node set = sets.item(i);
            String setName = set.getAttributes().getNamedItem("name").getNodeValue();
            System.out.println("Name = " + setName);

            //reads attributes and parts from the sets' children
            NodeList setChildren = set.getChildNodes();

            AreaData setArea;
            AreaData takeArea;

            for (int j = 0; j < setChildren.getLength(); j++) {

                Node setChildSub = setChildren.item(j);

                if ("neighbors".equals(setChildSub.getNodeName())) {
                    //read/parse  neighbor children
                    NodeList neighborList = setChildSub.getChildNodes();
                    for (int k = 1; k < neighborList.getLength(); k++) {
                        Node neighborListSub = neighborList.item(k);
                        // not all items in neighborList are actual "neighbor"s; some are metadata or something
                        if ("neighbor".equals(neighborListSub.getNodeName())) {
                            Node neighbor = neighborListSub;
                            String neighborName = neighbor.getAttributes().getNamedItem("name").getNodeValue();
                            System.out.println("  neighbor name: " + neighborName);
                        }
                    }
                    // System.out.println("\n");
                } else if ("area".equals(setChildSub.getNodeName())) {
                    setArea = handleAreaData(setChildSub);
                } else if ("takes".equals(setChildSub.getNodeName())) {
                    //read attributes for takes and their area children
                    NodeList takeList = setChildSub.getChildNodes();
                    for (int k = 1; k < takeList.getLength(); k++) {
                        Node takeListSub = takeList.item(k);
                        // not all items in takeList are actual "take"s
                        if ("take".equals(takeListSub.getNodeName())) {
                            Node take = takeListSub;
                            String takeNumber = take.getAttributes().getNamedItem("number").getNodeValue();
                            System.out.println("  take number: " + takeNumber);

                            //handle take's child <area>
                            // handleAreaData(take.getChildNodes().item(0));
                            // technically, there's 1 item, but we want loose coupling
                            NodeList takeChildrenNodes = take.getChildNodes();
                            for (int l = 0; l < takeChildrenNodes.getLength(); l++) {
                                Node takeChildrenSub = takeChildrenNodes.item(l);
                                if ("area".equals(takeChildrenSub.getNodeName())) {
                                    takeArea = handleAreaData(takeChildrenSub);
                                }
                            }
                        }
                    }
                } else if ("part".equals(setChildSub.getNodeName())) {
                    handlePartData(setChildSub);
                } //for part nodes
                // don't use an else block

            } //for set childnodes
            System.out.println("\n");
        }//for set nodes

    }//readBoardData() method

    // reads card data from XML file, stores it in Card objects, stores those objects in a stack and returns stack
    public Stack<Card> convertDocToCardDeck(Document d) {

        Element root = d.getDocumentElement();

        NodeList cards = root.getElementsByTagName("card");

        // declare Card, Role, and stacks of Card and Role
        Card cardObj;
        Role role;

        Stack<Card> cardDeck = new Stack<Card>();
        Stack<Role> cardRoles = new Stack<Role>();

        for (int i = 0; i < cards.getLength(); i++) {

            // System.out.println("Printing information for card " + (i + 1));
            System.out.println("\nParsing card " + (i + 1));

            //reads attributes from the cards/nodes
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            // String imgName = card.getAttributes().getNamedItem("img").getNodeValue();
            String budget = card.getAttributes().getNamedItem("budget").getNodeValue();
            // System.out.println("Name = " + cardName + ", budget = " + budget);

            //reads attributes and parts from the cards' children
            NodeList cardList = card.getChildNodes();

            for (int j = 0; j < cardList.getLength(); j++) {

                Node cardListSub = cardList.item(j);

                if ("scene".equals(cardListSub.getNodeName())) {
                    //read attributes and text in scene
                    Node scene = cardListSub;
                    String sceneNumber = scene.getAttributes().getNamedItem("number").getNodeValue();
                    String sceneDescription = scene.getTextContent();
                    // System.out.println(" scene description:" + sceneDescription);
                    // System.out.println(" scene number = " + sceneNumber);
                    // System.out.println("\n");
                    
                } else if ("part".equals(cardListSub.getNodeName())) {
                    role =  handlePartData(role, cardListSub); // passing empty role in for now--wasn't sure if role should've been init-ed within this method
                    cardRoles.push(role);
                    
                } //for part nodes

            } //for card childnodes
            // System.out.println("\n");

            // init card obj w/ parsed data, push it to deck, and clear stack of roles for this card
            cardObj = new Card(cardName, budget, sceneNumber, sceneDescription, cardRoles);
            cardDeck.push(cardObj);
            cardRoles.clear();

        }//for card nodes

    }//readCardData() method


}//class