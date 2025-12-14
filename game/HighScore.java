package game;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class HighScore extends JDialog {

    private final DefaultTableModel model;

    public HighScore(JFrame parent) {
        super(parent, "High Scores", true); // ✅ modal
        setLayout(new BorderLayout());
        setSize(300, 250);
        setResizable(false);
        setLocationRelativeTo(parent);

        // table
        String[] columns = {"Rank", "Name", "Score"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> setVisible(false));
        add(btnClose, BorderLayout.SOUTH);
    }

    public void updateTable(Vector<Player> scoreVec) {
        model.setRowCount(0);
        for (int i = 0; i < scoreVec.size(); i++) {
            Player p = scoreVec.get(i);
            model.addRow(new Object[]{i + 1, p.name(), p.score()});
        }
    }
}
