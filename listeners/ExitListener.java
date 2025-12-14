package listeners;

import game.ChiliMonkey;

import java.awt.event.*;

public class ExitListener extends WindowAdapter {

    public ExitListener(ChiliMonkey game) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
