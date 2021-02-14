/**
 * Example Code for parsing XML file
 * Author: Dr. Moushumi Sharmin
 * CS 345
 */

import org.w3c.dom.Document;


public class MyXMLTest {

    public static void main(String[] args) {

        Document doc = null;
        MyParseXML parsing = new MyParseXML();

        // // board data
        // try {
        //     doc = parsing.getDocFromFile("board.xml");
        //     parsing.readBoardData(doc);

        // } catch (NullPointerException e) {
        //     System.out.println("Error = " + e);
        //     return;
        // } catch (Exception e) {
        //     System.out.println("Error = " + e);
        //     return;
        // }

        // card data
        try {
            doc = parsing.getDocFromFile("cards.xml");
            parsing.readCardData(doc);

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }
    }
}