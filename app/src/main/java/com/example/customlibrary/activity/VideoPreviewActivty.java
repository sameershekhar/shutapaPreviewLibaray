package com.example.customlibrary.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
//
//import com.example.customlibrary.MediaCode.ExtendedOnPageChangedListener;
//import com.example.customlibrary.MediaCode.MediaItemPagerAdaptor;
import com.example.customlibrary.R;
import com.example.customlibrary.adapter.VideoPreviewAdaptor;

import com.example.customlibrary.adapter.VideoPreviewBottomIconAdaptor;
import com.example.customlibrary.adapter.VideoPreviewViewPagerAdaptor;
import com.example.customlibrary.entity.ChosenMediaFile;
import com.example.customlibrary.fragments.VideoPreviewFragment;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;


import java.util.ArrayList;

public class VideoPreviewActivty extends AppCompatActivity implements
        VideoPreviewAdaptor.VideoPrevieAdaptorCallback,VideoPreviewBottomIconAdaptor.VideoIconInterface {

    private ArrayList<ChosenMediaFile> chosenVideoList;
    private RecyclerView mRecyclerView;
    private RecyclerView video_preview_icon_recycler_view;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager1;
    private ViewPager viewPager;
    private VideoPreviewViewPagerAdaptor videoPreviewViewPagerAdaptor;



    private DefaultTrackSelector trackSelector;
    private ExoPlayer player;


    protected int mPreviousPos = 0;
    private Toolbar topToolBar;
    private ImageView moreVideos;
    private VideoPreviewBottomIconAdaptor videoPreviewBottomIconAdaptor;
    private int curent_recycler_position;


    //experiment code

  //  MediaItemPagerAdaptor mediaItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview_activty);


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


        //initialsign video bootom icon recyclerview
        linearLayoutManager1 = new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false);
        video_preview_icon_recycler_view = (RecyclerView) findViewById(R.id.preview_icon_recyclerView);
        video_preview_icon_recycler_view.setLayoutManager(linearLayoutManager1);
        video_preview_icon_recycler_view.setItemAnimator(new DefaultItemAnimator());



        moreVideos=findViewById(R.id.moreImage);

        //getting video for the first time
        chosenVideoList= getIntent().getParcelableArrayListExtra("data");

        if(chosenVideoList.size()>0){
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            videoPreviewViewPagerAdaptor =new VideoPreviewViewPagerAdaptor(getSupportFragmentManager(),chosenVideoList,this);

//            for (ChosenMediaFile video: chosenVideoList) {
//                videoPreviewViewPagerAdaptor.addFragment(VideoPreviewFragment.newInstance(video.getUri(),VideoPreviewActivty.this));
//            }

            viewPager.setAdapter(videoPreviewViewPagerAdaptor);
            viewPager.setCurrentItem(mPreviousPos,false);
            viewPager.setOffscreenPageLimit(chosenVideoList.size());

            DisplayVideosIcons(chosenVideoList);
            onRotateVideos(0);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
              @Override
              public void onPageScrolled(int position, float v, int i1) {

                  try {


                      ((VideoPreviewFragment)videoPreviewViewPagerAdaptor.getItem(mPreviousPos)).HiddenNow();
                     // ((VideoPreviewFragment)videoPreviewViewPagerAdaptor.getItem(position)).VisibleNow();
                     ((VideoPreviewFragment)videoPreviewViewPagerAdaptor.getItem(position)).VisibleNow();


//                      videoPreviewViewPagerAdaptor.getFragmentList(position).VisibleNow();
//                      videoPreviewViewPagerAdaptor.getFragmentList(mPreviousPos).HiddenNow();

                  } catch (Exception e) {
                      e.printStackTrace();
                  }

                  mPreviousPos = position;
              }

              @Override
              public void onPageSelected(int position) {
                 //  mPreviousPos = position;
                  Log.d("TAG", "onPageSelected: "+position+" "+mPreviousPos);
                  //videoPreviewViewPagerAdaptor.getFragmentList(mPreviousPos).HiddenNow();
                  curent_recycler_position= position;
                  onRotateVideos(position);
              }

              @Override
              public void onPageScrollStateChanged(int i) {
                     // releaseExoplayer();
              }
          });
        }

        moreVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PickVideos();
                finish();
            }
        });

    }

    private void DisplayVideosIcons(ArrayList<ChosenMediaFile> chosenVideoList) {

        this.chosenVideoList=chosenVideoList;


        videoPreviewBottomIconAdaptor=new VideoPreviewBottomIconAdaptor(this,chosenVideoList,this);
        video_preview_icon_recycler_view.setAdapter(videoPreviewBottomIconAdaptor);
        //helper.attachToRecyclerView(video_preview_icon_recycler_view);
        //videoPreviewAdaptor.notifyDataSetChanged();
        videoPreviewBottomIconAdaptor.notifyDataSetChanged();

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_preview_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.delete:
                DeleteSelectedVideo();
                break;
            case R.id.moreVideos:
                finish();
                break;
            default:
                return true;


        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onVideoSelecte(VideoPreviewAdaptor.MyViewHolder holder, int position) {

        Toast.makeText(this,position+"",Toast.LENGTH_LONG).show();



    }


    private void onRotateVideos(int position) {
        for (ChosenMediaFile video : chosenVideoList) {
            video.setIsSelected(0);
        }

        chosenVideoList.get(position).setIsSelected(1);
        videoPreviewBottomIconAdaptor.notifyDataSetChanged();
        video_preview_icon_recycler_view.getLayoutManager().scrollToPosition(position);

        //Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_LONG).show();

    }





    @Override
    public void onVideoSelectIcon(int position) {

        viewPager.setCurrentItem(position);

        //mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, new RecyclerView.State(),position);


    }

    private void DeleteSelectedVideo() {
        if(chosenVideoList.size()==1){
            return;
        }
        if(chosenVideoList!=null && curent_recycler_position>=0 && curent_recycler_position<chosenVideoList.size()){
           // videoPreviewAdaptor.ReleasePlayer();
            this.chosenVideoList.remove(curent_recycler_position);

            videoPreviewViewPagerAdaptor.notifyDataSetChanged();
            videoPreviewBottomIconAdaptor.notifyItemRemoved(curent_recycler_position);
            videoPreviewBottomIconAdaptor.notifyItemRangeChanged(curent_recycler_position, chosenVideoList.size());
            //videoPreviewAdaptor.notifyDataSetChanged();
            //videoPreviewBottomIconAdaptor.notifyDataSetChanged();


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){

        }

    }

//    private class ViewPagerChangeListener  extends ExtendedOnPageChangedListener {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            //Toast.makeText(getApplicationContext(),"selecting page"+position,Toast.LENGTH_LONG).show();
//
//            curent_recycler_position= position;
//            onRotateVideos(position);
//        }
//
//        @Override
//        public void onPageUnselected(int position) {
//           // mediaItemAdapter.setAutoPlay(false);
//            Toast.makeText(getApplicationContext(),"unselecting page"+position,Toast.LENGTH_LONG).show();
//           // mediaItemAdapter.onPause(position);
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            super.onPageScrollStateChanged(state);
//        }
//    }
//
//    public void ExperimentVideo(){
//        //expermient code
//        if(chosenVideoList.size()>0){
//            viewPager = (ViewPager) findViewById(R.id.view_pager);
//            mediaItemAdapter =new MediaItemPagerAdaptor(this);
//            mediaItemAdapter.setData(chosenVideoList);
//            viewPager.setAdapter(mediaItemAdapter);
//            viewPager.setOffscreenPageLimit(1);
//
//            DisplayVideosIcons(chosenVideoList);
//            onRotateVideos(0);
//            viewPager.addOnPageChangeListener(new ViewPagerChangeListener());
//
//
//        }
//
//
//    }
}
