package cs120.TextTwist.Controller;
import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

/**
 * This is the set of tests for the Word Game Class.
 * 
 * @author JonathanMantel
 *
 */
public class WordGameTest {

	/**
	 * This set of tests is for testing if a particular word is in 
	 * the word dictionary chosen
	 */
	@Test
	public void inDictionaryTest() {
		WordGame w = new WordGame(); // a test wordgame object
		w.setLevel(3);
		w.setLvlPoints(0);
		
		try {
			assertTrue(w.testWord("CAT")); // does it get an acutal word?
			System.out.println("CAT Has been added");
			assertFalse(w.testWord("BBB")); // what about something that isn't a word
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			assertTrue(w.testWord("A")); // a word that doesn't have as many letters as the current level
			// is still acceptable
			System.out.println("A Has been added");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		assertTrue(w.getList().size()==2); // the word have been added to the list
		assertTrue("CAT".equalsIgnoreCase(w.getList().get(0).toString())); // the correct word has been added
		assertTrue("A".equalsIgnoreCase(w.getList().get(1).toString())); // the correct word has been added
		try {
			assertFalse(w.testWord("CAT")); // and it can't be added again
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * A set of tests that resets the game
	 */
	@Test
	public void resetTest() {
		WordGame w = new WordGame(); // a test wordgame object
		w.setLevel(4); // set it to a level so that the user won't advance immediately
		try {
			assertTrue(w.testWord("ABODE")); // five letter word in the dictionary
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue("ABODE".equalsIgnoreCase(w.getList().get(0).toString())); // it's the first word in the list
		w.restart();
		assertTrue(w.getList().isEmpty()); // it shouldn't be in the list now
	}

	
	/**
	 * A set of tests devised to check the advance() method. It moves the player up
	 * a level, reseting the level points and words used list in the process
	 */
	@Test
	public void advanceTests() {
		WordGame w = new WordGame();
		w.setPoints(WordGame.getThreshold()-1); // set the points to one less than the threshold
		w.setLvlPoints(WordGame.getThreshold()-1); // and level points to one less than the threshold
		w.setLevel(1);
		
		try {
			assertTrue(w.testWord("CAT")); // if the word CAT is available,
			// it should add the points to advance the game one level.

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// advance clears the list so the size should be 0
		assertTrue(w.getList().size()==0);
		
		assertTrue(w.getLevel()==2); // the threshold of level points should be 
		// enough to bring the user to level 4
	}
	
	
	/**
	 * A simple test to see if the timer works correctly
	 */
	@Test
	public void decrementTimerTests() {
		WordGame w = new WordGame();
		w.setLives(3);
		w.setTimer(1);
		w.setLvlPoints(10);
		
		try {
			w.decrementTimer(); // go from 1 tick left to 0 ticks then reset the level
			// and lose a life
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(w.getTimer()==w.getTimerMax()); // the level starts over so does the time
		try {
			w.decrementTimer(); // one less tick
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(w.getTimer()==w.getTimerMax()-1); // one tick passed
		assertTrue(w.getLives()==2); // 2 lives left
		
		w.setLvlPoints(30); // set the level points to the minimum
		w.setTimer(1); // set the timer to one second left
		try {
			w.decrementTimer(); // subtract 1 from the timer
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(w.getLives()==2); // the lives should be the same because the user has the minimum amount to
		// continue without losing a life
	}
	
	/**
	 * This set of tests checks to see if an array of letters 
	 * can be returned properly.
	 */
	@Test
	public void newLetterRandTest() {
		WordGame w = new WordGame();
		w.setLevel(5); // five letter words
		Random r = new Random(); // seed 7
		r.setSeed(7);
		w.setRand(r); // set the random seed to be this random
		
		char[] letters;
		try {
			letters = w.newLetters(); // lets get some new letters
			// it should be a "Random" word
			
			System.out.println();
			System.out.println("The word is now: ");
			for(int i=0; i<letters.length; i++) { // says the word
				System.out.print(w.getLetters()[i]); 
			}
			
			// Is it the right word?
			assertArrayEquals("RIDDLED", new String[]{"R", "I", "D", "D", "L", "E", "D"}, w.getLetters());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * This set of tests checks to see if the shuffle method works.
	 */
	@Test
	public void randLetterTests() {
		WordGame w = new WordGame(); // test wordgame object
		w.setLevel(5); // 5 letter words
		Random r = new Random(); // new random number generator
		r.setSeed(17);  // seed 17
		w.setRand(r); // use that seed
		
		char[] letters;
		try {
			letters = w.newLetters(); // lets get some letters
			// it gets a "Random" word
			
			System.out.println("The word is: ");
			for(int i=0; i<letters.length; i++) { // says the word
				System.out.print(w.getLetters()[i]);
			}
			
			assertArrayEquals("NOWNESS", new String[]{"N", "O", "W", "N", "E", "S", "S"}, w.getLetters());
			
			// shuffles it "Randomly"
			w.shuffleLetters();
			
			System.out.println();
			System.out.println("The word is now: ");
			for(int i=0; i<letters.length; i++) { // says the word mixed up
				System.out.print(w.getLetters()[i]);
			}
			
			// and returns the letters in a "Random" order
			assertArrayEquals("SWNSONE", new String[]{"S", "W", "N", "S", "O", "N", "E"}, w.getLetters());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * A test to check if the WordGame Object can end the game properly
	 */
	@Test
	public void endGameTests() {
		WordGame w = new WordGame();
		
		w.setLives(-1); // Player has lost their last life
		assertTrue(w.isGameOver()); // the game is over
		
		w.setLives(3); // the user has lives left 
		w.setLevel(11); // and have beaten the last level
		
		assertTrue(w.isGameOver()); // the game is over
	}
}
