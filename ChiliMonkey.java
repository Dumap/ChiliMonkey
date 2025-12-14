import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class ChiliMonkey extends JFrame {
	private final JPanel jpStatus;
	private final HighScore myHighScore;
	private final Target[] myTarget = new Target[4];
	private Vector<Target> targetVec;
	private Vector<Player> scoreVec;
	private Target2 bigTeeth;
	private JLabel ali;
	private final JLabel background;
	private final JLabel jlOne;
	private final boolean[] isHit = {false,false,false,false,false};
	private boolean left, right, up, down, isFired, out, go;
	private boolean hasGame;
	private boolean isStarted = false;
	private boolean isDead = false;
	private final int MAX = 5;
	private int numKill;
	private long startTime;
	private long endTime;
	private int score;
	private final ImageIcon teeth = new ImageIcon("img/ClackyTeeth2.gif");
	private final ImageIcon monkey = new ImageIcon("img/MOHNkee.gif");
	private final ImageIcon chili = new ImageIcon("img/CHILI.GIF");
	private final ImageIcon alien = new ImageIcon("img/Alien.gif");
	private final ImageIcon mis = new ImageIcon("img/missileXYZ.gif");
	private final ImageIcon banana = new ImageIcon("img/banana.gif");
	private final ImageIcon boom = new ImageIcon("img/Explode.gif");

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenWidth = screenSize.width;
	int screenHeight = screenSize.height;

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		new ChiliMonkey();
	}

	public ChiliMonkey() {

		ImageIcon bigMonkey = new ImageIcon("img/bigMonkey2.gif");
		background = new JLabel(bigMonkey);
		background.setSize(500,500);
		jlOne = new JLabel();
		jpStatus = new JPanel();
		Color myColor = new Color(177, 189, 201);
		jpStatus.setBackground(myColor);
		jpStatus.setLayout(new GridLayout(1,1));
		jpStatus.add(jlOne);
		readFromFile();
		newGame();
		myHighScore = new HighScore();
		this.setTitle("ChiliMonkey");

		addKeyListener(new Klistener());

		JMenuItem miNew = new JMenuItem("New Game", KeyEvent.VK_N);
		miNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK));
		miNew.addActionListener(new MListener());

		JMenuItem miExit = new JMenuItem("Exit", KeyEvent.VK_E);
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
		miExit.addActionListener(new MListener2());

		JMenu mGame = new JMenu("Game");
		mGame.setBackground(myColor);
		mGame.setMnemonic(KeyEvent.VK_G);
		mGame.add(miNew);
		mGame.addSeparator();
		mGame.add(miExit);

		JMenuBar mb = new JMenuBar();
		mb.setBackground(myColor);
		mb.add(mGame);

		this.setJMenuBar(mb);
		setSize(500,520);
		Dimension frameSize = this.getSize();
		int x = (screenWidth - frameSize.width) / 2;
		int y =(screenHeight - frameSize.height) / 2;
		setLocation(x,y); //center frame
		setResizable(false);
		setVisible(true);
		Image icon = Toolkit.getDefaultToolkit().getImage("img/MOHNkee.gif");
		this.setIconImage(icon);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitListener());

		hasGame = true;

	}
	public void newGame() {
		if (hasGame) {
			clear();
		}

		left = false;
		right = false;
		up = false;
		down = false;
		isFired = false;
		out = false;
		numKill = 0;
		isDead = false;

		targetVec = new Vector<>();

		myTarget[0] = new Target(55, chili, 8, 450, 0);
		myTarget[0].setBounds(0,55,45,45);
		targetVec.add(myTarget[0]);
		myTarget[1] = new Target(105, monkey, 6, 460, 1);
		myTarget[1].setBounds(0,105,35,35);
		targetVec.add(myTarget[1]);
		myTarget[2] = new Target(155, chili, 3, 450, 2);
		myTarget[2].setBounds(0,155,45,45);
		targetVec.add(myTarget[2]);
		myTarget[3] = new Target(205, monkey, 4, 460, 3);
		myTarget[3].setBounds(0,205,45,45);
		targetVec.add(myTarget[3]);
		bigTeeth = new Target2(15, teeth, 2, 540, 4);
		bigTeeth.setBounds(0,15,30,30);
		targetVec.add(bigTeeth);
		ali = new JLabel(alien);
		ali.setBounds(230,405,35,35);
		ali.setVisible(true);
		Container c = getContentPane();

		jlOne.setText(" Press Enter to Start");
		c.add(background,BorderLayout.CENTER);
		c.add(jpStatus,BorderLayout.SOUTH);

		background.add(myTarget[0]);
		background.add(myTarget[1]);
		background.add(myTarget[2]);
		background.add(myTarget[3]);
		background.add(bigTeeth);
		background.add(ali);
		background.repaint();

		hasGame = true;
	}
	public void start() {
		Calendar calendar = new GregorianCalendar();
		startTime = calendar.getTimeInMillis();
		Arrays.fill(isHit, false);
		go = true;
		Thread myThread = new Thread(myTarget[0]);
		Thread myThread2 = new Thread(myTarget[1]);
		Thread myThread3 = new Thread(myTarget[2]);
		Thread myThread4 = new Thread(myTarget[3]);
		Thread myThread5 = new Thread(bigTeeth);
		ShipMover sm = new ShipMover();
		myThread.start();
		myThread2.start();
		myThread3.start();
		myThread4.start();
		myThread5.start();
		sm.start();
		jlOne.setText(" Spacebar to fire.   Arrows to navigate");
		isStarted = true;
	}
	public void end() {
		Calendar calendar = new GregorianCalendar();
		endTime = calendar.getTimeInMillis();
		String duration = returnDiff();
		try {
			Thread.sleep(1000);
		} catch(InterruptedException iex) {
			System.out.println("Interrupted");
		}
		clear();
		if (!isDead) {
			JOptionPane.showMessageDialog(null, "You killed them all in "+ duration + "\n Your score is "+ score,
				"Game Over", JOptionPane.INFORMATION_MESSAGE);
			checkScore();
		} else {
			JOptionPane.showMessageDialog(null, "         GAME OVER ! \n       Your score is 0", "ChiliMonkey", JOptionPane.INFORMATION_MESSAGE);
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
		for (int x=0; x<targetVec.size();x++) {
			JLabel temp = targetVec.elementAt(x);
			isHit[x] = true;
			temp.setVisible(false);
		}
		jlOne.setText(" Alt + N for New Game    Alt + E to Exit");

	}
	public String returnDiff() {
		String retVal = "";
		long milliDiff = endTime - startTime;
		long diff = milliDiff / 1000;
		score = 60000 - (int)milliDiff;
		if(score < 0){score = 0;}
		System.out.println(milliDiff);
		System.out.println(score);
		long min = diff / 60;
		long hour = min / 60;
		long sec = diff % 60;

		if (hour > 0) {
			if (hour == 1) {
				retVal = hour + " hour, ";
			} else {
				retVal = hour + " hours, ";
			}
		} else if (min > 0) {
			if (min == 1) {
				retVal = retVal + min + " minute, ";
			} else {
				retVal = retVal + min + " minutes, ";
			}
		}
		if (sec == 1) {
			retVal = retVal + sec + " second, ";
		} else {
			retVal = retVal + sec + " seconds ";
		}
		return retVal;
	}
	public void readFromFile() {
		scoreVec = new Vector<>();
		StringBuilder sbText = new StringBuilder();

		try {
			int c;
			File f = new File("HighScore.txt");
			FileReader in = new FileReader(f);
			while ((c = in.read()) != -1) {
				sbText.append((char) c);
			}

			in.close();
			String text = ""+sbText;
			StringTokenizer st = new StringTokenizer(text,"\n");
			while (st.hasMoreTokens()) {
				Vector<String> temp = new Vector<>();
				StringTokenizer st2 = new StringTokenizer(st.nextToken(), "|");
				while (st2.hasMoreTokens()) {
					temp.add(st2.nextToken());
				}

				Player play = new Player(temp.elementAt(0), Integer.parseInt(temp.elementAt(1)));
				System.out.println("Name: " + play.name() + " Score " + play.score());
				scoreVec.add(play);
			}
		} catch (Exception ex) {
			for (int x=0; x<MAX; x++) {
				Player play = new Player("", 0);
				scoreVec.add(play);
			}
		}
	}
	public void WriteToFile() {
		System.out.println("Saved");
		StringBuilder text = new StringBuilder();
		for (int x=0; x<MAX; x++) {
			Player temp = scoreVec.elementAt(x);
			text.append(temp.name()).append("|").append(temp.score()).append("\n");
		}

		text = new StringBuilder(text.substring(0, text.length() - 1));

		try {
			File f = new File("HighScore.txt");
			FileWriter out = new FileWriter(f, false);
			for(int x=0; x<text.length(); x++){
				out.write(text.charAt(x));
			}
			out.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Could not locate this file.");
		} catch (IOException ioe) {
			System.out.println("Input Output error, sorry.");
		}
	}
	public void checkScore() {
		boolean isHigher = false;
		for (int x=0;x<MAX;x++) {
			Player temp = scoreVec.elementAt(x);
			if (score > temp.score()) {
				isHigher = true;
				break;
			}
		}
		if (isHigher) {
			String name;
			try {
				name = JOptionPane.showInputDialog("YOU'VE MADE THE TOP 5 !! \n Please enter your name:");
				if (name.equals("")) {
					name = "Anonymous";
				}
			} catch (NullPointerException npe) {
				name = "Anonymous";
				System.out.println(npe.getMessage());
			}

			for (int x=0; x<MAX; x++) {
				Player temp = scoreVec.elementAt(x);
				if (score > temp.score()) {
					Player newPlayer = new Player(name, score);
					scoreVec.insertElementAt(newPlayer,x);
					break;
				}
			}

			for (int x=0; x<MAX; x++) {
				Player temp = scoreVec.elementAt(x);
				System.out.println("player "+x+" score: "+temp.score()+"name: "+temp.name());
				myHighScore.names[x].setText(temp.name());
				myHighScore.score[x].setText(""+temp.score());
			}
		}
	}
	class ExitListener extends WindowAdapter {
	   	public void windowClosing(WindowEvent e) {
		   	WriteToFile();
			System.exit(0);
		}
 	}
	public class MListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			newGame();
			isStarted = false;
			System.out.println("New Game");
		}
	}
	public class MListener2 implements ActionListener {
		public void actionPerformed(ActionEvent e)  {
			WriteToFile();
			System.exit(0);
		}
	}
	class Target extends JLabel implements Runnable {
		int x = 0;
		int ypos;
		int speed;
		int end;
		int index;
		public Target (int ypos, Icon ico, int speed, int end, int index) {
			this.speed = speed;
			this.ypos = ypos;
			this.end = end;
			this.index = index;
			setIcon(ico);
		}
		public void run() {
			int offsetX = this.getWidth() - 10;
			int offsetY = this.getHeight() - 10;
			while (!isHit[index]) {
				int wait = ((int)(Math.random() * 400)+20);
				System.out.println(wait);
				for (int i=0; i<end; i++) {
					if(isHit[index]){
						break;
					}

					moveLeft();
					try {
						Thread.sleep(speed);
					} catch (InterruptedException iex) {
						System.out.println("Interrupted");
					}

					if (this.getLocation().getX()+offsetX >= ali.getLocation().getX() &&
					this.getLocation().getX() <= ali.getLocation().getX()  &&
					this.getLocation().getY()+offsetY >= ali.getLocation().getY()&&
					this.getLocation().getY() <= ali.getLocation().getY()+30) {
						hit();
						break;
					}

					if (index == 1 || index == 3) {
						if (i == wait) {
							Banana myBanana = new Banana((int)this.getLocation().getX()+10,(int)this.getLocation().getY()+20);
							Thread myThread7 = new Thread(myBanana);
							myThread7.start();
						}
					}
				}

				for (int i=0; i<end; i++) {
					if (isHit[index]) {
						break;
					}
					moveRight();
					try {
						Thread.sleep(speed);
					} catch(InterruptedException iex) {
						System.out.println("Interrupted");
					}
					if (this.getLocation().getX() <= ali.getLocation().getX()+30 &&
					this.getLocation().getX()+offsetX >= ali.getLocation().getX()  &&
					this.getLocation().getY()+offsetY >= ali.getLocation().getY() &&
					this.getLocation().getY()<= ali.getLocation().getY()+30) {
						hit();
						break;
					}
					if (index == 1 || index == 3) {
						if (i == wait) {
							Banana myBanana = new Banana((int)this.getLocation().getX()+10,(int)this.getLocation().getY()+20);
							Thread myThread7 = new Thread(myBanana);
							myThread7.start();
						}
					}
				}

			}
			background.remove(this);
		}
		public void moveLeft() {
			if (!isHit[index]) {
				x++;
				setLocation(x,ypos);
			}
		}
		public void moveRight() {
			if (!isHit[index]) {
				x--;
				setLocation(x,ypos);
			}
		}
		public void hit() {
			System.out.println("Hit Alien");
			isDead = true;
			isHit[index] = true;
			go = false;
			this.setVisible(false);
			ali.setVisible(false);
			Explosion myExplosion = new Explosion((int)ali.getLocation().getX() - 10,(int)ali.getLocation().getY() - 10);
			Thread myThread7 = new Thread(myExplosion);
			myThread7.start();
			end();
		}
	}

	class Target2 extends Target implements Runnable {
			public Target2(int ypos, Icon ico, int speed, int end, int index) {
				super(ypos,ico,speed,end,index);
			}
			public void run() {
				while (!isHit[index]) {
					for (int i=0; i<end; i++) {
						moveLeft();
						try {
							Thread.sleep(speed);
						} catch(InterruptedException iex) {
							System.out.println("Interrupted");
						}
					}
					try {
						int wait = ((int)(Math.random() * 6)+2) * 1000;
						Thread.sleep(wait);
					} catch (InterruptedException iex) {
						System.out.println("Interrupted");
					}
					x = -20;

				}
				background.remove(this);
			}
			public void moveLeft() {
				if (!isHit[index]) {
					x++;
					setLocation(x,ypos);
				}
			}
		}

	public class Klistener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 37) {
				left = true;
				right = false;
			} else if(e.getKeyCode() == 39) {
				right = true;
				left = false;
			} else if(e.getKeyCode() == 38) {
				up = true;
				down = false;
			} else if(e.getKeyCode() == 40) {
				down = true;
				up = false;
			} else if (e.getKeyCode() == 32) {
				if (!isFired && go) {
					Missile myMissile = new Missile((int)ali.getLocation().getX()+10,(int)ali.getLocation().getY()-30);
					Thread myThread6 = new Thread(myMissile);
					myThread6.start();
					isFired = true;
				}
			} else if (e.getKeyCode() == 10) {
				if (!isStarted){
					start();
				}
			}
		}
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == 37) {
				left = false;
			} else if(e.getKeyCode() == 39) {
				right = false;
			} else if(e.getKeyCode() == 38) {
				up = false;

			} else if(e.getKeyCode() == 40) {
				down = false;
			}
		}
	}
	class ShipMover extends Thread{
		public void run() {
			while(go){
				if (left && ali.getLocation().getX() >0) {
					ali.setLocation((int)ali.getLocation().getX() - 1, (int)ali.getLocation().getY());
				}

				if (right && ali.getLocation().getX() <460) {
					ali.setLocation((int)ali.getLocation().getX() + 1, (int)ali.getLocation().getY());
				}

				if (up && ali.getLocation().getY() >170) {
					ali.setLocation((int)ali.getLocation().getX(), (int)ali.getLocation().getY() - 1);
				}

				if (down && ali.getLocation().getY() <410) {
					ali.setLocation((int)ali.getLocation().getX(), (int)ali.getLocation().getY() + 1);
				}

				try {
					Thread.sleep(8);
				} catch (InterruptedException iex) {
					System.out.println("Interrupted");
				}
			}
			background.remove(ali);
		}
	}
	class Missile extends JLabel implements Runnable{
		int xpos;
		int ypos;
		public Missile (int xpos, int ypos) {
			this.ypos = ypos;
			this.xpos = xpos;
			setIcon(mis);
		}
		public void run() {
			setBounds(xpos, ypos,18,50);
			background.add(this);
			while(this.getLocation().getY() > -50 && !out){
				setLocation(xpos, ypos -= 2);
				int x=0;
				try {
					Thread.sleep(4);
				} catch(InterruptedException iex) {
					System.out.println("Interrupted");
				}

				while (x < MAX) {
					JLabel temp = targetVec.elementAt(x);
					int offsetX = temp.getWidth();
					int offsetY = temp.getHeight();
					if(this.getLocation().getX()+5 >= temp.getLocation().getX() &&
					this.getLocation().getX()+5 <= temp.getLocation().getX() + offsetX &&
					this.getLocation().getY()-5 >= temp.getLocation().getY() &&
					this.getLocation().getY()-5 <= temp.getLocation().getY() + offsetY){
						if (!isHit[x]) {
							this.setVisible(false);
							temp.setVisible(false);
							isHit[x] = true;
							out = true;
							Explosion myExplosion = new Explosion((int)temp.getLocation().getX() - 10,(int)temp.getLocation().getY() - 10);
							Thread myThread7 = new Thread(myExplosion);
							myThread7.start();
							numKill++;
							x=5;
						}
					}
					x++;
				}

			}
			if (numKill == 5) {
				numKill = 0;
				end();
			}
			out = false;
			isFired = false;

		}
	}
	class Banana extends JLabel implements Runnable{
			int xpos;
			int ypos;
			boolean impact =false;
			public Banana (int xpos, int ypos) {
				this.ypos = ypos;
				this.xpos = xpos;
				setIcon(banana);
			}
			public void run() {
				setBounds(xpos, ypos,19,23);
				background.add(this);
				while(this.getLocation().getY() < 550 && !impact){
					setLocation(xpos, ypos += 2);
					try {
						Thread.sleep(4);
					} catch (InterruptedException iex) {
						System.out.println("Interrupted");
					}
					int offsetX = ali.getWidth();
					int offsetY = ali.getHeight();
					if (this.getLocation().getX()+15 >= ali.getLocation().getX() &&
					this.getLocation().getX() <= ali.getLocation().getX() + offsetX &&
					this.getLocation().getY()-20 >= ali.getLocation().getY() &&
					this.getLocation().getY()+5 <= ali.getLocation().getY() + offsetY) {
						isDead = true;
						go = false;
						impact = true;
						this.setVisible(false);
						ali.setVisible(false);
						Explosion myExplosion = new Explosion((int)ali.getLocation().getX() - 10,(int)ali.getLocation().getY() - 10);
						Thread myThread7 = new Thread(myExplosion);
						myThread7.start();
						end();
					}
				}
				background.remove(this);
			}
		}

	class Explosion extends JLabel implements Runnable {
		int xpos;
		int ypos;
		public Explosion (int xpos, int ypos) {
			this.ypos = ypos;
			this.xpos = xpos;
			this.setIcon(boom);
			this.setSize(100,100);
			background.add(this);
		}
		public void run() {
			this.setLocation(xpos + 10,ypos - 30);
			try {
				Thread.sleep(300);
			} catch (InterruptedException iex) {
				System.out.println("Interrupted");
			}
			setVisible(false);
		}
	}

	class HighScore extends JDialog{
		public JTextField[] names = new JTextField[5];
		public JTextField[] score = new JTextField[5];
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		public HighScore() {
			super(ChiliMonkey.this, "Hight Score", true);
			Container c2 = getContentPane();
			c2.setLayout(null);

			JLabel lbName = new JLabel("Name");
			lbName.setSize(100,20);
			lbName.setLocation(20,10);
			c2.add(lbName);

			JLabel lbScore = new JLabel("Score");
			lbScore.setSize(100,20);
			lbScore.setLocation(160,10);
			c2.add(lbScore);
			int h = 0;
			int l = 0;
			for (int x=0; x<names.length; x++) {
				Player temp = scoreVec.elementAt(x);
				names[x] = new JTextField();
				names[x].setEditable(false);
				names[x].setFocusable(true);
				names[x].setBackground(Color.white);
				names[x].setSize(120,20);
				names[x].setLocation(20, h+=30);
				names[x].setText(temp.name());
				c2.add(names[x]);

				score[x] = new JTextField();
				score[x].setEditable(false);
				score[x].setFocusable(true);
				score[x].setBackground(Color.white);
				score[x].setSize(120,20);
				score[x].setLocation(160,l+=30);
				score[x].setText(""+temp.score());
				c2.add(score[x]);

				JLabel[] lbRank = new JLabel[5];
				lbRank[x] = new JLabel((x+1) +".");
				lbRank[x].setSize(15,20);
				lbRank[x].setLocation(5, h);
				c2.add(lbRank[x]);
			}
			MyButton btExit = new MyButton(this);
			c2.add(btExit);

			setSize(300,250); //300 wide, 200 tall
			Dimension frameSize = this.getSize();
			int x = (screenWidth - frameSize.width) / 2;
			int y =(screenHeight - frameSize.height) / 2;
			setLocation(x,y); //center frame
			setResizable(false);
			setTitle("High Score");
		}
		class MyButton extends JButton implements ActionListener {
			private final JDialog temp;
			public MyButton(JDialog temp) {
				this.temp = temp;
				this.setText("OK");
				this.setBounds(220,185,60,25);
				this.grabFocus();
				this.addActionListener(this);
			}
			public void actionPerformed(ActionEvent ae) {
				temp.setVisible(false);
			}
		}
	}
	record Player (String name, int score) {

	}
}
