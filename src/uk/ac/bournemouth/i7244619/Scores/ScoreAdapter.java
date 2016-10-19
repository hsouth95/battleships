package uk.ac.bournemouth.i7244619.Scores;

import java.util.ArrayList;

import uk.ac.bournemouth.i7244619.Activities.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The way of showing scores in the hiscores lists.
 * 
 * @author Harrison
 *
 */
public class ScoreAdapter extends ArrayAdapter<Score> {


	public ScoreAdapter(Context context, ArrayList<Score> scores) {
		super(context, R.layout.hiscore_list_item, R.id.user, scores);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Score score = getItem(position);

		if (convertView == null) {
			convertView =
					LayoutInflater.from(getContext()).inflate(R.layout.hiscore_list_item, parent,
							false);
		}

		TextView txtUser = (TextView) convertView.findViewById(R.id.user);
		TextView txtScore = (TextView) convertView.findViewById(R.id.score);

		txtUser.setText(score.getUser());
		int userScore = score.getValue();
		String userScoreString;
		if (userScore == 0) {
			userScoreString = "--";
		} else {
			userScoreString = String.valueOf(userScore);
		}
		txtScore.setText(userScoreString);


		return convertView;

	}
}
