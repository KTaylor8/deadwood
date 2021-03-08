public class Controller {
    private final View view;

    public Controller() {
        view = new View();
    }

    public void run() {
        view.show(); // initialize and show GUI
    }
}
