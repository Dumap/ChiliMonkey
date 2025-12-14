package game;

import javax.swing.*;

public class Target2 extends Target {

    public Target2(
            ChiliMonkey game,
            int ypos,
            Icon ico,
            int speed,
            int end,
            int index) {

        super(game, ypos, ico, speed, end, index);
        setBounds(0, ypos, 30, 30);
    }

    @Override
    public void run() {

        while (!game.isHit[index]) {

            // Move left → right
            for (int i = 0; i < end; i++) {
                if (game.isHit[index]) {
                    break;
                }
                moveLeft();
                sleep(speed);
            }

            // Random pause (same logic as original)
            try {
                int wait = ((int)(Math.random() * 6) + 2) * 1000;
                Thread.sleep(wait);
            } catch (InterruptedException ignored) {}

            // Reset off-screen
            x = -20;
        }

        game.getGameBackground().remove(this);
    }

    /**
     * Override to ensure Target2 NEVER fires bananas
     */
    @Override
    protected void fireBanana() {
        // intentionally empty
    }

    /**
     * Override to ensure Target2 NEVER kills the alien on contact
     */
    @Override
    protected void checkAlienCollision(int ox, int oy) {
        // intentionally empty
    }
}
