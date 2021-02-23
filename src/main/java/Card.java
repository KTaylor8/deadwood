import java.util.*;

public class Card{
    String name;
    String budget; // that's how it is in the xml idk if we want to make it an int
    String sceneNumber;
    String sceneDescription;
    Stack<Role> roles;

    Card(String name, String budget, String sceneNumber, String sceneDescription, Stack<Role> cardRoles) {
        this.name = name;
        this.budget = budget;
        this.sceneNumber = sceneNumber;
        this.sceneDescription = sceneDescription;
        this.roles = cardRoles;
    }

}