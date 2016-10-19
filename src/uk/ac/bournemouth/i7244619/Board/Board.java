package uk.ac.bournemouth.i7244619.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import uk.ac.bournemouth.i7244619.Ship.BattleshipShip;
import uk.ac.bournemouth.i7244619.Ship.CarrierShip;
import uk.ac.bournemouth.i7244619.Ship.CruiserShip;
import uk.ac.bournemouth.i7244619.Ship.DestroyerShip;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import uk.ac.bournemouth.i7244619.Ship.SubmarineShip;
import android.content.Context;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is charged with holding a board of a given size, of which then contains the ships on that
 * board and the methods to add, update or reset everything on the board.
 * 
 * @author Harrison
 *
 */
public class Board implements Parcelable {

	private final int columns;
	private final int rows;
	public static final int MINIMUM_ROWS = 7;
	public static final int MINIMUM_COLUMNS = 7;
	private MarkerType[][] board;
	private ArrayList<Ship> shipsOnBoard = new ArrayList<Ship>();
	private Ship[] possibleShips;

	public Board(int columns, int rows) {

		if (columns < MINIMUM_COLUMNS || rows < MINIMUM_ROWS) {
			throw new IllegalArgumentException("Columns must be at least " + MINIMUM_COLUMNS
					+ " and rows must be at least " + MINIMUM_ROWS);
		}

		this.columns = columns;
		this.rows = rows;

		board = new MarkerType[columns][rows];
		setupBoard();
	}

	public Ship getShip(int index) {
		return shipsOnBoard.get(index);
	}

	public int getShipAmount() {
		return shipsOnBoard.size();
	}

	public void resetBoard() {
		shipsOnBoard.clear();
		setupBoard();
	}

	public static Board createNullBoard() {
		return null;
	}

	public boolean isWin() {

		for (Ship ship : shipsOnBoard) {
			if (!ship.isDestroyed()) {
				return false;
			}
		}
		return true;
	}

	private void setupBoard() {

		// for each row in a column
		for (MarkerType[] rows : board) {
			Arrays.fill(rows, MarkerType.NOT_TOUCHED);
		}
	}

	public ArrayList<Ship> getShipsOnBoard() {
		return shipsOnBoard;
	}

	public MarkerType getMarkerAt(int column, int row) {
		return board[column][row];
	}

	public boolean isLegalPlacement(int shipSize, Point coord, Direction direction) {
		if (!isLegalBounds(shipSize, coord, direction))
			return false;

		Point[] possibleCoords = coordsFromDirection(shipSize, coord, direction);

		if (!isShipOverlap(possibleCoords))
			return false;

		return true;
	}

	private boolean isShipOverlap(Point[] coords) {
		for (Point coord : coords) {
			if (board[coord.x][coord.y] == MarkerType.SHIP) {
				return false; // there is already a ship here
			}
		}
		return true;
	}

	private boolean isLegalBounds(int shipSize, Point coord, Direction direction) {

		switch (direction) {

			case LEFT:
				if (coord.x - shipSize < 0)
					return false;
				break;
			case UP:
				if (coord.y - shipSize < 0)
					return false;
				break;
			case RIGHT:
				if (coord.x + shipSize >= columns)
					return false;
				break;
			case DOWN:
				if (coord.y + shipSize >= rows)
					return false;
				break;
			default:
				return false; // invalid argument
		}

		return true; //
	}

	public void setupPossibleShips(int shipAmount) {
		possibleShips = new Ship[shipAmount];
	}

	public void setPossibleShip(Ship ship, int index) {
		possibleShips[index] = ship;
	}

	public void setPossibleShips(Ship[] ships) {
		possibleShips = ships;
	}

	public Ship getPossibleShip(int index) {
		return possibleShips[index];
	}

	public Ship getShipAt(int column, int row) {

		for (Ship ship : shipsOnBoard) {
			for (Point coord : ship.getCoords()) {
				if (coord.x == column && coord.y == row) {
					return ship;
				}
			}
		}
		return null;

	}

	public boolean isLegalPlacement(Point coord) {

		// if the marker at this location is a hit or miss then it has already been played
		if (board[coord.x][coord.y] == MarkerType.HIT || board[coord.x][coord.y] == MarkerType.MISS) {
			return false;
		} else {
			return true;
		}
	}

	public void placeMarker(Point coord) {
		MarkerType markerAtPos = board[coord.x][coord.y];
		if (markerAtPos == MarkerType.SHIP) {
			board[coord.x][coord.y] = MarkerType.HIT;
			hitShip(coord);

		} else {
			board[coord.x][coord.y] = MarkerType.MISS;
		}
	}

