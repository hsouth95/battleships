package uk.ac.bournemouth.i7244619.Sound;

import uk.ac.bournemouth.i7244619.Activities.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * The background music playing throughout the game.
 * 
 * @author Harrison - i7244619
 *
 */
public class BackgroundSoundService extends Service {
	MediaPlayer player;

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		player = MediaPlayer.create(this, R.raw.background);
		player.setLooping(true); // Set looping
		player.setVolume(0.3f, 0.3f);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		player.start();
		return 1;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		player.start();
	}

	public IBinder onUnBind(Intent arg0) {
		return null;
	}

	public void onStop() {
		player.stop();
	}

	public void onPause() {
		player.stop();
	}

	@Override
	public void onDestroy() {
		player.stop();
		player.release();
	}

	@Override
	public void onLowMemory() {

	}
}
