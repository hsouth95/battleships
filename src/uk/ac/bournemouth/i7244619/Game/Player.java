package uk.ac.bournemouth.i7244619.Game;

/**
 * Represents the possible players in a game, can update to have more than two players.
 * 
 * @author Harrison
 *
 */
public enum Player {
	ONE, TWO;
	public Player getOppositePlayer() {
		switch (this) {
			case ONE:
				return TWO;
			case TWO:
				return ONE;
			default:
				return null;
		}
	}
}
