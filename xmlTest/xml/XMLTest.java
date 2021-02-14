/**
 * Example Code for parsing XML file
 * Author: Dr. Moushumi Sharmin
 * CS 345
 */

import org.w3c.dom.Document;


public class XMLTest {

    public static void main(String[] args) {

        Document doc = null;
        ParseXML parsing = new ParseXML();

        try {
            doc = parsing.getDocFromFile("deadwood/board.xml");
            parsing.readBookData(doc);

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }
    }
}