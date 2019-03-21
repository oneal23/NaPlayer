package com.na.player.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.na.player.INaPlayer;
import com.na.player.NaIjkPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etVideoUrl)
    EditText etVideoUrl;
    @BindView(R.id.videoContaniner)
    FrameLayout videoContaniner;

    private Unbinder unbinder;

    private INaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btPlay)
    void onBtPlayClick() {
        startPlay();
    }

    private void startPlay() {
        String videoUrl = etVideoUrl.getText().toString();
        if (!TextUtils.isEmpty(videoUrl)){
            releasePlayer();
            if (player == null) {
                player = new NaIjkPlayer();
                player.setVideoContainer(videoContaniner);
            }
            player.init(videoUrl);
            player.start();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.resume();
        }
    }

    @OnLongClick(R.id.btPlay)
    boolean onBtPlayLongClick() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
