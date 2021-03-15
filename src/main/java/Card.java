import java.util.*;

public class Card{
    private String cardName;
    private String budget; // that's how it is in the xml idk if we want to make it an int
    private String sceneNumber;
    private String sceneDescription;
    private List<Role> onCardRoles;
    private String picturePath = "";
    // private Controller controller = new Controller();

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

    public String getPicturePath(){
        return picturePath;
    }

    public List<Role> getOnCardRoles() {
        return onCardRoles;
    }

    public boolean hasRole(Role role) {
        boolean hasRole = false;
        for (Role r : onCardRoles) {
            if (r.equals(role)) {
                hasRole = true;
            }
        }
        return hasRole;
    }
}