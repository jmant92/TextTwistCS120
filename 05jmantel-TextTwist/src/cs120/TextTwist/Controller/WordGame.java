package cs120.TextTwist.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import cs120.TextTwist.BackEnd.WordDictionary;

/**
 * The Word Game dealer class has its getters and setters but also:
 * constructs the game settings
 * resets the game
 * advances the level
 * finds out if a word has been used or is in the dictionary
 * generates new letters
 * shuffles those letters
 * decrements the timer
 * checks if the player can still play
 * or if the game is over
 * @author JonathanMantel
 *
 */

public class WordGame {
	private static int MAX = 10; // Maximum WORD SIZE
	private static int MIN = 3; // Minimum WORD SIZE

	private static int THRESHOLD = 10; // Number of points before advancing to the next level.

	private int timerMax; // Number of seconds

	private int points; // Number of points in the current game, starts at 0
	private int timer; // An integer representing the number of ticks left for the current level.
	private int lvlPoints; // The number of points gathered in the current level. Restarts at zero for each level.
	private int level; // Indicates the game level (ie, the number of characters per word) starts at MIN=3.
	private int lives; // represents the number of extra chances before game is over.
	private Random rand; // A handle on a random number generator.

	private ArrayList list; // A handle on a list of used/old successful words for which 
	// points were assigned. Invalid words not included.

	private String[] letters; // current array of letters (character or string) from which 
	// the player will create words.

	private boolean advanced;

	/**
	 * Initializes the current WordGame object to an appropriate initial state. 
	 * Creates and holds onto a new Random object (rand).
	 */
	public WordGame() {
		advanced = false; // you can not advance
		level = 1; // start at level 1
		MAX = (level+2); // MAX word length is 3
		MIN = level; // min word length is 1
		THRESHOLD = (MAX)*10; // must get 30 points to advance
		timerMax = (MAX)*60; // start off at 3 minutes
		list = new ArrayList(); 
		lives = 3; // 3 lives
		points = 0; // no points
		rand = new Random(); // new random
		rand.setSeed(97); // new seed
		this.restart(); // do restart
	}

	/**
	 *  GETTERS AND SETTERS
	 */

	public static void setMAX(int mAX) {
		MAX = mAX;
	}

	public boolean isAdvanced() {
		return advanced;
	}

	public static void setMIN(int mIN) {
		MIN = mIN;
	}

	public static void setTHRESHOLD(int tHRESHOLD) {
		THRESHOLD = tHRESHOLD;
	}

	public void setTimerMax(int timerMax) {
		this.timerMax = timerMax;
	}

