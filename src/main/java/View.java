import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// uses singleton with lazy initialization b/c of args
public class View implements ActionListener{
    
    private JFrame frame;
    private JLabel display;
    private JLayeredPane layeredPane = new JLayeredPane();
    private JPanel cardPanel = new JPanel();
    private JPanel shotPanel = new JPanel();
    private JPanel dicePanel = new JPanel();
    private JPanel leftPlayers = new JPanel();
    private JPanel rightPlayers = new JPanel();
    
    //panel for the top information about current player
    private JPanel displayPanel = new JPanel();
    private Controller controller;

    private static View uniqueInstance;

    /**
     * constructor for the view that connects to the controller.
     * @param controller
     */
    private View(Controller controller){
        this.controller = controller;
    }

    /**
     * initializer (w/ args)
     * @param controller
     * @return
     */
    public static synchronized View getInstance(Controller controller) {
        if (uniqueInstance == null) {
            uniqueInstance = new View(controller);
        }
        return uniqueInstance;
    }
    
    /**
     * accessor (no args)
     * @return
     */
    public static synchronized View getInstance() {
        return uniqueInstance;
    }

    /**
     * Creates and displays the frame with the different panels
     */
    public void init(){
        int leftMargin = 300; // sets left margin of display and panels, for centering

        //frame that has all of the different panels
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //The top panel that displays the current player
        display = new JLabel("Current player: ", JLabel.CENTER);
        display.setPreferredSize(new Dimension(leftMargin, 50));
        display.setOpaque(false);
        display.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        displayPanel.add(display);

        //panel with the action buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 6));
        String[] buttonText = {"move", "take role", "upgrade", "rehearse", "act", "end"};
        for (String symbol: buttonText) {
            JButton b = new JButton(symbol);
            b.addActionListener(this);
            actionPanel.add(b);
        }

        //The panel on the left of the board that displays the player status
        leftPlayers.setLayout(new GridLayout(4, 1));
        leftPlayers.setBounds(10,0, 400, 1000);

        //The panel on the right of the board that displays the player status
        rightPlayers.setLayout(new GridLayout(4, 1));
        rightPlayers.setBounds(0,0, 400, 1000);

        //The center board img with the png of the board
        ImageIcon img = new ImageIcon("src/main/resources/img/board.png");
        layeredPane.setSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
        JLabel board = new JLabel(img, JLabel.CENTER);
        board.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());

