package uk.ac.bournemouth.i7244619.Ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Board.Direction;

/**
 * The Cruiser representation of the ships
 * 
 * @author Harrison - i7244619
 *
 */
public class CruiserShip extends Ship {

	public static final int SHIP_SIZE = 3;

	public CruiserShip(Direction direction) {

		super("Cruiser", SHIP_SIZE, direction);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public CruiserShip() {
		super("Cruiser", SHIP_SIZE);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}


	public CruiserShip(Parcel in) {
		super(in);
	}

	@Override
	public void setShipBitmaps(Context context) {

		shipBitmaps[0] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser_start);
		shipBitmaps[1] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.cruiser_first_center);
		shipBitmaps[2] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser_last);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

	public static final Parcelable.Creator<CruiserShip> CREATOR =
			new Parcelable.Creator<CruiserShip>() {

				@Override
				public CruiserShip createFromParcel(Parcel in) {
					return new CruiserShip(in);
				}

				@Override
				public CruiserShip[] newArray(int size) {
					// TODO Auto-generated method stub
					return new CruiserShip[size];
				}

			};

}
