public class Controller implements ControllerInterface{
    private final View view;
    UI model;

    public Controller(UI ui) {
        model = ui;
        view = new View(model);
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
        //view.givePopUp(notif);
    }

    @Override
    public void updateCurrentPlayer(String notif){
        view.changeCurrentPlayer(notif);
    }

    @Override
    public void end(){

    }

    @Override
    public void setCard(Set s){
        view.setCard(s);
    }
}
