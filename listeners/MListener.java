package listeners;

import game.ChiliMonkey;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MListener implements ActionListener {

    private final ChiliMonkey game;

    public MListener(ChiliMonkey game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.newGame();
        game.isStarted = false;
        System.out.println("New Game started");
    }
}
