package kooxda.saim.com.mybook.Activity;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import kooxda.saim.com.mybook.Adapter.AdapterPlayer;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.App;
import kooxda.saim.com.mybook.Utility.DBHelper;
import kooxda.saim.com.mybook.Utility.ServiceDownload;
import kooxda.saim.com.mybook.Utility.Utility;

public class VIdeoPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, View.OnClickListener, CacheListener {

    public static CoordinatorLayout mainLayout;
    private DBHelper mydb;

    VideoView videoView;
    ProgressBar progVidVideo;

    RelativeLayout layoutVideoPlayer, layoutvidVideo;
    LinearLayout layoutVideoController, layoutVideoControllerTop;
    ImageView imgControlPlay, imgControlPrevious, imgControlNext, imgControlFullScreen;
    TextView txtControlCurrent, txtControlEnd, txtControlVideoName;
    SeekBar seekBarControl;

    //Audio Layout
    RelativeLayout layoutAudioPlayer;
    ImageView layoutAudioPlaceholder;
    ProgressBar progVidAudio;
    public static TextView txtControlAudioName, txtControlCurrentAudio, txtControlEndAudio;
    ImageView imgControlPlayAudio;
    SeekBar seekBarControlAudio;

    static Handler progressBarHandlerAudio = new Handler();
    public static MediaPlayer mpAudio;
    private static boolean isPause = false;
    //Audio layiut end

    private static MediaPlayer mediaPlayer;
    static Handler progressBarHandler = new Handler();

    public boolean isFullscreen = false;
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    public static String contentType = "";
    public static int contentPosition;
    public static String jsonAdapterList = "";
    public static ArrayList<ModelContent> modelContentArrayList = new ArrayList<>();

    public static String audioUrl = "";
    public static String audioTitle = "";
    public static String audioCover = "";
    public static String videoUrl = "";
    public static String videoTitle = "";

    ArrayList<ModelContent> modelContents = new ArrayList<>();
    RecyclerView recyclerViewContentVideoLayout;
    RecyclerView.LayoutManager layoutManagerContentVideoLayout;
    RecyclerView.Adapter contentVideoLayoutAdapter;

    ImageView imgDownloadAndSave, imgDownloadAndSaveV;

    public boolean isDownload = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setTheme(R.style.AppThemeMainActivity);
        setContentView(R.layout.activity_video_player);

        mydb = new DBHelper(this);

        contentType = getIntent().getExtras().getString("TYPE");
        contentPosition = getIntent().getExtras().getInt("POSITION");
        jsonAdapterList = getIntent().getExtras().getString("LIST");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ModelContent>>() {
        }.getType();
        modelContentArrayList = gson.fromJson(jsonAdapterList, type);

        String appPath = getApplicationContext().getFilesDir().getAbsolutePath();
        Log.i("SAIM SAIM DIRECTORY", appPath);

