package listeners;

import game.*;
import java.awt.event.*;

public class KListener implements KeyListener {
    private final ChiliMonkey game;

    public KListener(ChiliMonkey game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> {
                game.left = true;
                game.right = false;
            }
            case KeyEvent.VK_RIGHT -> {
                game.right = true;
                game.left = false;
            }
            case KeyEvent.VK_UP -> {
                game.up = true;
                game.down = false;
            }
            case KeyEvent.VK_DOWN -> {
                game.down = true;
                game.up = false;
            }
            case KeyEvent.VK_SPACE -> {
                if (!game.isFired && game.go) {
                    Missile m = new Missile(
                            game,
                            game.ali.getX() + 10,
                            game.ali.getY() - 30
                    );
                    new Thread(m).start();
                    game.isFired = true;
                }
            }
            case KeyEvent.VK_ENTER -> {
                if (!game.isStarted) {
                    game.start();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> game.left = false;
            case KeyEvent.VK_RIGHT -> game.right = false;
            case KeyEvent.VK_UP -> game.up = false;
            case KeyEvent.VK_DOWN -> game.down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }
}
