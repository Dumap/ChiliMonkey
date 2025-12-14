package game;

import javax.swing.*;

public class Banana extends JLabel implements Runnable {

    private final int xpos;
    private int ypos;
    private boolean impact = false;
    private final ChiliMonkey game;

    public Banana(ChiliMonkey game, int xpos, int ypos) {
        this.game = game;
        this.xpos = xpos;
        this.ypos = ypos;
        setIcon(game.banana);
        setSize(19, 23);
    }

    @Override
    public void run() {
        // Only add to background if game is running
        if (game.go && !impact) {
            game.getGameBackground().add(this);
            setVisible(true);
        }

        while (ypos < 550 && !impact && game.go && !game.isDead) {
            ypos += 2;
            setLocation(xpos, ypos);

            // Collision with alien
            JLabel ali = game.ali;
            int offsetX = ali.getWidth();
            int offsetY = ali.getHeight();

            if (xpos + 15 >= ali.getX() &&
                    xpos <= ali.getX() + offsetX &&
                    ypos - 20 >= ali.getY() &&
                    ypos + 5 <= ali.getY() + offsetY) {

                // Hit!
                game.isDead = true;
                game.go = false;
                impact = true;

                setVisible(false);
                ali.setVisible(false);

                // Explosion effect
                Explosion myExplosion = new Explosion(game, ali.getX() - 10, ali.getY() - 10);
                new Thread(myExplosion).start();

                // End the game properly
                game.end();
                break;
            }

            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                System.out.println("Banana interrupted");
                break;
            }
        }

        // Remove from background when done
        game.getGameBackground().remove(this);
        game.getGameBackground().repaint();
    }
}
