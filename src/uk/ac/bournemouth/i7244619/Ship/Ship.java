package uk.ac.bournemouth.i7244619.Ship;

import uk.ac.bournemouth.i7244619.Board.Direction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The superclass of ships, this holds all the operations dealt with any of the ships such as
 * validation, health checking and rotation.
 * 
 * @author Harrison
 *
 */
public class Ship implements Parcelable {


	private int shipSize;
	protected Direction direction = Direction.RIGHT;
	private Point[] coords;
	private boolean[] isHit; // based on the coords, booleans default to false so only need to set
	// size
	private boolean isDestroyed = false;
	private String shipName;
	protected Bitmap[] shipBitmaps;

	public Ship(String shipName, int shipSize) {
		this.shipName = shipName;
		this.shipSize = shipSize;
		this.coords = new Point[shipSize];
	}

	public Ship(String shipName, int shipSize, Direction direction) {
		this.shipName = shipName;
		this.shipSize = shipSize;
		this.direction = direction;
		this.coords = new Point[shipSize];
	}

	public Ship(String shipName, int shipSize, Point[] coords) {
		this.shipName = shipName;
		this.shipSize = shipSize;
		this.coords = coords;
	}

	public void setCoords(Point[] coords) {
		this.coords = coords;
		this.isHit = new boolean[coords.length];
	}

	public Bitmap getShipBitmapAt(int index) {

		if (index >= shipSize) {
			return null;
		} else {
			return shipBitmaps[index];
		}
	}

	public Bitmap getShipBitmapAt(int col, int row) {

		for (int i = 0; i < coords.length; i++) {
			if (coords[i].x == col && coords[i].y == row) {
				return shipBitmaps[i];
			}
		}
		return null;

	}

	public boolean setPartialCoords(Point coord) {

		for (int i = 0; i < shipSize; i++) {
			if (coords[i] == null) {
				if (i > 0) {
					if (i == 1 && isAdjacentCoord(coords[0], coord, i + 1)) {
						coords[i] = coord;

						// we can determine the direction
						Point firstCoord = coords[0];
						Point secondCoord = coords[1];
						setDirectionFromCoords(firstCoord, secondCoord);

						return true;
						// must be set in direction
					} else if (i > 1 && isPlaneDefinedCoordCorrect(coords[i - 1], coord, direction)) {

						coords[i] = coord;
						return true;
					} else {

						return false;
					}
				} else {
					coords[i] = coord;
					return true;
				}
			}
		}
		return false;

	}


	private boolean isPlaneDefinedCoordCorrect(Point lastSetCoord, Point possibleCoord,
			Direction direction) {

		switch (direction) {
			case DOWN:
				if (lastSetCoord.y + 1 == possibleCoord.y && lastSetCoord.x == possibleCoord.x) {
					return true;
				} else {
					int x, xx;
					x = lastSetCoord.x + 1;
					xx = possibleCoord.x;
					int y, yy;
					y = lastSetCoord.y + 1;
					yy = possibleCoord.y;
				}
				break;
			case LEFT:
				if (lastSetCoord.y == possibleCoord.y && lastSetCoord.x - 1 == possibleCoord.x) {
					return true;
				}
				break;
			case RIGHT:
				if (lastSetCoord.y == possibleCoord.y && lastSetCoord.x + 1 == possibleCoord.x) {
					return true;
				}
				break;
			case UP:
				if (lastSetCoord.y - 1 == possibleCoord.y && lastSetCoord.x == possibleCoord.x) {
					return true;
				}
				break;
			default:
				break;

		}
		return false;


	}

	private void setDirectionFromCoords(Point firstCoord, Point secondCoord) {
		if (firstCoord.x == secondCoord.x) {
			if (firstCoord.y < secondCoord.y) {
				direction = Direction.DOWN;
			} else {
				direction = Direction.UP;
			}
		} else {
			if (firstCoord.x < secondCoord.x) {
				direction = Direction.RIGHT;
			} else {
				direction = Direction.LEFT;
			}
		}
	}

