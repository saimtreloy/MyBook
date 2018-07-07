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

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;
import java.lang.ref.WeakReference;

import kooxda.saim.com.mybook.Activity.VIdeoPlayer;

public class ServiceDownload extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, CacheListener{

    public static WeakReference<TextView> txtControlAudioName;
    public MediaPlayer mp;

    DBHelper mydb;

    public static String S_ID = "";
    public static String S_NAME = "";
    public static String S_BANNER = "";
    public static String S_LOCATION = "";
    public static String S_TYPE = "";
    public static String S_CATEGORY = "";
    public static String S_DATE_TIME = "";


    public ServiceDownload() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intenta, int flags, int startId) {

        mydb = new DBHelper(this);

        mp = new MediaPlayer();
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        S_ID = intenta.getExtras().getString("S_ID");
        S_NAME = intenta.getExtras().getString("S_NAME");
        S_BANNER = intenta.getExtras().getString("S_BANNER");
        S_LOCATION = intenta.getExtras().getString("S_LOCATION");
        S_TYPE = intenta.getExtras().getString("S_TYPE");
        S_CATEGORY = intenta.getExtras().getString("S_CATEGORY");
        S_DATE_TIME = intenta.getExtras().getString("S_DATE_TIME");

        init();

        return START_STICKY;
    }

    private void init() {
        txtControlAudioName = new WeakReference<TextView>(VIdeoPlayer.txtControlAudioName);

        HttpProxyCacheServer proxy = App.getProxy(txtControlAudioName.get().getContext());
        proxy.registerCacheListener((CacheListener) txtControlAudioName.get().getContext(), S_LOCATION);
        String proxyUrl = proxy.getProxyUrl(S_LOCATION);

        try {
            mp.setDataSource(proxyUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkCachedState(S_LOCATION);

        mp.prepareAsync();
        mp.setOnPreparedListener(this);
        mp.setOnCompletionListener(this);
        mp.setOnBufferingUpdateListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        txtControlAudioName.get().setText(S_NAME + " (" + percent + ")");
        if (percent == 100) {
            Toast.makeText(getApplicationContext(), "Downlaod Complete", Toast.LENGTH_SHORT);
            Log.d("SAIM CACHE SER 1", percent + "%");
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
        return false;
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.d("SAIM CACHE SER 2", percentsAvailable + "%");
        if (percentsAvailable == 100 && mydb.getDataExits(Integer.parseInt(S_ID) )) {
            mydb.insertContent(Integer.parseInt(S_ID), S_NAME, S_BANNER, S_LOCATION, S_TYPE, S_CATEGORY, S_DATE_TIME);
            Toast.makeText(getApplicationContext(), "Content saved for offline 2", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }

    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = App.getProxy(this);
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached) {
            if (mydb.getDataExits(Integer.parseInt(S_ID) )) {
                mydb.insertContent(Integer.parseInt(S_ID), S_NAME, S_BANNER, S_LOCATION, S_TYPE, S_CATEGORY, S_DATE_TIME);
                Toast.makeText(getApplicationContext(), "Content saved for offline 3", Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        }
    }
}
