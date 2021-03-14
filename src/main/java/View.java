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
<<<<<<< HEAD
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
=======

        JPanel leftPlayers = new JPanel();
        leftPlayers.setLayout(new GridLayout(4, 1));
        leftPlayers.setSize(new Dimension(800, 1200));
        for (int i = 1; i <= 7; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            leftPlayers.add(b);
        }
>>>>>>> be135bf719a88526107ad26063814662a43cef15

        rightPlayers.setLayout(new GridLayout(4, 1));
<<<<<<< HEAD
        rightPlayers.setBounds(0,0, 400, 1000);
        //rightPlayers.add(player2);
        //rightPlayers.add(player4);
        //rightPlayers.add(player6);
        //rightPlayers.add(player8);
        
        //for (int i = 2; i <= 8; i = i+2) {
            //JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            //rightPlayers.add(b);
        //}
=======
        rightPlayers.setSize(new Dimension(800, 1200));
        for (int i = 2; i <= 8; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            rightPlayers.add(b);
        }
>>>>>>> be135bf719a88526107ad26063814662a43cef15

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
        return(options[n] + "");
    }

    public String showMovePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "Where would you like to move to?", "Warning",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        return(options[n] + "");
    }

    public String showUpgradePopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "What Upgrade would you like?", "Warning",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        return(options[n] + "");
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

    public void clearShot(){
        shotPanel.removeAll();
        shotPanel.revalidate();
	    shotPanel.repaint();
    }

    public void resetShot(Set s){
        JLabel l;
        AreaData area;


        for(int i = 0; i < s.getTakesLeft(); i++){
            l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
            area = s.getShotTokens().get(i).getArea();
            l.setBounds(area.getX(), area.getY(), area.getW(), area.getH());
            shotPanel.add(l);
        }
        
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