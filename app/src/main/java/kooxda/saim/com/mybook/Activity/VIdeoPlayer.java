package kooxda.saim.com.mybook.Activity;

import android.content.pm.ActivityInfo;
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
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kooxda.saim.com.mybook.Adapter.AdapterCategoryContent;
import kooxda.saim.com.mybook.Model.ModelContent;
import kooxda.saim.com.mybook.R;
import kooxda.saim.com.mybook.Utility.Utility;

public class VIdeoPlayer extends AppCompatActivity {

    public static CoordinatorLayout mainLayout;

    VideoView videoView;
    ProgressBar progVidVideo;

    RelativeLayout layoutVideoPlayer, layoutvidVideo;
    LinearLayout layoutVideoController, layoutVideoControllerTop;
    ImageView imgControlPlay, imgControlPrevious,imgControlNext, imgControlFullScreen;
    TextView txtControlCurrent, txtControlEnd, txtControlVideoName;
    SeekBar seekBarControl;


    private static MediaPlayer mediaPlayer;
    static Handler progressBarHandler = new Handler();


    public boolean isFullscreen = false;
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    public static String videoUrl = "";
    public static String videoTitle = "";
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

        videoUrl = getIntent().getExtras().getString("URL");
        videoTitle = getIntent().getExtras().getString("TITLE");
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


        recyclerViewContentVideoLayout = (RecyclerView) findViewById(R.id.recyclerViewContentVideoLayout);
        recyclerViewContentVideoLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewContentVideoLayout.setHasFixedSize(true);
        contentVideoLayoutAdapter = new AdapterCategoryContent(modelContentArrayList);
        recyclerViewContentVideoLayout.setAdapter(contentVideoLayoutAdapter);


        settingVideoView();
        VideoController();

    }


    public void settingVideoView(){
        //https://www.demonuts.com/Demonuts/smallvideo.mp4
        Uri uri= Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

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
                //String fileName = "Big Buck Bunny Saim";
                txtControlVideoName.setText(videoTitle);
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


    @Override
    public void onBackPressed() {
        if (isFullscreen == true){
            isFullscreen = false;
            exitFullScreen();
        }else {
            finish();
        }
    }




}
