package uk.ac.bournemouth.i7244619.View;


import uk.ac.bournemouth.i7244619.AI.Computer;
import uk.ac.bournemouth.i7244619.Activities.ComputerGameActivity;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Activities.ResultActivity;
import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Game.Game;
import uk.ac.bournemouth.i7244619.Game.Player;
import uk.ac.bournemouth.i7244619.Game.TwoPlayerGame;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * The computer game mode that inherits the game view, this holds a computer player and adapts the
 * view to accomodate this.
 * 
 * @author Harrison - i7244619
 *
 */
public class ComputerGameView extends GameView {


	private static final int MILISECONDS_BETWEEN_TURNS = 1500;
	private boolean isTurnTransitioning = false;

	private String mPlayer1Title;
	private String mPlayer2Title;
	Computer computerPlayer;
	private boolean isViewingOwnBoard;


	public ComputerGameView(Context context) {
		super(context);
		init();
	}

	public ComputerGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ComputerGameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

		ComputerGameActivity cga = (ComputerGameActivity) getContext();

		if (cga.hasShips()) {
			Ship[] tPlayer1Ships = cga.getShips();
			mGame = new TwoPlayerGame(Game.DEFAULT_COLUMNS, Game.DEFAULT_ROWS);

			mPlayer1Title = cga.getName();
			mPlayer2Title = Computer.getName();
			mPlayerTitle = mPlayer1Title;

			((TwoPlayerGame) mGame).setShips(tPlayer1Ships, Player.ONE);
			mGame.getBoard(Player.TWO).createRandomBoard();


		}
		computerPlayer = new Computer(mGame.getBoard(Player.ONE));
		loadBitmaps();

