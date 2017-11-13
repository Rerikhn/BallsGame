package Balls;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Ball {

    //size of Box
    private static final int BOX_HEIGHT = 725;
    private static final int BOX_WIDTH = 1366;

    Random r = new Random();

    /**
     * For Vectors
     */
    private Vector2d velocity;
    private Vector2d position;

    private float radius;
    private float mass;
    private float density;
    private float capacity;

    //color palette by RGB
    private int red;
    private int green;
    private int blue;

    public Ball() {
        /** Randomize Vector's for each */
        velocity = new Vector2d((r.nextFloat() * (2f - 0.1f) + 0.1f),
                (r.nextFloat() * (2f - 0.1f) + 0.1f));

        position = new Vector2d(
                (r.nextInt() * ((BOX_WIDTH - radius) - BOX_WIDTH / 2) + BOX_WIDTH / 2),
                (r.nextInt() * ((BOX_HEIGHT - radius) - BOX_HEIGHT / 2) + BOX_HEIGHT / 2));

        radius = (r.nextFloat() * (5 - 2) + 2);

        mass = radius * Constants.pi;

        density = mass / Constants.pi;

        /**
         * Randomize color set for each
         * */
        red = r.nextInt(255);
        green = r.nextInt(255);
        blue = r.nextInt(255);
    }

    /**
     * THE BIG BALL!!!
     */
    public Ball(Vector2d velocity, float radius) {
        this.velocity = velocity;
        this.radius = radius;

        position = new Vector2d(
                (r.nextInt() * ((BOX_WIDTH - radius) - BOX_WIDTH / 2) + BOX_WIDTH / 2),
                (r.nextInt() * ((BOX_HEIGHT - radius) - BOX_HEIGHT / 2) + BOX_HEIGHT / 2));
        mass = (radius * Constants.pi); //Manual mass to biggest ball

        red = r.nextInt(255);
        green = r.nextInt(255);
        blue = r.nextInt(255);
    }

    public void draw(Graphics g) {
        //draw a ball
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(red, green, blue));
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.fillOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public void reverseColor(Ball b1, Ball b2, boolean truth) {
        if (truth == true) {
            int rb1, gb1, bb1;
            rb1 = b1.getRed();
            gb1 = b1.getGreen();
            bb1 = b1.getBlue();
            b1.setRGB(b2.getRed(), b2.getGreen(), b2.getBlue());
            b2.setRGB(rb1, gb1, bb1);
        }
    }

    public void agarIO(Ball b1, Ball b2, ArrayList<Ball> temp, int i, boolean truth) {
        if (truth == true) {
            b1.setRadius(b2.getRadius() + b1.getRadius());
            b2.setRadius(b1.getRadius() + b2.getRadius());
            if (temp.size() > 1)
                temp.remove(i);
        }
    }

    public void setRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float rad) {
        this.radius = rad;
    }

    public boolean colliding(Ball ball) {
        float xd = position.getX() - ball.position.getX();
        float yd = position.getY() - ball.position.getY();

        float sumRadius = getRadius() + ball.getRadius();
        float sqrRadius = sumRadius * sumRadius;

        float distSqr = (xd * xd) + (yd * yd);

        if (distSqr <= sqrRadius) {
            return true;
        }
        return false;
    }

    public void resolveCollision(Ball ball, boolean truth) {
        if (truth == true) {
            // get the mtd
            Vector2d delta = (position.subtract(ball.position));
            float r = getRadius() + ball.getRadius();
            float dist2 = delta.dot(delta);

            if (dist2 > r * r) return; // they aren't colliding

            float d = delta.getLength();

            Vector2d mtd;
            if (d != 0.0f) {
                mtd = delta.multiply(((getRadius() + ball.getRadius()) - d) / d); // minimum translation distance to push balls apart after intersecting

            } else // Special case. Balls are exactly on top of eachother.  Don't want to divide by zero.
            {
                d = ball.getRadius() + getRadius() - 1.0f;
                delta = new Vector2d(ball.getRadius() + getRadius(), 0.0f);

                mtd = delta.multiply(((getRadius() + ball.getRadius()) - d) / d);
            }

            // resolve intersection
            float mass1 = 1 / getMass(); // inverse mass quantities
            float mass2 = 1 / ball.getMass();

            // push-pull them apart
            position = position.add(mtd.multiply(mass1 / (mass1 + mass2)));
            ball.position = ball.position.subtract(mtd.multiply(mass2 / (mass1 + mass2)));

            // impact speed
            Vector2d v = (this.velocity.subtract(ball.velocity));
            float vn = v.dot(mtd.normalize());

            // sphere intersecting but moving away from each other already
            if (vn > 0.0f) return;

            // collision impulse
            float i = (-(1.0f + Constants.restitution) * vn) / (mass1 + mass2);
            Vector2d impulse = mtd.multiply(i);

            // change in momentum
            this.velocity = this.velocity.add(impulse.multiply(mass1));
            ball.velocity = ball.velocity.subtract(impulse.multiply(mass2));
        }
    }

    public void movePhysics() {

        /**
         * Move the ball normally by X-axis and Y-axis
         * */
        position.set(position.getX() + velocity.getX(),
                position.getY() + velocity.getY());

        if (position.getX() - radius < 0) {
            velocity.setX(-velocity.getX());
            position.setX(radius);
        } else if (position.getX() + radius > BOX_WIDTH) {
            velocity.setX(-velocity.getX()); // Reflect along normal
            position.setX(BOX_WIDTH - radius);           // // Re-position the ball at the edge
        }

        if (position.getY() - radius < 0) {
            velocity.setY(-velocity.getY());
            position.setY(radius);
        } else if (position.getY() + radius > BOX_HEIGHT) {
            velocity.setY(-velocity.getY()); // Reflect along normal
            position.setY(BOX_HEIGHT - radius);
        }

    }

}
