package cs120.TextTwist.PresentationLayer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import cs120.TextTwist.Controller.UsedWordException;
import cs120.TextTwist.Controller.WordGame;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.util.concurrent.TimeoutException;

import javax.swing.JLabel;

/**
 * The main class file. It starts up the TextTwist game and passes down its information to the 
 * word game class. Most of what this layer does is handle the looks of the interface and updates it when
 * the values are passed back to it from the back end of the game.
 * @author JonathanMantel
 *
 */

public class MainFrame extends JFrame {
	private WordGame w; // a wordgame object to be used in the game
	
	private JScrollPane wordList; // this will show all of the words used
	private JList list; // this will show all of the words used
	private DefaultListModel listModel; // a default list model so that words can be added to
	// the JList
	
	private JPanel panel; // main panel
	private JPanel panelDecoration; // decoration panel, holds the level the player is on, lives, and a separator
	private JLabel lblLvl; // tells the player the level they are on
	private JLabel lblLives; // shows lives left
	private JLabel lblSpace; // separates the level from the lives left
	
	private JPanel panelGuess; // panel for holding the word the user guessed
	private JTextField txtGuess; // holds the word the user guessed
	
	private JPanel panelLetters; // holds the letter buttons
	private JButton[] letters; // an array of buttons that will act as the letters
	
	private JPanel panelButtons; // holds the buttons for various actions
	private JButton twist; // mixes the letters around
	private JButton enter; // enters the word
	private JButton lastWord; // button is a short cut for retyping the same letters as the last word in the same order.
	// This allows a user to quickly add a suffix.
	private JButton clear; // clears the word from the guess text field
	private JButton refresh; // a refresh button signaling the start of the game
	
	private JPanel panelDashboard; // a panel to hold the points and time
	private JLabel points; // a label for the points
	private JTextField txtPoints; // text field to show the points
	private JLabel lblTime; // a label for the time
	private JTextField txtTime; // a text field to show the points
	
	private JPanel panelExit; // the panel that holds the exit button
	private JButton quit; // quit the game;
	
	private Timer t; // a timer for the game
	private JLabel lblBeginMsg; // a beginning message on how to start
	
	private final int MAX_WIDTH = 800; // max width of the screen
	private final int MAX_HEIGHT = 800; // max height of the screen
	
	private String prevWord; // holds onto the previous word
	
