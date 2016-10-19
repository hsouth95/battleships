package uk.ac.bournemouth.i7244619.View;

import uk.ac.bournemouth.i7244619.Activities.CreateTwoPlayerGameActivity;
import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Activities.TwoPlayerGameActivity;
import uk.ac.bournemouth.i7244619.Board.Board;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Game.CreateGame;
import uk.ac.bournemouth.i7244619.Game.Game;
import uk.ac.bournemouth.i7244619.Game.Player;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * The two player version of the create board, this allows for a switch between the two players and
 * means that the ships placed can be passed to the play board.
 * 
 * @author Harrison - i7244619
 *
 */
public class CreateTwoBoardView extends GameView {

	private final int TRANSITION_TIME = 5000;
	private final int WAIT_TIME = 3000;

	private Paint mTransitionPaint;
	private Paint mTransitionTextPaint;

	private String mPlayer1Title;
	private String mPlayer2Title;

	private String mPlayerTitle;

	private Ship currentShip;

	private boolean changingPlayer = false; // indicate that the changing player transition occurs

	public CreateTwoBoardView(Context context) {
		super(context);
		init();
	}

	public CreateTwoBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CreateTwoBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mGame = new CreateGame(Game.DEFAULT_COLUMNS, Game.DEFAULT_ROWS);

		CreateTwoPlayerGameActivity cga = (CreateTwoPlayerGameActivity) getContext();

		mPlayer1Title = cga.getPlayerTitle(Player.ONE);
		mPlayer2Title = cga.getPlayerTitle(Player.TWO);

		mPlayerTitle = mPlayer1Title;

		LoadShipBitmapsTask lsbt = new LoadShipBitmapsTask();
		lsbt.execute((Void[]) null);

		currentShip = ((CreateGame) mGame).getCurrentShip();


		mTransitionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTransitionPaint.setStyle(Paint.Style.FILL);
		mTransitionPaint.setColor(0xff000000);

		mTransitionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTransitionTextPaint.setStyle(Paint.Style.FILL);
		mTransitionTextPaint.setColor(0xffffffff);
		mTransitionTextPaint.setTextSize(getResources().getDimensionPixelSize(
				R.dimen.transition_size));

