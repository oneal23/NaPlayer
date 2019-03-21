//package com.na.player;
//
//import android.graphics.SurfaceTexture;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import tv.danmaku.ijk.media.player.IMediaPlayer;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//
///**
// * Created by taotao on 2019/1/10.
// */
//public class NaIjkPlayer implements INaPlayer, TextureView.SurfaceTextureListener
//        , IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener
//        , IMediaPlayer.OnErrorListener, IMediaPlayer.OnVideoSizeChangedListener
//        , IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener {
//
//
//    private static final String TAG = "NaIjkPlayer";
//    private INaPlayController controller;
//    private FrameLayout container;
//    private NaTextureView textureView;
//    private Surface surface;
//    private String videoUri;
//
//    private int currentState = STATE_IDLE;
//    private IMediaPlayer mediaPlayer;
//
//    public int getCurrentState() {
//        return currentState;
//    }
//
//    public void setCurrentState(int currentState) {
//        this.currentState = currentState;
//    }
//
//    public INaPlayController getController() {
//        return controller;
//    }
//
//    public void setController(INaPlayController controller) {
//        this.controller = controller;
//    }
//
//    public FrameLayout getContainer() {
//        return container;
//    }
//
//    public String getVideoUri() {
//        return videoUri;
//    }
//
//    private void initTextureView() {
//        if (textureView == null) {
//            textureView = new NaTextureView(getContainer().getContext());
//            textureView.setSurfaceTextureListener(this);
//        }
//    }
//
//    private void addTextureView() {
//        getContainer().removeView(textureView);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                Gravity.CENTER);
//        getContainer().addView(textureView, 0, params);
//    }
//
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
//        surface = new Surface(surfaceTexture);
//        if (getMediaPlayer() != null) {
//            if (getCurrentState() == STATE_IDLE) {
//                openMediaPlayer();
//            } else {
//                getMediaPlayer().setSurface(surface);
//            }
//        }
//        log("onSurfaceTextureAvailable surfaceTexture=" + surfaceTexture + ",width=" + width + ",height=" + height);
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
//        log("onSurfaceTextureSizeChanged surfaceTexture=" + surfaceTexture + ",width=" + width + ",height=" + height);
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//        if (surface != null) {
//            surface.release();
//            surface = null;
//        }
//        if (getMediaPlayer() != null) {
//            getMediaPlayer().setSurface(null);
//        }
//        log("onSurfaceTextureDestroyed surfaceTexture=" + surfaceTexture);
//        return true;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//        log("onSurfaceTextureUpdated surfaceTexture=" + surfaceTexture);
//    }
//
//    @Override
//    public void init(String videoUri) {
//        this.videoUri = videoUri;
//    }
//
//    @Override
//    public void setVideoContainer(FrameLayout container) {
//        this.container = container;
//    }
//
//    private void initMediaPlayer() {
//        mediaPlayer = new IjkMediaPlayer();
//    }
//
//    public void releasePlayer() {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//
//        if (container != null) {
//            container.removeView(textureView);
//            container = null;
//        }
//
//        if (surface != null) {
//            surface.release();
//            surface = null;
//        }
//        textureView = null;
//        controller = null;
//        videoUri = null;
//        currentState = STATE_IDLE;
//    }
//
//    public IMediaPlayer getMediaPlayer() {
//        return mediaPlayer;
//    }
//
//    private void openMediaPlayer() {
//        if (getVideoUri() != null && getContainer() != null) {
//            try {
//                // 设置监听
//                getMediaPlayer().setOnPreparedListener(this);
//                getMediaPlayer().setOnVideoSizeChangedListener(this);
//                getMediaPlayer().setOnCompletionListener(this);
//                getMediaPlayer().setOnErrorListener(this);
//                getMediaPlayer().setOnInfoListener(this);
//                getMediaPlayer().setOnBufferingUpdateListener(this);
//                // 设置dataSource
//                getMediaPlayer().setDataSource(getVideoUri());
//                getMediaPlayer().setSurface(surface);
//                getMediaPlayer().prepareAsync();
//                setCurrentState(STATE_PREPARING);
//                if (getController() != null) {
//                    getController().onPlayStateChanged(getCurrentState());
//                }
//                log("STATE_PREPARING");
//            } catch (Exception e) {
//                e.printStackTrace();
//                log("打开播放器发生错误", e);
//            }
//        }
//    }
//
//    @Override
//    public void start() {
//        if (getVideoUri() != null && getContainer() != null) {
//            if (getCurrentState() == STATE_IDLE) {
//                initMediaPlayer();
//                initTextureView();
//                addTextureView();
//            }
//        }
//    }
//
//    @Override
//    public void pause() {
//        if (getCurrentState() == STATE_PLAYING) {
//            getMediaPlayer().pause();
//            setCurrentState(STATE_PAUSED);
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//            log("STATE_PAUSED");
//        } else if (getCurrentState() == STATE_BUFFERING_PLAYING) {
//            getMediaPlayer().pause();
//            setCurrentState(STATE_BUFFERING_PAUSED);
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//            log("STATE_BUFFERING_PAUSED");
//        }
//    }
//
//    @Override
//    public void resume() {
//        if (getCurrentState() == STATE_PAUSED) {
//            getMediaPlayer().start();
//            setCurrentState(STATE_PLAYING);
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//            log("STATE_PLAYING");
//        } else if (getCurrentState() == STATE_BUFFERING_PAUSED) {
//            getMediaPlayer().start();
//            setCurrentState(STATE_BUFFERING_PLAYING);
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//            log("STATE_BUFFERING_PLAYING");
//        } else if (getCurrentState() == STATE_COMPLETED || getCurrentState() == STATE_ERROR) {
//            getMediaPlayer().reset();
//            openMediaPlayer();
//        } else {
//            log("NiceVideoPlayer在mCurrentState == " + getCurrentState() + "时不能调用restart()方法.");
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (getController() != null) {
//            getController().onReset(getVideoUri());
//        }
//        videoUri = null;
//    }
//
//    @Override
//    public void release() {
//        releasePlayer();
//    }
//
//    @Override
//    public void onPrepared(IMediaPlayer mp) {
//        setCurrentState(STATE_PREPARED);
//        if (getController() != null) {
//            getController().onPlayStateChanged(getCurrentState());
//        }
//        log("onPrepared ——> STATE_PREPARED");
//        mp.start();
////        // 从上次的保存位置播放
////        if (continueFromLastPosition) {
////            long savedPlayPosition = NiceUtil.getSavedPlayPosition(mContext, mUrl);
////            mp.seekTo(savedPlayPosition);
////        }
////        // 跳到指定位置播放
////        if (skipToPosition != 0) {
////            mp.seekTo(skipToPosition);
////        }
//    }
//
//    @Override
//    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
//        if (textureView != null) {
//            textureView.adaptVideoSize(width, height);
//        }
//        log("onVideoSizeChanged ——> width：" + width + "， height：" + height);
//    }
//
//    @Override
//    public void onCompletion(IMediaPlayer mp) {
//        setCurrentState(STATE_COMPLETED);
//        if (getController() != null) {
//            getController().onPlayStateChanged(getCurrentState());
//        }
//
//        if (getContainer() != null) {
//            getContainer().setKeepScreenOn(false);
//        }
//        log("onCompletion ——> STATE_COMPLETED");
//    }
//
//    @Override
//    public boolean onError(IMediaPlayer mp, int what, int extra) {
//        // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
//        if (what != -38 && what != -2147483648 && extra != -38 && extra != -2147483648) {
//            setCurrentState(STATE_ERROR);
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//            log("onError ——> STATE_ERROR ———— what：" + what + ", extra: " + extra);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
//        if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//            // 播放器开始渲染
//            setCurrentState(STATE_PLAYING);
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//            log("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING");
//        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
//            // MediaPlayer暂时不播放，以缓冲更多的数据
//            if (getCurrentState() == STATE_PAUSED || getCurrentState() == STATE_BUFFERING_PAUSED) {
//                setCurrentState(STATE_BUFFERING_PAUSED);
//                log("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED");
//            } else {
//                setCurrentState(STATE_BUFFERING_PLAYING);
//                log("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING");
//            }
//            if (getController() != null) {
//                getController().onPlayStateChanged(getCurrentState());
//            }
//        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
//            // 填充缓冲区后，MediaPlayer恢复播放/暂停
//            if (getCurrentState() == STATE_BUFFERING_PLAYING) {
//                setCurrentState(STATE_PLAYING);
//                if (getController() != null) {
//                    getController().onPlayStateChanged(getCurrentState());
//                }
//                log("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING");
//            } else if (getCurrentState() == STATE_BUFFERING_PAUSED) {
//                setCurrentState(STATE_PAUSED);
//                if (getController() != null) {
//                    getController().onPlayStateChanged(getCurrentState());
//                }
//                log("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED");
//            }
//        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
//            // 视频旋转了extra度，需要恢复
//            if (textureView != null) {
//                textureView.setRotation(extra);
//                log("视频旋转角度：" + extra);
//            }
//        } else if (what == IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
//            log("视频不能seekTo，为直播视频");
//        } else {
//            log("onInfo ——> what：" + what);
//        }
//        return true;
//    }
//
//    @Override
//    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
//
//    }
//
//    private void log(String msg) {
//        Log.e(TAG, msg);
//    }
//
//    private void log(String msg, Exception e) {
//        Log.e(TAG, msg, e);
//    }
//}
