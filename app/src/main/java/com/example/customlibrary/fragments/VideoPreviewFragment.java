package com.example.customlibrary.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.customlibrary.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPreviewFragment extends Fragment implements ExoPlayer.EventListener{


    private DefaultTrackSelector trackSelector;
    private PlayerView simpleExoPlayerView;
    private ExoPlayer player;
    private String video_url;
    private static Context context1;

    public VideoPreviewFragment() {
        // Required empty public constructor
    }

    public static VideoPreviewFragment newInstance(String video_url, Context context) {
        context1=context;
        VideoPreviewFragment fragment = new VideoPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL", video_url);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View root = inflater.inflate(R.layout.fragment_video_preview, container, false);
//        simpleExoPlayerView=root.findViewById(R.id.preview_video_player);
//
//        video_url = getArguments().getString("URL");

       // SetUpVideoPlayer(video_url);
        //return root;
        return inflater.inflate(R.layout.fragment_video_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleExoPlayerView=view.findViewById(R.id.preview_video_player);
        video_url = getArguments().getString("URL");
        Log.d("TAG", "onViewCreated: view creating");//(context1,"view creating",Toast.LENGTH_LONG).show();
        if(video_url== null) {
            return;
        }else {
            initializePlayer();
        }

    }


    private void initializePlayer() {
        SetUpVideoPlayer(video_url);

    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            trackSelector = null;
            simpleExoPlayerView.setPlayer(null);
        }
    }

    private void SetUpVideoPlayer(String url) {

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //test

        TrackSelection.Factory  videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//
        // 2. Create the player
        player= ExoPlayerFactory.newSimpleInstance(context1, trackSelector);
//        playerControlView=new PlayerControlView(getContext());


        //simpleExoPlayerView.setUseController(true);//set to true or false to see controllers
        //simpleExoPlayerView.requestFocus();
        // Bind the player to the view.


        DataSource.Factory  dataSourceFactory =
                new DefaultDataSourceFactory(context1, Util.getUserAgent(context1, "ShuttApp"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


        if(simpleExoPlayerView!=null && url!=null){

            Uri videoUri = Uri.parse(url);

            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            player.prepare(videoSource);
            simpleExoPlayerView.setPlayer(player);
        }

    }

//
//
//    private void PlayVideo(String url){
//        // This is the MediaSource representing the media to be played.
//        Uri videoUri = Uri.parse(url);
//
//        MediaSource audioSource = new ExtractorMediaSource(videoUri,
//                dataSourceFactory, extractorsFactory, null, null);
//
//        // Prepare the player with the source.
//        player.prepare(audioSource);
//
//        //player.setPlayWhenReady(true);
//      //  previe_video_frame.setVisibility(View.VISIBLE);
//
//
//    }



    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null && video_url != null) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player!=null){
            player.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (player == null && video_url != null) {
            initializePlayer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void HiddenNow() {

        //Toast.makeText(context1,"relaesing player"+pos,Toast.LENGTH_LONG).show();
        releasePlayer();
       // player.setPlayWhenReady(false);
    }

    public void VisibleNow() {
        //Toast.makeText(context1,"initialising player"+pos,Toast.LENGTH_LONG).show();
        initializePlayer();
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {


    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
