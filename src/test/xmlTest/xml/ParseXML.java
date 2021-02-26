/**
 * Example Code for parsing XML file
 * Author: Dr. Moushumi Sharmin
 * CS 345
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParseXML {


    // building a document from the XML file
    // returns a Document object after loading the book.xml file.
    public Document getDocFromFile(String filename) throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;

            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                ui.print("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    // reads data from XML file and prints data
    public void readBookData(Document d) {

        Element root = d.getDocumentElement();

        NodeList books = root.getElementsByTagName("book");

        for (int i = 0; i < books.getLength(); i++) {

            ui.print("Printing information for book " + (i + 1));

            //reads data from the nodes
            Node book = books.item(i);
            String bookCategory = book.getAttributes().getNamedItem("category").getNodeValue();
            ui.print("Category = " + bookCategory);

            //reads data

            NodeList children = book.getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {

                Node sub = children.item(j);

                if ("title".equals(sub.getNodeName())) {
                    String bookLanguage = sub.getAttributes().getNamedItem("lang").getNodeValue();
                    ui.print("Language = " + bookLanguage);
                    String title = sub.getTextContent();
                    ui.print("Title = " + title);

                } else if ("author".equals(sub.getNodeName())) {
                    String authorName = sub.getTextContent();
                    ui.print(" Author = " + authorName);

                } else if ("year".equals(sub.getNodeName())) {
                    String yearVal = sub.getTextContent();
                    ui.print(" Publication Year = " + yearVal);

                } else if ("price".equals(sub.getNodeName())) {
                    String priceVal = sub.getTextContent();
                    ui.print(" Price = " + priceVal);

                }


            } //for childnodes

            ui.print("\n");

        }//for book nodes

    }// method


}//class
