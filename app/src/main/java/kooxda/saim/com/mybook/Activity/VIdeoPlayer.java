package kooxda.saim.com.mybook.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kooxda.saim.com.mybook.Adapter.AdapterCategoryContent;
import kooxda.saim.com.mybook.Adapter.AdapterPlayer;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.Utility;

public class VIdeoPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, View.OnClickListener{

    public static CoordinatorLayout mainLayout;

    VideoView videoView;
    ProgressBar progVidVideo;

    RelativeLayout layoutVideoPlayer, layoutvidVideo;
    LinearLayout layoutVideoController, layoutVideoControllerTop;
    ImageView imgControlPlay, imgControlPrevious,imgControlNext, imgControlFullScreen;
    TextView txtControlCurrent, txtControlEnd, txtControlVideoName;
    SeekBar seekBarControl;

    //Audio Layout
    RelativeLayout layoutAudioPlayer;
    ImageView layoutAudioPlaceholder;
    ProgressBar progVidAudio;
    TextView txtControlAudioName, txtControlCurrentAudio, txtControlEndAudio;
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
    public static String jsonAdapterList = "";
    public static ArrayList<ModelContent> modelContentArrayList = new ArrayList<>();


    ArrayList<ModelContent> modelContents = new ArrayList<>();
    RecyclerView recyclerViewContentVideoLayout;
    RecyclerView.LayoutManager layoutManagerContentVideoLayout;
    RecyclerView.Adapter contentVideoLayoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setTheme(R.style.AppThemeMainActivity);
        setContentView(R.layout.activity_video_player);


        contentType = getIntent().getExtras().getString("TYPE");
        jsonAdapterList = getIntent().getExtras().getString("LIST");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ModelContent>>(){}.getType();
        modelContentArrayList = gson.fromJson(jsonAdapterList, type);

        for (ModelContent content : modelContentArrayList){
            Log.i("MODEL CONTENT DATA", content.getId()+" : "+content.getName());
        }

        Init();
    }

    public void Init(){

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
            String audioUrl = getIntent().getExtras().getString("URL");
            String audioTitle = getIntent().getExtras().getString("TITLE");
            String audioCover = getIntent().getExtras().getString("COVER");

            playSong(audioUrl, audioTitle, audioCover);
        } else {
            layoutAudioPlayer.setVisibility(View.GONE);
            layoutvidVideo.setVisibility(View.VISIBLE);

            String videoUrl = getIntent().getExtras().getString("URL");
            String videoTitle = getIntent().getExtras().getString("TITLE");

            settingVideoView(videoUrl, videoTitle);
        }

        VideoController();

    }


    public void settingVideoView(String vURL, String vTITLE){
        Uri uri= Uri.parse(vURL);
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
                    if (isFullscreen == false){
                        enterFullScreen();
                        isFullscreen = true;
                        imgControlFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
                    }else if (isFullscreen == true){
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

    public void VideoController(){
        imgControlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){
                    videoView.pause();
                    imgControlPlay.setImageResource(R.drawable.ic_play);
                }else {
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
                if (isFullscreen == false){
                    enterFullScreen();
                    isFullscreen = true;
                    imgControlFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
                }else if (isFullscreen == true){
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

    private void enterFullScreen(){
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutVideoPlayer.getLayoutParams().height = metrics.widthPixels;
        layoutVideoPlayer.getLayoutParams().width = metrics.heightPixels;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    private void exitFullScreen(){

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_VISIBLE);
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
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
        seekBarControlAudio.setEnabled(false);
        mpAudio.stop();
        mpAudio.reset();
        progressBarHandlerAudio.removeCallbacks(mUpdateTimeTask);
        try {
            txtControlAudioName.setText(aTITLE);
            mpAudio.setDataSource(aURL);
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
        Log.d("Progress", progress+"");
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
        if (seekBarControlAudio.getProgress() < percent){

        }else {

        }
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
        if (isFullscreen == true){
            isFullscreen = false;
            exitFullScreen();
        }else {
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
    }



}
