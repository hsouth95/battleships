package uk.ac.bournemouth.i7244619.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * This activity displays a message to the user depending on what occured in the game activities.
 * 
 * @author Harrison
 *
 */
public class ResultActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();
		String message;
		if (intent.hasExtra("Message")) {
			message = intent.getStringExtra("Message");
		} else {
			message = "Well done you won!";
		}

		setContentView(R.layout.activity_result);

		TextView tv = (TextView) findViewById(R.id.result_title);
		tv.setText(message);
	}

	public void goMainMenu(View view) {
		Intent intent = new Intent(this, MainMenuActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.exit_up_in, R.anim.exit_up_out);
	}

}
