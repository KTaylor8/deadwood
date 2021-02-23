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

import java.util.*;

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

    private Set[] appendSet(Set[] setArr, Set newSet) {
        Set[] newSetArr = new Set[setArr.length];

        int i;
        for (i = 0; i < setArr.length; i++) {
            newSetArr[i] = setArr[i];
        }
        newSetArr[i] = newSet;

        return newSetArr;
    }

    private int[] appendInt(int[] intArr, int newInt) {
        int[] newIntArr = new int[intArr.length];

        int i;
        for (i = 0; i < intArr.length; i++) {
            newIntArr[i] = intArr[i];
        }
        newIntArr[i] = newInt;

        return newIntArr;
    }

    private Stack<String> handleNeighborData(NodeList neighborList) {
        Stack<String> neighbors = new Stack<String>();
        Node neighbor;
        String neighborName;
        Node neighborListSub;

        for (int k = 1; k < neighborList.getLength(); k++) {
            neighborListSub = neighborList.item(k);
            // not all items in neighborList are actual "neighbor"s; some are metadata or something
            if ("neighbor".equals(neighborListSub.getNodeName())) {
                neighbor = neighborListSub;
                neighborName = neighbor.getAttributes().getNamedItem("name").getNodeValue();
                // System.out.println("  neighbor name: " + neighborName);
            }
        }

        return neighbors;
    }

    // class for Area, so then it's easier to store and access the dimensions
    // let's move this to it's own java file later if we decide to keep this class
    // public class AreaData{ // uncomment for GUI
    //     String x;
    //     String y;
    //     String h;
    //     String w;

    //     AreaData(String x, String y, String h, String w) {
    //         this.x = x;
    //         this.y = y;
    //         this.h = h;
    //         this.w = w;
    //     }
    // }

    // private AreaData handleAreaData(Node area) { // uncomment for GUI
    //     // String areaX = area.getAttributes().getNamedItem("x").getNodeValue();
    //     // String areaY = area.getAttributes().getNamedItem("y").getNodeValue();
    //     // String areaH = area.getAttributes().getNamedItem("h").getNodeValue();
    //     // String areaW = area.getAttributes().getNamedItem("w").getNodeValue();
    //     // System.out.println("  area: ");
    //     // System.out.println("   x = " + areaX + ", y = " + areaY  + ", h = " + areaH + ", w = " + areaW);

    //     return new AreaData(
    //         area.getAttributes().getNamedItem("x").getNodeValue(),
    //         area.getAttributes().getNamedItem("y").getNodeValue(),
    //         area.getAttributes().getNamedItem("h").getNodeValue(),
    //         area.getAttributes().getNamedItem("w").getNodeValue()
    //     );
    // }//handleAreaData() method

    private Role handlePartData(Role roleObj, Node part){
        
        // Part data for Role init
        // AreaData partArea; // uncomment for GUI
        String partName;
        String partLevel;
        String partLine = "";

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
                // partArea = handleAreaData(partChildrenSub); // uncomment for GUI
                // System.out.println("\n");
            } else if ("line".equals(partChildrenSub.getNodeName())) {
                //reads text from part's line
                partLine = partChildrenSub.getTextContent();
                // System.out.println("  Line = " + line);
            }

        } //for part childnodes

        // roleObj = new Role(partName, partLevel, partArea, partLine); // uncomment for GUI
        roleObj = new Role(partName, partLevel, partLine);

        return roleObj;
    }//handlePartData() method

    // reads data in board.xml, stores it in Set objects, stores those objects in a stack and returns stack
    public Set[] parseBoardData(Document d) {
        Node office; /* Element with tag name "office" */
        Node trailers; /* Element with tag name "trailers" */
        NodeList sets; /* NodeList of elements with tag name "set" */

        // declare class objects and their stacks
        Set setObj;
        Role role;

        Set[] setArr;
        Stack<Role> setRoles;

        Stack<String> neighborStack;

        Element root = d.getDocumentElement();

        // parse office Node and append it to setArr
        office = root.getElementsByTagName("office").item(0);
        System.out.println("Parsing data for office");

        NodeList officeChildren;
        String officeName;
        Node officeChildSub;

        // NodeList neighborList;
        // Node neighborListSub;

        NodeList takeList;
        Node takeListSub;
        Node take;
        int numTakes;

        System.out.println("Parsing data for office");

        // office vars
        NodeList upgradesList;
        Node upgradesListSub;
        Node upgrade;
        int upgradeCost;

        int[] upgradeD = new int[5];
        int[] upgradeC = new int[5];

        //reads attributes and parts from the offices' children
        officeChildren = office.getChildNodes();

        // AreaData officeArea;
        // AreaData takeArea;

        for (int j = 0; j < officeChildren.getLength(); j++) {

            officeChildSub = officeChildren.item(j);

            if ("neighbors".equals(officeChildSub.getNodeName())) {
                //read/parse  neighbor children
                neighborStack = handleNeighborData(officeChildSub.getChildNodes());
            } else if ("area".equals(officeChildSub.getChildNodes())) {
                // officeArea = handleAreaData(officeChildSub); // uncomment for GUI
            } else if ("upgrades".equals(officeChildSub.getNodeName())) {
                //read attributes for takes and their area children
                upgradesList = officeChildSub.getChildNodes();
                Queue<Node> filteredUpgrades = new PriorityQueue<Node>();
                for (int k = 1; k < upgradesList.getLength(); k++) {
                    upgradesListSub = upgradesList.item(k);
                    // not all items in upgradesList are actual "upgrade"s
                    if ("upgrade".equals(upgradesListSub.getNodeName())) {
                        filteredUpgrades.add(upgradesListSub);
                    }
                }

                // upgrade amounts in dollars
                for (int k = 0; k < 5; k++) {
                    upgradeCost = Integer.parseInt(filteredUpgrades.poll().getAttributes().getNamedItem("amt").getNodeValue());
                    appendInt(upgradeD, upgradeCost);
                }

                // upgrade amounts in credits
                for (int k = 0; k < 5; k++) {
                    upgradeCost = Integer.parseInt(filteredUpgrades.poll().getAttributes().getNamedItem("amt").getNodeValue());
                    appendInt(upgradeD, upgradeCost);
                }

                //handle upgrade's child <area>
                // handleAreaData(upgrade.getChildNodes().item(0));
                // technically, there's 1 item, but we want loose coupling
                // NodeList upgradeChildrenNodes = upgrade.getChildNodes();
                // ignoring area data for now; uncomment for GUI
                // for (int l = 0; l < upgradeChildrenNodes.getLength(); l++) {
                //     Node upgradeChildrenSub = upgradeChildrenNodes.item(l);
                //     if ("area".equals(upgradeChildrenSub.getNodeName())) {
                //         // upgradeArea = handleAreaData(upgradeChildrenSub); // uncomment for GUI
                //     }
                // }

            }
            // don't use an else block

        } //for office childnodes

        setObj = new Set("office", neighborStack, upgradeD, upgradeC);
        appendSet(setArr, setObj);

        // parse trailers Node and append it to setArr
        System.out.println("Parsing data for trailers");
        NodeList trailersChildren;
        Node trailersChildSub;

        trailers = root.getElementsByTagName("trailers").item(0);

        //reads attributes and parts from the trailers' children
        trailersChildren = trailers.getChildNodes();

        for (int j = 0; j < trailersChildren.getLength(); j++) {

            trailersChildSub = trailersChildren.item(j);

            if ("neighbors".equals(trailersChildSub.getNodeName())) {
                //read/parse  neighbor children
                neighborStack = handleNeighborData(trailersChildSub.getChildNodes());
            } else if ("area".equals(trailersChildSub.getChildNodes())) {
                // trailersArea = handleAreaData(trailersChildSub); // uncomment for GUI
            }
        }

        setObj = new Set("trailers", neighborStack);
        appendSet(setArr, setObj);

        // parse set Nodes and append them to setArr
        sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            Node set; /* Individual set Node from sets */
            NodeList setChildren;
            String setName;
            Node setChildSub;

            System.out.println("Parsing data for set " + (i + 1));

            //reads attributes from the sets/nodes
            set = sets.item(i);
            setName = set.getAttributes().getNamedItem("name").getNodeValue();
            // String setName = set.getAttributes().getNamedItem("name").getNodeValue();
            // System.out.println("Name = " + setName);

            //reads attributes and parts from the sets' children
            setChildren = set.getChildNodes();

            // AreaData setArea;
            // AreaData takeArea;

            for (int j = 0; j < setChildren.getLength(); j++) {

                setChildSub = setChildren.item(j);

                if ("neighbors".equals(setChildSub.getNodeName())) {
                    //read/parse  neighbor children
                    neighborStack = handleNeighborData(setChildSub.getChildNodes());

                    // System.out.println("\n");
                } else if ("area".equals(setChildSub.getNodeName())) {
                    // setArea = handleAreaData(setChildSub); // uncomment for GUI
                } else if ("takes".equals(setChildSub.getNodeName())) {
                    //read attributes for takes and their area children
                    takeList = setChildSub.getChildNodes();
                    numTakes = 0;
                    for (int k = 1; k < takeList.getLength(); k++) {
                        takeListSub = takeList.item(k);
                        // not all items in takeList are actual "take"s
                        if ("take".equals(takeListSub.getNodeName())) {
                            take = takeListSub;
                            numTakes++;
                            // String takeNumber = take.getAttributes().getNamedItem("number").getNodeValue();
                            // System.out.println("  take number: " + takeNumber);

                            //handle take's child <area>
                            // handleAreaData(take.getChildNodes().item(0));
                            // technically, there's 1 item, but we want loose coupling
                            // NodeList takeChildrenNodes = take.getChildNodes();
                            // ignoring area data for now; uncomment for GUI
                            // for (int l = 0; l < takeChildrenNodes.getLength(); l++) {
                            //     Node takeChildrenSub = takeChildrenNodes.item(l);
                            //     if ("area".equals(takeChildrenSub.getNodeName())) {
                            //         // takeArea = handleAreaData(takeChildrenSub); // uncomment for GUI
                            //     }
                            // }
                        }
                    }
                } else if ("part".equals(setChildSub.getNodeName())) {
                    role = handlePartData(role, setChildSub);
                } //for part nodes
                // don't use an else block

                setObj = new Set(setName, neighborStack, setRoles, numTakes);

            } //for set childnodes

            appendSet(setArr, setObj);

        
        }//for set nodes

        return setArr;

    }//readBoardData() method

    // reads card data from XML file, stores it in Card objects, stores those objects in a stack and returns stack
    public Stack<Card> convertDocToCardDeck(Document d) {
        NodeList cards;

        // declare Card, Role, and stacks of Card and Role
        Card cardObj;
        Role role;

        Stack<Card> cardDeck;
        Stack<Role> cardRoles;

        // Declare vars for Role args
        String cardName;
        String budget;
        String sceneNumber;
        String sceneDescription;

        Element root = d.getDocumentElement();

        cards = root.getElementsByTagName("card");

        for (int i = 0; i < cards.getLength(); i++) {
            cardRoles = new Stack<Role>(); // want a new stack each time, don't try to clear and reuse the same one

            // System.out.println("Printing information for card " + (i + 1));
            System.out.println("Parsing card " + (i + 1));

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

        }//for card nodes

        return cardDeck;
    }//readCardData() method


}//class