		AlertDialog instructionsDialog =
				new AlertDialog.Builder(getContext())
						.setTitle("Instructions")
						.setMessage(
								"Tap on a block to hit that block, you can swipe to the right to view your own ships and swipe left to go back.")
						.setNeutralButton("Close", null).show();

	}

	@Override
	public void drawTitle(Canvas canvas) {
		canvas.drawText(mPlayerTitle,
				getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin),
				mTitlePaint.getTextSize(), mTitlePaint);

	}

	@Override
	public Parcelable onSaveInstanceState() {

		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());

		bundle.putString("Player 1 Title", mPlayer1Title);
		bundle.putString("Player 2 Title", mPlayer2Title);
		bundle.putParcelable("Computer", computerPlayer);
		bundle.putParcelable("Board 1", mGame.getBoard(Player.ONE));
		bundle.putParcelable("Board 2", mGame.getBoard(Player.TWO));

		bundle.putSerializable("Current Player", currentPlayer);
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			currentPlayer = (Player) bundle.getSerializable("Current Player");
			Board player2Board = bundle.getParcelable("Board 2");
			Board player1Board = bundle.getParcelable("Board 1");

			computerPlayer = bundle.getParcelable("Computer");
			mPlayer1Title = bundle.getString("Player 1 Title");
			mPlayer2Title = bundle.getString("Player 2 Title");

			this.mGame.setBoard(player1Board, Player.ONE);
			this.mGame.setBoard(player2Board, Player.TWO);

			state = bundle.getParcelable("instanceState");

		}
		super.onRestoreInstanceState(state);
	}

	@Override
	public void drawFooter(Canvas canvas) {
		return;

	}


	@Override
	public boolean onTap(MotionEvent e) {
		if (isTurnTransitioning || isViewingOwnBoard) {
			return false;
		}

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

		if (!((TwoPlayerGame) mGame).fire(column, row, currentPlayer)) {
			return false; // there was an error with the coordinates or players given
		}

		Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);
		invalidate();
		// call the onDraw method
		Toast toast;
		if (((TwoPlayerGame) mGame).getLastMove(currentPlayer) == MarkerType.HIT) {
			playHitSound();
			toast = Toast.makeText(getContext().getApplicationContext(), "Hit!", TOAST_DURATION);
		} else {
			playMissSound();
			toast = Toast.makeText(getContext().getApplicationContext(), "Miss!", TOAST_DURATION);
		}

		toast.show();

		if (((TwoPlayerGame) mGame).isGameWon()) {

			if (currentPlayer == Player.ONE) {
				Intent intent = new Intent(getContext(), ResultActivity.class);
				intent.putExtra("Message", "Well done " + mPlayer1Title + ", you won!");
				getContext().startActivity(intent);
			} else {
				Intent intent = new Intent(getContext(), ResultActivity.class);
				intent.putExtra("Message", "Unlucky! You lost to the computer!");
				getContext().startActivity(intent);
			}


			return true;
		}

		isTurnTransitioning = true;
		mChangePlayerTask changePlayerTask = new mChangePlayerTask();

		changePlayerTask.execute(MILISECONDS_BETWEEN_TURNS);
		return true;
	}

	@Override
	public boolean onSwingRight() {
		isViewingOwnBoard = true;
		((TwoPlayerGame) mGame).setIsOwnBoard(true);
		invalidate();
		return false;
	}

	@Override
	public boolean onSwingLeft() {
		isViewingOwnBoard = false;
		((TwoPlayerGame) mGame).setIsOwnBoard(false);
		invalidate();
		return false;
	}

	private void drawOwnBoard(int col, int row, float cx, float cy, MarkerType marker, Canvas canvas) {

		if (marker == MarkerType.HIT || marker == MarkerType.SHIP) {
			Ship ship = mGame.getShipAt(col, row, currentPlayer);
			if (isShipsBitmapsLoaded) {
				canvas.drawBitmap(Bitmap.createScaledBitmap(ship.getShipBitmapAt(col, row),
						(int) (length + separator), (int) (length + separator), false), cx, cy,
						null);
			}

			if (isMainBitmapsLoaded && marker == MarkerType.HIT) {
				canvas.drawBitmap(mExplosionBitmap, cx, cy, null);
			} else if (marker == MarkerType.HIT) {
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
	public void drawMarker(int col, int row, float cx, float cy, MarkerType marker, Canvas canvas) {
		if (isViewingOwnBoard) {
			drawOwnBoard(col, row, cx, cy, marker, canvas);
			return;
		}
		if (marker == MarkerType.HIT) {
			Ship ship = mGame.getShipAt(col, row, currentPlayer.getOppositePlayer());
			if (ship.isDestroyed()) {
				if (isShipsBitmapsLoaded) {
					canvas.drawBitmap(Bitmap.createScaledBitmap(ship.getShipBitmapAt(col, row),
							(int) (length + separator), (int) (length + separator), false), cx, cy,
							null);
				}
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

	private class mChangePlayerTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... time) {
			if (time.length == 0) {
				return null;
			}

			try {
				Thread.sleep(time[0]);
			} catch (InterruptedException e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			currentPlayer = currentPlayer == Player.ONE ? Player.TWO : Player.ONE;
			if (currentPlayer == Player.TWO) {
				mPlayerTitle = mPlayer2Title;
				invalidate();
				ComputerMove cm = new ComputerMove();
				cm.execute((Void[]) null);
			} else {



				isTurnTransitioning = false;
				if (currentPlayer == Player.ONE) {
					mPlayerTitle = mPlayer1Title;
				} else {
					mPlayerTitle = mPlayer2Title;

				}
				invalidate();

			}
		}

	}

	private class ComputerMove extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {


			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {


			Point computerCoords = computerPlayer.fire(mGame.getBoard(Player.ONE));
			((TwoPlayerGame) mGame).fire(computerCoords.x, computerCoords.y, Player.TWO);
			computerPlayer.setLastMove(computerCoords);

			Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 500 milliseconds
			v.vibrate(500);

			invalidate();

			if (((TwoPlayerGame) mGame).isGameWon()) {
				Intent intent = new Intent(getContext(), ResultActivity.class);
				intent.putExtra("Message", "Unlucky! You lost to the computer!");
				getContext().startActivity(intent);
				return;
			}

			mChangePlayerTask cpt = new mChangePlayerTask();
			cpt.execute(MILISECONDS_BETWEEN_TURNS);
		}

	}



}
