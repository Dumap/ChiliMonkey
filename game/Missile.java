package game;

import javax.swing.*;

public class Missile extends JLabel implements Runnable {

    private final ChiliMonkey game;
    private final int x;
    private int y;

    public Missile(ChiliMonkey game, int x, int y) {
        this.game = game;
        this.x = x;
        this.y = y;
        setIcon(game.mis);
    }

    @Override
    public void run() {
        setBounds(x, y, 18, 50);
        game.getGameBackground().add(this);

        while (getY() > -50 && !game.out) {
            setLocation(x, y -= 2);
            sleep(4);

            checkTargetHits();
        }

        game.out = false;
        game.isFired = false;
        setVisible(false);
    }

    private void checkTargetHits() {
        for (int i = 0; i < game.MAX; i++) {
            JLabel t = game.targetVec.elementAt(i);

            if (!game.isHit[i] &&
                    getX() + 5 >= t.getX() &&
                    getX() + 5 <= t.getX() + t.getWidth() &&
                    getY() - 5 >= t.getY() &&
                    getY() - 5 <= t.getY() + t.getHeight()) {

                game.isHit[i] = true;
                game.out = true;

                Explosion ex = new Explosion(
                        game,
                        t.getX() - 10,
                        t.getY() - 10
                );
                new Thread(ex).start();

                game.numKill++;
            }
        }

        if (game.numKill == game.MAX) {
            game.numKill = 0;
            game.end();
        }
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}
