package com.example.vrplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

public class LocalVideoActivityPlayer extends AppCompatActivity {

    private VrVideoView videoWidgetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video_player);
        Intent intent = getIntent();
        String videopath = intent.getStringExtra("VIDEOURI");
        Uri videouri = Uri.parse(videopath);

        videoWidgetView = (VrVideoView)findViewById(R.id.video_view);
        VrVideoView.Options options = new VrVideoView.Options();//Options是表示VrVideo的播放方式
        try {
            options.inputType = VrVideoView.Options.FORMAT_DEFAULT;
            videoWidgetView.loadVideo(videouri, options);
        }catch (IOException e){
            videoWidgetView.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LocalVideoActivityPlayer.this, "打开失败 ", Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoWidgetView.pauseRendering();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoWidgetView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        videoWidgetView.shutdown();
        super.onDestroy();
    }
}
