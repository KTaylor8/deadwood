public class Deadwood{
    public static void main(String[] args){

        Game game = new Game(args);

        Controller controller = new Controller(game);
        controller.run();
        // game.run();
    }

}
