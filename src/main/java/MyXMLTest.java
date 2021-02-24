/**
 * Example Code for parsing XML file
 * Author: Dr. Moushumi Sharmin
 * CS 345
 */

import org.w3c.dom.Document;
import java.util.*;

public class MyXMLTest {

    public static void main(String[] args) {

        Document doc = null;
        XMLParser parsing = new XMLParser();

        //parse board data
        try {
            doc = parsing.getDocFromFile("src/main/resources/xml/board.xml"); //path will be passed in as arg
            List<Set> boardSets = parsing.parseBoardData(doc);
            // for (Set set: boardSets) { // uncomment to test that all the Sets are there
            //     set.printInfo();
            // }

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }

        // parse card data
        try {
            doc = parsing.getDocFromFile("src/main/resources/xml/cards.xml"); //path will be passed in as arg
            List<Card> cardDeck = parsing.convertDocToCardDeck(doc);
            // for (Card card: cardDeck) { // uncomment to test that all the Cards are there
            //     card.printInfo();
            // }

        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }
    }
}