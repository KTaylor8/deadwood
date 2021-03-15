public class Deadwood{
    public static void main(String[] args){

        Controller controller = new Controller(args);
        //runs an instance of the game
        Game.getInstance().run();

    }

}
