package cs120.TextTwist.BackEnd;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

/**
 * This is the set of Word Dictionary Tests. It covers initialization (finding the right
 * file)
 * @author JonathanMantel
 *
 */
public class WordDictionaryTest {

	/**
	 * This set of tests, checks if the word dictionary initializes properly and
	 */
	
	@Test
	public void inDictionaryTests() {
		WordDictionary wd;
		try {
			wd = new WordDictionary();
			
			// does it get the correct word entered?
			assertTrue(wd.wordInDictionary("I")); // Is I a valid word?
			assertFalse(wd.wordInDictionary("B")); // Is B a valid word?
			assertFalse(wd.wordInDictionary("null")); // How about null? will it read that?
			assertFalse(wd.wordInDictionary(0+"")); // numbers posing as strings?
			assertTrue(wd.wordInDictionary("Nope!")); // I entered Nope! as the first word
			// I expect it to be true because thats what I set the isWord String to be as
			// default. It's not, nor will be part of the dictionary of words because of the "!".
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This is the set of tests for entering a series of letters in a single string and
	 * returning all the possible combinations using those letters.
	 * @throws Exception
	 */
	@Test
	public void rtnWordsTests() {
		WordDictionary wd;
		try {
			wd = new WordDictionary();
			
			wd.setFile(new File("2.txt"));
			
			// Does the algorithm work?
			String[] testStr = wd.words("DO");
			assertArrayEquals(new String[]{"DO", "OD", null, null, null}, testStr);
			
			// Does it work for repeating letters
			testStr = null;
			testStr = wd.words("AA");
			assertArrayEquals(new String[]{"AA", null, null, null, null}, testStr);
			
			// Does the extending array algorithm work?
			wd.setFile(new File("6.txt")); // 6 letter words
			testStr = null; // reset the test string of letters
			testStr = wd.words("ABCDEF"); // use these words
			
			for(int i=0; i<testStr.length; i++) { // print out the words for fun
				System.out.println(testStr[i]);
			}
			
			assertTrue(testStr.length>5); // I know that it works on printing out the word
			// possibilities. Now I want to know if the array will successfully increase in size.
			// the default size is 5, so the array length should be larger than 5 if it works.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Checks to see if the dictionary can find a random word
	 */
	
	@Test
	public void randWordTests() {
		WordDictionary wd;
		try {
			wd = new WordDictionary();
			
			assertTrue("A".equalsIgnoreCase(wd.fetchRandomWord(1))); // A should be the first word
			assertTrue("I".equalsIgnoreCase(wd.fetchRandomWord(2))); // I should be the second word
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
