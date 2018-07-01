package kooxda.saim.com.mybook.Utility;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import kooxda.saim.com.mybook.Activity.VIdeoPlayer;

public class ServiceDownload extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener{

    public static WeakReference<TextView> txtControlAudioName;
    public MediaPlayer mp;
    public static String songName = "";
    public static String songUrl = "";


    public ServiceDownload() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intenta, int flags, int startId) {

        songUrl = intenta.getExtras().getString("SONG_URL");
        init();

        return START_STICKY;
    }

    private void init() {
        txtControlAudioName = new WeakReference<TextView>(VIdeoPlayer.txtControlAudioName);
        songName = txtControlAudioName.get().getText().toString();

        try {
            mp.setDataSource(songUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.prepareAsync();
        mp.setOnPreparedListener(this);
        mp.setOnCompletionListener(this);
        mp.setOnBufferingUpdateListener(this);
    }


    @Override
    public void onCreate() {
        mp = new MediaPlayer();
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        Log.d("SONG PERCENT", "Song load " + percent);
        txtControlAudioName.get().setText(songName + " (" + percent + ")");
        if (percent == 100) {
            Toast.makeText(getApplicationContext(), "Downlaod Complete", Toast.LENGTH_SHORT);
            stopSelf();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mp.stop();
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.pause();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d("SAIM ERROR", "Hi im error");
        return false;
    }
}
