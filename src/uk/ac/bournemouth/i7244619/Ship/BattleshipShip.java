package uk.ac.bournemouth.i7244619.Ship;

import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Board.Direction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The battleship representation of the ships
 * 
 * @author Harrison - i7244619
 *
 */
public class BattleshipShip extends Ship implements Parcelable {

	public static final int SHIP_SIZE = 4;



	public BattleshipShip(Direction direction) {
		super("Battleship", SHIP_SIZE, direction);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public BattleshipShip() {
		super("Battleship", SHIP_SIZE);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public BattleshipShip(Parcel in) {
		super(in);
	}

	@Override
	public void setShipBitmaps(Context context) {

		shipBitmaps[0] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.battleship_start);
		shipBitmaps[1] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.battleship_first_center);
		shipBitmaps[2] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.battleship_second_center);
		shipBitmaps[3] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.battleship_last);
	}


	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

	public static final Parcelable.Creator<BattleshipShip> CREATOR =
			new Parcelable.Creator<BattleshipShip>() {

				@Override
				public BattleshipShip createFromParcel(Parcel in) {
					return new BattleshipShip(in);
				}

				@Override
				public BattleshipShip[] newArray(int size) {
					// TODO Auto-generated method stub
					return new BattleshipShip[size];
				}

			};


}
