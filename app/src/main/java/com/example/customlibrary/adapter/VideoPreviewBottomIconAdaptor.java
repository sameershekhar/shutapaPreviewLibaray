package com.example.customlibrary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;


import java.util.List;

public class VideoPreviewBottomIconAdaptor extends RecyclerView.Adapter<VideoPreviewBottomIconAdaptor.MyViewHolder> {
    private Context context;
    private List<ChosenMediaFile> chosenVideoList=null;
    private VideoIconInterface videoIconInterface;

    public VideoPreviewBottomIconAdaptor(Context context, List<ChosenMediaFile> chosenVideoList, VideoIconInterface videoIconInterface) {
        this.context = context;
        this.chosenVideoList = chosenVideoList;
        this.videoIconInterface = videoIconInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_video_type, parent, false);
        return new VideoPreviewBottomIconAdaptor.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ChosenMediaFile video : chosenVideoList) {
                    video.setIsSelected(0);
                }
                chosenVideoList.get(position).setIsSelected(1);
                notifyDataSetChanged();
                videoIconInterface.onVideoSelectIcon(position);
            }
        });
        if(chosenVideoList.get(position).getIsSelected()==1){

            holder.view.setBackground(ContextCompat.getDrawable(context, R.drawable.icon_border1));
        }
        else
        {
            holder.view.setBackground(ContextCompat.getDrawable(context, android.R.color.transparent));
            //holder.view.setBackground(null);
        }


//
//        Glide.with(context).load(chosenVideoList.get(position).getUri())
//                .into(holder.imageView);

        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                //.load(Uri.fromFile(new File(chosenVideoList.get(position).getUri())))
                .load(chosenVideoList.get(position).getUri())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return chosenVideoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            imageView=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // imageView.setBackground(R.drawable.icon_border1);
                    //itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.icon_border1));
                    videoIconInterface.onVideoSelectIcon(getAdapterPosition());
                }
            });
        }
    }

    public interface VideoIconInterface {
        void onVideoSelectIcon(int position);
    }


}
