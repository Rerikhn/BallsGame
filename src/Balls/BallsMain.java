package Balls;

import javax.swing.*;
import java.awt.event.*;

public class BallsMain {

    private JFrame mainWindow;
    private JButton start;
    private JPopupMenu popupMenu;
    private JButton pause;

    BallsMain() {
        mainWindow = new JFrame("Menu");
        start = new JButton("Start");
        start.setBounds(115, 150, 150, 80);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.start();
            }
        });
        mainWindow.add(start);
        mainWindow.setSize(400, 400);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            mainWindow.setTitle("Game");
            mainWindow.setDefaultCloseOperation(mainWindow.EXIT_ON_CLOSE);
            mainWindow.setContentPane(new BallsGame(1366, 768));
            mainWindow.pack();
            mainWindow.setVisible(true);
            mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    };
    Thread t = new Thread(r);

    public static void main(String[] args) {
        new BallsMain();
    }
}
