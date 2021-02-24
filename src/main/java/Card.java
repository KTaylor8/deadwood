import java.util.*;

public class Card{
    String name;
    String budget; // that's how it is in the xml idk if we want to make it an int
    String sceneNumber;
    String sceneDescription;
    List<Role> roles;

    Card(String name, String budget, String sceneNumber, String sceneDescription, List<Role> cardRoles) {
        this.name = name;
        this.budget = budget;
        this.sceneNumber = sceneNumber;
        this.sceneDescription = sceneDescription;
        this.roles = cardRoles;
    }

    public void printInfo() {
        String cardInfo = "";
        cardInfo += ("\nCard name: " + name);
        cardInfo += ("\n\tCard budget: " + budget);
        cardInfo += ("\n\tScene number: " + sceneNumber);
        cardInfo += ("\n\tScene description: " + sceneDescription);
        for (int i = 0; i < roles.size(); i++) {
            cardInfo += "\n\tOn-card role #" + (i+1) + ":";
            cardInfo += "\n\t\tOn-card role name: " + roles.get(i).name;
            cardInfo += "\n\t\tOn-card role level: " + roles.get(i).level;
            cardInfo += "\n\t\tOn-card role line: " + roles.get(i).line;
        }
        System.out.println(cardInfo);
    }

}