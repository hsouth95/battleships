package uk.ac.bournemouth.i7244619.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * This is the activity for the unusual game where the player goes aginast a random board, to see
 * how many turns it takes to beat it.
 * 
 * @author Harrison - i7244619
 *
 */
public class QuickGameActivity extends Activity {

	private String mPlayerName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (intent.hasExtra("Player Name")) {
			mPlayerName = intent.getStringExtra("Player Name");

		}

		setContentView(R.layout.activity_quickmatch);



	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_right_in, R.anim.exit_right_out);
	}


	public String getName() {
		return mPlayerName;
	}



}
