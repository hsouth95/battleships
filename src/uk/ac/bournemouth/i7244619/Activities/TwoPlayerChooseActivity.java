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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This is the activity where if only two players are needed then they can pick only two names
 * allowing them to pass to their designated game with a name.
 * 
 * @author Harrison - i7244619
 *
 */
public class TwoPlayerChooseActivity extends Activity implements OnItemSelectedListener {

	Spinner playerSpinner1;
	Spinner playerSpinner2;
	Button btnGo;
	EditText etPlayerName1;
	EditText etPlayerName2;
	String name1;
	String name2;

	boolean isName1New = false;
	boolean isName2New = false;
	int size;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_player_choose);

		playerSpinner1 = (Spinner) findViewById(R.id.player_spinner1);
		playerSpinner2 = (Spinner) findViewById(R.id.player_spinner2);
		btnGo = (Button) findViewById(R.id.submit_choice);
		etPlayerName1 = (EditText) findViewById(R.id.input_label);
		etPlayerName2 = (EditText) findViewById(R.id.input_label2);

		loadSpinnerData();

		btnGo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String label1 = etPlayerName1.getText().toString();
				String label2 = etPlayerName2.getText().toString();



				if (label1.trim().length() > 0 && label2.trim().length() > 0) {
					name1 = label1;
					name2 = label2;

					isName1New = true;
					isName2New = true;

					AlertDialog.Builder builder =
							new AlertDialog.Builder(TwoPlayerChooseActivity.this);
					builder.setMessage("Are you sure?")
							.setPositiveButton("Yes", dialogClickListener)
							.setNegativeButton("No", dialogClickListener).show();

				} else if (label1.trim().length() > 0) {
					name1 = label1;
					isName1New = true;
					name2 = (String) playerSpinner2.getSelectedItem();

					AlertDialog.Builder builder =
							new AlertDialog.Builder(TwoPlayerChooseActivity.this);
					builder.setMessage("Are you sure?")
							.setPositiveButton("Yes", dialogClickListener)
							.setNegativeButton("No", dialogClickListener).show();

				} else if (label2.trim().length() > 0) {
					name1 = (String) playerSpinner1.getSelectedItem();
					name2 = label2;
					isName2New = true;

					AlertDialog.Builder builder =
							new AlertDialog.Builder(TwoPlayerChooseActivity.this);
					builder.setMessage("Are you sure?")
							.setPositiveButton("Yes", dialogClickListener)
							.setNegativeButton("No", dialogClickListener).show();

				} else if (size == 0) {

					AlertDialog.Builder builder =
							new AlertDialog.Builder(TwoPlayerChooseActivity.this);
					builder.setMessage("You need to add a player!");
					builder.setNeutralButton("Close", null).show();
					builder.show();

				} else {
					name1 = (String) playerSpinner1.getSelectedItem();
					name2 = (String) playerSpinner2.getSelectedItem();

					if (name1.equals(name2)) {
						new AlertDialog.Builder(TwoPlayerChooseActivity.this).setTitle("Error")
								.setMessage("Cannot use the same player, please choose another.")
								.setNeutralButton("Close", null).show();
						return;
					}
					startIntent(name1, name2);
				}

			}
		});


	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		return;

	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		return;

	}

	public void startIntent(String name1, String name2) {
		Intent intent = new Intent(getApplicationContext(), CreateTwoPlayerGameActivity.class);
		intent.putExtra("Player Name 1", name1);
		intent.putExtra("Player Name 2", name2);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_left_in, R.anim.enter_left_out);
	}

	private void loadSpinnerData() {
		// database handler
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		// Spinner Drop down elements
		List<String> lables = db.getAllLabels();

		if (lables.size() == 0) {
			size = 0;
			playerSpinner1.setClickable(false);
			playerSpinner2.setClickable(false);
		} else {
			size = lables.size();
		}

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter =
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button


		// attaching data adapter to spinner
		playerSpinner1.setAdapter(dataAdapter);
		playerSpinner2.setAdapter(dataAdapter);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.exit_up_in, R.anim.exit_up_out);
	}



	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// database handler
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());



					if (isName1New) {
						db.insertUser(name1);
					}
					if (isName2New) {
						db.insertUser(name2);
					}

					// making input filed text to blank
					etPlayerName1.setText("");
					etPlayerName2.setText("");

					// Hiding the keyboard
					InputMethodManager imm =
							(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etPlayerName1.getWindowToken(), 0);
					imm.hideSoftInputFromInputMethod(etPlayerName2.getWindowToken(), 0);



					startIntent(name1, name2);

					break;

				case DialogInterface.BUTTON_NEGATIVE:

					isName1New = false;
					isName2New = false;

					etPlayerName1.setText("");
					etPlayerName2.setText("");

					break;
			}
		}
	};

}
