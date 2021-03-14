import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

// import java.awt.Graphics;

// uses singleton with lazy initialization b/c of args
public class View implements ActionListener{
    
    JFrame frame;
    JLabel display, player1, player2, player3, player4, player5, player6, player7, player8;
    JLayeredPane[] places = new JLayeredPane[10];
    JLabel mainStreet = new JLabel();
    JLabel mainStreet2 = new JLabel();
    JLayeredPane layeredPane = new JLayeredPane();
    JPanel cardPanel = new JPanel();
    JPanel shotPanel = new JPanel();
    JPanel dicePanel = new JPanel();
    JPanel leftPlayers = new JPanel();
    JPanel rightPlayers = new JPanel();
    
    
        //panel for the top information about current player
    JPanel displayPanel = new JPanel();
    Controller controller;

    private static View uniqueInstance;

    public View(Controller controller){
        this.controller = controller;
    }

    // initializer (w/ args)
    public static synchronized View getInstance(Controller controller) {
        if (uniqueInstance == null) {
            uniqueInstance = new View(controller);
        }
        return uniqueInstance;
    }
    
    // accessor (no args)
    public static synchronized View getInstance() {
        return uniqueInstance;
    }

    public void init(){
        int leftMargin = 300; // sets left margin of display and panels, for centering

        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        display = new JLabel("Current player: ", JLabel.CENTER);
        display.setPreferredSize(new Dimension(leftMargin, 50));
        display.setOpaque(false);
        display.setFont(new Font("TimesRoman", Font.PLAIN, 18));

        displayPanel.add(display);

        //panel with the buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 6));

        String[] buttonText = {"move", "take role", "upgrade", "rehearse", "act", "end"};
        for (String symbol: buttonText) {
            JButton b = new JButton(symbol);
            b.addActionListener(this);
            actionPanel.add(b);
        }
        /*
        places[0] = new JLayeredPane();
        places[1] = new JLayeredPane();
        places[2] = new JLayeredPane();
        places[3] = new JLayeredPane();
        places[4] = new JLayeredPane();
        places[5] = new JLayeredPane();
        places[6] = new JLayeredPane();
        places[7] = new JLayeredPane();
        places[8] = new JLayeredPane();
        places[9] = new JLayeredPane();
*/
        leftPlayers.setLayout(new GridLayout(4, 1));
        leftPlayers.setBounds(0,0, 400, 1000);
        //leftPlayers.add(player1);
        //leftPlayers.add(player3);
        //leftPlayers.add(player5);
        //leftPlayers.add(player7);
        /*
        leftPlayers.setSize(new Dimension(50,400));
        */
        //for (int i = 1; i <= 7; i = i+2) {
            //JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            //leftPlayers.add(b);
        //}

        rightPlayers.setLayout(new GridLayout(4, 1));
        rightPlayers.setBounds(0,0, 400, 1000);
        //rightPlayers.add(player2);
        //rightPlayers.add(player4);
        //rightPlayers.add(player6);
        //rightPlayers.add(player8);
        
        //for (int i = 2; i <= 8; i = i+2) {
            //JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            //rightPlayers.add(b);
        //}

        ImageIcon img = new ImageIcon("src/main/resources/img/board.png");
        layeredPane.setSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
        JLabel board = new JLabel(img, JLabel.CENTER);
        board.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        cardPanel.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);
        shotPanel.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        shotPanel.setOpaque(false);
        shotPanel.setLayout(null);
        dicePanel.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        dicePanel.setOpaque(false);
        dicePanel.setLayout(null);
        layeredPane.add(dicePanel, 0);
        layeredPane.add(shotPanel, 1);
        layeredPane.add(cardPanel, 2);
        layeredPane.add(board, 3);   

