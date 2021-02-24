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

    // private Set[] appendSet(Set[] setArr, Set newSet) {
    //     Set[] newSetArr = new Set[setArr.length+1];

    //     int i;
    //     for (i = 0; i < setArr.length; i++) {
    //         newSetArr[i] = setArr[i];
    //     }
    //     newSetArr[i] = newSet;

    //     return newSetArr;
    // }

    // private int[] appendInt(int[] intArr, int newInt) {
    //     int[] newIntArr = new int[intArr.length+1];

    //     int i;
    //     for (i = 0; i < intArr.length; i++) {
    //         newIntArr[i] = intArr[i];
    //     }
    //     newIntArr[i] = newInt;

    //     return newIntArr;
    // }

    // accepts a list of potential neighbors
    private List<String> handleNeighborData(NodeList neighborList) {
        List<String> neighbors = new ArrayList<String>();
        String neighborName;
        Node neighborListSub;

        for (int k = 1; k < neighborList.getLength(); k++) {
            neighborListSub = neighborList.item(k);
            // not all items in neighborList are actual "neighbor"s; some are metadata or something
            if ("neighbor".equals(neighborListSub.getNodeName())) {
                neighborName = neighborListSub.getAttributes().getNamedItem("name").getNodeValue();

                neighbors.add(neighborName);
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

    // accepts a single Node part
    private Role handlePartData(Role roleObj, Node part){
        // Declare vars for part data handling
        NodeList partChildren;
        Node partChildrenSub;

        // Part data for Role constructor
        // AreaData partArea; // uncomment for GUI
        String partName;
        String partLevel;
        String partLine = "";

        //read part's attributes and text
        partName = part.getAttributes().getNamedItem("name").getNodeValue();
        partLevel = part.getAttributes().getNamedItem("level").getNodeValue();

        //reads attributes and text from parts' children
        partChildren = part.getChildNodes();

        for (int k = 0; k < partChildren.getLength(); k++) {
            partChildrenSub = partChildren.item(k);

            if ("area".equals(partChildrenSub.getNodeName())) {
                // partArea = handleAreaData(partChildrenSub); // uncomment for GUI
            } else if ("line".equals(partChildrenSub.getNodeName())) {
                partLine = partChildrenSub.getTextContent();
            }

        } //for part childnodes

        // roleObj = new Role(partName, partLevel, partArea, partLine); // uncomment for GUI
        roleObj = new Role(partName, partLevel, partLine);

        return roleObj;
    }//handlePartData() method

    // reads data in board.xml, stores it in Set objects, stores those objects in a List and returns List
    public List<Set> parseBoardData(Document d) {
        NodeList sets; /* NodeList of elements with tag name "set" */

        // declare class objects and their Lidyd
        Set setObj;
        Role role = new Role();
        List<Set> setList = new ArrayList<Set>();
        List<Role> setRoles = new ArrayList<Role>();
        List<String> neighbors = new ArrayList<String>();

        Element root = d.getDocumentElement();


        /* Parse office data */

        // Declare vars for office data handling
        Node office; /* Element with tag name "office" */
        NodeList officeChildren;
        Node officeChildSub;

        // Upgrade vars
        NodeList upgradesList;
        Queue<Node> filteredUpgrades; /* Intermediary storage helps parse upgrades */
        Node upgradesListSub;
        int upgradeCost;
        int[] upgradeDollars = new int[5]; // number of upgrade options currently hard-coded, but we might make it dynamic later
        int[] upgradeCredits = new int[5];

        //reads attributes and parts from the offices' children
        office = root.getElementsByTagName("office").item(0);
        officeChildren = office.getChildNodes();

        // AreaData officeArea;
        // AreaData takeArea;

        for (int j = 0; j < officeChildren.getLength(); j++) {

            officeChildSub = officeChildren.item(j);

            if ("neighbors".equals(officeChildSub.getNodeName())) {
                neighbors = handleNeighborData(officeChildSub.getChildNodes());
            } else if ("area".equals(officeChildSub.getNodeName())) {
                // officeArea = handleAreaData(officeChildSub); // uncomment for GUI
            } else if ("upgrades".equals(officeChildSub.getNodeName())) {
                //read attributes for takes and their area children
                upgradesList = officeChildSub.getChildNodes();
                filteredUpgrades = new LinkedList<Node>();
                for (int k = 1; k < upgradesList.getLength(); k++) {
                    upgradesListSub = upgradesList.item(k);
                    // not all items in upgradesList are actual "upgrade"s
                    if ("upgrade".equals(upgradesListSub.getNodeName())) {
                        filteredUpgrades.add(upgradesListSub);
                    }
                }

                // parse upgrade amounts in dollars
                for (int k = 0; k < 5; k++) {
                    upgradeCost = Integer.parseInt(filteredUpgrades.poll().getAttributes().getNamedItem("amt").getNodeValue());
                    upgradeDollars[k] = upgradeCost;
                }

                // parse upgrade amounts in credits
                for (int k = 0; k < 5; k++) {
                    upgradeCost = Integer.parseInt(filteredUpgrades.poll().getAttributes().getNamedItem("amt").getNodeValue());
                    upgradeCredits[k] = upgradeCost;
                }

                // ignoring area data for now; uncomment for GUI
                // for (int l = 0; l < upgradeChildrenNodes.getLength(); l++) {
                //     Node upgradeChildrenSub = upgradeChildrenNodes.item(l);
                //     if ("area".equals(upgradeChildrenSub.getNodeName())) {
                //         // upgradeArea = handleAreaData(upgradeChildrenSub);
                //     }
                // }
            }
            // don't use an else block

        } //for office childnodes
        setList.add(new Set("office", neighbors, upgradeDollars, upgradeCredits));


        /* Parse trailer data */

        // Declare vars for trailer data handling
        Node trailer; /* Element with tag name "trailer" */
        NodeList trailerChildren;
        Node trailerChildSub;

        //reads attributes and parts from the trailer's children
        trailer = root.getElementsByTagName("trailer").item(0);
        trailerChildren = trailer.getChildNodes();

        for (int j = 0; j < trailerChildren.getLength(); j++) {
            trailerChildSub = trailerChildren.item(j);

            if ("neighbors".equals(trailerChildSub.getNodeName())) {
                neighbors = handleNeighborData(trailerChildSub.getChildNodes());
            } else if ("area".equals(trailerChildSub.getNodeName())) {
                // trailerArea = handleAreaData(trailerChildSub); // uncomment for GUI
            }
        }
        setList.add(new Set("trailer", neighbors));


        /* Parse normal Set data */
        sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            Node set; /* Individual set Node from sets */
            NodeList setChildren;
            String setName;
            Node setChildSub;

            NodeList takeList;
            Node takeListSub;
            Node take;
            int numTakes = 0;

            NodeList partList;
            Node partListSub;

            setRoles = new ArrayList<Role>(); // want a new List each time, don't try to clear and reuse the same one

            // System.out.println("Parsing data for set " + (i + 1));

            //reads attributes from the sets/nodes
            set = sets.item(i);
            setName = set.getAttributes().getNamedItem("name").getNodeValue();

            //reads attributes and parts from the sets' children
            setChildren = set.getChildNodes();

            // AreaData setArea;
            // AreaData takeArea;

            for (int j = 0; j < setChildren.getLength(); j++) {

                setChildSub = setChildren.item(j);

                if ("neighbors".equals(setChildSub.getNodeName())) {
                    // parse  neighbor children
                    neighbors = handleNeighborData(setChildSub.getChildNodes());
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
                            // // ignoring the actual individual takes for now
                            // String takeNumber = take.getAttributes().getNamedItem("number").getNodeValue();
                            // System.out.println("  take number: " + takeNumber);

                            // // ignoring area data for now; uncomment for GUI
                            // for (int l = 0; l < takeChildrenNodes.getLength(); l++) {
                            //     Node takeChildrenSub = takeChildrenNodes.item(l);
                            //     if ("area".equals(takeChildrenSub.getNodeName())) {
                            //         // takeArea = handleAreaData(takeChildrenSub);
                            //     }
                            // }
                        }
                    }
                } else if ("parts".equals(setChildSub.getNodeName())) {
                    partList = setChildSub.getChildNodes();
                    // loops through the Node s"part"s in NodeList "parts"
                    for (int k = 1; k < partList.getLength(); k++) {
                        partListSub = partList.item(k);

                        if ("part".equals(partListSub.getNodeName())) {
                            role = handlePartData(role, partListSub);
                            setRoles.add(role);
                        }
                    }
                } //for part nodes
                // don't use an else block                

            } //for set childnodes
            setList.add(new Set(setName, neighbors, setRoles, numTakes));
        
        }//for set nodes

        return setList;

    }//readBoardData() method

    // reads card data from XML file, stores it in Card objects, stores those objects in a List and returns List
    public List<Card> convertDocToCardDeck(Document d) {
        // Declare vars for card data handling
        NodeList cards;
        Node card;
        NodeList cardList;
        Node cardListSub;
        Node scene;

        // declare Card, Role, and Lists of Cards and Roles
        Card cardObj;
        Role role = new Role();
        List<Card> cardDeck = new ArrayList<Card>();
        List<Role> cardRoles = new ArrayList<Role>(); //

        // Declare vars for Role constructor args
        String cardName;
        String budget;
        String sceneNumber = "";
        String sceneDescription = "";

        Element root = d.getDocumentElement();

        cards = root.getElementsByTagName("card");

        for (int i = 0; i < cards.getLength(); i++) {
            cardRoles = new ArrayList<Role>(); // want a new ArrayList each time, don't try to clear and reuse the same one

            // System.out.println("Parsing card " + (i + 1));

            //reads attributes from the Cards/Nodes
            card = cards.item(i);
            cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            budget = card.getAttributes().getNamedItem("budget").getNodeValue();

            //reads attributes and parts from the cards' children
            cardList = card.getChildNodes();

            for (int j = 0; j < cardList.getLength(); j++) {
                cardListSub = cardList.item(j);

                if ("scene".equals(cardListSub.getNodeName())) {
                    //read attributes and text in scene
                    scene = cardListSub;
                    sceneNumber = scene.getAttributes().getNamedItem("number").getNodeValue();
                    sceneDescription = scene.getTextContent();
                } else if ("part".equals(cardListSub.getNodeName())) {
                    role =  handlePartData(role, cardListSub);
                    cardRoles.add(role);
                } //for part nodes

            } //for card childnodes

            // init card obj w/ parsed data and add it to cardDeck
            cardObj = new Card(cardName, budget, sceneNumber, sceneDescription, cardRoles);
            cardDeck.add(cardObj);

        }//for card nodes

        return cardDeck;
    }//readCardData() method


}//class