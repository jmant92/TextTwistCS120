package cs120.TextTwist.BackEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is the Word Dictionary class. It:
 * constructs the file 
 * has a getter and setter for the file 
 * it can get the file name
 * tell whether a word is in the word dictionary file
 * it has a helper method to parse letters of a single string as well
 * as another helper method to count the repetition of each letter in a letter string
 * these two methods are for the method that returns all possible words that can be made with those
 * letters. (All of those letters must be used)
 * It also has a method to fetch a random word from the word dictionary file
 * 
 * @author JonathanMantel
 *
 */

public class WordDictionary {
	File file;

	/**
	 * Construct the file. We can choose whatever file by using setFile method.
	 * @throws Exception
	 */
	public WordDictionary() throws Exception {
		file = new File(this.getFilename(1)); // Start at 1 letter words for the test
		// case, in the actual game, the file will be automatically set to 3 letter
		// words.
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	/**
	 * Returns the string filename for the correct file to use based on the number
	 * of words in the word letters n
	 * @param n
	 * @return
	 */
	public String getFilename(int n) {
		return (n+".txt");
	}

	/**
	 * Opens a FileReader stream on the correct file and prepares a BufferedReader
	 * on the FileReader stream. Then reads each word from the dictionary one line
	 * at a time. Compares the target word. If a match is found, return true. 
	 * Returns false if specified word is not found in the dictionary. Closes
	 * the file.
	 * @param word
	 * @return
	 * @throws IOException
	 */
	public boolean wordInDictionary(String word) throws IOException {
		String f = this.getFilename(word.length());

		BufferedReader br = null;
		String isWord = "Nope!"; // a third-party string so the buffered reader can close
		// ! was added because it's not a letter (Don't want nope to be the correct word)
		try {
			FileReader fr = new FileReader(this.file);
			br = new BufferedReader(fr);
			String str = br.readLine();
			while (str != null) {
				// process data in str
				str = br.readLine(); // start reading the next line
				if(word.equalsIgnoreCase(str)) { // if the buffered reader just so happens
					// to come across the word used
					isWord = str; // set the word in question to a third-party string
				}
			}
		}
		catch (IOException ex) {
			// handle exception here
			System.err.println("trouble with file :"+ex.getMessage());
		}

		finally {
			/*
			 ** attempt to close the file
			 */
			try {
				if (br != null) {
					br.close();
				}
			}
			catch (Exception ex1) {
				ex1.printStackTrace(); // give up
				System.exit(-1);
			}
		}

		// if the isWord is equal to the word in question, return true
		if(isWord.equalsIgnoreCase(word)) return true;
		return false; // otherwise false will be returned
	}

	/**
	 * Parses Strings with letters
	 */

	public String[] parseLet(String letters) {
		String[] indLet = new String[letters.length()]; // make a string array that is
		// the same length as the string 'letters' passed through
		for(int i=0; i<letters.length();i++) { // go through the entire 'letters' string
			indLet[i] = letters.substring(i, i+1); // and add each individual letter
			// to the indLet array
		}

		return indLet;
	}

	/**
	 * This is just a helper method to count how many times each letter appears.
	 * @param indLet
	 * @return
	 */
	public int[] numEachLetter(String[] indLet) {
		int[] howManyLetters = new int[indLet.length];
		for(int i=0; i<indLet.length; i++) { // go through the array of letters
			howManyLetters[i]=0; // start them all off at 0
			for(int j=0; j<indLet.length; j++) { // this is to compare all of the letters to the other letters in the array
				if(indLet[i].equalsIgnoreCase(indLet[j])) howManyLetters[i]++;
			}
		}
		// so what this previous algorithm basically says is, compare each letter to 
		// all of the other letters in the array. If you come across repeating letters
		// add 1 to the number in the corresponding index of the number times repeated array
		// since it starts at 0, the first time the letter appears it will automatically
		// go to one, if it doesn't appear again, then the integer array will move onto the next
		// index

		return howManyLetters;
	}

	/**
	 * Given the letters, returns all of the possible valid words with 
	 * the same letters from the current file. Returns null if no valid words
	 * @param letters
	 * @return
	 */
	public String[] words(String letters) {
		String[] indLet = this.parseLet(letters); // parse the letters passed into the method
		int[] howManyLetters = this.numEachLetter(indLet); // how many of each letter does the
		// string array hold

		String[] rtnWords = new String[5]; // make a guess as to how many of those matching words will be
		// found. This array will return the words that have those letters.
		int wdPlacement = 0;
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(this.file);
			br = new BufferedReader(fr);
			String str = br.readLine();
			
			while (str != null) {
				// process data in str
				str = br.readLine(); // start reading the next line
				if(str!=null) {
				for(int i=0; i<indLet.length; i++) {
						for(int j=0; j<str.length(); j++) {
							if(indLet[i].equalsIgnoreCase(str.substring(j, j+1))) { // lets see 
								// if the word being read has the letter.
								if(howManyLetters[i] == 0) { // is the number of times that letter
									// expected equal 0?
									indLet[i] = "Check!"; // if it does, replace the individual
								// letter from the indLet array with something that is 
								// obviously not a letter or rather something not even valid, 
								// like the word, Check!
								}
								
								howManyLetters[i]--; // subtract 1 from the times that letter
								// appears in the parsed array
								
								
								/*
								 * Of course, after we do this, the indexes of
								 * indLet will be useless
								 */
							}
						}
					
					// Now lets move onto the next letter in the array
				}
			}
				indLet = null; // lets make all of indLet useless!
				int tempSum = 0;
				for(int i=0; i<howManyLetters.length; i++) {
					tempSum+=howManyLetters[i];
					// going to get a sum of how many letters were not used
				}
				
				if(tempSum == 0) {
					if(wdPlacement<rtnWords.length) { // if the index is not out of bounds
						rtnWords[wdPlacement] = str; // add the word the buffered reader was on to the array
						// only if the number of letters left was 0
						wdPlacement++; // move to the next index if necessary of course
					} else { // otherwise
						// Make a temporary array to hold the old strings added already,
						// lengthen the previous array
						// copy them back to the main array
						String[] tempList = new String[rtnWords.length];
						for(int i=0; i<rtnWords.length; i++) {
							tempList[i]=rtnWords[i];
						}
						int newArraySize = wdPlacement;
						rtnWords = new String[newArraySize*=2]; // double the size of the array
						for(int i=0; i<tempList.length; i++) { // recopy the words back to the original
							rtnWords[i]=tempList[i]; // array
						}
						rtnWords[wdPlacement] = str; // add the word the buffered reader was on to the array
						// only if the number of letters left was 0
						wdPlacement++; // AND ONLY THEN move onto the next index
					}
				}
				howManyLetters = null;
				indLet = this.parseLet(letters); // reparse the letters again to get a useful array
				howManyLetters = this.numEachLetter(indLet); // renumber the letters
			}
		}
		catch (IOException ex) {
			// handle exception here
			System.err.println("trouble with file :"+ex.getMessage());
		}

		finally {
			/*
			 ** attempt to close the file
			 */
			try {
				if (br != null) {
					br.close();
				}
			}
			catch (Exception ex1) {
				ex1.printStackTrace(); // give up
				System.exit(-1);
			}
		}
		
		if(rtnWords.length>0) return rtnWords;
		else return (rtnWords=null);
	}

	/**
	 * Pass in a random word of size n
	 * @param n
	 * @return
	 */
	public String fetchRandomWord(int n) {
		BufferedReader br = null;
		String str = null; // the word being read
		int onWord = 0; // on the first word (Won't count the first line which has the number
		// of words
		try {
			FileReader fr = new FileReader(this.file);
			br = new BufferedReader(fr);
			str = br.readLine();
			while (onWord < n) { // while less than the random number chosen
				// process data in str
				str = br.readLine(); // start reading the next line
				onWord++; // move onto the next word
			}
		}
		catch (IOException ex) {
			// handle exception here
			System.err.println("trouble with file :"+ex.getMessage());
		}

		finally {
			/*
			 ** attempt to close the file
			 */
			try {
				if (br != null) {
					br.close();
				}
			}
			catch (Exception ex1) {
				ex1.printStackTrace(); // give up
				System.exit(-1);
			}
		}

		return str;
	}
}
