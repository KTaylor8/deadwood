import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class View implements ActionListener{
    
    JFrame frame;
    JLabel display, player1, player2, player3, player4, player5, player6, player7, player8;
    JLabel mainStreet;
    JLayeredPane lpane = new JLayeredPane();
    JPanel boardPanel = new JPanel();


    private UI ui;

    public View(UI ui){
        
        this.ui = ui;
    }

    public void show(){
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel for the top information about current player
        JPanel displayPanel = new JPanel();

        display = new JLabel("Current player: ", JLabel.CENTER);
        display.setPreferredSize(new Dimension(300, 50));
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
        //player1 = new JLabel("", JLabel.LEFT);
        //player2 = new JLabel("", JLabel.LEFT);
        //player3 = new JLabel("", JLabel.LEFT);
        //player4 = new JLabel("", JLabel.LEFT);
        //player5 = new JLabel("", JLabel.LEFT);
        //player6 = new JLabel("", JLabel.LEFT);
        //player7 = new JLabel("", JLabel.LEFT);
        //player8 = new JLabel("", JLabel.LEFT);

        JPanel leftPlayers = new JPanel();
        leftPlayers.setLayout(new GridLayout(4, 1));
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
        //rightPlayers.add(player2);
        //rightPlayers.add(player4);
        //rightPlayers.add(player6);
        //rightPlayers.add(player8);
        
        for (int i = 2; i <= 8; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            rightPlayers.add(b);
        }

        boardPanel.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        ImageIcon img = new ImageIcon("src/main/resources/img/board.png");
        layeredPane.setPreferredSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
        layeredPane.setBorder(BorderFactory.createTitledBorder("title"));
        //layeredPane.setBorder(BorderFactory.createTitledBorder(“Pane of pain, please let me die"));
        JLabel playerlabel = new JLabel(img, JLabel.CENTER);
        playerlabel.setBounds(200, 0, img.getIconWidth(), img.getIconHeight());
        layeredPane.add(playerlabel, 0);

        JLabel b = new JLabel(img);
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

        frame.add(b, BorderLayout.CENTER);
        frame.add(displayPanel,BorderLayout.PAGE_START);
        frame.add(actionPanel, BorderLayout.PAGE_END);
        frame.add(leftPlayers, BorderLayout.LINE_START);
        frame.add(rightPlayers, BorderLayout.LINE_END);
        //frame.add(mainStreet);

        frame.pack();

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();

        if ("move".equals(buttonText)) {
            //controller.move();
        } else if ("take role".equals(buttonText)) {
            //controller.takeRole();
        } else if ("upgrade".equals(buttonText)) {
            //controller.upgrade();
        } else if ("rehearse".equals(buttonText)) {
            //controller.rehearse();
        } else if ("act".equals(buttonText)) {
            //controller.act();
        }else {
            //controller.end();
        }
    }

    public void popUp(String notif){
        JOptionPane.showMessageDialog(frame, notif); 
    }

    public void changeCurrentPlayer(String playerName){
        display.setText("Current Player: " + playerName);
    }

    public void changePlayerStats(String stats){ // not functional
        player1.setText("Player 1: \nDollars: \n Credits: ");
    }

    public void setCard(Set s){
        if((s.getName()).equals("mainStreet")){
            ImageIcon img = new ImageIcon((s.getCard()).getPicturePath());
            // = new JLabel(img);
            System.out.println("hello? " + (s.getCard()).getPicturePath());
        }
        
    }

}