	/**
	 *  Constructor. Constructs the wordGame controller and the entire game window.
	 */
	public MainFrame() {
		
		this.setSize(MAX_WIDTH, MAX_HEIGHT);
		
		w = new WordGame();
		
		listModel = new DefaultListModel();
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panelDecoration = new JPanel();
		panel.add(panelDecoration);
		
		lblLvl = new JLabel("Level "+w.getLevel());
		panelDecoration.add(lblLvl);
		
		lblSpace = new JLabel("               ");
		panelDecoration.add(lblSpace);
		
		lblLives = new JLabel("Lives: "+w.getLives());
		panelDecoration.add(lblLives);
		
		panelGuess = new JPanel();
		panel.add(panelGuess);
		
		txtGuess = new JTextField();
		txtGuess.setHorizontalAlignment(SwingConstants.CENTER);
		txtGuess.setEnabled(false);
		txtGuess.setEditable(false);
		txtGuess.setText("Guess");
		panelGuess.add(txtGuess);
		txtGuess.setColumns(15);
		
		panelLetters = new JPanel();
		panel.add(panelLetters);
		
		letters = new JButton[10]; // The random letters
		for(int i=0; i<letters.length; i++) { // make, place, and set visible the necessary buttons
			letters[i]=new JButton("X"); // make a JButton with an "X" on it
			panelLetters.add(letters[i]); // add it to the panel
			if(i>=w.getLevel()+2) letters[i].setVisible(false); // as long as it isn't above the current level
			letters[i].addActionListener(new ActionListener() { // make an action listener
				@Override
				public void actionPerformed(ActionEvent e) {
					for(JButton b: letters) { // go through every button
						if(e.getSource()==b) { // if the source matches the button
							addLetter(b); // add letter method
						}
					}
				}
			});
		}
		
		panelButtons = new JPanel();
		panel.add(panelButtons);
		
		twist = new JButton("Twist");
		twist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				twist();
			}
		});
		panelButtons.add(twist);
		
		enter = new JButton("Enter");
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enter();
			}
		});
		panelButtons.add(enter);
		
		lastWord = new JButton("Last Word");
		lastWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lastWord();
			}
		});
		panelButtons.add(lastWord);
		
		prevWord = "";
		
		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		panelButtons.add(clear);
		
		refresh = new JButton("Refresh");
		panelButtons.add(refresh);
		refresh.setAlignmentY(0.0f);
		
		lblBeginMsg = new JLabel("Press Refresh to Start!");
		panelButtons.add(lblBeginMsg);
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshButton();
			}
		});
		
		panelDashboard = new JPanel();
		panel.add(panelDashboard);
		panelDashboard.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		points = new JLabel("Points");
		points.setHorizontalAlignment(SwingConstants.LEFT);
		panelDashboard.add(points);
		
		txtPoints = new JTextField("0");
		txtPoints.setEnabled(false);
		txtPoints.setEditable(false);
		panelDashboard.add(txtPoints);
		txtPoints.setColumns(10);
		
		lblTime = new JLabel("Time");
		panelDashboard.add(lblTime);
		
		txtTime = new JTextField();
		txtTime.setEditable(false);
		txtTime.setEnabled(false);
		int min = w.getTimer()/60;
		int sec = w.getTimer()%60;
		if(sec<10) {
			txtTime.setText(min+":0"+sec);
		} else {
			txtTime.setText(min+":"+sec);
		}
		panelDashboard.add(txtTime);
		txtTime.setColumns(10);
		
		panelExit = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelExit.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.add(panelExit);
		
		quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quit");
				System.exit(0);
			}
		});
		quit.setHorizontalAlignment(SwingConstants.TRAILING);
		panelExit.add(quit);
		
		list = new JList(listModel);
		
		wordList = new JScrollPane(list);
		getContentPane().add(wordList, BorderLayout.WEST);
		wordList.setMinimumSize(new Dimension(MAX_WIDTH/3, MAX_HEIGHT));
		wordList.setPreferredSize(new Dimension(MAX_WIDTH/3, MAX_HEIGHT));
		wordList.setMaximumSize(new Dimension(MAX_WIDTH/3, MAX_HEIGHT));
		wordList.setViewportView(list);
		
		t = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer();
			}
		});
	}
	
	
	/**
	 * This starts off the game, gives new letters, shuffles those letters, clears the guess field
	 * and makes the start message disappear
	 */
	public void refreshButton() {
		try {
			w.newLetters(); // the letters being used
			w.shuffleLetters(); // shuffle them
			String[] rtnLetters = w.getLetters(); // new set of letters to use
			
			for(int i=0; i<rtnLetters.length; i++) {
				letters[i].setText(rtnLetters[i]); // set the button text to the appropriate letter
			}
			this.clear(); // clear the "Guess" at the beginning
			this.lblBeginMsg.setVisible(false); // make the start message invisible
			
			t.start(); // start the timer
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This returns the last word entered into the text field to the text field.
	 */
	public void lastWord() {
		txtGuess.setText(prevWord); // set the previous word to be placed inside the text field
		for(JButton b: letters) { // for every button b in letters
			for(int i=0; i<prevWord.length(); i++) { // go through every letter in prevWord
				if(prevWord.substring(i, i+1).equalsIgnoreCase(letters[i].getText())) letters[i].setEnabled(false);
			}
		}
	}
	
	/**
	 * Clears the current guess
	 */
	public void clear() {
		txtGuess.setText(""); // empty the field
		
		for(JButton b: letters) {
			b.setEnabled(true); // enable all buttons
		}
	}
	
	
	/**
	 * This method handles what happens when theplayer presses enter. It tests to see if the word they entered is an okay word
	 * reenables the letter buttons
	 * clears the guess text field
	 * updates the points
	 * and the level
	 * and the buttons
	 */
	public void enter() {
		prevWord = txtGuess.getText(); // get the word entered
		try {
			if(w.testWord(txtGuess.getText())) { // was the word entered into the txt field a valid word?
				listModel.addElement(txtGuess.getText()); // if so add it to the list on the side
				for(JButton b: letters) { // enable all the buttons
					b.setEnabled(true);
				}
			}
		} catch (Exception e) {
			txtGuess.setText("ERROR WORD USED: "+txtGuess.getText());
		}
		
		System.out.println("The max number of letters is: "+w.getLetters().length);
		System.out.println("The max is: "+WordGame.getMax());
		if(w.getAdvanced()) { // if the player has advanced
			w.setAdvanced(false); // set it back to false
			lblLvl.setText("Level "+w.getLevel()); // change the level
			try {
				w.newLetters(); // get new letters
				w.shuffleLetters(); // shuffle them
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0; i<w.getLetters().length; i++) { // make, place, and set visible the necessary buttons
				letters[i].setText(w.getLetters()[i]); // set the buttons to have those letters on them
				if(i>= w.getLetters().length) letters[i].setVisible(false); // as long as it isn't above the current level+2
				else letters[i].setVisible(true); // set the other letters visible
			}
			this.clockTime(); // do the fancy clock
		}
		txtPoints.setText(w.getPoints()+""); // give the points
		this.clear(); // clear the guess txt field
	}
	
	/**
	 * This method mixes up the words and resets the text
	 */
	public void twist() {
		this.clear(); // clear the guess area
		String[] rtnLetters = new String[WordGame.getMax()]; // a new string of the appropriate length
		rtnLetters = w.getLetters(); // new letters
		System.out.println("The max number of letters is: "+w.getLetters().length);
		System.out.println("The number of letter buttons is: "+letters.length);
		w.shuffleLetters(); // shuffle
		for(int i=0; i<rtnLetters.length; i++) {
			System.out.println("Setting button to be the letter: "+letters[i].getText());
			letters[i].setText(rtnLetters[i]); // set the text on all of the letter buttons
		}
	}
	
	/**
	 * This method is the timer method. It counts down 1 second per tick. It checks to see if the user
	 * needs to reset the letters,
	 * shows the time change
	 * changes the state of the refresh and enter buttons if the game is over and
	 * makes a pop up window that ends the game
	 */
	public void timer() {
		if(w.getTimer()==0) { // if time has run out
			this.refreshButton(); // do the same actions as refresh
			this.twist(); // mix up the letters
		}
		
		try {
			w.decrementTimer(); // decrement the timer by 1 second
			txtTime.setText(this.clockTime());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lblLives.setText("Lives: "+w.getLives()); // reset the lives label to show the change
		
		if(w.isGameOver()) { // if the game is over
			for(int i=0; i<letters.length; i++) letters[i].setVisible(false); // hide all the letter buttons
			enter.setEnabled(false); // disable the enter button
			refresh.setEnabled(false); // disable the refresh button
			
			// Show a window saying that the user lost.
			JOptionPane.showMessageDialog(panel,"Game Over", "Game Over!", JOptionPane.DEFAULT_OPTION);
			System.exit(0);
		}
	}
	
	/**
	 * Makes the timer fancy like a clock. Returns a string so the time txt field will show time remaining
	 * @return
	 */
	public String clockTime() {
		int min = w.getTimer()/60; // get the minutes
		int sec = w.getTimer()%60; // get the seconds 
		if(sec<10) { // if the seconds are less than 10
			return min+":0"+sec; // put a 0 in front so the time doesn't look weird
		} else { // otherwise
			return min+":"+sec; // write the time as is
		}
	}
	
	/**
	 * This adds a letter to the guess txt field as well as disable the letter button to prevent it from being used again
	 * @param b
	 */
	public void addLetter(JButton b) {
		b.setEnabled(false); // disable the button
		txtGuess.setText(txtGuess.getText()+b.getText()); // add that onto the text
	}
	
	/**
	 * Starts things off
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mf.setVisible(true);
	}

}
