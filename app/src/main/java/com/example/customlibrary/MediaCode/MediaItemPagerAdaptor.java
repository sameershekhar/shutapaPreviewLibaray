package com.example.customlibrary.MediaCode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MediaItemPagerAdaptor extends PagerAdapter implements PlayPauseListner {
    List<ChosenMediaFile> chosenMediaFiles = new ArrayList<>();
    private WeakHashMap<Integer, MediaPreviewItem> mediaPeviewViews = new WeakHashMap<>();
    private boolean autoPlay = false;
    private Context context;


    public void setData(List<ChosenMediaFile> chosenVideos ){
        this.chosenMediaFiles = chosenVideos;
        notifyDataSetChanged();
    }

    public MediaItemPagerAdaptor(Context context) {
        this.context = context;

    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.media_preview_page, container, false);
        MediaPreviewItem mediaPreviewItem = itemView.findViewById(R.id.media_preview_item);
        mediaPeviewViews.put(position, mediaPreviewItem);
        try {

            mediaPreviewItem.setData(chosenMediaFiles.get(position).getUri(),false);
        }catch (Exception e){
            e.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        MediaPreviewItem mediaPreviewItem = ((FrameLayout)object).findViewById(R.id.media_preview_item);
        mediaPreviewItem.cleanup();

        mediaPeviewViews.remove(position);
        container.removeView((FrameLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public int getCount() {
        return chosenMediaFiles.size();
    }

    @Override
    public void onPause(int position) {
        MediaPreviewItem mediaPreviewItem = mediaPeviewViews.get(position);
        if(mediaPreviewItem != null){
            mediaPreviewItem.pause();
        }
    }

    @Override
    public void onPlay(int position) {

    }

    public void setAutoPlay(boolean autoPlay){
        this.autoPlay = autoPlay;
    }


}
