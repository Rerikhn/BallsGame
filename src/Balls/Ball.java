package Balls;

import java.awt.*;
import java.util.Formatter;
import java.util.Random;

public class Ball {

    //size of Box
    private static final int BOX_HEIGHT = 600;
    private static final int BOX_WIDTH = 600;

    Random r = new Random();

    float radius = (r.nextFloat()*40);
    float speedX = (r.nextFloat()*(10-0.5f)+0.5f); //(maxX - minX) + minX
    float speedY = (r.nextFloat()*(10-0.5f)+0.5f); //(maxX - minX) + minX

    public float getSpeedY() {
        return speedY;
    }

    //randomize coordinates
    float circleX = (r.nextInt()*(BOX_WIDTH - radius));
    float circleY = (r.nextInt()*(BOX_HEIGHT - radius));

    //color palette by RGB
    int red = r.nextInt(255);
    int green = r.nextInt(255);
    int blue = r.nextInt(255);


    public Ball() {
    }


    public void draw(Graphics g) {

        //draw a ball
        g.setColor(new Color(red, green, blue));
        g.fillOval((int) (circleX - radius), (int) (circleY - radius), (int) (2 * radius), (int) (2 * radius));

    }

    public void movePhysics(Container container) {
        circleX += speedX; //run the ball
        circleY += speedY;
        // Check if the ball moves over the bounds
        // If so, adjust the position and speed.
        if (circleX - radius < 0) {
            speedX = -speedX; // Reflect along normal
            circleX = radius; // Re-position the ball at the edge
        } else if (circleX + radius > BOX_WIDTH) {
            speedX = -speedX;
            circleX = BOX_WIDTH - radius;
        }
        // May cross both x and y bounds
        if (circleY - radius < 0) {
            speedY = -speedY;
            circleY = radius;
        } else if (circleY + radius > BOX_HEIGHT) {
            speedY = -speedY;
            circleY = BOX_HEIGHT - radius;
        }
    }

}
