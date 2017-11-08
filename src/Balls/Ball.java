package Balls;

import java.awt.*;
import java.util.Formatter;
import java.util.Random;

public class Ball {

    //size of Box
    private static final int BOX_HEIGHT = 600;
    private static final int BOX_WIDTH = 600;

    Random r = new Random();

    float radius = (r.nextFloat() * 40);
    float speedX = (r.nextFloat() * (2 - 0.1f) + 0.1f); //(maxX - minX) + minX
    float speedY = (r.nextFloat() * (2 - 0.1f) + 0.1f); //(maxX - minX) + minX

    //randomize coordinates
    float circleX = (r.nextInt() * (BOX_WIDTH - radius));
    float circleY = (r.nextInt() * (BOX_HEIGHT - radius));

    //
    float distance;
    float deltaX;
    float deltaY;

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

    public float getSpeedY() {
        return speedY;
    }

    public float getCircleX() {
        return circleX;
    }

    public float getCircleY() {
        return circleY;
    }

    public float getRadius() {
        return radius;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setRadius (float rad) {
        this.radius = rad;
    }

    public void setSpeedX (float speed) {
        this.speedX = speed;
    }

    public void setSpeedY (float speed) {
        this.speedY = speed;
    }

    public void setCircleX (float circleX) {
        this.circleX = circleX;
    }

    public void setCircleY (float circleY) {
        this.circleY = circleY;
    }

    public void checkCollision(Ball b1, Ball b2) {

        deltaX = Math.abs(b1.getCircleX() - b2.getCircleX());
        deltaY = Math.abs(b1.getCircleY() - b2.getCircleY());
        distance = deltaX * deltaX + deltaY * deltaY;

        if (distance < (b1.getRadius() + b2.getRadius()) * (b1.getRadius() + b2.getRadius())) {

            float newxSpeed1 = (b1.getSpeedX() * (4 - 7) + (2 * 7 * b2.getSpeedX())) / 11;

            float newxSpeed2 = (b2.getSpeedX() * (7 - 4) + (2 * 4 * b1.getSpeedX())) / 11;

            float newySpeed1 = (b1.getSpeedY() * (4 - 7) + (2 * 7 * b2.getSpeedY())) / 11;

            float newySpeed2 = (b2.getSpeedY() * (7 - 4) + (2 * 4 * b1.getSpeedY())) / 11;

            b2.setSpeedX(newxSpeed2);
            b2.setSpeedY(newySpeed2);
            b1.setSpeedX(newxSpeed1);
            b1.setSpeedY(newySpeed1);

        }
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
