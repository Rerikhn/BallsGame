package Balls;

import javax.swing.*;
import java.awt.event.*;

public class BallsGameMain {

    private JFrame mainWindow;

    BallsGameMain() {

        mainWindow = new JFrame("Menu");

        /**Start Button*/
        JButton start = new JButton("Start");
        start.setBounds(3, 680, 1360, 60);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.start();
            }
        });

        JSlider maxRadiusBig = new JSlider(JSlider.HORIZONTAL, 50,100, 50 );
        maxRadiusBig.setBounds(10, 600, 100,60);
        maxRadiusBig.setMinorTickSpacing(50);
        maxRadiusBig.setMajorTickSpacing(100);
        maxRadiusBig.setPaintLabels(true);
        maxRadiusBig.setPaintTicks(true);

        mainWindow.add(start);
        mainWindow.add(maxRadiusBig);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        new BallsGameMain();
    }
}