		AlertDialog instructionsDialog =
				new AlertDialog.Builder(getContext())
						.setTitle("Instructions")
						.setMessage(
								"Tap on a block to place a part of the ship, if you want a random board then shake your phone!")
						.setNeutralButton("Close", null).show();

	}

	@Override
	public Parcelable onSaveInstanceState() {

		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putParcelable("Board 1", mGame.getBoard(Player.ONE));
		bundle.putParcelableArray("Ships 1", ((CreateGame) mGame).getShips(Player.ONE));
		bundle.putParcelable("Current Ship", ((CreateGame) mGame).getCurrentShip());
		bundle.putInt("Current Ship Pointer", ((CreateGame) mGame).getCurrentShipPointer());

		bundle.putParcelable("Board 2", mGame.getBoard(Player.TWO));
		bundle.putParcelableArray("Ships 2", ((CreateGame) mGame).getShips(Player.TWO));
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			Parcelable[] player2Ships = bundle.getParcelableArray("Ships 2");
			Ship[] testShips = new Ship[player2Ships.length];
			for (int i = 0; i < player2Ships.length; i++) {
				testShips[i] = (Ship) player2Ships[i];
			}


			Board player2Board = bundle.getParcelable("Board 2");
			int currentShipPointer = bundle.getInt("Current Ship Pointer");
			Parcelable[] player1Ships = bundle.getParcelableArray("Ships 1");
			Ship[] testShips2 = new Ship[player1Ships.length];
			for (int i = 0; i < player1Ships.length; i++) {
				testShips2[i] = (Ship) player1Ships[i];
			}

			Board player1Board = bundle.getParcelable("Board 1");



			mGame.setBoard(player1Board, Player.ONE);
			mGame.setBoard(player2Board, Player.TWO);
			((CreateGame) mGame).setShips(testShips2, Player.ONE);
			((CreateGame) mGame).setShips(testShips, Player.TWO);
			((CreateGame) mGame).setCurrentShipPointer(currentShipPointer);

			state = bundle.getParcelable("instanceState");

		}
		super.onRestoreInstanceState(state);
	}

	@Override
	public void doExtras(Canvas canvas) {

	}

	@Override
	public boolean overrideOnDraw(Canvas canvas) {
		if (changingPlayer) {
			canvas.drawRect(0, 0, mWidth, mHeight, mTransitionPaint);
			float textWidth = mTransitionTextPaint.measureText("Pass to other player");
			canvas.drawText("Pass to other player", (mWidth - textWidth) / 2, mHeight / 2,
					mTransitionTextPaint);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setCounterValues(Canvas canvas) {

		mHeight = canvas.getHeight();
		mWidth = canvas.getWidth();
		lengthX =
				(float) Math.floor(mWidth
						/ (mGame.getColumns() + (mGame.getColumns() + 1) * SEPERATOR_RATIO));
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

	public void createRandomBoard() {

		((CreateGame) mGame).createRandomBoard(currentPlayer);
		invalidate();
		ConfirmRandomChoiceTask crct = new ConfirmRandomChoiceTask();
		crct.execute((Void[]) null);
	}



	class TransitionTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
				Thread.sleep(TRANSITION_TIME);
			} catch (InterruptedException e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			changingPlayer = false;
			invalidate();
			AlertDialog instructionsDialog =
					new AlertDialog.Builder(getContext())
							.setTitle("Instructions")
							.setMessage(
									"Tap on a block to place a part of the ship, if you want a random board then shake your phone!")
							.setNeutralButton("Close", null).show();
		}

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

					if (currentPlayer == Player.ONE) {
						mPlayerTitle = mPlayer2Title;
						currentPlayer = Player.TWO;
						changingPlayer = true;
						invalidate();
						TransitionTask transition = new TransitionTask();
						transition.execute((Void[]) null);
					} else {
						Intent intent = new Intent(getContext(), TwoPlayerGameActivity.class);
						intent.putExtra("Player 1 Ships", ((CreateGame) mGame).getShips(Player.ONE));
						intent.putExtra("Player 2 Ships", ((CreateGame) mGame).getShips(Player.TWO));
						intent.putExtra("Player 1 Name", mPlayer1Title);
						intent.putExtra("Player 2 Name", mPlayer2Title);
						getContext().startActivity(intent);
						((Activity) getContext()).overridePendingTransition(R.anim.enter_left_in,
								R.anim.enter_left_out);
					}


					break;

				case DialogInterface.BUTTON_NEGATIVE:

					((CreateGame) mGame).resetBoard(currentPlayer);
					invalidate();

					break;
			}
		}
	};

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
		if (((CreateGame) mGame).getCurrentPlayer() == Player.TWO) {
			if (currentPlayer == Player.ONE) {
				mPlayerTitle = mPlayer2Title;
				currentPlayer = Player.TWO;
				changingPlayer = true;
				TransitionTask transition = new TransitionTask();
				transition.execute((Void[]) null);
			}
			if (((CreateGame) mGame).isShipsSet()) {
				// start intent
				Intent intent = new Intent(getContext(), TwoPlayerGameActivity.class);
				intent.putExtra("Player 1 Ships", ((CreateGame) mGame).getShips(Player.ONE));
				intent.putExtra("Player 2 Ships", ((CreateGame) mGame).getShips(Player.TWO));
				intent.putExtra("Player 1 Name", mPlayer1Title);
				intent.putExtra("Player 2 Name", mPlayer2Title);
				getContext().startActivity(intent);
				((Activity) getContext()).overridePendingTransition(R.anim.enter_left_in,
						R.anim.enter_left_out);
			}
		}

		currentShip = ((CreateGame) mGame).getCurrentShip();


		invalidate();
		return true;
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
	public boolean onSwingRight() {
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

}
