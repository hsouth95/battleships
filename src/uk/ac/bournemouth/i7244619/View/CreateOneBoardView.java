package uk.ac.bournemouth.i7244619.View;

import uk.ac.bournemouth.i7244619.Activities.ComputerGameActivity;
import uk.ac.bournemouth.i7244619.Activities.CreateOnePlayerGameActivity;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Activities.TwoPlayerGameActivity;
import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Game.CreateGame;
import uk.ac.bournemouth.i7244619.Game.Game;
import uk.ac.bournemouth.i7244619.Game.Player;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import uk.ac.bournemouth.i7244619.View.CreateTwoBoardView.ConfirmRandomChoiceTask;
import uk.ac.bournemouth.i7244619.View.CreateTwoBoardView.TransitionTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * The one player version of the create board, this adapts the gameview to have the ability to place
 * the ships and take these ships to the play game views.
 * 
 * @author Harrison - i7244619
 *
 */
public class CreateOneBoardView extends GameView {


	private Ship currentShip;
	private static final int WAIT_TIME = 3000;

	public CreateOneBoardView(Context context) {
		super(context);
		init();
	}

	public CreateOneBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mGame = new CreateGame(Game.DEFAULT_COLUMNS, Game.DEFAULT_ROWS, CreateGame.ONE_PLAYER_FLAG);


		CreateOnePlayerGameActivity cga = (CreateOnePlayerGameActivity) getContext();
		mPlayerTitle = cga.getName();
		LoadShipBitmapsTask lsbt = new LoadShipBitmapsTask();
		lsbt.execute((Void[]) null);

		currentShip = ((CreateGame) mGame).getCurrentShip();

