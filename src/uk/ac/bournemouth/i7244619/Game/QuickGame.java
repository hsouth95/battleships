package uk.ac.bournemouth.i7244619.Game;

import android.graphics.Point;
import uk.ac.bournemouth.i7244619.Board.MarkerType;

/**
 * The application of the quick game mode, where the player plays against one board, this class
 * adapts its parent class to represent this.
 * 
 * @author Harrison
 *
 */
public class QuickGame extends Game {

	private MarkerType player1LastMove;
	private boolean gameWon = false;

	public QuickGame(int columns, int rows) {
		super(columns, rows);
	}

	public QuickGame(int columns, int rows, int type) {
		super(columns, rows, type);
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public MarkerType getLastMove() {

		return player1LastMove;

	}

	private void updateLastMove(MarkerType marker) {
		player1LastMove = marker;
	}

	public boolean fire(int column, int row) {

		if (mPlayer1Data.isLegalPlacement(new Point(column, row))) {
			mPlayer1Data.placeMarker(new Point(column, row));

			updateLastMove(getMarkerAt(column, row, Player.ONE));

			if (mPlayer1Data.isWin()) {
				gameWon = true;
			}
			return true;
		}


		return false; // illegal move
	}

}
