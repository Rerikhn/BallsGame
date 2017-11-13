package Balls;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Formatter;
import java.util.List;

public class BallsGame
        extends JPanel
        implements MouseListener {

    /**
     * ArrayList of many balls
     */
    protected ArrayList<Ball> balls = new ArrayList<Ball>();

    private Container box;
    private int canvasWidth;
    private int canvasHeight;
    private DrawCanvas canvas;
    private JMenu menu, submenu;
    private JMenuItem i1, i2, i3 , bigBall, agar;
    private JCheckBox recolor, colliding;
    private Thread game;

    private boolean truthColor = false;
    private boolean truthCollide = false;

    /**
     * The current motion of this ball.
     * This vector is added to the position at each step.
     */


    /*** Draw frame per second*/
    private static int UPDATE_RATE = 60;

    /*** Count of balls */
    int count = 0;

    public BallsGame(int width, int height) {
        JMenuBar bar = new JMenuBar();

        /** Upper menu */
        menu = new JMenu("Menu");
        submenu = new JMenu("Specific");

        /** Throw a big ball */
        bigBall = new JMenuItem("Run the big!");
        bigBall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector2d velocity = new Vector2d(6,6);
                balls.add(new Ball(velocity, 50));
            }
        });

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

        recolor = new JCheckBox("Reverse color");
        recolor.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cbLog = (JCheckBox) e.getSource();
                if (cbLog.isSelected()) {
                    truthColor = true;
                } else {
                    truthColor = false;
                }
            }
        });

        colliding = new JCheckBox("Turn on/off colliding");
        colliding.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cbLog = (JCheckBox) e.getSource();
                if (cbLog.isSelected()) {
                    truthCollide = true;
                } else {
                    truthCollide = false;
                }
            }
        });


        JSlider js = new JSlider(30, 1000, 60);
        js.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    UPDATE_RATE = source.getValue();
                }
            }
        });
        js.setMinorTickSpacing(0);
        js.setMajorTickSpacing(120);
        js.setPaintTicks(true);
        js.setPaintLabels(true);
        JMenu update = new JMenu("Update Rate");
        update.add(js);

        submenu.add(i3);
        submenu.add(bigBall);
        menu.add(i1);
        menu.add(i2);
        menu.add(submenu);
        bar.add(menu);
        bar.add(update);
        bar.add(recolor);
        bar.add(colliding);

        canvasWidth = width;
        canvasHeight = height;
        box = new Container();
        canvas = new DrawCanvas();

        /**JPanel with balls*/
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.add(bar, BorderLayout.NORTH);
        this.addMouseListener(this);

        start();
    }

    public void start() {
        game = new Thread() {
            @Override
            public void run() {
                while (true) {
                    updateFrame();
                    repaint();
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        game.start();
    }

    public void updateFrame() {
        Ball[] ball = balls.toArray(new Ball[balls.size()]);
        for (int i = 0; i < ball.length; i++) {
            ball[i].movePhysics();
            for (int j = i + 1; j < ball.length; j++) {
                if (ball[i].colliding(ball[j])) {
                    ball[i].resolveCollision(ball[j], truthCollide);
                    ball[i].reverseColor(ball[i], ball[j], truthColor);
                    //ball[i].agarIO(ball[i], ball[j], balls, j, truth);
                }
            }
        }
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

            /** FPS information */
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.PLAIN, 12));
            sb = new StringBuilder();
            formatter = new Formatter(sb);
            formatter.format("Current update rate: " + UPDATE_RATE);
            g.drawString(sb.toString(), 170, 30);
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
        if (balls.size() < 10000) {
            count+=100;
            for (int i = 0; i<100; i++)
            balls.add(new Ball());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}
