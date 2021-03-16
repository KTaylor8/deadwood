public class Deadwood{
    public static void main(String[] args){

        // initialized game and view and by controller
        Controller controller = new Controller(args);
        // runs an instance of the game
        Game.getInstance().run();

    }

}