	public void setAdvanced(boolean advanced) {
		this.advanced = advanced;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void setLvlPoints(int lvlPoints) {
		this.lvlPoints = lvlPoints;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setRand(Random rand) {
		this.rand = rand;
	}

	public void setList(ArrayList list) {
		this.list = list;
	}

	public void setLetters(String[] letters) {
		this.letters = letters;
	}

	public static int getMax() {
		return MAX;
	}

	public static int getMin() {
		return MIN;
	}

	public static int getThreshold() {
		return THRESHOLD;
	}

	public boolean getAdvanced() {
		return advanced;
	}

	public int getTimerMax() {
		return timerMax;
	}

	public int getPoints() {
		return points;
	}

	public int getTimer() {
		return timer;
	}

	public int getLvlPoints() {
		return lvlPoints;
	}

	public int getLevel() {
		return level;
	}

	public int getLives() {
		return lives;
	}

	public Random getRand() {
		return rand;
	}

	public ArrayList getList() {
		return list;
	}

	public String[] getLetters() {
		return letters;
	}

	/**
	 *	Initializes to the very beginning of the game.
	 */
	public void restart() {
		timer = timerMax; // timer set to max
		lvlPoints = 0; // no points on this level

		try {
			this.newLetters(); // get some new letters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		list.clear(); // clear the list
	}

	/**
	 * looks up specified word (w) in the used word list. If the word already exists 
	 * in the used word list, then throw an UsedWordException. Otherwise looks up the 
	 * specified word in the dictionary files.Returns true if the word is found in 
	 * the dictionary file, false otherwise. If the word is found, then increments the points
	 *  variables by the size of the word and add the word to the old word list. 
	 *  When the lvlPoints exceeds TRESHOLD then advance to the next level
	 * @throws Exception 
	 */
	public boolean testWord(String w) throws Exception {
		MAX = level+2; // make sure MAX is updated
		THRESHOLD = MAX*10; // make sure threshold is updated
		WordDictionary wd = new WordDictionary(); // Makes a word dictionary

		for(int i=0; i<list.size(); i++) { // go through the list to check if the word w has been used already
			if(w.equalsIgnoreCase(list.get(i).toString())) throw new UsedWordException(w);	
		}

		for(int i=1; i<=MAX; i++) { // go through every file up to the current level +2
			wd.setFile(new File(i+".txt")); // get the right file
			if(wd.wordInDictionary(w)) { // if the word is in a particular word dictionary
				lvlPoints+=10*w.length(); // increase level points
				points+=10*w.length(); // increase total points 
				list.add(w); // add the word to the list
				System.out.println("Adding to the list: "+w);
				System.out.println(w+" added. Level points: "+lvlPoints+" points. "+THRESHOLD+" is the threshold.");
				if(lvlPoints>THRESHOLD) { // if the level points are past the threshold
					System.out.println("ADVANCE");
					this.advance(); // advance
				}
				return true; // stop looking, you found it
			}
		}	
		return false; // the word could not be found
	}

	/**
	 * advances the game state to the next level (ie, throw away letters, increment level 
	 * counter, reset lvlPoints, generate new letters, etc). Resets the timer count.
	 * @throws Exception 
	 */
	public void advance() throws Exception {
		letters = null; // empty letter string
		list.clear(); // clear the list 
		this.newLetters(); // generate new letters 
		level++; // advance the level
		lvlPoints = 0; // start the level points over
		MAX = level+2; // set the MAX
		timerMax = MAX*60; // set the timerMax
		timer = timerMax; // set the timer
		THRESHOLD = (MAX)*10; // set the threshold
		advanced = true;  // make advanced true
		letters = new String[MAX]; // get a new array of letters with the appropriate amount of space
		
	}

	/**
	 * periodically this method is called (from presentation layer). 
	 * This causes the game to decrement its timer counter. If the timer counter goes 
	 * to zero, then the game forfeits a life and resets the current level and throws 
	 * a TimeoutException
	 * @throws TimeoutException
	 */
	public void decrementTimer() throws TimeoutException {
		timer--; // subtract 1 from the timer

		this.stayAlive(); // check to see if the player can continue without losing a life
	}

	/**
	 * This method determines if the player is losing a life or can continue with another set of letters
	 * penalty free
	 */
	public void stayAlive() {
		if(this.getTimer() == 0) { // has the timer run out?
			if(this.getLvlPoints()<0.6*10*(MAX)) { // if the player is below the minimum points needed
				lives--; // lose a life
			}
			this.restart(); // restart the level with a new set of letters
		}
	}

	/**
	 * ￼Select a n letter word from the dictionary and randomize.
	 * @return
	 * @throws Exception 
	 */
	public char[] newLetters() throws Exception {
		MAX = level+2;
		WordDictionary wd = new WordDictionary(); // new word dictionary
		wd.setFile(new File(MAX+".txt")); // get a word that is the current level +2 letters long
		int dic = MAX;

		int top = 0;
		// find the number of words in the dictionary. That is the highest number that can be chosen
		switch(dic) {
		case 1:
			top = 2;
			break;
		case 2:
			top = 96;
			break;
		case 3:
			top = 972;
			break;
		case 4:
			top = 3903;
			break;
		case 5:
			top = 8636;
			break;
		case 6:
			top = 15232;
			break;
		case 7:
			top = 23109;
			break;
		case 8:
			top = 28419;
			break;
		case 9:
			top = 24792;
			break;
		case 10:
			top = 20194;
			break;
		}

		int randNum = rand.nextInt(top); // random number
		String chosenWord = wd.fetchRandomWord(randNum+1); // a random word that won't be the first one
		letters = wd.parseLet(chosenWord); // gets parsed into a string array
		char[] newLet = new char[MAX]; // make an array of characters to copy those
		// letters over
	
		// copy the letters
		for(int i=0; i<this.MAX; i++) {	
			newLet[i] = letters[i].charAt(0);
		}

		return newLet;
	}

	/**
	 * returns true if the game is at level>MAX or the remaining lives is negative, 
	 * false otherwise.
	 * @return
	 */
	public boolean isGameOver() {
		if(level+2>10||lives<0) return true; // has the last level been beaten or has the player lost all of their lives
		return false;
	}

	/**
	 * Randomly shuffle the current letters
	 */
	public void shuffleLetters() {
		String tempLet = letters[0]; // string for temp storage
		int otherPlace = 0; // placement storage

		for(int i=0; i<MAX; i++) { // go through the whole list 
			otherPlace = rand.nextInt(MAX); // choose a random spot
			tempLet = letters[i]; // copy the letter it's on to the temp storage
			letters[i] = letters[otherPlace]; // place the random spot letter to the current letter spot
			letters[otherPlace] = tempLet; // place the current letter to the random letter spot
		}
	}
}
