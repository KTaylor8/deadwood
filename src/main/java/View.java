import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class View implements ActionListener{
    JFrame frame;
    JLabel display;

    private ControllerInterface controller;

    public View(ControllerInterface controller) {
        this.controller = controller;
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

        JPanel leftPlayers = new JPanel();
        leftPlayers.setLayout(new GridLayout(4, 1));

        leftPlayers.setSize(new Dimension(50,400));
        
        for (int i = 1; i <= 7; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            leftPlayers.add(b);
        }

        JPanel rightPlayers = new JPanel();
        rightPlayers.setLayout(new GridLayout(4, 1));

        rightPlayers.setSize(new Dimension(50,400));
        
        for (int i = 2; i <= 8; i = i+2) {
            JLabel b = new JLabel("Player " + i, JLabel.LEFT);
            rightPlayers.add(b);
        }

        ImageIcon img = new ImageIcon("src/main/resources/img/board.png");
        JLabel board = new JLabel(img);
        //board.setMaximumSize(new Dimension(100, 100));

        //JLabel board = new JLabel(new ImageIcon("src/main/resources/img/board.png"));
        board.setBounds(0,0,img.getIconWidth(), img.getIconHeight());

        frame.add(board, BorderLayout.CENTER);
        frame.add(displayPanel,BorderLayout.PAGE_START);
        frame.add(actionPanel, BorderLayout.PAGE_END);
        frame.add(leftPlayers, BorderLayout.LINE_START);
        frame.add(rightPlayers, BorderLayout.LINE_END);

        frame.pack();

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();

        if ("move".equals(buttonText)) {
            controller.move();
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

    public void givePopUp(String notif){
        JOptionPane.showMessageDialog(frame, notif); 
    }

}