package uk.ac.bournemouth.i7244619.Game;

import android.graphics.Point;
import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Ship.Ship;

/**
 * This class represents the Player vs. Player situation, where the players will swap turns and hit
 * each others boards.
 * 
 * @author Harrison - i7244619
 *
 */
public class TwoPlayerGame extends Game {


	private MarkerType player1LastMove; // either hit or miss
	private MarkerType player2LastMove; // either hit or miss
	private boolean isOwnBoard = false;
	private boolean gameWon = false;


	public TwoPlayerGame() {
		super(Game.DEFAULT_COLUMNS, Game.DEFAULT_ROWS);
	}

	public TwoPlayerGame(int columns, int rows) {
		super(columns, rows);
	}

	public TwoPlayerGame(int columns, int rows, int type) {
		super(columns, rows, type);
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public MarkerType getLastMove(Player player) {

		if (player == Player.ONE) {
			return player1LastMove;
		} else if (player == Player.TWO) {
			return player2LastMove;
		} else {
			throw new IllegalArgumentException("Must be either player one or two.");
		}

	}

	private void updateLastMove(MarkerType marker, Player player) {

		if (player == Player.ONE) {
			player1LastMove = marker;
		} else {
			player2LastMove = marker;
		}

	}

	public void setShips(Ship[] ships, Player player) {

		if (player == Player.ONE) {
			mPlayer1Data.addShips(ships);
		} else {
			mPlayer2Data.addShips(ships);
		}

	}

	public void setIsOwnBoard(boolean isOwnBoard) {
		this.isOwnBoard = isOwnBoard;
	}


	@Override
	public MarkerType getMarkerAt(int column, int row, Player player) {
		if (player == Player.ONE && isOwnBoard) {
			return mPlayer1Data.getMarkerAt(column, row);
		} else if (player == Player.TWO && isOwnBoard) {
			return mPlayer2Data.getMarkerAt(column, row);
		}

		if (player == Player.ONE) {
			return mPlayer2Data.getMarkerAt(column, row);
		} else {
			return mPlayer1Data.getMarkerAt(column, row);
		}
	}

	public boolean fire(int column, int row, Player player) {
		if (player != Player.ONE && player != Player.TWO) {
			throw new IllegalArgumentException("Must be a valid player type");
		}


		Board playerBoard;

		if (player == Player.ONE) {
			playerBoard = mPlayer2Data;
		} else {
			playerBoard = mPlayer1Data;
		}

		if (playerBoard.isLegalPlacement(new Point(column, row))) {
			playerBoard.placeMarker(new Point(column, row));

			updateLastMove(getMarkerAt(column, row, player), player);

			if (playerBoard.isWin()) {
				gameWon = true;
			}
			return true;
		}


		return false; // illegal move
	}


}
