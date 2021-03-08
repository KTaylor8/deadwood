public class Controller implements ControllerInterface{
    private final View view;

    public Controller() {
        view = new View(this);
    }

    public void run() {
        view.show(); // initialize and show GUI
    }

    @Override
    public void move(){
        System.out.println("wow");
    }

    @Override
    public void popUp(String notif){
        view.givePopUp(notif);
    }
}
