package Balls;

import java.awt.*;

public class Container {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 600;
    private static final Color COLOR = Color.BLACK;

    public void draw(Graphics g) {

        g.setColor(COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }
}