        //The panel on top of the board that has all of the card pngs on it
        cardPanel.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);

        //the panel on top of the board that has all of the shot pngs on it
        shotPanel.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        shotPanel.setOpaque(false);
        shotPanel.setLayout(null);

        //the panel on top of the board that has all of the dice pngs on it
        dicePanel.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        dicePanel.setOpaque(false);
        dicePanel.setLayout(null);

        //Layered pane that imposes the different pngs with the board as the base and the dice as the top
        layeredPane.add(dicePanel, 0);
        layeredPane.add(shotPanel, 1);
        layeredPane.add(cardPanel, 2);
        layeredPane.add(board, 3);   

        //Boarders for each of the elements
        int borderThickness = 10;
        layeredPane.setBorder(BorderFactory.createEmptyBorder(borderThickness, borderThickness, borderThickness, borderThickness));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(borderThickness, borderThickness, borderThickness, borderThickness));
        leftPlayers.setBorder(BorderFactory.createEmptyBorder(borderThickness, borderThickness, borderThickness, borderThickness));
        rightPlayers.setBorder(BorderFactory.createEmptyBorder(borderThickness, borderThickness, borderThickness, borderThickness));

        //adding all of the components onto the frame
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.add(displayPanel,BorderLayout.PAGE_START);
        frame.add(actionPanel, BorderLayout.PAGE_END);
        frame.add(leftPlayers, BorderLayout.LINE_START);
        frame.add(rightPlayers, BorderLayout.LINE_END);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    }

    /**
     * sets the frame to be shown
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * when a button is pressed, notify the controller to do the action
     */
    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();

        controller.process(buttonText);
    }
     
    /**
     * Displays the different roles that are given in the parameters, and when the player chooses one, returns that string unless it is empty
     * @param options
     * @return String
     */
    public String showRolePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "Which role would you like?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (n == -1) {
            return "";
        }
        else {
            return(options[n] + "");
        }
    }

    /**
     * Displays the different move options that are given in the parameters, and when the player chooses one, returns that string unless it is empty
     * @param options
     * @return String
     */
    public String showMovePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "Where would you like to move to?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (n == -1) {
            return "";
        }
        else {
            return(options[n] + "");
        }
    }

    /**
     * Displays the different upgrade options that are given in the parameters, and when the player chooses one, returns that string unless it is empty
     * @param options
     * @return String
     */
    public String showUpgradePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "What upgrade would you like?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (n == -1) {
            return "";
        }
        else {
            return(options[n] + "");
        }
    }

    /**
     * Displays a pop up with the given string to notify the player about an event that has occurred 
     * @param notif
     */
    public void showPopUp(String notif){
        JOptionPane.showMessageDialog(frame, notif); 
    }

    /**
     * Displays a pop up with the given string to notify the player about an error that has occurred 
     * @param notif
     */
    public void showErrorPopUp(String notif){
        JOptionPane.showMessageDialog(
            frame,
            notif,
            "Error",
            JOptionPane.ERROR_MESSAGE
        ); 
    }

    /**
     * Updates the images that are on the side of the board that displays the information of every player, in the order that the player array is in
     * odd numbered players are on the left panel while even numbered players are on the right
     * @param players
     */
    public void updateSidePanel(Player[] players){
        leftPlayers.removeAll();
        leftPlayers.revalidate();
        leftPlayers.repaint();

        rightPlayers.removeAll();
        rightPlayers.revalidate();
        rightPlayers.repaint();

        for (int i = 0; i < players.length; i++) {
            
            JLabel b = new JLabel("<html>" + players[i].getName() + "<br/>Location: " + players[i].getLocation().getName() + "<br/>Level: " + players[i].getRank() + "<br/>Is Employed?: " + players[i].isEmployed() + "<br/>Dollars: " + players[i].getDollars() + "<br/>Credits: " + players[i].getCredits() + "<br/>Rehearse Tokens: " + players[i].getRehearseTokens() + "</html>", JLabel.CENTER);
            if(i%2 == 0){
               leftPlayers.add(b);
            }
            else{
               rightPlayers.add(b);
            }
        }
    }

    /**
     * changes the display on the top of the board that shows the current player as well as an image of their die
     * @param playerName
     * @param path
     */
    public void changeCurrentPlayer(String playerName, String path){
        displayPanel.removeAll();
        displayPanel.revalidate();
	    displayPanel.repaint();

        display = new JLabel("Current player: " + playerName, JLabel.CENTER);
        display.setPreferredSize(new Dimension(300, 50));
        display.setOpaque(false);
        display.setFont(new Font("TimesRoman", Font.PLAIN, 18));

        displayPanel.add(display);

        JLabel label = new JLabel(new ImageIcon(path));

        label.setBounds(100, 25, 46, 46); // dice w/h = 46
        displayPanel.add(label);
    }

    /**
     * removes all of the cards from the card panel
     */
    public void clearCard(){
        cardPanel.removeAll();
        cardPanel.revalidate();
	    cardPanel.repaint();
    }

    /**
     * Checks to see what state the set is in and whether the card should be face up, down, or gone, and places the card at that 
     * set accordingly
     * @param set
     */
    public void resetCard(Set set){
        ImageIcon img;
        JLabel label;

        AreaData area = set.getArea();
        if(set.getFlipStage() != 2){
            if(set.getFlipStage() != 1){
                img = new ImageIcon("src/main/resources/img/cardback.png");
            }
            else{
                img = new ImageIcon(set.getCard().getPicturePath());
            }
            label = new JLabel(img);
            
            label.setBounds(area.getX(), area.getY(), area.getW(), area.getH());
            cardPanel.add(label);
        }
    }

    /**
     * removes all of the shots from the shot panel
     */
    public void clearShot(){
        shotPanel.removeAll();
        shotPanel.revalidate();
	    shotPanel.repaint();
    }

    /**
     * places the remaining number of takes at the specified set that is given in the place that they're supposed to.
     * @param set
     */
    public void resetShot(Set set){
        JLabel l;
        AreaData area;

        for(int i = 0; i < set.getTakesLeft(); i++){
            l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
            area = set.getShotTokens().get(i).getArea();
            l.setBounds(area.getX(), area.getY(), area.getW(), area.getH());
            shotPanel.add(l);
        }
        
    }
    
    /**
     * removes all of the dice from the dice panel
     */
    public void clearDice(){

        dicePanel.removeAll();
	    dicePanel.revalidate();
	    dicePanel.repaint();
    }

    /**
     * places the players die on the area that they are currently supposed to be at, and displays it
     * @param player
     */
    public void setDie(Player player){
        JLabel l;
        int x = player.getAreaData().getX();
        int y = player.getAreaData().getY();


        // set 
        l = new JLabel(new ImageIcon(player.getPlayerDiePath()));

        l.setBounds(x, y, 46, 46); // dice w/h = 46
        dicePanel.add(l);
    }

}