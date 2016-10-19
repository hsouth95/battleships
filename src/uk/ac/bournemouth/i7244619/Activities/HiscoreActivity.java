package uk.ac.bournemouth.i7244619.Activities;

import java.util.ArrayList;

import uk.ac.bournemouth.i7244619.Database.DatabaseHandler;
import uk.ac.bournemouth.i7244619.Scores.Score;
import uk.ac.bournemouth.i7244619.Scores.ScoreAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * This is an activity of the hiscores for the player, it accesses the database to display every
 * user and how many games they have won.
 * 
 * @author Harrison - i7244619
 *
 */
public class HiscoreActivity extends ListActivity {


	private ArrayAdapter<Score> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hiscore);

		loadHiscores();
		setListAdapter(mAdapter);



	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_up_in, R.anim.exit_up_out);
	}


	private void loadHiscores() {

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		ArrayList<Score> scores = (ArrayList<Score>) db.getAllScores();

		mAdapter = new ScoreAdapter(this, scores);


	}
}
