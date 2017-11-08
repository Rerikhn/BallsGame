package Balls;

import javax.swing.*;

public class BallsMain {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Шарики");
                f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
                f.setContentPane(new BallsGame(600, 600));
                f.pack();
                f.setVisible(true);
            }
        });
    }
}
