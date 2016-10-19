package uk.ac.bournemouth.i7244619.Activities;

import uk.ac.bournemouth.i7244619.Game.Player;
import uk.ac.bournemouth.i7244619.Ship.Ship;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.WindowManager;

/**
 * This is the activity for a Player vs. Player playing board in which they play against eachother
 * after having created their own boards.
 * 
 * @author Harrison - i7244619
 *
 */
public class TwoPlayerGameActivity extends Activity {

	private Ship[] player1Ships;
	private Ship[] player2Ships;

	private String player1Title;
	private String player2Title;
	private boolean hasShips = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = this.getIntent();

		if (intent.hasExtra("Player 1 Ships") && intent.hasExtra("Player 1 Ships")
				&& intent.hasExtra("Player 2 Ships") && intent.hasExtra("Player 2 Ships")) {
			Parcelable[] p1 = intent.getParcelableArrayExtra("Player 1 Ships");
			Parcelable[] p2 = intent.getParcelableArrayExtra("Player 2 Ships");

			player1Title = intent.getStringExtra("Player 1 Name");
			player2Title = intent.getStringExtra("Player 2 Name");

			player1Ships = new Ship[p1.length];
			player2Ships = new Ship[p2.length];

			for (int i = 0; i < p1.length; i++) {
				player1Ships[i] = (Ship) p1[i];
				player2Ships[i] = (Ship) p2[i];
			}
			hasShips = true;
		}

		setContentView(R.layout.activity_two_player_game);
	}

	public boolean hasShips() {
		return hasShips;
	}

	public Ship[] getShips(Player player) {

		if (player == Player.ONE) {
			return player1Ships;
		} else {
			return player2Ships;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_right_in, R.anim.exit_right_out);
	}

	public String getPlayerTitle(Player player) {
		if (player == Player.ONE) {
			return player1Title;
		} else {
			return player2Title;
		}
	}


}