        frame.add(layeredPane, BorderLayout.CENTER);
        frame.add(displayPanel,BorderLayout.PAGE_START);
        frame.add(actionPanel, BorderLayout.PAGE_END);
        frame.add(leftPlayers, BorderLayout.LINE_START);
        frame.add(rightPlayers, BorderLayout.LINE_END);

        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    }

    public void show() {
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();

        controller.process(buttonText);
    }

     

    public String showRolePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "Which role would you like?", "Warning",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        if (n == -1) {
            return "";
        }
        else {
            return(options[n] + "");
        }
    }

    public String showMovePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "Where would you like to move to?", "Warning",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        if (n == -1) {
            return "";
        }
        else {
            return(options[n] + "");
        }
    }

    public String showUpgradePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "What Upgrade would you like?", "Warning",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        if (n == -1) {
            return "";
        }
        else {
            return(options[n] + "");
        }
    }

    // equivalent to Calculator2View's updateResult()
    public void showPopUp(String notif){
        JOptionPane.showMessageDialog(frame, notif); 
    }

    public void updateSidePanel(Player[] players){
        leftPlayers.removeAll();
        leftPlayers.revalidate();
        leftPlayers.repaint();

        rightPlayers.removeAll();
        rightPlayers.revalidate();
        rightPlayers.repaint();
        System.out.println(players.length);

        for (int i = 1; i < players.length; i++) {
            
            JLabel b = new JLabel("<html>" + players[i].getName() + "<br/>Location: " + players[i].getLocation().getName() + "<br/>Level: " + players[i].getRank() + "<br/>Is Employed?: " + players[i].isEmployed() + "<br/>Dollars: " + players[i].getDollars() + "<br/>Credits: " + players[i].getCredits() + "<br/>Rehearse Tokens: " + players[i].getRehearseTokens() + "</html>", JLabel.CENTER);
            if(i%2 == 0){
               leftPlayers.add(b);
            }
            else{
               rightPlayers.add(b);
            }
        }
    }

    public void changeCurrentPlayer(String playerName, String path){
        displayPanel.removeAll();
        displayPanel.revalidate();
	    displayPanel.repaint();

        display = new JLabel("Current player: " + playerName, JLabel.CENTER);
        display.setPreferredSize(new Dimension(300, 50));
        display.setOpaque(false);
        display.setFont(new Font("TimesRoman", Font.PLAIN, 18));

        displayPanel.add(display);

        JLabel l = new JLabel(new ImageIcon(path));

        l.setBounds(100, 25, 46, 46); // dice w/h = 46
        displayPanel.add(l);
    }

    public void changePlayerStats(String stats){ // not functional
        player1.setText("Player 1: \nDollars: \n Credits: ");
    }

    public void clearCard(){
        cardPanel.removeAll();
        cardPanel.revalidate();
	    cardPanel.repaint();
    }

    public void resetCard(Set s){
        ImageIcon img;
        JLabel l;

        AreaData area = s.getArea();
System.out.println(s.getName() + " " + s.getFlipStage());
        if(s.getFlipStage() != 2){
            if(s.getFlipStage() != 1){
                img = new ImageIcon("src/main/resources/img/cardback.png");
            }
            else{
                img = new ImageIcon(s.getCard().getPicturePath());
            }
            l = new JLabel(img);
            
            l.setBounds(area.getX(), area.getY(), area.getW(), area.getH());
            cardPanel.add(l);
        }
    }

    public void clearShotPanel(){
        shotPanel.removeAll();
        shotPanel.revalidate();
        shotPanel.repaint();
        // shotPanel.setVisible(false); // if I uncomment this here it hides all the tokens

        JLabel l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
        l.setBounds(450, 200, 300, 300);
        System.out.println(shotPanel.getComponentCount());
        shotPanel.add(l);
        // shotPanel.removeAll(); // vv also works
        // l.setVisible(false); // this hides my random token in middle of board tho...
    }

    public void clearShotPanelTest(){ //TEST
        // shotPanel.removeAll();
        shotPanel.revalidate();
        shotPanel.repaint();
        // shotPanel.setVisible(false); // if I uncomment this here it hides all the tokens

        JLabel l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
        l.setBounds(450, 300, 300, 300);
        System.out.println(shotPanel.getComponentCount());
        shotPanel.add(l); // adds to view
        System.out.println(shotPanel.getComponentCount());
        // shotPanel.removeAll(); // vv also works
        // l.setVisible(false); // this hides my random token in middle of board tho...
        Component[] components = shotPanel.getComponents();
        // components[12].setVisible(false); // should hide 3 in main street
        components[13].setVisible(false); // should hide 2 in main street
        // components[22].setVisible(false); // this hid the one in the saloon...why tho?
        // components[23].setVisible(false); // this hid the one I just made
        // components[0].setVisible(false); // this hid the one I made at the start in clearShotPanel()

        // shotPanel.setVisible(true);
    }

    public void clearShotPanelTest2(){
        // shotPanel.removeAll();
    //     shotPanel.revalidate();
    //     shotPanel.repaint();
        // shotPanel.setVisible(false); // doesn't do anything if I uncomment it

        // shotPanel.removeAll();
        shotPanel.revalidate();
        shotPanel.repaint();
        // shotPanel.setVisible(false); // if I uncomment this here it hides all the tokens

        JLabel l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
        l.setBounds(450, 350, 300, 300);
        System.out.println(shotPanel.getComponentCount());
        l.setVisible(true);
        shotPanel.add(l); // hmm doesn't add it to the view like the previous one did, but increases panel component count
        System.out.println(shotPanel.getComponentCount());
        // shotPanel.removeAll(); // vv also works
        // l.setVisible(false); // this hides my random token in middle of board tho...
        Component[] components = shotPanel.getComponents();
        components[12].setVisible(false); // should hide 3 in main street -- but doesn't
        // components[13].setVisible(false); // should hide 2 in main street
        // components[22].setVisible(false); // this hid the one in the saloon...why tho?
        // components[23].setVisible(false); // this hid the one I just made
        // components[0].setVisible(false); // this hid the one I made at the start in clearShotPanel()

        components[22].setVisible(true); // this only works on the component i just made
    }

    public void resetShotPanel(Set s){
        JLabel l;
        AreaData area;

        for(int i = 0; i < s.getTakesLeft(); i++){
            l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
            area = s.getShotTokens().get(i).getArea();
            l.setBounds(area.getX(), area.getY(), area.getW(), area.getH());
            shotPanel.add(l);
        }
        
    }

    public void refreshShotPanelTest(Player p, int tokenSum) {
        // shotPanel.removeAll();
        // shotPanel.revalidate();
        // shotPanel.repaint();
        // shotPanel.setVisible(false);
        // shotPanel.revalidate();
	    // shotPanel.repaint();
        // shotPanel.setVisible(false);

        // Component[] components = shotPanel.getComponents();
        // for(int i =0; i<15; i++){
        //     components[i].setVisible(false);
        // }

        Component[] components = shotPanel.getComponents();
        components[12].setVisible(false); // should hide 3 in main street
        components[13].setVisible(false); // should hide 2 in main street

        // hides used shot tokens

        // int lastToken = tokenSum-1;
        // int takesUsed = p.getLocation().getTotalTakes() - p.getLocation().getTakesLeft();
        // takesUsed = 2; // for testing only
        // Component[] components = shotPanel.getComponents();
        //     for (int j = lastToken; j>(lastToken-takesUsed); j--) {
        //         System.out.println("#" + j + ": " + components[j].toString());
        //         shotPanel.getComponent(j).setVisible(false);
        //     }

        shotPanel.revalidate();
	    shotPanel.repaint();
    }

    
    public void clearDice(){

        dicePanel.removeAll();
	    dicePanel.revalidate();
	    dicePanel.repaint();
    }

    public void setDie(Player p){
        JLabel l;
        int x = p.getAreaData().getX();
        int y = p.getAreaData().getY();

       

        // set 
        l = new JLabel(new ImageIcon(p.getPlayerDiePath()));

        l.setBounds(x, y, 46, 46); // dice w/h = 46
        dicePanel.add(l);
         
        
    }

    // reset a given player's die's location to the trailers
    public void resetPlayerDie(Player p, int pNum){
        JLabel l;
        int trailerX = 1000;
        int trailerY = 262;

        // set 
        int x = trailerX + (46*pNum);
        int y;
        if (pNum <= 4) { // 1st row; 4 dice/row
            y = trailerY;
        } else { // 2nd row
            y = trailerY + 50;
        }
        movePlayerPosition(p, x, y);

        l = new JLabel(new ImageIcon(p.getPlayerDiePath()));

        l.setBounds(x, y, 46, 46); // dice w/h = 46
        dicePanel.add(l);
    }

    // moves player's die to specified x and y positions
    public void movePlayerPosition(Player p, int x, int y){
        p.getPlayerDieArea().setX(x);
        p.getPlayerDieArea().setY(y);
    }

    // DO THIS IF ENOUGH TIME BUT NOT NECESSARY
    // public void changePlayerDieVal(Player p, int newVal){
    //     // p.setPlayerDieCurrentNum(newVal-1);
    // }

    /*if((s.getName()).equals("Main Street")){
        ImageIcon img = new ImageIcon((s.getCard()).getPicturePath());
        mainStreet = new JLabel(img);
        mainStreet.setBounds((s.getArea()).getX(), (s.getArea()).getY(), (s.getArea()).getW(),( s.getArea()).getH());
        layeredPane.add(mainStreet, 1);
        System.out.println("hello? " + (s.getArea()).getX()+ " " +(s.getArea()).getY()+ " " + (s.getArea()).getW()+ " " +( s.getArea()).getH());

        mainStreet2 = new JLabel(img);
        mainStreet2.setBounds((s.getArea()).getX()-500, (s.getArea()).getY(), (s.getArea()).getW(),( s.getArea()).getH());
        layeredPane.add(mainStreet2, 2);
        System.out.println("hello? " + (s.getArea()).getX()+ " " +(s.getArea()).getY()+ " " + (s.getArea()).getW()+ " " +( s.getArea()).getH());
    }*/
        
    

}