package uk.ac.bournemouth.i7244619.Activities;



import uk.ac.bournemouth.i7244619.Sound.BackgroundSoundService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The main menu of the application, this gives all of the different options for the user, such as
 * the game types or the different hi-scores they can access.
 * 
 * @author Harrison - i7244619
 *
 */
public class MainMenuActivity extends Activity {



	boolean isOpeningActivity = false; // for when onStop is called, to signify if we need to stop
										// service or not

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__menu);
		Intent intent = new Intent(this, BackgroundSoundService.class);
		startService(intent);

	}

	@Override
	public void onStop() {
		super.onStop();

		if (!isOpeningActivity) {
			stopService(new Intent(this, BackgroundSoundService.class));
		}
		isOpeningActivity = false;
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Intent intent = new Intent(this, BackgroundSoundService.class);
		startService(intent);
	}

	public void openGame(View view) {

		isOpeningActivity = true;

		Intent intent = new Intent(this, TwoPlayerChooseActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_down_in, R.anim.enter_down_out);


	}

	public void openQuickGame(View view) {
		isOpeningActivity = true;

		Intent intent = new Intent(this, OnePlayerChooseActivity.class);
		intent.putExtra("Quick Game", false);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_down_in, R.anim.enter_down_out);
	}

	public void openHiscores(View view) {
		isOpeningActivity = true;

		Intent intent = new Intent(this, HiscoreActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_down_in, R.anim.enter_down_out);
	}

	public void openQuickScores(View view) {
		isOpeningActivity = true;

		Intent intent = new Intent(this, QuickscoreActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_down_in, R.anim.enter_down_out);
	}

	public void openComputerGame(View view) {
		isOpeningActivity = true;

		Intent intent = new Intent(this, OnePlayerChooseActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_down_in, R.anim.enter_down_out);
	}



}
