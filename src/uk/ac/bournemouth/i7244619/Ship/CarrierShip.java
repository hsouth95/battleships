package uk.ac.bournemouth.i7244619.Ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Board.Direction;

/**
 * The carrier representation of the ships
 * 
 * @author Harrison - i7244619
 *
 */
public class CarrierShip extends Ship {

	public static final int SHIP_SIZE = 5;

	public CarrierShip(Direction direction) {

		super("Carrier", SHIP_SIZE, direction);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public CarrierShip() {
		super("Carrier", SHIP_SIZE);
		shipBitmaps = new Bitmap[SHIP_SIZE];
	}

	public CarrierShip(Parcel in) {
		super(in);
	}


	@Override
	public void setShipBitmaps(Context context) {


		// images taken from
		// http://militaryzoneupdate.blogspot.co.uk/2012/12/google-earth-spotts-chinese-liaoning.html


		shipBitmaps[0] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.carrier_start);
		shipBitmaps[1] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.carrier_first_center);
		shipBitmaps[2] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.carrier_second_center);
		shipBitmaps[3] =
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.carrier_third_center);
		shipBitmaps[4] =
				BitmapFactory.decodeResource(context.getResources(), R.drawable.carrier_last);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

	public static final Parcelable.Creator<CarrierShip> CREATOR =
			new Parcelable.Creator<CarrierShip>() {

				@Override
				public CarrierShip createFromParcel(Parcel in) {
					return new CarrierShip(in);
				}

				@Override
				public CarrierShip[] newArray(int size) {
					// TODO Auto-generated method stub
					return new CarrierShip[size];
				}

			};

}
