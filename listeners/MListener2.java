package listeners;

import game.ChiliMonkey;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MListener2 implements ActionListener {

    private final ChiliMonkey game;

    public MListener2(ChiliMonkey game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.writeToFile();  // save high scores
        System.exit(0);
    }
}
