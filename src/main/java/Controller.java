public class Controller{

    View view;
    Game game;

    public Controller(String[] args) {
        // scan = new Scanner(System.in);
        View view = View.getInstance(this);
        game = Game.getInstance(args);
        game.registerObserver(view);
    }

    public String chooseRole() {
        String roleChosen = "";
        // lets user choose role from interface
        return roleChosen;
    }

    public String[] chooseUpgrade() {
        String[] upgradeChosen = {"dollars", "2"};
        // lets user choose upgrade from interface
        return upgradeChosen;
    }

    public void process(String res) {
        if (res.equals("move")) {
            // currentPlayer.moveTo(destStr, game.getBoardSet(destStr));
            game.tryMove();
        } else if (res.equals("take role")) {
            // showPopUp("dont take that role, trust me");
            String desiredRole = chooseRole(); // need to make function that lets user choose role from those available
            game.tryTakeRole(desiredRole);
        } else if (res.equals("upgrade")) {
            // showPopUp("upgrades people, upgrades");

            String[] upgradeChoices = chooseUpgrade(); // have a way for user to choose upgrade type and rank, should return a String[] w/ the 2 values
            game.tryUpgrade(upgradeChoices);

        } else if (res.equals("rehearse")) {
            // showPopUp("oh honey, you're gonna need something a lil more than rehearsing");
            game.tryRehearse();
        } else if (res.equals("act")) {
            // showPopUp("ha, yea right");
            game.tryAct();
        } else if (res.equals("end")) {
            game.endTurn();
        }
    }
}