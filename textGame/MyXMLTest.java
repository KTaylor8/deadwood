/**
 * Example Code for parsing XML file
 * Author: Dr. Moushumi Sharmin
 * CS 345
 */

import org.w3c.dom.Document;


public class MyXMLTest {

    public static void main(String[] args) {

        Document doc = null;
        XMLParser parsing = new XMLParser();

        // parse board data
        try {
            doc = parsing.getDocFromFile("xml/board.xml");
            parsing.readBoardData(doc);

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }

        //parse card data
        try {
            doc = parsing.getDocFromFile("xml/cards.xml");
            Stack<Card> cardDeck = parsing.convertDocToCardDeck(doc);

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }
    }
}