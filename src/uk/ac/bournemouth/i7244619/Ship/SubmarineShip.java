package uk.ac.bournemouth.i7244619.Ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Board.Direction;

/**
 * The submarine representation of the ships.
 * 
 * @author Harrison - i7244619
 *
 */
public class SubmarineShip extends Ship {


	public static final int SHIP_SIZE = 3;

	public SubmarineShip(Direction direction) {

		super("Submarine", SHIP_SIZE, direction);
		shipBitmaps = new Bitmap[SHIP_SIZE];

	}

	public SubmarineShip() {
		super("Submarine", SHIP_SIZE);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public SubmarineShip(Parcel in) {
		super(in);
	}


	@Override
	public void setShipBitmaps(Context context) {

		shipBitmaps[0] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine_start);
		shipBitmaps[1] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.submarine_first_center);
		shipBitmaps[2] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine_last);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

	public static final Parcelable.Creator<SubmarineShip> CREATOR =
			new Parcelable.Creator<SubmarineShip>() {

				@Override
				public SubmarineShip createFromParcel(Parcel in) {
					return new SubmarineShip(in);
				}

				@Override
				public SubmarineShip[] newArray(int size) {
					// TODO Auto-generated method stub
					return new SubmarineShip[size];
				}

			};

}
