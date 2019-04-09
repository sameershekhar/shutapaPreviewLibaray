package com.example.customlibrary.MediaCode;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.customlibrary.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public class MediaPreviewItem extends FrameLayout {
    SimpleExoPlayer simpleExoPlayer;
    PlayerView playerView;
    ImageButton play;
    DataSource.Factory dataSourceFactory;
    PlayerControlView.VisibilityListener visibilityListener;

    LoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(
            15 * 1000, // Min buffer size
            30 * 1000, // Max buffer size
            500, // Min playback time buffered before starting video
            100).createDefaultLoadControl();

    public MediaPreviewItem( Context context) {
        super(context);
        initialize();
    }

    public MediaPreviewItem( Context context,  AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MediaPreviewItem( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public MediaPreviewItem( Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.media_preview_activity_item, this);
        this.playerView = findViewById(R.id.player_view);
        this.play = findViewById(R.id.exo_play);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(), loadControl);
        simpleExoPlayer.addListener(eventListener);
        playerView.setPlayer(simpleExoPlayer);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener) bandwidthMeter);
    }

    public void setData(String uri,Boolean autoPlay){
        playerView.setVisibility(VISIBLE);
        simpleExoPlayer.setPlayWhenReady(autoPlay);
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(uri));
        simpleExoPlayer.prepare(mediaSource);

        play.setOnClickListener(view -> {
            simpleExoPlayer.setPlayWhenReady(true);
        });
    }

    public void pause() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        //playerView.pause();
    }

    public void cleanup() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }

        //playerView.cleanup();
    }

    Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_ENDED:
                    break;
                case Player.STATE_READY:
                    break;
                case Player.STATE_BUFFERING:
                    break;
                case Player.STATE_IDLE:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
        }
    };

}
