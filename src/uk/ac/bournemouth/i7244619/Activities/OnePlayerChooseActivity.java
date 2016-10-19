package uk.ac.bournemouth.i7244619.Activities;

import java.util.List;

import uk.ac.bournemouth.i7244619.Database.DatabaseHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the activity where if only one player is needed then they can pick only one name allowing
 * them to pass to their designated game with a name.
 * 
 * @author Harrison - i7244619
 *
 */
public class OnePlayerChooseActivity extends Activity implements OnItemSelectedListener {

	Spinner playerSpinner;
	Button btnGo;
	EditText etPlayerName;
	String name;
	int size;

	boolean goToCreateBoard = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		if (intent.hasExtra("Quick Game")) {
			goToCreateBoard = false;
		} else {
			goToCreateBoard = true;
		}

		setContentView(R.layout.activity_one_player_choose);
		playerSpinner = (Spinner) findViewById(R.id.player_spinner1);

		btnGo = (Button) findViewById(R.id.submit_choice);
		etPlayerName = (EditText) findViewById(R.id.input_label);
		playerSpinner.setOnItemSelectedListener(this);
		loadSpinnerData();

		btnGo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String label = etPlayerName.getText().toString();

				if (label.trim().length() > 0) {
					name = label;
					AlertDialog.Builder builder =
							new AlertDialog.Builder(OnePlayerChooseActivity.this);
					builder.setMessage("Are you sure?").setTitle("Error!")
							.setPositiveButton("Yes", dialogClickListener)
							.setNegativeButton("No", dialogClickListener).show();

				} else if (size == 0) {

					AlertDialog.Builder builder =
							new AlertDialog.Builder(OnePlayerChooseActivity.this);
					builder.setMessage("You need to add a player!");
					builder.show();


				} else {
					name = (String) playerSpinner.getSelectedItem();
					startIntent(name);
				}
				loadSpinnerData();

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_up_in, R.anim.exit_up_out);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		return;

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

		return;
	}

	private void loadSpinnerData() {
		// database handler
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		// Spinner Drop down elements
		List<String> lables = db.getAllLabels();

		if (lables.size() == 0) {
			playerSpinner.setClickable(false);
			size = 0;
			return;
		} else {
			size = lables.size();
		}

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter =
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button


		// attaching data adapter to spinner
		playerSpinner.setAdapter(dataAdapter);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public void startIntent(String name) {
		Intent intent;

		if (goToCreateBoard) {
			intent = new Intent(getApplicationContext(), CreateOnePlayerGameActivity.class);
		} else {
			intent = new Intent(getApplicationContext(), QuickGameActivity.class);
		}
		intent.putExtra("Player Name", name);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_left_in, R.anim.enter_left_out);
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// database handler
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());

					// inserting new label into database
					db.insertUser(name);
					db.close();
					// making input filed text to blank
					etPlayerName.setText("");

					// Hiding the keyboard
					InputMethodManager imm =
							(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etPlayerName.getWindowToken(), 0);

					startIntent(name);

					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
			}
		}
	};
}