	private boolean isAdjacentCoord(Point startPoint, Point possiblePoint, int shipPartNumber) {

		int x = startPoint.x;
		int y = startPoint.y;

		shipPartNumber--; // to make calculations easier

		int px = possiblePoint.x;
		int py = possiblePoint.y;

		if (x == px && (py - shipPartNumber == y || py + shipPartNumber == y)) {
			// in different rows
			return true;
		} else if (y == py && (px - shipPartNumber == x || px + shipPartNumber == x)) {
			// in different columns
			return true;
		} else {
			return false;
		}
	}

	public void setShipBitmaps(Context context) {
		return;
	}

	public void setScaledBitmaps(int height, int width) {

		for (Bitmap bitmap : shipBitmaps) {
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		}
	}

	public void setBitmaps(Context context) {
		setShipBitmaps(context);
		switch (getDirection()) {
			case DOWN:
				rotateBitmaps(90, 90, 90);
				break;
			case LEFT:
				rotateBitmaps(180, 180, 180);
				break;
			case RIGHT:
				// picture is made for facing right
				break;
			case UP:
				rotateBitmaps(-90, -90, -90);
				break;
			default:
				break;

		}

	}

	public boolean isCoordsSet() {
		for (int i = 0; i < shipSize; i++) {
			if (coords[i] == null) {
				return false;
			}
		}
		return true;
	}

	public String getShipName() {
		return shipName;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	private void checkDestroyed() {

		for (boolean hit : isHit) {
			if (!hit) {
				return; // is not destroyed
			}
		}
		isDestroyed = true;
	}

	private void rotateBitmaps(int rotateFirst, int rotateCenter, int rotateLast) {
		for (int i = 0; i < shipBitmaps.length; i++) {
			Matrix matrix = new Matrix();
			if (i == 0) {
				matrix.postRotate(rotateFirst);
				shipBitmaps[0] =
						Bitmap.createBitmap(shipBitmaps[0], 0, 0, shipBitmaps[0].getWidth(),
								shipBitmaps[0].getHeight(), matrix, true);
			} else if (i == shipBitmaps.length - 1) {
				matrix.postRotate(rotateLast);
				shipBitmaps[i] =
						Bitmap.createBitmap(shipBitmaps[i], 0, 0, shipBitmaps[i].getWidth(),
								shipBitmaps[i].getHeight(), matrix, true);
			} else {
				matrix.postRotate(rotateCenter);
				shipBitmaps[i] =
						Bitmap.createBitmap(shipBitmaps[i], 0, 0, shipBitmaps[i].getWidth(),
								shipBitmaps[i].getHeight(), matrix, true);
			}
		}

	}


	/**
	 * Hit a section of the ship, there are many checks to see if this is a valid hit.
	 * 
	 * @param point indicates the coordinates of the shot.
	 * @return a {@link #boolean} value that indicates if the shot was successful or not.
	 */
	public boolean hitShip(Point point) {

		// change to throw an exception
		if (coords == null || isHit == null) {
			return false;
		}

		for (int i = 0; i < coords.length; i++) {

			if (coords[i].equals(point)) {
				if (isHit[i] != true) {
					isHit[i] = true;
					checkDestroyed();
					return true; // exists and has not been hit
				} else {
					return false; // already been hit
				}
			}

		}
		return false; // didn't find a coordinate that fits
	}

	public Point[] getCoords() {
		return coords;
	}

	public int getShipSize() {
		return shipSize;
	}



	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelableArray(coords, 0);
		dest.writeString(shipName);
		dest.writeSerializable(direction);
	}

	public Ship(Parcel in) {
		Parcelable[] temps = in.readParcelableArray(Point.class.getClassLoader());
		Point[] coords = new Point[temps.length];
		for (int i = 0; i < temps.length; i++) {
			coords[i] = (Point) temps[i];
		}
		this.coords = coords;
		this.isHit = new boolean[coords.length];
		this.shipBitmaps = new Bitmap[coords.length];
		this.shipName = in.readString();
		this.direction = (Direction) in.readSerializable();
	}

	public Ship() {

	}

	public static final Parcelable.Creator<Ship> CREATOR = new Parcelable.Creator<Ship>() {

		@Override
		public Ship createFromParcel(Parcel in) {
			return new Ship(in);
		}

		@Override
		public Ship[] newArray(int size) {
			return new Ship[size];
		}

	};



}
