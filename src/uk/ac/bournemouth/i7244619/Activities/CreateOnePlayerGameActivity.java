package uk.ac.bournemouth.i7244619.Activities;

import uk.ac.bournemouth.i7244619.View.CreateTwoBoardView;
import uk.ac.bournemouth.i7244619.util.ShakeListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * This activity is used for when it is a Player vs. X situation and therefore is extendable for
 * future use. It used for the player to create their boards.
 * 
 * @author Harrison - i7244619
 *
 */
public class CreateOnePlayerGameActivity extends Activity {


	private String playerTitle;
	private ShakeListener mShaker;
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);


		Intent intent = getIntent();

		if (intent.hasExtra("Player Name")) {
			playerTitle = intent.getStringExtra("Player Name");
		}

		view = (CreateTwoBoardView) findViewById(R.id.createOneBoardView);
		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				((CreateTwoBoardView) view).createRandomBoard();
			}
		});

		setContentView(R.layout.activity_create_one_player_game);



	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_right_in, R.anim.exit_right_out);
	}

	public String getName() {
		return playerTitle;
	}
}
