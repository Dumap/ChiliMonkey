package game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import listeners.KListener;
import listeners.MListener;
import listeners.MListener2;
import listeners.ExitListener;

public class ChiliMonkey extends JFrame {

	public final JLabel ali;
	private final JLabel background;
	private final JLabel jlOne;

	public final ImageIcon teeth = new ImageIcon("img/ClackyTeeth2.gif");
	public final ImageIcon monkey = new ImageIcon("img/MOHNkee.gif");
	public final ImageIcon chili = new ImageIcon("img/CHILI.GIF");
	public final ImageIcon alien = new ImageIcon("img/Alien.gif");
	public final ImageIcon mis = new ImageIcon("img/missileXYZ.gif");
	public final ImageIcon banana = new ImageIcon("img/banana.gif");
	public final ImageIcon boom = new ImageIcon("img/Explode.gif");

	public final int MAX = 5;
	public boolean left, right, up, down, isFired, out, go;
	public boolean isStarted = false;
	public boolean isDead = false;
	public boolean[] isHit = new boolean[MAX];

	public int numKill = 0;
	public int score = 0;
	private long startTime;
	private long endTime;

	public Vector<Target> targetVec;
	public Target[] myTarget = new Target[4];
	public Target2 bigTeeth;

	public Vector<Player> scoreVec;
	public HighScore myHighScore;

	public boolean hasGame;

	public static void main(String[] args) {
		new ChiliMonkey();
	}

	public ChiliMonkey() {
		// basic setup
		setTitle("Chili MOHNkee");
		setSize(500, 520);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// main background
		ImageIcon bigMonkey = new ImageIcon("img/bigMonkey2.gif");
		background = new JLabel(bigMonkey);
		background.setSize(500, 500);
		add(background, BorderLayout.CENTER);

		// status panel
		JPanel jpStatus = new JPanel();
		jpStatus.setLayout(new GridLayout(1,1));
		jpStatus.setBackground(new Color(177, 189, 201));
		jlOne = new JLabel("Press Enter to Start");
		jpStatus.add(jlOne);
		add(jpStatus, BorderLayout.SOUTH);

		ali = new JLabel(alien);
		ali.setBounds(230, 405, 35, 35);

		readFromFile();
		newGame();
		myHighScore = new HighScore(this);

		// menu
		JMenuItem miNew = new JMenuItem("New Game", KeyEvent.VK_N);
		miNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK));
		miNew.addActionListener(new MListener(this));

		JMenuItem miExit = new JMenuItem("Exit", KeyEvent.VK_E);
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
		miExit.addActionListener(new MListener2(this));

		JMenu mGame = new JMenu("Game");
		mGame.setMnemonic(KeyEvent.VK_G);
		mGame.add(miNew);
		mGame.addSeparator();
		mGame.add(miExit);

		JMenuBar mb = new JMenuBar();
		mb.add(mGame);
		setJMenuBar(mb);

		// listeners
		addKeyListener(new KListener(this));
		addWindowListener(new ExitListener(this));

		// center frame
		setLocationRelativeTo(null);
		setVisible(true);

		hasGame = true;
	}

	public void newGame() {
		if (hasGame) clear();

		left = right = up = down = isFired = out = false;
		numKill = 0;
		isDead = false;
		Arrays.fill(isHit, false);

		targetVec = new Vector<>();
		myTarget[0] = new Target(this, 55, chili, 8, 450, 0);
		myTarget[1] = new Target(this, 105, monkey, 6, 460, 1);
		myTarget[2] = new Target(this, 155, chili, 3, 450, 2);
		myTarget[3] = new Target(this, 205, monkey, 4, 460, 3);
		bigTeeth = new Target2(this, 15, teeth, 2, 540, 4);

		targetVec.addAll(Arrays.asList(myTarget));
		targetVec.add(bigTeeth);

		for (Target t : targetVec) background.add(t);
		background.add(ali);
		ali.setBounds(230, 405, 35, 35);
		ali.setVisible(true);

		jlOne.setText("Press Enter to Start");
		jlOne.setHorizontalAlignment(SwingConstants.CENTER);
		jlOne.setBorder(new EmptyBorder(3,0,3,0));
		hasGame = true;
	}

	public void start() {
		startTime = System.currentTimeMillis();
		go = true;
		Arrays.fill(isHit, false);

		new Thread(myTarget[0]).start();
		new Thread(myTarget[1]).start();
		new Thread(myTarget[2]).start();
		new Thread(myTarget[3]).start();
		new Thread(bigTeeth).start();
		new ShipMover(this).start();

		jlOne.setText("  Spacebar to fire. Arrows to navigate");
		isStarted = true;
	}

	public void end() {
		endTime = System.currentTimeMillis();
		clear();

		if (!isDead) {
			JOptionPane.showMessageDialog(
					null,
					"You killed them all in " + getTimeDiff() + "\nYour score is " + score,
					"Game Over",
					JOptionPane.INFORMATION_MESSAGE
			);
			checkScore();
		} else {
			JOptionPane.showMessageDialog(
					null,
					"GAME OVER! Your score is 0",
					"Game Over",
					JOptionPane.INFORMATION_MESSAGE
			);
		}

		myHighScore.setVisible(true);
		hasGame = false;
	}

	public void clear() {
		ali.setVisible(false);
		go = false;
		background.remove(ali);
		bigTeeth.setVisible(false);
		background.remove(bigTeeth);

		for (JLabel t : targetVec) {
			t.setVisible(false);
		}
	}

	public String getTimeDiff() {
		long diffMillis = endTime - startTime;
		score = Math.max(0, 60000 - (int) diffMillis);
		long sec = (diffMillis / 1000) % 60;
		long min = (diffMillis / 60000) % 60;
		long hour = diffMillis / 3600000;

		StringBuilder sb = new StringBuilder();
		if (hour > 0) sb.append(hour).append(" hour(s), ");
		if (min > 0) sb.append(min).append(" minute(s), ");
		sb.append(sec).append(" second(s)");
		return sb.toString();
	}

	// -----------------------
	// High score logic
	// -----------------------
	public void readFromFile() {
		scoreVec = new Vector<>();
		try {
			File f = new File("HighScore.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\|");
				scoreVec.add(new Player(parts[0], Integer.parseInt(parts[1])));
			}
			br.close();
		} catch (Exception e) {
			for (int i = 0; i < MAX; i++) scoreVec.add(new Player("", 0));
		}
	}

	public void writeToFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("HighScore.txt"));
			for (Player p : scoreVec) {
				bw.write(p.name() + "|" + p.score());
				bw.newLine();
			}
			bw.close();
		} catch (IOException ignored) {}
	}

	public void checkScore() {
		boolean higher = false;
		for (Player p : scoreVec) {
			if (score > p.score()) {
				higher = true;
				break;
			}
		}

		if (higher) {
			String name = JOptionPane.showInputDialog("YOU'VE MADE THE TOP 5! Enter your name:");
			if (name == null || name.isEmpty()) name = "Anonymous";

			for (int i = 0; i < MAX; i++) {
				if (score > scoreVec.get(i).score()) {
					scoreVec.add(i, new Player(name, score));
					break;
				}
			}

			// trim to MAX
			while (scoreVec.size() > MAX) scoreVec.remove(scoreVec.size() - 1);

			// update HighScore dialog
			myHighScore.updateTable(scoreVec);
			writeToFile();
		}
	}

	// -----------------------
	// Utility methods
	// -----------------------
	public JLabel getGameBackground() {
		return background;
	}

}
