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

public class MyParseXML {


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

    // reads board data from XML file and prints data
    public void readBoardData(Document d) {

        Element root = d.getDocumentElement();

        NodeList cards = root.getElementsByTagName("card");

        for (int i = 0; i < cards.getLength(); i++) {

            System.out.println("Printing information for card " + (i + 1));

            //reads data from the nodes
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            System.out.println("Name = " + cardName);

            //reads data from their children

            NodeList children = card.getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {

                Node sub = children.item(j);

                if ("title".equals(sub.getNodeName())) {
                    String cardLanguage = sub.getAttributes().getNamedItem("lang").getNodeValue();
                    System.out.println("Language = " + cardLanguage);
                    String title = sub.getTextContent();
                    System.out.println("Title = " + title);

                } else if ("author".equals(sub.getNodeName())) {
                    String authorName = sub.getTextContent();
                    System.out.println(" Author = " + authorName);

                } else if ("year".equals(sub.getNodeName())) {
                    String yearVal = sub.getTextContent();
                    System.out.println(" Publication Year = " + yearVal);

                } else if ("price".equals(sub.getNodeName())) {
                    String priceVal = sub.getTextContent();
                    System.out.println(" Price = " + priceVal);

                }


            } //for childnodes

            System.out.println("\n");

        }//for card nodes
    }

    // reads card data from XML file and prints data
    public void readCardData(Document d) {

        Element root = d.getDocumentElement();

        NodeList cards = root.getElementsByTagName("card");

        for (int i = 0; i < cards.getLength(); i++) {

            System.out.println("Printing information for card " + (i + 1));

            //reads attributes from the cards/nodes
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            // String imgName = card.getAttributes().getNamedItem("img").getNodeValue();
            String budget = card.getAttributes().getNamedItem("budget").getNodeValue();
            System.out.println("Name = " + cardName + ", budget = " + budget);

            //reads attributes and parts from the cards' children
            NodeList cardChildren = card.getChildNodes();

            for (int j = 0; j < cardChildren.getLength(); j++) {

                Node cardChild = cardChildren.item(j);

                if ("scene".equals(cardChild.getNodeName())) {
                    //read attributes and text in scene
                    Node scene = cardChild;
                    String sceneNumber = scene.getAttributes().getNamedItem("number").getNodeValue();
                    String sceneDescription = scene.getTextContent();
                    System.out.println(" scene description:" + sceneDescription);
                    System.out.println(" scene number = " + sceneNumber);

                } else if ("part".equals(cardChild.getNodeName())) {
                    //read attributes and text in part
                    Node part = cardChild;
                    String partName = part.getAttributes().getNamedItem("name").getNodeValue();;
                    String partLevel = part.getAttributes().getNamedItem("level").getNodeValue();
                    System.out.println(" part name = " + partName + ", part level = " + partLevel);

                    //reads attributes and text from parts' children
                    NodeList partChildren = part.getChildNodes();

                    for (int k = 0; k < partChildren.getLength(); k++) {
        
                        Node partChild = partChildren.item(k);
        
                        if ("area".equals(partChild.getNodeName())) {
                            //reads attributes and text from part's area
                            Node area = partChild;
                            String areaX = area.getAttributes().getNamedItem("x").getNodeValue();
                            String areaY = area.getAttributes().getNamedItem("y").getNodeValue();
                            String areaH = area.getAttributes().getNamedItem("h").getNodeValue();
                            String areaW = area.getAttributes().getNamedItem("w").getNodeValue();
                            System.out.println("  x = " + areaX + ", y = " + areaY  + ", h = " + areaH + ", w = " + areaW);

                        } else if ("line".equals(partChild.getNodeName())) {
                            //reads text from part's line
                            String line = partChild.getTextContent();
                            System.out.println("  Line = " + line);
                        }
        
                    } //for part childnodes

                    System.out.println("\n");
                    
                } //for part nodes


            } //for card childnodes

            // System.out.println("\n");

        }//for card nodes

    }// readCardData() method

}//class