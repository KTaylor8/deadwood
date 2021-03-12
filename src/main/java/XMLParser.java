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
    private UI ui = new UI();

    public XMLParser() {}

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
                view.showPopUp("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    /**
     * Parse neighbor data
     * @param neighborList accepts a list of potential neighbors
     * @return neighbors
     */
    private List<String> getNeighborData(NodeList neighborList) {
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

    private AreaData getAreaData(Node area) { 

        return new AreaData(
            area.getAttributes().getNamedItem("x").getNodeValue(),
            area.getAttributes().getNamedItem("y").getNodeValue(),
            area.getAttributes().getNamedItem("h").getNodeValue(),
            area.getAttributes().getNamedItem("w").getNodeValue()
        );
    }


    /**
     * Parse role/part data
     * @param roleObj
     * @param part accepts a single Node part
     * @return
     */
    private Role getPartData(Role roleObj, Node part){
        // Declare vars for part data handling
        NodeList partChildren;
        Node partChildrenSub;
        AreaData partArea = new AreaData();

        // Part data for Role constructor
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
                partArea = getAreaData(partChildrenSub);
            } else if ("line".equals(partChildrenSub.getNodeName())) {
                partLine = partChildrenSub.getTextContent();
            }

        } //for part child nodes

        roleObj = new Role(partName, partLevel, partLine, partArea);

        return roleObj;
    }//getPartData() method


    /**
     * Parse office data into Set
     * @param root -- document root
     * @return
     */
    private Set getOfficeData(Element root){
        // Declare vars for office data handling
        Node office; /* Element with tag name "office" */
        NodeList officeChildren;
        Node officeChildSub;
        AreaData officeArea = new AreaData();

        List<String> neighbors = new ArrayList<String>();     

        // Upgrade vars
        NodeList upgradesList;
        // UNSURE IF QUEUE IS RIGHT DATA STRUCTURE FOR THIS
        Queue<Node> filteredUpgrades; /* Intermediary storage helps parse upgrades */
        Node upgradesListSub;
        int upgradeCost;
        int numUpgradeOptions = 5;
        AreaData[] upgradeDollarsArea = new AreaData[numUpgradeOptions];
        int[] upgradeDollars = new int[numUpgradeOptions]; // number of upgrade options currently hard-coded, but we might make it dynamic later
        AreaData[] upgradeCreditsArea = new AreaData[numUpgradeOptions];
        int[] upgradeCredits = new int[numUpgradeOptions];

        //reads attributes and parts from the offices' children
        office = root.getElementsByTagName("office").item(0);
        officeChildren = office.getChildNodes();


        for (int j = 0; j < officeChildren.getLength(); j++) {

            officeChildSub = officeChildren.item(j);

            if ("neighbors".equals(officeChildSub.getNodeName())) {
                neighbors = getNeighborData(officeChildSub.getChildNodes());
            } else if ("area".equals(officeChildSub.getNodeName())) {
                officeArea = getAreaData(officeChildSub);
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
                    Node curUpgrade = filteredUpgrades.poll();
                    upgradeCost = Integer.parseInt(curUpgrade.getAttributes().getNamedItem("amt").getNodeValue());
                    upgradeDollars[k] = upgradeCost;
                    for (int l = 0; l < curUpgrade.getChildNodes().getLength(); l++) {
                        Node upgradeChildrenSub = curUpgrade.getChildNodes().item(l);
                        if ("area".equals(upgradeChildrenSub.getNodeName())) {
                            upgradeDollarsArea[k] = getAreaData(upgradeChildrenSub);
                        }
                    }
                }

                // parse upgrade amounts in credits
                for (int k = 0; k < 5; k++) {
                    Node curUpgrade = filteredUpgrades.poll();
                    upgradeCost = Integer.parseInt(curUpgrade.getAttributes().getNamedItem("amt").getNodeValue());
                    upgradeCredits[k] = upgradeCost;
                    for (int l = 0; l < curUpgrade.getChildNodes().getLength(); l++) {
                        Node upgradeChildrenSub = curUpgrade.getChildNodes().item(l);
                        if ("area".equals(upgradeChildrenSub.getNodeName())) {
                            upgradeCreditsArea[k] = getAreaData(upgradeChildrenSub);
                        }
                    }
                }


            }
            // don't use an else block

        } //for office child nodes

        return new Set("office", neighbors, upgradeDollars, upgradeCredits, upgradeDollarsArea, upgradeCreditsArea, officeArea);
    }


    /* Parse trailer data into Set */
    private Set getTrailerData(Element root){
        // Declare vars for trailer data handling
        Node trailer; /* Element with tag name "trailer" */
        NodeList trailerChildren;
        Node trailerChildSub;
        AreaData trailerArea = new AreaData();
        
        List<String> neighbors = new ArrayList<String>();

        //reads attributes and parts from the trailer's children
        trailer = root.getElementsByTagName("trailer").item(0);
        trailerChildren = trailer.getChildNodes();

        for (int j = 0; j < trailerChildren.getLength(); j++) {
            trailerChildSub = trailerChildren.item(j);

            if ("neighbors".equals(trailerChildSub.getNodeName())) {
                neighbors = getNeighborData(trailerChildSub.getChildNodes());
            } else if ("area".equals(trailerChildSub.getNodeName())) {
                trailerArea = getAreaData(trailerChildSub);
            }
        }

        return new Set("trailer", neighbors, trailerArea);
    }

    private List<Set> addSets(Element root, List<Set> setList) {
        // declare class objects and their Lists
        Role role;
        List<Role> setRoles = new ArrayList<Role>();
        List<String> neighbors = new ArrayList<String>();

        // Declare vars for normal set data handling
        NodeList sets; /* NodeList of elements with tag name "set" */
        Node set; /* Individual set Node from sets */
        NodeList setChildren;
        String setName;
        Node setChildSub;
        NodeList partList;
        Node partListSub;
        AreaData setArea = new AreaData();

        // take vars
        NodeList takeList;
        Node takeListSub;
        Node take;
        int takeNum;
        // int numTakes;
        AreaData takeArea;
        List<ShotToken> tokens;

        sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            // init new objects and take counter for new Set
            role = new Role();
            setRoles = new ArrayList<Role>();
            neighbors = new ArrayList<String>();
            // numTakes = 0;

            setRoles = new ArrayList<Role>(); // want a new List each time, don't try to clear and reuse the same one

            tokens = new ArrayList<ShotToken>();
            takeArea = new AreaData();

            // view.showPopUp("Parsing data for set " + (i + 1));

            //reads attributes from the sets/nodes
            set = sets.item(i);
            setName = set.getAttributes().getNamedItem("name").getNodeValue();

            //reads attributes and parts from the sets' children
            setChildren = set.getChildNodes();

            for (int j = 0; j < setChildren.getLength(); j++) {

                setChildSub = setChildren.item(j);

                if ("neighbors".equals(setChildSub.getNodeName())) {
                    // parse  neighbor children
                    neighbors = getNeighborData(setChildSub.getChildNodes());
                } else if ("area".equals(setChildSub.getNodeName())) {
                    setArea = getAreaData(setChildSub);
                } else if ("takes".equals(setChildSub.getNodeName())) {
                    //read attributes for takes and their area children
                    takeList = setChildSub.getChildNodes();
                    // numTakes = 0;
                    for (int k = 1; k < takeList.getLength(); k++) {
                        takeListSub = takeList.item(k);
                        // not all items in takeList are actual "take"s
                        if ("take".equals(takeListSub.getNodeName())) {
                            take = takeListSub;
                            // numTakes++;

                            takeNum = Integer.parseInt(take.getAttributes().getNamedItem("number").getNodeValue());
                            // view.showPopUp("  take number: " + takeNumber);

                            NodeList takeChildrenNodes = take.getChildNodes();
                            for (int l = 0; l < takeChildrenNodes.getLength(); l++) {
                                Node takeChildrenSub = takeChildrenNodes.item(l);
                                if ("area".equals(takeChildrenSub.getNodeName())) {
                                    takeArea = getAreaData(takeChildrenSub);
                                }
                            }
                            tokens.add(new ShotToken(takeNum, takeArea));
                        }
                    }
                } else if ("parts".equals(setChildSub.getNodeName())) {
                    partList = setChildSub.getChildNodes();
                    // loops through the Node s"part"s in NodeList "parts"
                    for (int k = 1; k < partList.getLength(); k++) {
                        partListSub = partList.item(k);

                        if ("part".equals(partListSub.getNodeName())) {
                            setRoles.add(getPartData(role, partListSub));
                        }
                    }
                } //for part nodes
                // don't use an else block                

            } //for set child nodes
            setList.add(new Set(setName, neighbors, setRoles, tokens, setArea));
        
        }//for set nodes

        return setList;
    }

    // reads data in board.xml, stores it in Set objects, stores those objects in a List and returns List
    public List<Set> parseBoardData(Document d) {
        List<Set> setList = new ArrayList<Set>(); /* List of all board tiles */
        Element root = d.getDocumentElement();

        /* Parse office data */
        setList.add(getOfficeData(root));

        /* Parse trailer data */
        setList.add(getTrailerData(root));

        /* Parse normal Set data */
        setList = addSets(root, setList);

        return setList;

    }//readBoardData() method

    // reads card data from XML file, stores it in Card objects, stores those objects in a List and returns List
    public List<Card> parseCardData(Document d) {
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
        List<Role> cardRoles = new ArrayList<Role>();

        // Declare vars for Role constructor args
        String cardName;
        String budget;
        String sceneNumber = "";
        String sceneDescription = "";
        

        Element root = d.getDocumentElement();

        cards = root.getElementsByTagName("card");

        for (int i = 0; i < cards.getLength(); i++) {
            String picturePath = "src/main/resources/img/card_";
            cardRoles = new ArrayList<Role>(); // want a new ArrayList each time, don't try to clear and reuse the same one

            // view.showPopUp("Parsing card " + (i + 1));

            if(i < 9){
                picturePath += (("0"+(i+1)) + ".png");

            }
            else{
                picturePath += ((i+1) + ".png");
            }
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
                    cardRoles.add(getPartData(role, cardListSub));
                } //for part nodes

            } //for card child nodes

            // init card obj w/ parsed data and add it to cardDeck
            cardObj = new Card(cardName, budget, sceneNumber, sceneDescription, cardRoles, picturePath);
            cardDeck.add(cardObj);

        }//for card nodes

        return cardDeck;
    }//readCardData() method


}//class