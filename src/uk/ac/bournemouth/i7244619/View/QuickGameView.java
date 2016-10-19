package uk.ac.bournemouth.i7244619.View;

import uk.ac.bournemouth.i7244619.Activities.QuickGameActivity;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Activities.ResultActivity;
import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Database.DatabaseHandler;
import uk.ac.bournemouth.i7244619.Game.Game;
import uk.ac.bournemouth.i7244619.Game.QuickGame;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * This is the representation of the quick game mode, this view inherits the properties of the game
 * view and allows for the playing of
 * 
 * @author Harrison
 *
 */
public class QuickGameView extends GameView {

	private String playerName;
	private int turn = 0;


	public QuickGameView(Context context) {
		super(context);
		init();
	}

	public QuickGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QuickGameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mGame = new QuickGame(Game.DEFAULT_COLUMNS, Game.DEFAULT_ROWS, Game.RANDOM_SINGLE_GAME);
		loadBitmaps();
		QuickGameActivity ga = (QuickGameActivity) getContext();
		playerName = ga.getName();

		AlertDialog instructionsDialog =
				new AlertDialog.Builder(getContext())
						.setTitle("Instructions")
						.setMessage(
								"Tap on a block to hit that block, use as little moves as possible!")
						.setNeutralButton("Close", null).show();

	}

	@Override
	public boolean onTap(MotionEvent e) {

		if (e.getX() <= offsetX || e.getY() <= offsetY) {
			return false; // they have pressed on either the left or the top of the grid
		} else if (e.getX() >= (mGame.getColumns() * (length + separator)) + offsetX
				|| e.getY() >= (mGame.getRows() * (length + separator) + offsetY)) {
			return false; // they have pressed on either the right or the bottom of the grid
		}


		float x = e.getX() - offsetX;
		float y = e.getY() - offsetY;

		int column = (int) Math.floor(x / (length + separator));
		int row = (int) Math.floor(y / (length + separator));

		turn++;

		if (!((QuickGame) mGame).fire(column, row)) {
			return false; // there was an error with the coordinates or players given
		}

		Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);
		invalidate();
		// call the onDraw method
		if (((QuickGame) mGame).getLastMove() == MarkerType.HIT) {
			playHitSound();
		} else {
			playMissSound();
		}


		if (((QuickGame) mGame).isGameWon()) {
			addToDatabase();
			Intent intent = new Intent(getContext(), ResultActivity.class);
			intent.putExtra("Message", "Well done " + playerName + ", you won in " + turn
					+ " turns!");
			getContext().startActivity(intent);
			return true;
		}
		return true;

	}

	private void addToDatabase() {

		DatabaseHandler db = new DatabaseHandler(getContext().getApplicationContext());
		db.updateQuickMatchTurns(turn, db.getUserId(playerName));
		db.close();
	}


	@Override
	public boolean onSwingRight() {
		return false;
	}



	@Override
	public Parcelable onSaveInstanceState() {

		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putParcelable("Board", mGame.getBoard(currentPlayer));
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			Board board = bundle.getParcelable("Board");
			this.mGame.setBoard(board, currentPlayer);

			state = bundle.getParcelable("instanceState");

		}
		super.onRestoreInstanceState(state);
	}


	@Override
	public void drawTitle(Canvas canvas) {
		canvas.drawText(playerName,
				getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin),
				mTitlePaint.getTextSize(), mTitlePaint);
	}

	@Override
	public void drawFooter(Canvas canvas) {
		return;

	}


	@Override
	public void drawMarker(int col, int row, float cx, float cy, MarkerType marker, Canvas canvas) {

		if (marker == MarkerType.HIT) {
			Ship ship = mGame.getShipAt(col, row, currentPlayer);
			if (ship.isDestroyed()) {
				canvas.drawBitmap(Bitmap.createScaledBitmap(ship.getShipBitmapAt(col, row),
						(int) (length + separator), (int) (length + separator), false), cx, cy,
						null);
			}

			if (isMainBitmapsLoaded) {
				canvas.drawBitmap(mExplosionBitmap, cx, cy, null);
			} else {
				canvas.drawRect(cx, cy, cx + length, cy + length, mHitPaint);
			}
		} else if (marker == MarkerType.MISS) {
			if (isMainBitmapsLoaded) {
				canvas.drawBitmap(mWaterBitmap, cx, cy, null);
			} else {
				canvas.drawRect(cx, cy, cx + length, cy + length, mMissPaint);
			}
		} else {
			canvas.drawRect(cx, cy, cx + length, cy + length, mGridPaint);
		}
	}

	@Override
	public boolean onSwingLeft() {
		// TODO Auto-generated method stub
		return false;
	}

}
