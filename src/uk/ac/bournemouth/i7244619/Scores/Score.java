package uk.ac.bournemouth.i7244619.Scores;

/**
 * Represents a possible score with a user holding a value for their score.
 * 
 * @author Harrison - i7244619
 *
 */
public class Score {

	private int value;
	private String user;


	public Score(int value, String user) {
		this.value = value;
		this.user = user;
	}


	public int getValue() {
		return value;
	}


	public String getUser() {
		return user;
	}



}
