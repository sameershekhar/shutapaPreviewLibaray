package com.example.customlibrary.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.customlibrary.entity.ChosenMediaFile;
import com.example.customlibrary.fragments.VideoPreviewFragment;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoPreviewViewPagerAdaptor extends FragmentStatePagerAdapter {

    private List<ChosenMediaFile> chosenVideoList;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private Context context;
    public VideoPreviewViewPagerAdaptor(FragmentManager fm, ArrayList<ChosenMediaFile> chosenVideoList, Context context) {
        super(fm);
        this.chosenVideoList=chosenVideoList;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
         //return VideoPreviewFragment.newInstance(chosenVideoList.get(position).getUri(),context);
       return  VideoPreviewFragment.newInstance(chosenVideoList.get(position).getUri(),context);
        //return mFragmentList.get(position);

    }


    @Override
    public int getCount() {
        return chosenVideoList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }



    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
        notifyDataSetChanged();

    }

//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        Log.d("TAG", "instantiateItem: "+position);
//        VideoPreviewFragment videoPreviewFragment=(VideoPreviewFragment)super.instantiateItem(container, position);
//         videoPreviewFragment.VisibleNow();
//        fragments.put(position,videoPreviewFragment);
//        return videoPreviewFragment;
//    }

//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
//    }
//
//    public VideoPreviewFragment getFragmentList(int position) {
//        return fragments.get(position);
//    }

}
