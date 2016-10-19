package uk.ac.bournemouth.i7244619.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

/**
 * This is the launched activity that the user is faced with when they open the application, it
 * shows what is in the image view and disappears after a short while.
 * 
 * @author Harrison - i7244619
 *
 */
public class SplashScreenActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
		 */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Create an Intent that will start the Menu-Activity.
				Intent mainIntent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
				startActivity(mainIntent);
				finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

}
