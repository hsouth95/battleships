package uk.ac.bournemouth.i7244619.Game;


import uk.ac.bournemouth.i7244619.Board.MarkerType;
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
 * The class that is used for any creating of boards, this class validates any placements assists
 * with creating the properly formatted ships and holds the flags for when the board is ready to be
 * played on
 * 
 * @author Harrison - i7244619
 *
 */
public class CreateGame extends Game implements Parcelable {

	public static int ONE_PLAYER_FLAG = 1;

	private Ship[] mPlayer1Ships = {new DestroyerShip(), new CruiserShip(), new SubmarineShip(),
			new BattleshipShip(), new CarrierShip()};
	private Ship[] mPlayer2Ships = {new DestroyerShip(), new CruiserShip(), new SubmarineShip(),
			new BattleshipShip(), new CarrierShip()};


	private int currentShipPointer = 0;
	private Ship currentShip = mPlayer1Ships[currentShipPointer];
	private boolean isOnePlayerOnly = false;


	private Player currentPlayer = Player.ONE;
	private boolean isShipsSet = false;

	public CreateGame() {
		super(Game.DEFAULT_COLUMNS, Game.DEFAULT_ROWS);
	}

	public CreateGame(int columns, int rows) {
		super(columns, rows);
	}

	public CreateGame(int columns, int rows, int type) {
		super(columns, rows);
		if (type == ONE_PLAYER_FLAG) {
			isOnePlayerOnly = true;
		}
	}

	public void setCurrentShip(Ship ship) {
		currentShip = ship;
	}

	public void setCurrentShipBitmaps(Context context) {
		currentShip.setBitmaps(context);

	}

	public void setupShips(Context context) {

		mPlayer1Data.setShipBitmaps(mPlayer1Ships, context);
		mPlayer2Data.setShipBitmaps(mPlayer2Ships, context);
	}

	public boolean isShipsSet() {
		return isShipsSet;
	}

	public Ship getCurrentShip() {
		return currentShip;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getCurrentShipPointer() {
		return currentShipPointer;
	}

	public void setCurrentShipPointer(int pointer) {
		currentShipPointer = pointer;
	}

	public void setShips(Ship[] ships, Player player) {

		if (player == Player.ONE) {
			this.mPlayer1Ships = ships;
		} else {
			this.mPlayer2Ships = ships;
		}
	}

	public Ship[] getShips(Player player) {
		if (player == Player.ONE) {
			return mPlayer1Ships;
		} else {
			return mPlayer2Ships;
		}
	}

	public void createRandomBoard(Player player) {
		if (player == Player.ONE) {
			mPlayer1Data.resetBoard();
			mPlayer1Data.createRandomBoard();
			mPlayer1Ships = mPlayer1Data.getShipsOnBoard().toArray(mPlayer1Ships);
		} else {
			mPlayer2Data.resetBoard();
			mPlayer2Data.createRandomBoard();
			mPlayer2Ships = mPlayer2Data.getShipsOnBoard().toArray(mPlayer2Ships);
		}
	}

	public void resetBoard(Player player) {
		if (player == Player.ONE) {
			mPlayer1Data.resetBoard();
		} else {
			mPlayer2Data.resetBoard();
		}
	}

	public boolean placeShipPart(int column, int row, Player player) {

		if (getMarkerAt(column, row, player) == MarkerType.SHIP) {
			return false;
		}

		if (!currentShip.setPartialCoords(new Point(column, row))) {
			return false;
		}



		if (player == Player.ONE) {
			mPlayer1Data.placeMarker(new Point(column, row), MarkerType.SHIP);
		} else {
			mPlayer2Data.placeMarker(new Point(column, row), MarkerType.SHIP);
		}

		if (currentShip.isCoordsSet()) {
			if (currentPlayer == Player.ONE) {
				mPlayer1Ships[currentShipPointer] = currentShip;
			} else {
				mPlayer2Ships[currentShipPointer] = currentShip;
			}
			currentShipPointer++;

			if (currentShipPointer > mPlayer1Ships.length - 1 && currentPlayer == Player.ONE) {
				if (isOnePlayerOnly) {
					isShipsSet = true;
					return true;
				}
				currentPlayer = Player.TWO;
				currentShipPointer = 0;
				currentShip = mPlayer2Ships[currentShipPointer];
			} else if (currentShipPointer > mPlayer2Ships.length - 1 && currentPlayer == Player.TWO) {
				isShipsSet = true;
			} else if (currentPlayer == Player.ONE) {
				currentShip = mPlayer1Ships[currentShipPointer];
			} else if (currentPlayer == Player.TWO) {
				currentShip = mPlayer2Ships[currentShipPointer];
			}

		}


		return true;

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {


		dest.writeParcelable(mPlayer1Data, 0);
		dest.writeParcelable(mPlayer2Data, 0);

		dest.writeParcelableArray(mPlayer1Ships, 0);
		dest.writeParcelableArray(mPlayer2Ships, 0);

		dest.writeInt(currentShipPointer);
		dest.writeParcelable(currentShip, 0);

		// if one player only is true then it is 1
		dest.writeByte((byte) (isOnePlayerOnly ? 1 : 0));
		dest.writeSerializable(currentPlayer);
		dest.writeByte((byte) (isShipsSet ? 1 : 0));

	}

	private CreateGame(Parcel in) {
		super(ONE_PLAYER_FLAG, ONE_PLAYER_FLAG);


	}

	public static final Parcelable.Creator<CreateGame> CREATOR =
			new Parcelable.Creator<CreateGame>() {

				public CreateGame createFromParcel(Parcel in) {
					return new CreateGame(in);
				}

				public CreateGame[] newArray(int size) {
					return new CreateGame[size];
				}

			};


}
