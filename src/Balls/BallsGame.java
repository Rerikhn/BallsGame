package Balls;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Formatter;
import java.util.Timer;

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
    private JCheckBox agarIO;
    private float massBig;
    private boolean truthColor = false;
    private boolean truthCollide = false;
    private boolean truthIO = false;
    private int click;

    private Thread game;
    private Thread timer;

    /**
     * The current motion of this ball.
     * This vector is added to the position at each step.
     */


    /*** Draw frame per second*/
    private static int UPDATE_RATE = 120;

    /*** Count of balls */
    int count = 0;

    public BallsGame(int width, int height) {
        JMenuBar bar = new JMenuBar();

        /** Upper menu */
        JMenu menu = new JMenu("Menu");
        JMenu submenu = new JMenu("Specific");

        /** Throw a big ball */
        JMenuItem bigBall = new JMenuItem("Run the big!");
        bigBall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector2d velocity = new Vector2d(6, 6);
                balls.add(new Ball(velocity, 60, BallsGame.this.massBig));
            }
        });

        /** Pause thread */
        JMenuItem pause = new JMenuItem("Pause");
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.suspend();
                game.suspend();
            }
        });

        /** Resume thread */
        JMenuItem resume = new JMenuItem("Resume");
        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.resume();
                timer.resume();
            }
        });

        /** Reset count of balls */
        JMenuItem reset = new JMenuItem("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count = 0;
                balls.clear();
            }
        });

        /** Reverse colors of both balls if collide */
        JCheckBox recolor = new JCheckBox("Reverse color");
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

        /** Add 1 ball for 1 second */
        JCheckBox timer = new JCheckBox("Timer");
        timer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cbLog = (JCheckBox) e.getSource();
                if (cbLog.isSelected()) {
                    addByTimer(true);
                } else {
                    addByTimer(false);
                }
            }
        });

        /*agarIO = new JCheckBox("ON/OFF agarIO");
        agarIO.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox cbLog = (JCheckBox) e.getSource();
                if (cbLog.isSelected()) {
                    truthIO = true;
                } else {
                    truthIO = false;
                }
            }
        });*/

        /** Turn on/off colliding */
        JCheckBox colliding = new JCheckBox("ON/OFF colliding");
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

        /** Set the count to generate balls */
        SpinnerModel value = new SpinnerNumberModel(1, 0, 100, 10);
        JSpinner clicks = new JSpinner(value);
        //setting up default clicks by one
        click = (int) clicks.getValue();
        clicks.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                click = (int) ((JSpinner) e.getSource()).getValue();
            }
        });
        JMenu spinMenu = new JMenu("Clicks");
        spinMenu.add(clicks);

        /** Set the mass of big ball */
        JSlider massBig = new JSlider(0, 1000, 0);
        massBig.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    BallsGame.this.massBig = source.getValue();
                }
            }
        });
        massBig.setMinorTickSpacing(1);
        massBig.setMajorTickSpacing(300);
        massBig.setPaintTicks(true);
        massBig.setPaintLabels(true);
        JMenu massBigMenu = new JMenu("Mass of big ball");
        massBigMenu.add(massBig);

        /** Set update rate */
        JSlider js = new JSlider(2, 1000, UPDATE_RATE);
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

        /** Adding all to upper menu */
        submenu.add(reset);
        submenu.add(bigBall);
        menu.add(pause);
        menu.add(resume);
        menu.add(submenu);
        bar.add(menu);
        bar.add(update);
        bar.add(massBigMenu);
        bar.add(recolor);
        bar.add(colliding);
        bar.add(timer);
        //bar.add(agarIO);
        bar.add(spinMenu);

        canvasWidth = width;
        canvasHeight = height;
        box = new Container();
        canvas = new DrawCanvas();

        /** JPanel with balls*/
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.add(bar, BorderLayout.NORTH);
        this.addMouseListener(this);

        start();
    }

    /**
     * Thread of balls
     */
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

    /**
     * Main method to organize logic
     */
    public void updateFrame() {
        Ball[] ball = balls.toArray(new Ball[balls.size()]);
        for (int i = 0; i < ball.length; i++) {
            ball[i].movePhysics();
            for (int j = i + 1; j < ball.length; j++) {
                if (ball[i].colliding(ball[j])) {
                    ball[i].resolveCollision(ball[j], truthCollide);
                    ball[i].reverseColor(ball[j], truthColor);
                    //ball[i].agarIO(ball[j], balls, j, truthIO);
                }
            }
        }
    }

    /**
     * JPanel with balls
     */
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

    public void addByTimer(boolean t) {
        if (t == true) {
            timer = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (balls.size() < 1500) {
                        count += click;
                        for (int i = 0; i < click; i++)
                            balls.add(new Ball());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            timer.start();
        } else {
            timer.stop();
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
        if (balls.size() < 1500) {
            count += click;
            for (int i = 0; i < click; i++)
                balls.add(new Ball());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}
