package uk.ac.bournemouth.i7244619.Game;

import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.content.Context;
import android.graphics.Point;

/**
 * The superclass of all game classes, it holds the basis for access all of the data needed from the
 * game class.
 * 
 * @author Harrison - i7244619
 *
 */
public class Game {

	public static final int DEFAULT_ROWS = 10;
	public static final int DEFAULT_COLUMNS = 10;
	public static final int RANDOM_GAME = 1;
	public static final int RANDOM_SINGLE_GAME = 2;


	private int mColumns;
	private int mRows;

	protected Board mPlayer1Data;
	protected Board mPlayer2Data;


	public Game(int columns, int rows) {
		this.mColumns = columns;
		this.mRows = rows;
		mPlayer1Data = new Board(columns, rows);
		mPlayer2Data = new Board(columns, rows);

	}

	public Game(int columns, int rows, int type) {
		this.mColumns = columns;
		this.mRows = rows;

		if (type == Game.RANDOM_GAME) {
			mPlayer1Data = new Board(columns, rows);
			mPlayer2Data = new Board(columns, rows);
			mPlayer1Data.createRandomBoard();
			mPlayer2Data.createRandomBoard();
		} else if (type == Game.RANDOM_SINGLE_GAME) {
			mPlayer1Data = new Board(columns, rows);
			mPlayer2Data = null; // do not need this so set it to null just in case
			mPlayer1Data.createRandomBoard();
		} else {
			// error has occured with typing
			mPlayer1Data = null;
			mPlayer2Data = null;
		}

	}


	public Board getBoard(Player player) {
		if (player == Player.ONE) {
			return mPlayer1Data;
		} else {
			return mPlayer2Data;
		}
	}

	public void setBoard(Board board, Player player) {
		if (player == Player.ONE) {
			mPlayer1Data = board;
		} else {
			mPlayer2Data = board;
		}
	}

	public int getColumns() {
		return mColumns;
	}

	public int getRows() {
		return mRows;
	}

	public MarkerType getMarkerAt(int column, int row, Player player) {
		if (player == Player.ONE) {
			return mPlayer1Data.getMarkerAt(column, row);
		} else {
			return mPlayer2Data.getMarkerAt(column, row);
		}
	}

	public Ship getShipAt(int column, int row, Player player) {

		if (player == Player.ONE) {
			return mPlayer1Data.getShipAt(column, row);
		} else {
			return mPlayer2Data.getShipAt(column, row);
		}
	}

	public void setShipBitmaps(Context context) {

		if (mPlayer1Data != null) {
			mPlayer1Data.setShipBitmaps(context);
		}
		if (mPlayer2Data != null) {
			mPlayer2Data.setShipBitmaps(context);
		}


	}

	public int getShipAmount() {
		return mPlayer1Data.getShipAmount();
	}

	public void setShipsScaledBitmaps(int height, int width) {

		if (mPlayer1Data != null) {
			mPlayer1Data.setShipScaledBitmaps(height, width);
		}
		if (mPlayer2Data != null) {
			mPlayer2Data.setShipScaledBitmaps(height, width);
		}
	}

	public void placeMarkerAt(int column, int row, Player player, MarkerType marker) {

		if (player == Player.ONE) {
			mPlayer1Data.placeMarker(new Point(column, row), marker);
		} else {
			mPlayer2Data.placeMarker(new Point(column, row), marker);
		}

	}



}
