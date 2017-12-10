package com.dopechat.grv.dopechat;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn;
    private TextView mSubtitle, mTerms, madeWith;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make fullscreen>>>>START
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Make fullscreen>>>>END

        setContentView(R.layout.activity_start);

        Typeface font_book = Typeface.createFromAsset(getAssets(),"fonts/gotham_book.ttf");
        Typeface font_medium = Typeface.createFromAsset(getAssets(),"fonts/gotham_medium.ttf");
        setVideoBackground();

        //Toast toast = Toasty.custom(StartActivity.this, "Welcome",R.drawable.yo,getResources().getColor(R.color.colorAccent), Toast.LENGTH_LONG,true,true);
        //toast.setGravity(Gravity.TOP,0,100);
        //toast.show();

        mRegBtn = (Button) findViewById(R.id.start_reg_btn);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AcceptOtpActivity.class));
            }
        });

        mSubtitle = (TextView) findViewById(R.id.subtitle);
        mTerms = (TextView) findViewById(R.id.terms);
        madeWith = (TextView) findViewById(R.id.made_with);

        mSubtitle.setTypeface(font_medium);
        mTerms.setTypeface(font_book);
        mRegBtn.setTypeface(font_medium);
        madeWith.setTypeface(font_book);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setVideoBackground();
    }

    private void setVideoBackground() {
        final VideoView view = (VideoView)findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video_background;
        view.setVideoURI(Uri.parse(path));
        view.start();
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                view.start();
            }
        });
    }
}