		AlertDialog instructionsDialog =
				new AlertDialog.Builder(getContext())
						.setTitle("Instructions")
						.setMessage(
								"Tap on a block to place a part of the ship, if you want a random board then shake your phone!")
						.setNeutralButton("Close", null).show();

	}

	@Override
	public void drawTitle(Canvas canvas) {
		canvas.drawText(mPlayerTitle,
				getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin),
				mTitlePaint.getTextSize(), mTitlePaint);
	}

	@Override
	public void drawFooter(Canvas canvas) {

		canvas.drawText(currentShip.getShipName(), 10,
				(float) ((float) mHeight - ((length + separator) * 1.5)), mTextPaint);
		for (int i = 0; i < currentShip.getShipSize(); i++) {

			float cx = (float) ((float) separator + (length + separator) * i + length / 2);
			float cy = (float) (mHeight - (length + separator));
			if (isShipsBitmapsLoaded) {
				canvas.drawBitmap(Bitmap.createScaledBitmap(currentShip.getShipBitmapAt(i),
						(int) (length + separator), (int) (length + separator), false), cx, cy,
						null);
			} else {
				canvas.drawRect(cx, cy, cx + length, cy + length, mShipPaint);
			}
		}

	}


	@Override
	public Parcelable onSaveInstanceState() {

		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putParcelable("Board", mGame.getBoard(currentPlayer));
		bundle.putParcelableArray("Ships 1", ((CreateGame) mGame).getShips(Player.ONE));
		bundle.putParcelable("Current Ship", ((CreateGame) mGame).getCurrentShip());
		bundle.putInt("Current Ship Pointer", ((CreateGame) mGame).getCurrentShipPointer());

		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			int currentShipPointer = bundle.getInt("Current Ship Pointer");
			Parcelable[] player1Ships = bundle.getParcelableArray("Ships 1");
			Ship[] testShips2 = new Ship[player1Ships.length];
			for (int i = 0; i < player1Ships.length; i++) {
				testShips2[i] = (Ship) player1Ships[i];
			}
			Board board = bundle.getParcelable("Board");
			Ship ship = bundle.getParcelable("Current Ship");

			mGame.setBoard(board, Player.ONE);
			((CreateGame) mGame).setShips(testShips2, Player.ONE);
			((CreateGame) mGame).setCurrentShipPointer(currentShipPointer);
			((CreateGame) mGame).setCurrentShip(ship);
			currentShip = ship;
			state = bundle.getParcelable("instanceState");

		}
		super.onRestoreInstanceState(state);
	}

	@Override
	public boolean onTap(MotionEvent e) {
		float x = e.getX() - offsetX;
		float y = e.getY() - offsetY;

		int column = (int) Math.floor(x / (length + separator));
		int row = (int) Math.floor(y / (length + separator));

		if (mGame.getMarkerAt(column, row, currentPlayer) == MarkerType.SHIP) {

			return false;

		}

		if (!((CreateGame) mGame).placeShipPart(column, row, currentPlayer)) {
			return false;
		}

		if (((CreateGame) mGame).isShipsSet()) {
			// start intent
			Intent intent = new Intent(getContext(), ComputerGameActivity.class);
			intent.putExtra("Player 1 Ships", ((CreateGame) mGame).getShips(Player.ONE));
			intent.putExtra("Player 1 Name", mPlayerTitle);

			getContext().startActivity(intent);
			((Activity) getContext()).overridePendingTransition(R.anim.enter_left_in,
					R.anim.enter_left_out);
		}

		currentShip = ((CreateGame) mGame).getCurrentShip();


		invalidate();
		return true;
	}

	@Override
	public boolean onSwingRight() {
		return false;
	}

	public void createRandomBoard() {

		((CreateGame) mGame).createRandomBoard(currentPlayer);
		invalidate();
		ConfirmRandomChoiceTask crct = new ConfirmRandomChoiceTask();
		crct.execute((Void[]) null);

	}

	@Override
	public void setCounterValues(Canvas canvas) {
		super.setCounterValues(canvas);

		lengthY =
				(float) Math.floor((mHeight - mTitlePaint.getTextSize() - mTextPaint.getTextSize())
						/ (mGame.getRows() + (mGame.getRows() + 1) * SEPERATOR_RATIO));
		length = getSmallestDiameter(lengthX, lengthY);
		separator = (float) (length * SEPERATOR_RATIO);

		if (mWidth > mHeight) {
			offsetX =
					(float) (Math.floor((mWidth - ((length + separator) * mGame.getColumns()))
							+ mTitlePaint.measureText(mPlayerTitle)) / 2);
			offsetY =
					(float) (Math.floor((mHeight - ((length + separator) * mGame.getRows()))
							+ mTitlePaint.getTextSize()) / 2);
		} else {
			offsetX =
					(float) (Math.floor(mWidth - ((length + separator) * mGame.getColumns())) / 2);
			offsetY =
					(float) (Math.floor((mHeight - ((length + separator) * mGame.getRows()))
							+ mTitlePaint.getTextSize() - (mTextPaint.getTextSize() + length)) / 2);
		}

	}

	@Override
	public void drawMarker(int col, int row, float cx, float cy, MarkerType marker, Canvas canvas) {


		if (marker == MarkerType.SHIP) {


			canvas.drawRect(cx, cy, cx + length, cy + length, mShipPaint);


		} else {
			canvas.drawRect(cx, cy, cx + length, cy + length, mGridPaint);
		}


	}

	private class LoadShipBitmapsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			((CreateGame) mGame).setupShips(getContext());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			isShipsBitmapsLoaded = true;
			invalidate();
		}

	}

	@Override
	public boolean onSwingLeft() {
		return false;
	}

	public class ConfirmRandomChoiceTask extends AsyncTask<Void, Void, Void> {



		@Override
		protected Void doInBackground(Void... args) {

			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		}

	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:

					Intent intent = new Intent(getContext(), TwoPlayerGameActivity.class);
					intent.putExtra("Player 1 Ships", ((CreateGame) mGame).getShips(Player.ONE));
					intent.putExtra("Player 1 Name", mPlayerTitle);
					getContext().startActivity(intent);



					break;

				case DialogInterface.BUTTON_NEGATIVE:

					((CreateGame) mGame).resetBoard(currentPlayer);
					invalidate();

					break;
			}
		}
	};

}
