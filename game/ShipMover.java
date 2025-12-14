package game;

public class ShipMover extends Thread {

    private final ChiliMonkey game;

    public ShipMover(ChiliMonkey game) {
        this.game = game;
    }

    @Override
    public void run() {

        while (game.go) {

            // LEFT
            if (game.left && game.ali.getX() > 0) {
                game.ali.setLocation(
                        game.ali.getX() - 1,
                        game.ali.getY()
                );
            }

            // RIGHT
            if (game.right && game.ali.getX() < 460) {
                game.ali.setLocation(
                        game.ali.getX() + 1,
                        game.ali.getY()
                );
            }

            // UP
            if (game.up && game.ali.getY() > 170) {
                game.ali.setLocation(
                        game.ali.getX(),
                        game.ali.getY() - 1
                );
            }

            // DOWN
            if (game.down && game.ali.getY() < 410) {
                game.ali.setLocation(
                        game.ali.getX(),
                        game.ali.getY() + 1
                );
            }

            try {
                Thread.sleep(8);
            } catch (InterruptedException ignored) {}
        }

        // clean up when game ends
        game.getGameBackground().remove(game.ali);
    }
}
