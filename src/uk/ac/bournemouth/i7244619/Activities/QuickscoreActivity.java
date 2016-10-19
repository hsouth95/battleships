package uk.ac.bournemouth.i7244619.Activities;

import java.util.ArrayList;

import uk.ac.bournemouth.i7244619.Database.DatabaseHandler;
import uk.ac.bournemouth.i7244619.Scores.Score;
import uk.ac.bournemouth.i7244619.Scores.ScoreAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * This activity shows the list of the turns each player has taken in playing the quick match
 * version of the game.
 * 
 * @author Harrison
 *
 */
public class QuickscoreActivity extends ListActivity {


	private ArrayAdapter<Score> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quickscore);

		loadQuickScores();
		setListAdapter(mAdapter);


	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_up_in, R.anim.exit_up_out);
	}

	public void loadQuickScores() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		ArrayList<Score> scores = (ArrayList<Score>) db.getAllQuickmatchTurns();

		mAdapter = new ScoreAdapter(this, scores);


	}
}
