package com.example.customlibrary.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.customlibrary.R;
import com.example.customlibrary.adapter.AudioPreviewAdaptor;
import com.example.customlibrary.adapter.AudioPreviewBottomIconAdaptor;
import com.example.customlibrary.entity.ChosenMediaFile;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AudioPreviewActivty extends AppCompatActivity implements AudioPreviewBottomIconAdaptor.AudioIconInterface {

    private List<ChosenMediaFile> chosenAudioArrayList;
    private RecyclerView mRecyclerViewAudio;
    private RecyclerView mRecyclerViewAudioBottomIcon;

    private ExtractorsFactory extractorsFactory;
    private DataSource.Factory dataSourceFactory;
    private SimpleExoPlayer player;
    private int curent_recycler_position=0,previous_recycler_position=0;
    private Toolbar topToolBar;
    private ImageView moreAudios;
    private  PlayerView simpleExoPlayerView;
    private AudioPreviewAdaptor audioPreviewAdaptor;
    private AudioPreviewBottomIconAdaptor audioPreviewBottomIconAdaptor;
    //private VideoPreviewAdaptor videoPreviewAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_preview_activty);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        topToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initRecyclerView();

    }


    private void initRecyclerView(){

        mRecyclerViewAudio = findViewById(R.id.recycler_view);
        mRecyclerViewAudioBottomIcon = findViewById(R.id.preview_icon_recyclerView);
        moreAudios=findViewById(R.id.moreImage);


      //  chosenAudioArrayList= getIntent().getParcelableArrayListExtra("data1");
        chosenAudioArrayList=  this.getIntent().getExtras().getParcelableArrayList("data1");
        if(chosenAudioArrayList.size()>0){

           // Toast.makeText(getApplicationContext(),chosenAudioArrayList.size()+"",Toast.LENGTH_LONG).show();
            DisplayAudio(chosenAudioArrayList);
            onRotateAudios(0);
        }

        moreAudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMore();
            }
        });
    }


    public void DisplayAudio(List<ChosenMediaFile> chosenAudioArrayList){
        this.chosenAudioArrayList=chosenAudioArrayList;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false);
        mRecyclerViewAudio.setLayoutManager(layoutManager);
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(mRecyclerViewAudio);


        audioPreviewAdaptor = new AudioPreviewAdaptor(this,chosenAudioArrayList);
        mRecyclerViewAudio.setAdapter(audioPreviewAdaptor);

        mRecyclerViewAudio.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    previous_recycler_position=curent_recycler_position;
                    curent_recycler_position = layoutManager.findFirstVisibleItemPosition();
                    onRotateAudios(curent_recycler_position);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });



        LinearLayoutManager layoutManagerIcon = new LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false);
        mRecyclerViewAudioBottomIcon.setLayoutManager(layoutManagerIcon);

        audioPreviewBottomIconAdaptor = new AudioPreviewBottomIconAdaptor(this,chosenAudioArrayList,this);
        mRecyclerViewAudioBottomIcon.setAdapter(audioPreviewBottomIconAdaptor);
        SetUpAndPlayAudio(chosenAudioArrayList.get(0).getUri());

    }


    private void SetUpAndPlayAudio(String url) {

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //test

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        simpleExoPlayerView=findViewById(R.id.audio_view);

        player= ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayerView.setUseController(true);//set to true or false to see controllers
        simpleExoPlayerView.requestFocus();
        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, "ShuttApp"));

        // Produces Extractor instances for parsing the media data.
        extractorsFactory = new DefaultExtractorsFactory();
        PlayAudio(url);
    }

    private void PlayAudio(String url){
        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(url);

        MediaSource audioSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        player.prepare(audioSource);
        player.setPlayWhenReady(true);


    }

    private void onRotateAudios(int position) {
//        for (ChosenMediaFile audio : chosenAudioArrayList) {
//            audio.setIsSelected(0);
//        }

        chosenAudioArrayList.get(previous_recycler_position).getIsSelected();
        chosenAudioArrayList.get(curent_recycler_position).setIsSelected(1);
        audioPreviewBottomIconAdaptor.notifyDataSetChanged();
        mRecyclerViewAudioBottomIcon.getLayoutManager().scrollToPosition(position);
        PlayAudio(chosenAudioArrayList.get(position).getUri());

    }
    @Override
    protected void onDestroy() {
       if(player!=null){
           player.release();
           player = null;
           simpleExoPlayerView.setPlayer(null);
       }
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(player!=null){
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null){
            player.setPlayWhenReady(false);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audio_preview_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.delete:
                deleteSelectedAudio();
                break;
            case R.id.moreAudios:
                pickMore();
                break;
            default:
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedAudio() {
        if(chosenAudioArrayList.size()==1) {
            return;
        }
//        if(chosenAudioArrayList!=null && curent_recycler_position>=0 && curent_recycler_position<chosenAudioArrayList.size()){
//            chosenAudioArrayList.remove(curent_recycler_position);
//            audioPreviewBottomIconAdaptor.notifyDataSetChanged();
//            audioPreviewAdaptor.notifyDataSetChanged();
//            if(curent_recycler_position-1>=0){
//                int new_recycler_position=curent_recycler_position-1;
//                onRotateAudios(new_recycler_position);
//                PlayAudio(chosenAudioArrayList.get(new_recycler_position).getUri());
//            }
//
//        }

        else if(chosenAudioArrayList!=null){
            chosenAudioArrayList.remove(curent_recycler_position);
            curent_recycler_position=curent_recycler_position-1;
            if(curent_recycler_position<0){
                curent_recycler_position=0;
            }
            chosenAudioArrayList.get(curent_recycler_position).setIsSelected(1);
            audioPreviewAdaptor.notifyDataSetChanged();
            audioPreviewBottomIconAdaptor.notifyDataSetChanged();
            mRecyclerViewAudio.getLayoutManager().scrollToPosition(curent_recycler_position);
            mRecyclerViewAudioBottomIcon.getLayoutManager().scrollToPosition(curent_recycler_position);

        }

    }

    public void pickMore(){
        chosenAudioArrayList.clear();
        audioPreviewAdaptor.notifyDataSetChanged();
        audioPreviewBottomIconAdaptor.notifyDataSetChanged();
        finish();
    }


    @Override
    public void onAudioSelectIcon(int position) {
        mRecyclerViewAudio.getLayoutManager().smoothScrollToPosition(mRecyclerViewAudio, new RecyclerView.State(),position);

    }
}
