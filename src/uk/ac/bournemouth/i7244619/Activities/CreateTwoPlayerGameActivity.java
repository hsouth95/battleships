package uk.ac.bournemouth.i7244619.Activities;

import uk.ac.bournemouth.i7244619.Game.Player;
import uk.ac.bournemouth.i7244619.Sound.BackgroundSoundService;
import uk.ac.bournemouth.i7244619.View.CreateTwoBoardView;
import uk.ac.bournemouth.i7244619.util.ShakeListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * This activity is used when it is player vs. player, in this they will create their boards.
 * 
 * @author Harrison - i7244619
 *
 */
public class CreateTwoPlayerGameActivity extends Activity {

	String player1Name;
	String player2Name;

	private ShakeListener mShaker;
	private CreateTwoBoardView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();

		if (intent.hasExtra("Player Name 1") && intent.hasExtra("Player Name 2")) {
			player1Name = intent.getStringExtra("Player Name 1");
			player2Name = intent.getStringExtra("Player Name 2");
		}



		setContentView(R.layout.activity_create_two_player_game);

		view = (CreateTwoBoardView) findViewById(R.id.createGameView);
		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				view.createRandomBoard();
			}
		});

		Intent musicIntent = new Intent(this, BackgroundSoundService.class);
		startService(musicIntent);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_right_in, R.anim.exit_right_out);
	}

	public String getPlayerTitle(Player player) {
		if (player == Player.ONE) {
			return player1Name;
		} else {
			return player2Name;
		}
	}



}
