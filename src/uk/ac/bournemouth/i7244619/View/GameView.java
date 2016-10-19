package uk.ac.bournemouth.i7244619.View;

import uk.ac.bournemouth.i7244619.Activities.R;
import uk.ac.bournemouth.i7244619.Board.MarkerType;
import uk.ac.bournemouth.i7244619.Game.Game;
import uk.ac.bournemouth.i7244619.Game.Player;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


/**
 * This is the super class for all games that encapsulates all android features of the game view,
 * this allows for easy modification for custom game mode.
 * 
 * @author Harrison - i7244619
 *
 */
public abstract class GameView extends View {

	private GestureDetector mDetector = new GestureDetector(getContext(), new mListener());
	protected static final int TOAST_DURATION = 500;
	protected String mPlayerTitle;
	protected Game mGame;

	private MediaPlayer mpHitNoise;
	private MediaPlayer mpMissNoise;

	protected boolean isShipsBitmapsLoaded = false; // can we display the ships
	protected boolean isMainBitmapsLoaded = false; // can we display the water and explosions?
	protected boolean isMainBitmapsScaled = false;

	protected Player currentPlayer = Player.ONE;

	protected Paint mGridPaint;
	protected Paint mHitPaint;
	protected Paint mMissPaint;
	protected Paint mShipPaint;
	protected Paint mBGPaint;
	protected Paint mTitlePaint;
	protected Paint mTextPaint;

	protected Bitmap mWaterBitmap;
	protected Bitmap mExplosionBitmap;

	protected static final double SEPERATOR_RATIO = 0.2;
	protected int mWidth;
	protected int mHeight;

	protected float lengthX;
	protected float lengthY;
	protected float length;
	protected float separator;
	protected float offsetX;
	protected float offsetY;



	public GameView(Context context) {
		super(context);
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}



	public Game getGame() {
		return mGame;
	}

	protected void loadBitmaps() {
		LoadBitmapsTask ldt = new LoadBitmapsTask();
		ldt.execute((Void[]) null);

		LoadShipBitmapsTask lsbt = new LoadShipBitmapsTask();
		lsbt.execute((Void[]) null);


	}

	private void init() {

		mPlayerTitle = getResources().getString(R.string.player1);

		mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mGridPaint.setStyle(Paint.Style.FILL);
		mGridPaint.setColor(getResources().getColor(R.color.grid_paint));

		mHitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mHitPaint.setStyle(Paint.Style.FILL);
		mHitPaint.setColor(0xffff0000);

		mShipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mShipPaint.setStyle(Paint.Style.FILL);
		mShipPaint.setColor(Color.GRAY);

		mMissPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mMissPaint.setStyle(Paint.Style.FILL);
		mMissPaint.setColor(0xffffffff);

		mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBGPaint.setStyle(Paint.Style.FILL);
		mBGPaint.setColor(0xff52b3d9);

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setColor(getResources().getColor(R.color.text_color));
		mTextPaint.setTextSize(20);

		mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTitlePaint.setColor(getResources().getColor(R.color.gameTitle));
		mTitlePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.game_title_size));

		mpMissNoise = MediaPlayer.create(getContext(), R.raw.splash);
		mpHitNoise = MediaPlayer.create(getContext(), R.raw.explosion);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setCounterValues(canvas);
		doExtras(canvas);
		if (overrideOnDraw(canvas)) {
			return;
		}
		drawTitle(canvas);
		drawGrid(canvas);
		drawFooter(canvas);
	}

	public boolean overrideOnDraw(Canvas canvas) {
		return false;
	}

	public void doExtras(Canvas canvas) {
		return;
	}

	public abstract void drawTitle(Canvas canvas);

	public abstract void drawFooter(Canvas canvas);

	public void drawGrid(Canvas canvas) {
		MarkerType marker;

		for (int col = 0; col < mGame.getColumns(); col++) {
			for (int row = 0; row < mGame.getRows(); row++) {

				marker = mGame.getMarkerAt(col, row, currentPlayer);

				float cx = (length + separator) * col;
				cx += offsetX;

				float cy = (length + separator) * row;
				cy += offsetY;

				drawMarker(col, row, cx, cy, marker, canvas);
			}
		}

	}

	protected float getSmallestDiameter(float x, float y) {

		if (x > y) {
			return y;
		} else {
			return x;
		}
	}

	private void loadScaledBitmaps() {
		if (isMainBitmapsLoaded) {
			mWaterBitmap =
					Bitmap.createScaledBitmap(mWaterBitmap, (int) length, (int) length, false);
			mExplosionBitmap =
					Bitmap.createScaledBitmap(mExplosionBitmap, (int) length, (int) length, false);
			isMainBitmapsScaled = true;
		}
	}


	public void setCounterValues(Canvas canvas) {

		mHeight = canvas.getHeight();
		mWidth = canvas.getWidth();
		if (!isMainBitmapsScaled) {
			loadScaledBitmaps();
		}
		lengthX =
				(float) Math.floor(mWidth
						/ (mGame.getColumns() + (mGame.getColumns() + 1) * SEPERATOR_RATIO));

		lengthY =
				(float) Math.floor((mHeight - mTitlePaint.getTextSize())
						/ (mGame.getRows() + (mGame.getRows() + 1) * SEPERATOR_RATIO));
		length = getSmallestDiameter(lengthX, lengthY);
		separator = (float) (length * SEPERATOR_RATIO);

		offsetX = (float) (Math.floor(mWidth - ((length + separator) * mGame.getColumns())) / 2);
		offsetY =
				(float) (Math.floor((mHeight - ((length + separator) * mGame.getRows()))
						+ mTitlePaint.getTextSize()) / 2);

	}



	public abstract boolean onTap(MotionEvent e);

	public abstract boolean onSwingRight();

	public abstract boolean onSwingLeft();

	public abstract void drawMarker(int col, int row, float cx, float cy, MarkerType marker,
			Canvas canvas);

	public void playHitSound() {
		PlaySoundFileTask psft = new PlaySoundFileTask();
		psft.execute(0);
	}

	public void playMissSound() {
		PlaySoundFileTask psft = new PlaySoundFileTask();
		psft.execute(1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = mDetector.onTouchEvent(event);
		return result;
	}

	class mListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {


			if (onTap(e)) {
				return true;
			} else {
				return false;
			}
		}

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD
							&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
					result = true;
				}

				result = true;

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}


		public void onSwipeTop() {
			Toast.makeText(getContext().getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
		}

		public void onSwipeRight() {
			onSwingRight();

		}

		public void onSwipeLeft() {
			onSwingLeft();
		}



	}
	private class LoadShipBitmapsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			mGame.setShipBitmaps(getContext());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			isShipsBitmapsLoaded = true;
			invalidate();
		}

	}
	private class LoadBitmapsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			mWaterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.miss);
			mExplosionBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			isMainBitmapsLoaded = true;
			invalidate();
		}

	}
	private class PlaySoundFileTask extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... args) {

			if (args[0] == 0) {
				mpHitNoise.start();
			} else if (args[0] == 1) {
				mpMissNoise.start();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			return;
		}
	}



}
