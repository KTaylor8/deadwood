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

    public void printInfo() {
        String cardInfo = "";
        Role role;
        cardInfo += ("\nCard name: " + cardName);
        cardInfo += ("\n\tCard budget: " + budget);
        cardInfo += ("\n\tScene number: " + sceneNumber);
        cardInfo += ("\n\tScene description: " + sceneDescription);
        try {
            for (int i = 0; i < onCardRoles.size(); i++) {
                role = onCardRoles.get(i);
                cardInfo += "\n\tOn-card role #" + (i+1) + ":";
                cardInfo += "\n\t\tOn-card role name: " + role.getName();
                cardInfo += "\n\t\tOn-card role level: " + role.getLevel();
                cardInfo += "\n\t\tCard area: x = " + role.getArea().getX() + 
                            ", y = " + role.getArea().getY() +
                            ", w = " + role.getArea().getW() +
                            ", h = " + role.getArea().getH();
                cardInfo += "\n\t\tOn-card role line: " + role.getLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            View.getInstance().showPopUp(e.getMessage());
        }
        View.getInstance().showPopUp(cardInfo);
    }

}