package uk.ac.bournemouth.i7244619.AI;

import java.util.List;
import java.util.Random;

import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.Direction;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the class that handles the computers action when playing the game, to understand this we
 * need to know the different possibilities that may occur during a play of a game, for this see
 * below for the associated methods.
 * 
 * @author Harrison - i7244619
 *
 */
public class Computer implements Parcelable {

	private static final String NAME = "Computer";


	private Point lastMove;
	private Random computerRandom = new Random();
	private List<Ship> shipsOnBoard;
	private Ship currentShip;
	private Point currentPointer;
	private boolean isPlaneFound = false;
	private boolean isPlaneDefiningHit = false; // if its the second hit then we know what plane the
												// ship is on
	private Direction direction;
	private Point startPoint;

	public Computer(Board board) {

		shipsOnBoard = board.getShipsOnBoard();
	}



	public Point fire(Board board) {

		Point coord = new Point();

		if (lastMove == null) {
			return getRandomCoord(board);
		}

		// the last move was a hit
		if (board.getMarkerAt(lastMove.x, lastMove.y) == MarkerType.HIT) {

			// this is the first hit of another ship
			coord = lastShipHit(board);
			// last move was a miss
		} else {

			coord = lastShipMiss(board);

		}

		return coord;



	}

	private Point lastShipHit(Board board) {

		Point coord = new Point();
		// not found a ship yet
		if (currentShip == null) {

			currentShip = getShipHit(lastMove);
			currentShip.hitShip(lastMove);

			startPoint = lastMove;

			coord = getRandomAdjacentCoord(board, lastMove);// if first hit of a ship then it must
															// be an adjacent coordinate
			currentPointer = createCurrentPointer(board, coord, direction);

			isPlaneDefiningHit = true; // the next hit will define the plane


		} else {

			currentShip.hitShip(lastMove);
			// if the ship is destroyed then we have to find another ship
			if (currentShip.isDestroyed()) {

				currentShip = null;
				startPoint = null;
				isPlaneFound = false;
				isPlaneDefiningHit = false;
				coord = getRandomCoord(board);

			} else if (isPlaneDefiningHit) {

				isPlaneFound = true;
				isPlaneDefiningHit = true;
				coord = currentPointer;
				currentPointer = createCurrentPointer(board, currentPointer, direction);

			} else {
				coord = currentPointer;
				currentPointer = createCurrentPointer(board, currentPointer, direction);
			}

		}
		return coord;
	}

	private Point lastShipMiss(Board board) {

		Point coord = new Point();
		if (isPlaneFound) {

			direction = direction.getOppositeDirection();
			coord = createCurrentPointer(board, startPoint, direction);
			currentPointer = createCurrentPointer(board, coord, direction);

			// is the 2nd hit on the ship, the one that finds what plane it is on
		} else if (isPlaneDefiningHit) {

			coord = getRandomAdjacentCoord(board, startPoint);
			currentPointer = createCurrentPointer(board, coord, direction);

		} else {
			coord = getRandomCoord(board);
		}

		return coord;
	}


	public void setLastMove(Point point) {
		lastMove = point;
	}


	private Ship getShipHit(Point point) {

		for (Ship ship : shipsOnBoard) {

			for (Point coord : ship.getCoords()) {
				if (coord.equals(point)) {
					return ship;
				}
			}

		}
		return null;
	}

	private Point getRandomCoord(Board board) {
		boolean validMove = false;
		Point randomCoord = null;
		do {
			int col, row;
			col = computerRandom.nextInt(board.getColumns());
			row = computerRandom.nextInt(board.getRows());

			MarkerType markerAtPos = board.getMarkerAt(col, row);
			if (markerAtPos != MarkerType.HIT && markerAtPos != MarkerType.MISS) {
				validMove = true;
				randomCoord = new Point(col, row);
			}
		} while (!validMove);

		return randomCoord;

	}


	private Point getRandomAdjacentCoord(Board board, Point coord) {

		boolean validMove = false;
		Point adjacentPoint = null;

		do {

			direction = Direction.getRandomDirection(computerRandom);

			switch (direction) {
				case DOWN:
					if (coord.y != board.getRows()) {
						adjacentPoint = new Point(coord.x, coord.y + 1);
					}
					break;
				case LEFT:
					if (coord.x != 0) {
						adjacentPoint = new Point(coord.x - 1, coord.y);
					}
					break;
				case RIGHT:
					if (coord.x != board.getColumns()) {
						adjacentPoint = new Point(coord.x + 1, coord.y);
					}
					break;
				case UP:
					if (coord.y != board.getRows()) {
						adjacentPoint = new Point(coord.x, coord.y - 1);
					}
					break;
				default:
					adjacentPoint = new Point();
					adjacentPoint = getRandomCoord(board);
					break;

			}
			MarkerType markerAtPos = board.getMarkerAt(adjacentPoint.x, adjacentPoint.y);
			if (markerAtPos != MarkerType.HIT && markerAtPos != MarkerType.MISS) {
				validMove = true;
			}


		} while (!validMove);

		return adjacentPoint;



	}

	private Point createCurrentPointer(Board board, Point point, Direction direction) {

		Point tempPoint = new Point();

		switch (direction) {
			case DOWN:
				tempPoint = new Point(point.x, point.y + 1);
				break;
			case LEFT:
				tempPoint = new Point(point.x - 1, point.y);
				break;
			case RIGHT:
				tempPoint = new Point(point.x + 1, point.y);
				break;
			case UP:
				tempPoint = new Point(point.x, point.y - 1);
				break;
			default:
				break;

		}

		if (tempPoint.x >= board.getColumns() || tempPoint.y >= board.getRows() || tempPoint.x < 0
				|| tempPoint.y < 0) {
		} else {
			MarkerType markerAtPos = board.getMarkerAt(tempPoint.x, tempPoint.y);
			if (markerAtPos != MarkerType.HIT && markerAtPos != MarkerType.MISS) {
			} else {
				tempPoint = getRandomAdjacentCoord(board, startPoint);

			}



		}
		return tempPoint;
	}

	public static String getName() {
		return NAME;
	}



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeParcelable(lastMove, 0);
		dest.writeTypedList(shipsOnBoard);
		dest.writeParcelable(currentShip, 0);
		dest.writeParcelable(currentPointer, 0);
		dest.writeByte((byte) (isPlaneFound ? 1 : 0));
		dest.writeByte((byte) (isPlaneDefiningHit ? 1 : 0));
		dest.writeSerializable(direction);
		dest.writeParcelable(startPoint, 0);
	}

	public Computer(Parcel in) {

		startPoint = in.readParcelable(Point.class.getClassLoader());
		direction = (Direction) in.readSerializable();
		isPlaneDefiningHit = (in.readByte() != 0); // if the byte is not 0 then it is true otherwise
													// it is false
		isPlaneFound = (in.readByte() != 0); // see above
		currentPointer = in.readParcelable(Point.class.getClassLoader());
		currentShip = in.readParcelable(Ship.class.getClassLoader());
		in.readTypedList(shipsOnBoard, null); // should set shipsOnBoard
		lastMove = in.readParcelable(Point.class.getClassLoader());

	}

	public static final Parcelable.Creator<Computer> CREATOR = new Parcelable.Creator<Computer>() {

		@Override
		public Computer createFromParcel(Parcel in) {
			return new Computer(in);
		}

		@Override
		public Computer[] newArray(int size) {
			return new Computer[size];
		}

	};


}
