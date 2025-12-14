package game;

import javax.swing.*;

public class Target extends JLabel implements Runnable {

    protected int x = 0;
    protected int ypos, speed, end, index;
    protected ChiliMonkey game;

    public Target(ChiliMonkey game, int ypos, Icon ico, int speed, int end, int index) {
        this.game = game;
        this.ypos = ypos;
        this.speed = speed;
        this.end = end;
        this.index = index;
        setIcon(ico);
        setBounds(0, ypos, 45, 45);
    }

    @Override
    public void run() {
        int offsetX = getWidth() - 10;
        int offsetY = getHeight() - 10;

        while (!game.isHit[index]) {

            int wait = ((int)(Math.random() * 400) + 20);

            // move right
            for (int i = 0; i < end; i++) {
                if (game.isHit[index]) break;
                moveLeft();
                sleep(speed);

                checkAlienCollision(offsetX, offsetY);

                if ((index == 1 || index == 3) && i == wait) {
                    fireBanana();
                }
            }

            // move left
            for (int i = 0; i < end; i++) {
                if (game.isHit[index]) break;
                moveRight();
                sleep(speed);

                checkAlienCollision(offsetX, offsetY);

                if ((index == 1 || index == 3) && i == wait) {
                    fireBanana();
                }
            }
        }

        game.getGameBackground().remove(this);
    }

    protected void fireBanana() {
        Banana b = new Banana(
                game,
                getX() + 10,
                getY() + 20
        );
        new Thread(b).start();
    }

    protected void checkAlienCollision(int ox, int oy) {
        JLabel ali = game.ali;
        if (getX() + ox >= ali.getX() &&
                getX() <= ali.getX() &&
                getY() + oy >= ali.getY() &&
                getY() <= ali.getY() + 30) {

            hitAlien();
        }
    }

    protected void hitAlien() {
        game.isDead = true;
        game.go = false;
        game.isHit[index] = true;

        Explosion ex = new Explosion(
                game,
                game.ali.getX() - 10,
                game.ali.getY() - 10
        );
        new Thread(ex).start();

        game.end();
    }

    protected void moveLeft() {
        setLocation(++x, ypos);
    }
    protected void moveRight() {
        setLocation(--x, ypos);
    }

    protected void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}
