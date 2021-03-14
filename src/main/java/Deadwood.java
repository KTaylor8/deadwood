public class Deadwood{
    public static void main(String[] args){

        // Game game = new Game(args);
        // // game.init();
        // Controller controller = new Controller(game);
        // // controller.run(); // I wanted to have the controller run the game like in the calculator2 example, but then the controller would need to keep track of the board sceneNum, players queue, numDays, and call view methods, and that doesn't work as well.
        Controller controller = new Controller(args);

        Game.getInstance().run();

    }

}
