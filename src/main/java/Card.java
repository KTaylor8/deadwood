import java.util.*;

public class Card{
    String cardName;
    String budget; // that's how it is in the xml idk if we want to make it an int
    String sceneNumber;
    String sceneDescription;
    Stack<Role> roles;

    Card(String cardName, String budget, String sceneNumber, String sceneDescription, Stack<Role> cardRoles) {
        this.cardName = cardName;
        this.budget = budget;
        this.sceneNumber = sceneNumber;
        this.sceneDescription = sceneDescription;
    }

}