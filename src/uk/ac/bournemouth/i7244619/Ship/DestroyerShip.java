package uk.ac.bournemouth.i7244619.Ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Board.Direction;

/**
 * The destroyer representation of the ships
 * 
 * @author Harrison
 *
 */
public class DestroyerShip extends Ship {

	public static final int SHIP_SIZE = 2;

	public DestroyerShip(Direction direction) {
		super("Destroyer", SHIP_SIZE, direction);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public DestroyerShip() {
		super("Destroyer", SHIP_SIZE);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public DestroyerShip(Parcel in) {
		super(in);
	}

	@Override
	public void setShipBitmaps(Context context) {

		// taken from
		// http://www.worldaffairsboard.com/naval-warfare/56651-destroyers-fletcher-class-2.html

		shipBitmaps[0] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer_start);
		shipBitmaps[1] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer_last);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

	public static final Parcelable.Creator<DestroyerShip> CREATOR =
			new Parcelable.Creator<DestroyerShip>() {

				@Override
				public DestroyerShip createFromParcel(Parcel in) {
					return new DestroyerShip(in);
				}

				@Override
				public DestroyerShip[] newArray(int size) {
					// TODO Auto-generated method stub
					return new DestroyerShip[size];
				}

			};

}
