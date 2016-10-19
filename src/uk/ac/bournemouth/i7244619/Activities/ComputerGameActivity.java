package uk.ac.bournemouth.i7244619.Activities;

import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.WindowManager;

/**
 * This activity is for player vs. computer game, after the player has created their board.
 * 
 * @author Harrison - i7244619
 *
 */
public class ComputerGameActivity extends Activity {


	private String playerName;
	private Ship[] player1Ships;
	private boolean hasShips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();

		if (intent.hasExtra("Player 1 Ships") && intent.hasExtra("Player 1 Name")) {
			Parcelable[] p1 = intent.getParcelableArrayExtra("Player 1 Ships");

			playerName = intent.getStringExtra("Player 1 Name");
			player1Ships = new Ship[p1.length];

			for (int i = 0; i < p1.length; i++) {
				player1Ships[i] = (Ship) p1[i];
			}
			hasShips = true;

		}

		setContentView(R.layout.activity_computer_game);
	}

	public boolean hasShips() {
		return hasShips;
	}

	public Ship[] getShips() {
		return player1Ships;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_right_in, R.anim.exit_right_out);
	}

	public String getName() {
		return playerName;
	}
}
