public class Controller{
    private View view;
    private Game game;

    /**
     * Constructor that connects the view to the game
     * @param args
     */
    public Controller(String[] args) {
        view = View.getInstance(this); // first instantiation of view sets controller
        game = Game.getInstance(args);
    }

    /**
     * Processes the information that is given from view and calls the correct method within game to do the action
     * @param res
     */
    public void process(String res) {
        if (res.equals("move")) {
            game.tryMove();
        } else if (res.equals("take role")) {
            game.tryTakeRole();
        } else if (res.equals("upgrade")) {
            game.tryUpgrade();
        } else if (res.equals("rehearse")) {
            game.tryRehearse();
        } else if (res.equals("act")) {
            game.tryAct();
        } else if (res.equals("end")) {
            game.endTurn();
        }
    }
}