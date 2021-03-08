import java.util.*;

public class Card{
    private String cardName;
    private String budget; // that's how it is in the xml idk if we want to make it an int
    private String sceneNumber;
    private String sceneDescription;
    private List<Role> onCardRoles;
    private String picturePath = "";
    private UI ui = new UI();

    public Card(String name, String budget, String sceneNumber, String sceneDescription, List<Role> cardRoles, String picturePath) {
        this.cardName = name;
        this.budget = budget;
        this.sceneNumber = sceneNumber;
        this.sceneDescription = sceneDescription;
        this.onCardRoles = cardRoles;
        this.picturePath = picturePath;
    }

    public String getBudget() {
        return budget;
    }

    public List<Role> getOnCardRoles() {
        return onCardRoles;
    }

    public boolean hasRole(String roleName) {
        boolean hasRole = false;
        for (Role r : onCardRoles) {
            if (roleName.equals(r.getName())) {
                hasRole = true;
            }
        }
        return hasRole;
    }

    public void printInfo() {
        String cardInfo = "";
        cardInfo += ("\nCard name: " + cardName);
        cardInfo += ("\n\tCard budget: " + budget);
        cardInfo += ("\n\tScene number: " + sceneNumber);
        cardInfo += ("\n\tScene description: " + sceneDescription);
        for (int i = 0; i < onCardRoles.size(); i++) {
            cardInfo += "\n\tOn-card role #" + (i+1) + ":";
            cardInfo += "\n\t\tOn-card role name: " + onCardRoles.get(i).getName();
            cardInfo += "\n\t\tOn-card role level: " + onCardRoles.get(i).getLevel();
            cardInfo += "\n\t\tOn-card role line: " + onCardRoles.get(i).getLine();
        }
        ui.print(cardInfo);
    }

}