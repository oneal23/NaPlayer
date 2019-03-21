package com.na.player;

/**
 * Created by taotao on 2019/1/10.
 */
public interface INaPlayController {
    void onPlayStateChanged(int state);
    void onReset(String videoUrl);
}
