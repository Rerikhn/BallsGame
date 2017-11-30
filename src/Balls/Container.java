package Balls;

import javax.swing.*;
import java.awt.*;

public class Container {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color COLOR = Color.BLACK;

    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
    }
}
