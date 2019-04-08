package com.example.customlibrary.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class VideoPreviewAdaptor extends RecyclerView.Adapter<VideoPreviewAdaptor.MyViewHolder> {

    private Context context;
    private List<ChosenMediaFile> chosenVideoList;
    SimpleExoPlayer  player;
    private  DataSource.Factory dataSourceFactory ;
    private ExtractorsFactory extractorsFactory;
    private VideoPrevieAdaptorCallback videoPrevieAdaptorCallback;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;



    public VideoPreviewAdaptor(LinearLayoutManager linearLayoutManager,Context context, List<ChosenMediaFile> chosenVideoList,
                               VideoPrevieAdaptorCallback videoPrevieAdaptorCallback) {
        this.context = context;
        this.chosenVideoList = chosenVideoList;
        this.videoPrevieAdaptorCallback=videoPrevieAdaptorCallback;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.video_type, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



//       // StartPlayer(holder,chosenVideoList.get(position));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

//        StartPlayer(holder,chosenVideoList.get(holder.getAdapterPosition()));
//        Toast.makeText(context,holder.getAdapterPosition()+"", Toast.LENGTH_LONG).show();
    }

    private void StartPlayer(MyViewHolder holder, ChosenMediaFile chosenVideo) {

//
//        int h = holder.simpleExoPlayerView.getResources().getConfiguration().screenHeightDp;
//        int w = holder.simpleExoPlayerView.getResources().getConfiguration().screenWidthDp;


        //holder.video_frame.setVisibility(View.VISIBLE);
        holder.preview_image.setVisibility(View.GONE);
        holder.simpleExoPlayerView.setUseController(true);//set to true or false to see controllers
        holder.simpleExoPlayerView.requestFocus();
        // Bind the player to the view.
        holder.simpleExoPlayerView.setPlayer(player);

        MediaSource videoSource= new ExtractorMediaSource.Factory(dataSourceFactory).setExtractorsFactory
                (new DefaultExtractorsFactory()).createMediaSource(Uri.parse(chosenVideo.getUri()));

        // This is the MediaSource representing the media to be played.
//        Uri videoUri = Uri.parse(chosenVideo.getQueryUri());
//        MediaSource videoSource = new ExtractorMediaSource(videoUri,
//                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        player.prepare(videoSource);
        //player.setPlayWhenReady(true);

//
    }


    @Override
    public int getItemCount() {
        return chosenVideoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView preview_image;
        View view;
        PlayerView simpleExoPlayerView;
        ProgressBar progressBar;
        FrameLayout video_frame;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            simpleExoPlayerView=itemView.findViewById(R.id.video_view);
            //progressBar=itemView.findViewById(R.id.progressBar);
            preview_image=itemView.findViewById(R.id.video_preview_image);
//            video_frame=itemView.findViewById(R.id.video_frame);



        }


    }

    public interface VideoPrevieAdaptorCallback {
        void onVideoSelecte(MyViewHolder holder, int position);
    }

    public void ReleasePlayer(){
        if(player!=null) {
            player.release();
        }
    }


    public ExoPlayer GetPlayer(){
        if(player!=null) {
            return player;
        }
        return null;

    }





}
