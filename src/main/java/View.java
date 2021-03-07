import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class View{
    JFrame frame;
    JLabel display;



    public void show(){
        frame = new JFrame();
        frame.setSize(new Dimension(300, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel for the top information about current player
        JPanel displayPanel = new JPanel();

        display = new JLabel("0", JLabel.RIGHT);
        display.setPreferredSize(new Dimension(290, 50));
        display.setBorder(BorderFactory.createLoweredBevelBorder());
        display.setOpaque(true);
        display.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        display.setBackground(Color.WHITE);

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

        frame.add(displayPanel,BorderLayout.PAGE_START);
        frame.add(actionPanel, BorderLayout.PAGE_END);
        //frame.add(players1, BoarderLayout.LINE_START);
        //frame.add(players1, BoarderLayout.LINE_END);
        //frame.add(board, BoarderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();

        if ("move".equals(buttonText)) {
            controller.move();
        } else if ("take role".equals(buttonText)) {
            controller.takeRole();
        } else if ("upgrade".equals(buttonText)) {
            controller.upgrade();
        } else if ("rehearse".equals(buttonText)) {
            controller.rehearse();
        } else if ("act".equals(buttonText)) {
            controller.act();
        }else {
            controller.end();
        }
    }

}