package game;

import javax.swing.*;

public class Explosion extends JLabel implements Runnable {
    public Explosion(ChiliMonkey game, int x, int y) {
        setIcon(game.boom);
        setBounds(x,y,100,100);
        game.getGameBackground().add(this);
    }

    public void run() {
        try { Thread.sleep(300); }
        catch (InterruptedException ignored) {}
        setVisible(false);
    }
}

