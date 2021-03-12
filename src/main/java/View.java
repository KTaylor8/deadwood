// import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.Graphics;


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
    Controller controller;

    public View(Controller controller){
        this.controller = controller;
    }

    public void show(){
        int leftMargin = 300; // sets left margin of display and panels, for centering

        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel for the top information about current player
        JPanel displayPanel = new JPanel();

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
        JPanel leftPlayers = new JPanel();
        leftPlayers.setLayout(new GridLayout(4, 1));
        leftPlayers.setSize(new Dimension(800, 1200));
        //leftPlayers.add(player1);
        //leftPlayers.add(player3);
        //leftPlayers.add(player5);
        //leftPlayers.add(player7);
        /*
        leftPlayers.setSize(new Dimension(50,400));
        */
        for (int i = 1; i <= 7; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            leftPlayers.add(b);
        }

        JPanel rightPlayers = new JPanel();
        rightPlayers.setLayout(new GridLayout(4, 1));
        rightPlayers.setSize(new Dimension(800, 1200));
        //rightPlayers.add(player2);
        //rightPlayers.add(player4);
        //rightPlayers.add(player6);
        //rightPlayers.add(player8);
        
        for (int i = 2; i <= 8; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            rightPlayers.add(b);
        }

        //cardPanel.setLayout(null);

        ImageIcon img = new ImageIcon("src/main/resources/img/board.png");
        layeredPane.setSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
        JLabel board = new JLabel(img, JLabel.CENTER);
        board.setBounds(leftMargin, 0, img.getIconWidth(), img.getIconHeight());
        //layeredPane.add(board, 100);
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
        //layeredPane.add(cardPanel, 0);
        
        /*for(int i = 0; i < 10; i++){
            (places[i]).setSize(new Dimension(300,300));
            layeredPane.add(places[i]);
        }*/

        

        //JLabel b = new JLabel(img);
        //boardPanel.add(b, BorderLayout.CENTER);
        //lpane.add(boardPanel, 0);
        //JLayeredPane board = new JLayeredPane();
        //board.setLayout(new BorderLayout());

        //board.setPreferredSize(new Dimension(img.getIconWidth(), img.getIconHeight()));

        //JButton please = new JButton("please");
        //board.add(b, 0);
        //board.setVisible(true);
        //board.setMaximumSize(new Dimension(100, 100));

        //JLabel board = new JLabel(new ImageIcon("src/main/resources/img/board.png"));
        //board.setBounds(0,0,img.getIconWidth(), img.getIconHeight());

        

        frame.add(layeredPane, BorderLayout.CENTER);
        frame.add(displayPanel,BorderLayout.PAGE_START);
        frame.add(actionPanel, BorderLayout.PAGE_END);
        frame.add(leftPlayers, BorderLayout.LINE_START);
        frame.add(rightPlayers, BorderLayout.LINE_END);
        //frame.add(mainStreet);

        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();

        if ("move".equals(buttonText)) {
            // if (game.haveTheyPlayed()) {
            //     showPopUp("You've already moved, rehearsed or acted this turn. Try a different command or type `end` to end your turn.");
            // }
            // else {
            //     String[] options = ((game.getCurrentPlayer()).getLocation()).getNeighborStrings();
            //     String result = moveshowPopUp(options);
            //     game.changeHasPlayed((game.getCurrentPlayer()).moveTo(game.getBoardSet(result), this));
            //     game.refreshPlayerPanel(this);
            // }

            controller.tryMove();
        } else if ("take role".equals(buttonText)) {
            // showPopUp("dont take that role, trust me");
            controller.tryTakeRole();
        } else if ("upgrade".equals(buttonText)) {
            // showPopUp("upgrades people, upgrades");
            controller.tyrUpgrade();
        } else if ("rehearse".equals(buttonText)) {
            // showPopUp("oh honey, you're gonna need something a lil more than rehearsing");
            controller.tryRehearse();
        } else if ("act".equals(buttonText)) {
            // showPopUp("ha, yea right");
            controller.tryAct();
        }else {
            // game.changeTurn();
            controller.endTurn();
        }
    }

    public String moveshowPopUp(String[] options){
        int n = JOptionPane.showOptionDialog(null, "Where would you like to move to?", "Warning",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        return(options[n] + "");
    }

    public void showPopUp(String notif){
        JOptionPane.showMessageDialog(frame, notif); 
    }

    public void changeCurrentPlayer(String playerName){
        display.setText("Current Player: " + playerName);
    }

    public void changePlayerStats(String stats){ // not functional
        player1.setText("Player 1: \nDollars: \n Credits: ");
    }

    public void clearCard(){
        cardPanel.removeAll();
    }

    public void resetCard(Set s){
        ImageIcon img;
        JLabel l;

        AreaData area = s.getArea();

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

    public void clearShot(){
        shotPanel.removeAll();
    }

    public void resetShot(Set s){
        JLabel l;
        AreaData area;

        System.out.println(s.getTakesLeft());

        for(int i = 0; i < s.getTakesLeft(); i++){
            l = new JLabel(new ImageIcon("src/main/resources/img/shot.png"));
            area = s.getShotTokens().get(i).getArea();
            l.setBounds(area.getX(), area.getY(), area.getW(), area.getH());
            shotPanel.add(l);
        }
        //System.out.println("uh " + (s.getCard()).getPicturePath());
        
    }

    
    public void clearDice(){
        dicePanel.removeAll();
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
        movePlayerDie(p, x, y);

        l = new JLabel(new ImageIcon(p.getPlayerDiePath()));

        l.setBounds(x, y, 46, 46); // dice w/h = 46
        cardPanel.add(l);
    }

    // moves player's die to specified x and y positions
    public void movePlayerDie(Player p, int x, int y){
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