        Init();
    }

    public void Init() {

        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);

        videoView = (VideoView) findViewById(R.id.videoView);
        progVidVideo = (ProgressBar) findViewById(R.id.progVidVideo);

        layoutVideoPlayer = (RelativeLayout) findViewById(R.id.layoutVideoPlayer);
        layoutvidVideo = (RelativeLayout) findViewById(R.id.layoutvidVideo);

        layoutVideoController = (LinearLayout) findViewById(R.id.layoutVideoController);
        layoutVideoControllerTop = (LinearLayout) findViewById(R.id.layoutVideoControllerTop);

        imgControlPlay = (ImageView) findViewById(R.id.imgControlPlay);
        imgControlPrevious = (ImageView) findViewById(R.id.imgControlPrevious);
        imgControlNext = (ImageView) findViewById(R.id.imgControlNext);
        imgControlFullScreen = (ImageView) findViewById(R.id.imgControlFullScreen);

        txtControlCurrent = (TextView) findViewById(R.id.txtControlCurrent);
        txtControlEnd = (TextView) findViewById(R.id.txtControlEnd);
        txtControlVideoName = (TextView) findViewById(R.id.txtControlVideoName);

        seekBarControl = (SeekBar) findViewById(R.id.seekBarControl);

        //Audio Layout
        layoutAudioPlayer = (RelativeLayout) findViewById(R.id.layoutAudioPlayer);
        layoutAudioPlaceholder = (ImageView) findViewById(R.id.layoutAudioPlaceholder);
        progVidAudio = (ProgressBar) findViewById(R.id.progVidAudio);
        txtControlAudioName = (TextView) findViewById(R.id.txtControlAudioName);
        txtControlCurrentAudio = (TextView) findViewById(R.id.txtControlCurrentAudio);
        txtControlEndAudio = (TextView) findViewById(R.id.txtControlEndAudio);
        imgControlPlayAudio = (ImageView) findViewById(R.id.imgControlPlayAudio);
        imgControlPlayAudio.setOnClickListener(this);
        seekBarControlAudio = (SeekBar) findViewById(R.id.seekBarControlAudio);
        seekBarControlAudio.setOnSeekBarChangeListener(this);

        imgDownloadAndSave = (ImageView) findViewById(R.id.imgDownloadAndSave);
        imgDownloadAndSaveV = (ImageView) findViewById(R.id.imgDownloadAndSaveV);

        mpAudio = new MediaPlayer();
        mpAudio.reset();
        mpAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //Audio Layout End


        recyclerViewContentVideoLayout = (RecyclerView) findViewById(R.id.recyclerViewContentVideoLayout);
        recyclerViewContentVideoLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewContentVideoLayout.setHasFixedSize(true);
        contentVideoLayoutAdapter = new AdapterPlayer(modelContentArrayList);
        recyclerViewContentVideoLayout.setAdapter(contentVideoLayoutAdapter);

        if (contentType.equals("Audio")) {
            layoutvidVideo.setVisibility(View.GONE);
            layoutAudioPlayer.setVisibility(View.VISIBLE);
            audioUrl = getIntent().getExtras().getString("URL");
            audioTitle = getIntent().getExtras().getString("TITLE");
            audioCover = getIntent().getExtras().getString("COVER");

            playSong(audioUrl, audioTitle, audioCover);
            checkCachedState(audioUrl);
        } else {
            layoutAudioPlayer.setVisibility(View.GONE);
            layoutvidVideo.setVisibility(View.VISIBLE);

            videoUrl = getIntent().getExtras().getString("URL");
            videoTitle = getIntent().getExtras().getString("TITLE");

            settingVideoView(videoUrl, videoTitle);
            checkCachedState(videoUrl);
        }



        VideoController();


        imgDownloadAndSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
                boolean fullyCached = proxy.isCached(modelContentArrayList.get(contentPosition).getLocation());
                if (fullyCached) {
                    CompletNotification();
                    isDownload = false;
                    mydb.insertContent(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()),
                            modelContentArrayList.get(contentPosition).getName(),
                            modelContentArrayList.get(contentPosition).getBanner(),
                            modelContentArrayList.get(contentPosition).getLocation(),
                            modelContentArrayList.get(contentPosition).getType(),
                            modelContentArrayList.get(contentPosition).getCategory(),
                            modelContentArrayList.get(contentPosition).getDate_time());
                    Toast.makeText(getApplicationContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                } else {
                    if (mydb.getDataExits(Integer.parseInt(modelContentArrayList.get(contentPosition).getId())) == false) {
                        Toast.makeText(getApplicationContext(), "Content already in ofline mode", Toast.LENGTH_SHORT).show();
                    } else {
                        isDownload = true;
                    }
                }

            }
        });

        imgDownloadAndSaveV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
                boolean fullyCached = proxy.isCached(modelContentArrayList.get(contentPosition).getLocation());
                if (fullyCached) {
                    CompletNotification();
                    isDownload = false;
                    mydb.insertContent(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()),
                            modelContentArrayList.get(contentPosition).getName(),
                            modelContentArrayList.get(contentPosition).getBanner(),
                            modelContentArrayList.get(contentPosition).getLocation(),
                            modelContentArrayList.get(contentPosition).getType(),
                            modelContentArrayList.get(contentPosition).getCategory(),
                            modelContentArrayList.get(contentPosition).getDate_time());
                    Toast.makeText(getApplicationContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                } else {
                    if (mydb.getDataExits(Integer.parseInt(modelContentArrayList.get(contentPosition).getId())) == false) {
                        Toast.makeText(getApplicationContext(), "Content already in ofline mode", Toast.LENGTH_SHORT).show();
                    } else {
                        isDownload = true;
                    }
                }
            }
        });

    }


    public void settingVideoView(String vURL, String vTITLE) {
        isDownload = false;
        HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
        proxy.registerCacheListener(this, vURL);
        String proxyUrl = proxy.getProxyUrl(vURL);
        Uri uri = Uri.parse(proxyUrl);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        txtControlVideoName.setText(vTITLE);

        layoutvidVideo.setVisibility(View.VISIBLE);
        progVidVideo.setVisibility(View.VISIBLE);
        layoutVideoController.setVisibility(View.GONE);
        layoutVideoControllerTop.setVisibility(View.GONE);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        progVidVideo.setVisibility(View.GONE);
                        mediaPlayer = mp;
                        updateProgressBar();
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        progVidVideo.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        progVidVideo.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVisibility(View.VISIBLE);
                layoutvidVideo.setVisibility(View.VISIBLE);
                imgControlPlay.setImageResource(R.drawable.ic_play);
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (isFullscreen == false) {
                        enterFullScreen();
                        isFullscreen = true;
                        imgControlFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
                    } else if (isFullscreen == true) {
                        exitFullScreen();
                        isFullscreen = false;
                        imgControlFullScreen.setImageResource(R.drawable.ic_fullscreen);
                    }
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                layoutVideoController.setVisibility(View.VISIBLE);
                layoutVideoControllerTop.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutVideoController.setVisibility(View.GONE);
                        layoutVideoControllerTop.setVisibility(View.GONE);
                    }
                }, 2500);
                return true;
            }
        });
    }

    public void VideoController() {
        imgControlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    imgControlPlay.setImageResource(R.drawable.ic_play);
                } else {
                    videoView.start();
                    imgControlPlay.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        imgControlPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgControlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgControlFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullscreen == false) {
                    enterFullScreen();
                    isFullscreen = true;
                    imgControlFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
                } else if (isFullscreen == true) {
                    exitFullScreen();
                    isFullscreen = false;
                    imgControlFullScreen.setImageResource(R.drawable.ic_fullscreen);
                }
            }
        });

        seekBarControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressBarHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = Utility.progressToTimer(seekBar.getProgress(), totalDuration);
                mediaPlayer.seekTo(currentPosition);
                updateProgressBar();
            }
        });
    }

    private void enterFullScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutVideoPlayer.getLayoutParams().height = metrics.widthPixels;
        layoutVideoPlayer.getLayoutParams().width = metrics.heightPixels;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        videoView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
    }

    private void exitFullScreen() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_VISIBLE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutVideoPlayer.getLayoutParams().height = (220 * (metrics.densityDpi / 160));
        layoutVideoPlayer.getLayoutParams().width = metrics.heightPixels;

        //layoutVideoPlayer.getLayoutParams().height = metrics.widthPixels;
        videoView.setLayoutParams(layoutParams);
    }

    public void updateProgressBar() {
        try {
            progressBarHandler.postDelayed(mUpdateTimeTask, 100);
        } catch (Exception e) {

        }
    }

    Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = 0;
            long currentDuration = 0;

            try {
                totalDuration = mediaPlayer.getDuration();
                currentDuration = mediaPlayer.getCurrentPosition();
                txtControlEnd.setText(Utility.milliSecondsToTimer(totalDuration));
                txtControlCurrent.setText(Utility.milliSecondsToTimer(currentDuration));
                final int progress = (int) (Utility.getProgressPercentage(currentDuration, totalDuration));
                seekBarControl.setProgress(progress);
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        seekBarControl.setSecondaryProgress(percent);
                        if (percent > 99 && mydb.getDataExits(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()))) {
                            mydb.insertContent(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()),
                                    modelContentArrayList.get(contentPosition).getName(),
                                    modelContentArrayList.get(contentPosition).getBanner(),
                                    modelContentArrayList.get(contentPosition).getLocation(),
                                    modelContentArrayList.get(contentPosition).getType(),
                                    modelContentArrayList.get(contentPosition).getCategory(),
                                    modelContentArrayList.get(contentPosition).getDate_time());

                            Toast.makeText(getApplicationContext(), "Content saved for offline", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                progressBarHandler.postDelayed(this, 50);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void updateProgressBarAudio() {
        try {
            progressBarHandlerAudio.postDelayed(mUpdateTimeTaskAudio, 100);
        } catch (Exception e) {

        }
    }

    Runnable mUpdateTimeTaskAudio = new Runnable() {
        public void run() {
            long totalDuration = 0;
            long currentDuration = 0;

            try {
                totalDuration = mpAudio.getDuration();
                currentDuration = mpAudio.getCurrentPosition();
                txtControlCurrentAudio.setText(Utility.milliSecondsToTimer(currentDuration));
                txtControlEndAudio.setText(Utility.milliSecondsToTimer(totalDuration));
                final int progress = (int) (Utility.getProgressPercentage(currentDuration, totalDuration));
                seekBarControlAudio.setProgress(progress);
                progressBarHandlerAudio.postDelayed(this, 100);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgControlPlayAudio:
                if (mpAudio.isPlaying()) {
                    mpAudio.pause();
                    isPause = true;
                    progressBarHandler.removeCallbacks(mUpdateTimeTask);
                    imgControlPlayAudio.setImageResource(R.drawable.ic_play);
                    return;
                }

                if (isPause) {
                    mpAudio.start();
                    isPause = false;
                    updateProgressBar();
                    imgControlPlayAudio.setImageResource(R.drawable.ic_pause);
                    return;
                }
                break;
        }


    }

    public void playSong(String aURL, String aTITLE, String aCOVER) {
        Log.d("I AM URL", aURL);
        isDownload = false;
        seekBarControlAudio.setEnabled(false);
        mpAudio.stop();
        mpAudio.reset();
        progressBarHandlerAudio.removeCallbacks(mUpdateTimeTask);
        try {
            txtControlAudioName.setText(aTITLE);

            HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
            proxy.registerCacheListener(this, aURL);
            String proxyUrl = proxy.getProxyUrl(aURL);

            //mpAudio.setDataSource(aURL);
            mpAudio.setDataSource(proxyUrl);
            Picasso.with(getApplicationContext()).
                    load(aCOVER).
                    placeholder(R.drawable.ic_logo).
                    error(R.drawable.ic_logo).
                    into(layoutAudioPlaceholder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpAudio.prepareAsync();
        mpAudio.setOnPreparedListener(this);
        mpAudio.setOnCompletionListener(this);
        mpAudio.setOnBufferingUpdateListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mpAudio.stop();
        progressBarHandlerAudio.removeCallbacks(mUpdateTimeTaskAudio);
        seekBarControlAudio.setProgress(0);
        imgControlPlayAudio.setImageResource(R.drawable.ic_play);
        txtControlCurrentAudio.setText("00.00");
        txtControlEndAudio.setText("00.00");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressBarHandlerAudio.removeCallbacks(mUpdateTimeTaskAudio);
        int totalDuration = mpAudio.getDuration();
        int currentPosition = Utility.progressToTimer(seekBar.getProgress(), totalDuration);
        mpAudio.seekTo(currentPosition);
        updateProgressBarAudio();
    }


    @Override
    public void onPrepared(MediaPlayer player) {
        seekBarControlAudio.setEnabled(true);
        player.start();
        imgControlPlayAudio.setImageResource(R.drawable.ic_pause);

        updateProgressBarAudio();
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarControlAudio.setSecondaryProgress(percent);
        if (percent == 0) {
            progVidAudio.setVisibility(View.VISIBLE);
        } else {
            progVidAudio.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }


    @Override
    public void onBackPressed() {
        if (isFullscreen == true) {
            isFullscreen = false;
            exitFullScreen();
        } else {
            mpAudio.stop();
            mpAudio.release();
            videoView.stopPlayback();
            progressBarHandlerAudio.removeCallbacks(mUpdateTimeTaskAudio);
            progressBarHandler.removeCallbacks(mUpdateTimeTask);
            finish();
        }
    }

    //RECEIVER
    private final BroadcastReceiver PlayContentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String videoType_N = intent.getExtras().getString("TYPE");
            String videoUrl_N = intent.getExtras().getString("URL");
            String videoTitle_N = intent.getExtras().getString("TITLE");
            String videoCover_N = intent.getExtras().getString("COVER");

            videoView.stopPlayback();
            progressBarHandler.removeCallbacks(mUpdateTimeTask);
            progressBarHandlerAudio.removeCallbacks(mUpdateTimeTaskAudio);

            if (videoType_N.equals("Audio")) {
                layoutvidVideo.setVisibility(View.GONE);
                layoutAudioPlayer.setVisibility(View.VISIBLE);
                progVidAudio.setVisibility(View.VISIBLE);
                txtControlCurrent.setText("00:00");
                txtControlEnd.setText("00:00");
                playSong(videoUrl_N, videoTitle_N, videoCover_N);
            } else {
                layoutAudioPlayer.setVisibility(View.GONE);
                layoutvidVideo.setVisibility(View.VISIBLE);
                settingVideoView(videoUrl_N, videoTitle_N);
            }

            Toast.makeText(getApplicationContext(), "Starting " + videoTitle_N, Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(PlayContentReceiver, new IntentFilter("kooxda.saim.com.mybook.PlayContentReceiver"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(PlayContentReceiver);
        App.getProxy(this).unregisterCacheListener(this);
        //deleteFiles(getExternalCacheDir().getAbsolutePath());
    }


    public void DownloadFile() {
        DownloadManager downloadManager;
        long downloadReference;

        String song_download_link = "";
        String file_name = "";
        if (contentType.equals("Audio")) {
            song_download_link = audioUrl;
            file_name = audioTitle;
        } else {
            song_download_link = videoUrl;
            file_name = videoTitle;
        }

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(song_download_link);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setAllowedOverRoaming(false);
        request.setTitle(file_name);
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setVisibleInDownloadsUi(false);
        request.setDestinationInExternalFilesDir(getApplicationContext(), "/.IAMOK", file_name + ".bin");
        downloadReference = downloadManager.enqueue(request);

        Toast.makeText(getApplicationContext(), "Download Starting ", Toast.LENGTH_SHORT).show();

        String appPath = getApplicationContext().getFilesDir().getAbsolutePath() + "/.IAMOK/";
        mydb.insertContent(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()),
                modelContentArrayList.get(contentPosition).getName(),
                modelContentArrayList.get(contentPosition).getBanner(),
                appPath + modelContentArrayList.get(contentPosition).getName() + ".bin",
                modelContentArrayList.get(contentPosition).getType(),
                modelContentArrayList.get(contentPosition).getCategory(),
                modelContentArrayList.get(contentPosition).getDate_time());
        sendBroadcast(new Intent().setAction("android.intent.action.DOWNLOAD_COMPLETE").putExtra("SAIM", modelContentArrayList.get(contentPosition).getName()));
        Intent intent = new Intent(getApplicationContext(), ServiceDownload.class);
        startService(intent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        seekBarControlAudio.setSecondaryProgress(percentsAvailable);
        Log.d("SAIM CACHE ", percentsAvailable + " %");

        if (isDownload == true) {
            if (percentsAvailable == 100 && mydb.getDataExits(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()))) {
                CompletNotification();
                isDownload = false;
                mydb.insertContent(Integer.parseInt(modelContentArrayList.get(contentPosition).getId()),
                        modelContentArrayList.get(contentPosition).getName(),
                        modelContentArrayList.get(contentPosition).getBanner(),
                        modelContentArrayList.get(contentPosition).getLocation(),
                        modelContentArrayList.get(contentPosition).getType(),
                        modelContentArrayList.get(contentPosition).getCategory(),
                        modelContentArrayList.get(contentPosition).getDate_time());
                Toast.makeText(getApplicationContext(), "Download Complete", Toast.LENGTH_SHORT).show();
            } else if (percentsAvailable < 100){
                AddNotification(percentsAvailable);
            }
        }

    }

    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = App.getProxy(this);
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached) {

        }
    }

    public void AddNotification (int prog) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle("Offline Content")
                .setContentText("Downloading content...")
                .setProgress(100, prog, false)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);

        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public void CompletNotification () {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle("Offline Content")
                .setContentText("Download Complete")
                .setPriority(NotificationManager.IMPORTANCE_HIGH);

        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }


    public static void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }
}
