package Balls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class BallsGame extends JPanel implements MouseListener {

    //array of balls of max count
    protected List<Ball> balls = new ArrayList<Ball>();

    private Container box;
    private int canvasWidth;
    private int canvasHeight;
    private DrawCanvas canvas;

    //draw frame per second
    public static final int FPS_RATE = 120;

    //count of balls
    int count = 0;

    public BallsGame(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;
        box = new Container();
        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.addMouseListener(this);
        start();
    }

    public void start() {

        Thread t = new Thread() {
            public void run() {
                while (true) {
                    update();
                    repaint();
                    try {
                        Thread.sleep(1000 / FPS_RATE);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        t.start();
    }

    public void update() {
        for (Ball ball : balls) {
            ball.movePhysics(box);
        }
    }

    class DrawCanvas extends JPanel {

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            box.draw(g);
            for (Ball ball : balls) {
                ball.draw(g);
            }
        }

        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        count++;
        balls.add(new Ball());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}