	public void placeMarker(Point coord, MarkerType marker) {
		board[coord.x][coord.y] = marker;
	}

	private void hitShip(Point coord) {

		for (Ship ship : shipsOnBoard) {

			// will test each ship to see if they are hit
			if (ship.hitShip(coord)) {
				break;
			}

		}

	}

	private Point[] coordsFromDirection(int shipSize, Point startingLocation, Direction direction) {
		Point[] coords = new Point[shipSize];

		int x = startingLocation.x;
		int y = startingLocation.y;

		for (int i = 0; i < shipSize; i++) {
			switch (direction) {
				case LEFT:
					coords[i] = new Point(x - i, y);
					break;
				case UP:
					coords[i] = new Point(x, y - i);
					break;
				case RIGHT:
					coords[i] = new Point(x + i, y);
					break;
				case DOWN:
					coords[i] = new Point(x, y + i);
					break;
			}
		}

		return coords;
	}

	public void createRandomBoard() {
		Ship[] ships = new Ship[5];
		ships[0] = new CarrierShip();
		ships[1] = new BattleshipShip();
		ships[2] = new SubmarineShip();
		ships[3] = new CruiserShip();
		ships[4] = new DestroyerShip();

		Random random = new Random();

		for (Ship ship : ships) {
			Direction direction = Direction.LEFT; // default direction for now

			Point location;


			do {
				int x = random.nextInt(columns);
				int y = random.nextInt(rows);
				location = new Point(x, y);
				direction = Direction.getRandomDirection(random);

				if (!isLegalBounds(ship.getShipSize(), location, direction)) {


					direction = direction.getOppositeDirection();
				}



			} while (!isLegalPlacement(ship.getShipSize(), location, direction));

			Point[] coords = coordsFromDirection(ship.getShipSize(), location, direction);
			addShip(ship, direction, coords);
			addShipToBoard(coordsFromDirection(ship.getShipSize(), location, direction));
		}

	}

	private void addShip(Ship ship, Direction direction, Point[] coords) {

		ship.setDirection(direction);
		ship.setCoords(coords);
		shipsOnBoard.add(ship);
	}

	private void addShip(Ship ship) {
		shipsOnBoard.add(ship);
	}

	private void addShipToBoard(Point[] coords) {

		for (Point location : coords) {
			board[location.x][location.y] = MarkerType.SHIP;
		}

	}

	public void addShips(Ship[] ships) {

		for (Ship ship : ships) {
			addShip(ship);
			addShipToBoard(ship.getCoords());
		}

	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {


		int[][] tempBoard = new int[board.length][board[0].length];

		for (int col = 0; col < board.length; col++) {
			for (int row = 0; row < board[0].length; row++) {
				tempBoard[col][row] = board[col][row].ordinal();
			}
		}

		dest.writeSerializable(tempBoard);
		dest.writeTypedList(shipsOnBoard);

	}

	private Board(Parcel in) {

		List<Ship> tempShips = new ArrayList<Ship>();
		int[][] temp = (int[][]) in.readSerializable();
		tempShips = in.readArrayList(Ship.class.getClassLoader());

		MarkerType[][] tempMarkers = new MarkerType[temp.length][temp[0].length];

		for (int col = 0; col < temp.length; col++) {
			for (int row = 0; row < temp[0].length; row++) {
				tempMarkers[col][row] = MarkerType.values()[temp[col][row]];
			}
		}

		this.board = tempMarkers;
		columns = board.length;
		rows = board[0].length;

		this.shipsOnBoard = (ArrayList<Ship>) tempShips;

	}

	public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {

		@Override
		public Board createFromParcel(Parcel in) {
			return new Board(in);
		}

		@Override
		public Board[] newArray(int size) {
			return new Board[size];
		}


	};

	public void setShipBitmaps(Ship[] ships, Context context) {
		for (Ship ship : ships) {
			ship.setBitmaps(context);
		}
	}

	public void setShipBitmaps(Context context) {
		for (Ship ship : shipsOnBoard) {
			ship.setBitmaps(context);
		}
	}

	public void setShipScaledBitmaps(Ship[] ships, int height, int width) {
		for (Ship ship : ships) {
			ship.setScaledBitmaps(height, width);
		}
	}

	public void setShipScaledBitmaps(int height, int width) {
		for (Ship ship : shipsOnBoard) {
			ship.setScaledBitmaps(height, width);
		}
	}

}
