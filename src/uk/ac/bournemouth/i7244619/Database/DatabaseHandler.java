package uk.ac.bournemouth.i7244619.Database;

import java.util.ArrayList;
import java.util.List;

import uk.ac.bournemouth.i7244619.Scores.Score;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Is used to connect to the database and either update, insert or get the data needed from the
 * database.
 * 
 * @author Harrison
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 7;

	// Database Name
	private static final String DATABASE_NAME = "battleships";

	// Labels table name
	private static final String TABLE_USERS = "users";
	private static final String TABLE_HISCORES = "scores";
	private static final String TABLE_QUICKMATCH_SCORE = "quickmatch";

	// Users keys
	private static final String USERS_ID = "user_id";
	private static final String USERS_KEY_NAME = "user";

	// hiscore keys
	private static final String HISCORES_ID = "hiscores_id";
	private static final String HISCORES_KEY_SCORE = "score";
	private static final String HISCORES_KEY_FOREIGN_ID = USERS_ID;

	// quick match scores keys
	private static final String QUICKMATCH_ID = "quickmatch_id";
	private static final String QUICKMATCH_TURNS = "turns";
	private static final String QUICKMATCH_KEY_FOREIGN_ID = USERS_ID;



	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// Users table create query
		String CREATE_USERS_TABLE =
				"CREATE TABLE " + TABLE_USERS + "(" + USERS_ID + " INTEGER PRIMARY KEY,"
						+ USERS_KEY_NAME + " TEXT UNIQUE)";

		// Hiscores table create query
		String CREATE_HISCORES_TABLE =
				"CREATE TABLE " + TABLE_HISCORES + "(" + HISCORES_ID + " INTEGER PRIMARY KEY,"
						+ HISCORES_KEY_FOREIGN_ID + " INTEGER," + HISCORES_KEY_SCORE + " INTEGER)";
		// quick match table create query
		String CREATE_QUICKMATCH_TABLE =
				"CREATE TABLE " + TABLE_QUICKMATCH_SCORE + "(" + QUICKMATCH_ID
						+ " INTEGER PRIMARY KEY," + QUICKMATCH_KEY_FOREIGN_ID + " INTEGER,"
						+ QUICKMATCH_TURNS + " INTEGER)";

		db.execSQL(CREATE_USERS_TABLE);
		db.execSQL(CREATE_HISCORES_TABLE);
		db.execSQL(CREATE_QUICKMATCH_TABLE);



	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISCORES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUICKMATCH_SCORE);

		// Create tables again
		onCreate(db);

	}

	/**
	 * Inserting new lable into lables table
	 * */
	public void insertUser(String label) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USERS_KEY_NAME, label);

		// Inserting Row
		db.insert(TABLE_USERS, null, values);

		db = this.getReadableDatabase();
		String selectQuery =
				"SELECT " + USERS_ID + " from " + TABLE_USERS + " order by " + USERS_ID
						+ " DESC limit 1";
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			insertScore(0, cursor.getInt(cursor.getColumnIndex(USERS_ID)));
			insertQuickmatchScore(0, cursor.getInt(cursor.getColumnIndex(USERS_ID)));

		}

		cursor.close();
		db.close(); // Closing database connection
	}

	public void insertScore(int score, int userId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HISCORES_KEY_SCORE, score);
		values.put(HISCORES_KEY_FOREIGN_ID, userId);

		db.insert(TABLE_HISCORES, null, values);
		db.close();
	}

	public void insertQuickmatchScore(int score, int userId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(QUICKMATCH_TURNS, score);
		values.put(QUICKMATCH_KEY_FOREIGN_ID, userId);

		db.insert(TABLE_QUICKMATCH_SCORE, null, values);
		db.close();
	}

	public void updateQuickMatchTurns(int turns, int userId) {

		int currentTurns = getQuickmatchTurnsBy(userId);
		if (currentTurns == 0 || currentTurns > turns) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(QUICKMATCH_TURNS, turns);
			db.update(TABLE_QUICKMATCH_SCORE, values, QUICKMATCH_KEY_FOREIGN_ID + " = " + userId,
					null);
			db.close();

		}

	}


	/**
	 * Getting all labels returns list of labels
	 * */
	public List<String> getAllLabels() {
		List<String> labels = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				labels.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();

		// returning lables
		return labels;
	}

	private int getScoreBy(int id) {

		String selectQuery =
				"SELECT " + HISCORES_KEY_SCORE + " FROM " + TABLE_HISCORES + " WHERE "
						+ HISCORES_KEY_FOREIGN_ID + " = " + id;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {

			return cursor.getInt(cursor.getColumnIndex(HISCORES_KEY_SCORE));
		}
		cursor.close();
		return -1;



	}

	public int getUserId(String name) {

		String selectQuery =
				"SELECT " + USERS_ID + " FROM " + TABLE_USERS + " WHERE " + USERS_KEY_NAME + " = ?";
		int id = 0;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] {name});

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(cursor.getColumnIndex(USERS_ID));
		}
		cursor.close();
		return id;


	}

	public String getUserName(int id) {
		String selectQuery =
				"SELECT " + USERS_KEY_NAME + " FROM " + TABLE_USERS + " WHERE " + USERS_ID + " = ?";
		String name = null;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(id)});

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			name = cursor.getString(cursor.getColumnIndex(USERS_KEY_NAME));
		}
		cursor.close();
		return name;
	}

	public List<Integer> getScoresBy(int id) {
		List<Integer> scores = new ArrayList<Integer>();

		String selectQuery =
				"Select " + HISCORES_KEY_SCORE + " FROM " + TABLE_HISCORES + ", " + TABLE_USERS
						+ " WHERE " + HISCORES_KEY_FOREIGN_ID + " = " + id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				scores.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}

		cursor.close();

		return scores;
	}

	public List<Score> getAllQuickmatchTurns() {

		List<Score> scores = new ArrayList<Score>();

		String selectQuery = "SELECT * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(USERS_ID));
				int score = getQuickmatchTurnsBy(id);
				String name = getUserName(id);

				scores.add(new Score(score, name));

			} while (cursor.moveToNext());
		}

		return scores;

	}

	public List<Score> getAllScores() {
		List<Score> scores = new ArrayList<Score>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int score = getScoreBy(cursor.getInt(cursor.getColumnIndex(USERS_ID)));
				String name = cursor.getString(cursor.getColumnIndex(USERS_KEY_NAME));
				if (score != -1) {
					scores.add(new Score(score, name));
				}

			} while (cursor.moveToNext());
		}


		// closing connection
		cursor.close();
		return scores;
	}

	public int getQuickmatchTurnsBy(int id) {

		String selectQuery =
				"Select " + QUICKMATCH_TURNS + " FROM " + TABLE_QUICKMATCH_SCORE + " WHERE "
						+ HISCORES_KEY_FOREIGN_ID + " = " + id;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			return cursor.getInt(cursor.getColumnIndex(QUICKMATCH_TURNS));
		}

		return -1;

	}

	public void updateScore(String userName) {
		int score = incrementScore(userName);
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues args = new ContentValues();
		args.put(HISCORES_KEY_SCORE, score);
		synchronized (db) {
			db.update(TABLE_HISCORES, args, HISCORES_KEY_FOREIGN_ID + "=" + getUserId(userName),
					null);
		}

	}

	public int incrementScore(String userName) {
		int score;
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery =
				"SELECT " + HISCORES_KEY_SCORE + " FROM " + TABLE_HISCORES + " WHERE "
						+ HISCORES_KEY_FOREIGN_ID + " = " + getUserId(userName);

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			score = cursor.getInt(cursor.getColumnIndex(HISCORES_KEY_SCORE));
			score++;

		} else {
			return -1;
		}
		cursor.close();
		return score;
	}


}
