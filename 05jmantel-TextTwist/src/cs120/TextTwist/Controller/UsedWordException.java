package cs120.TextTwist.Controller;

public class UsedWordException extends Exception {

	public UsedWordException(String msg) {
		System.out.println("Repeat word: "+msg);
	}
}
