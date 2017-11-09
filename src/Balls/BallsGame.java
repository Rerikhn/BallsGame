package Balls;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class BallsGame extends JPanel implements MouseListener {

    /**
     * ArrayList of many balls
     */
    protected List<Ball> balls = new ArrayList<Ball>();

    private Container box;
    private int canvasWidth;
    private int canvasHeight;
    private DrawCanvas canvas;
    private JMenu menu, submenu;
    private JMenuItem i1, i2, i3;
    private Thread game;
    private JSlider speed;

    /**
     * Draw frame per second
     */
    private static int FPS_RATE = 120;

    /**
     * Count of balls
     */
    int count = 0;

    public BallsGame(int width, int height) {
        JMenuBar bar = new JMenuBar();

        /** Upper menu */
        menu = new JMenu("Menu");
        submenu = new JMenu("Specific");

        /** Pause thread */
        i1 = new JMenuItem("Pause");
        i1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.suspend();
            }
        });

        /** Resume thread */
        i2 = new JMenuItem("Resume");
        i2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.resume();
            }
        });

        /** Reset count of balls */
        i3 = new JMenuItem("Reset");
        i3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count = 0;
                balls.clear();
            }
        });

        //set speed of balls
        /*Font font = new Font("Serif", Font.ITALIC, 5);
        speed.setFont(font);*/
        //speed.setName("Speed");
        speed = new JSlider(JSlider.HORIZONTAL, 30, 240, 120);
        speed.setPreferredSize(new Dimension(2, 50));
        speed.setMajorTickSpacing(20);
        speed.setMinorTickSpacing(0);
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    Ball[] ball = balls.toArray(new Ball[balls.size()]);
                    for (int i = 0; i < ball.length; i++) {
                        FPS_RATE = source.getValue();
                    }
                }
            }
        });
        speed.setPaintTicks(true);
        speed.setPaintLabels(true);

        submenu.add(i3);
        menu.add(i1);
        menu.add(i2);
        menu.add(submenu);
        bar.add(menu);
        canvasWidth = width;
        canvasHeight = height;
        box = new Container();
        canvas = new DrawCanvas();

        /**JPanel with balls*/
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.add(bar, BorderLayout.NORTH);
        this.add(speed, BorderLayout.AFTER_LAST_LINE);
        this.addMouseListener(this);

        start();
    }

    public void start() {
        game = new Thread() {
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
        game.start();
    }

    public void update() {
        Ball[] ball = balls.toArray(new Ball[balls.size()]);
        for (int i = 0; i < ball.length; i++) {
            ball[i].movePhysics();
        }
        /*for (Ball ball : balls) {
            ball.movePhysics();
            //ball.checkCollision(ball, ball);
        }*/
    }

    class DrawCanvas extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            /** Normal box drawing */
            box.draw(g);
            for (Ball ball : balls) {
                ball.draw(g);
            }

            /** Count information */
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.PLAIN, 12));
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb);
            formatter.format("Count of balls: " + count);
            g.drawString(sb.toString(), 20, 30);
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
