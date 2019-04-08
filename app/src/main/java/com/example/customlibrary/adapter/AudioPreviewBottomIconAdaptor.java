package com.example.customlibrary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.List;

public class AudioPreviewBottomIconAdaptor extends RecyclerView.Adapter<AudioPreviewBottomIconAdaptor.MyViewHolder> {
    Context context;
    List<ChosenMediaFile> chosenAudioList;
    AudioIconInterface audioIconInterface;

    public AudioPreviewBottomIconAdaptor(Context context, List<ChosenMediaFile> chosenAudioList,AudioIconInterface audioIconInterface) {
        this.context = context;
        this.chosenAudioList = chosenAudioList;
        this.audioIconInterface=audioIconInterface;
    }

    @NonNull
    @Override
    public AudioPreviewBottomIconAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_audio_type,parent,false);
        return new AudioPreviewBottomIconAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioPreviewBottomIconAdaptor.MyViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ChosenMediaFile audio :chosenAudioList) {
                    audio.setIsSelected(0);
                }
                chosenAudioList.get(position).setIsSelected(1);
                notifyDataSetChanged();
                audioIconInterface.onAudioSelectIcon(position);
            }
        });
        if(chosenAudioList.get(position).getIsSelected()==1){

            holder.view.setBackground(ContextCompat.getDrawable(context, R.drawable.icon_border1));
        }
        else
        {
            holder.view.setBackground(ContextCompat.getDrawable(context, android.R.color.transparent));
            //holder.view.setBackground(null);
        }
        holder.icon_audio_count.setText(position+1+"");


//
//        Glide.with(context).load(chosenAudioList.get(position).getQueryUri())
//                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return chosenAudioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView icon_audio_count;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            this.icon_audio_count=itemView.findViewById(R.id.icon_audio_count);
        }
    }

    public interface AudioIconInterface {
        void onAudioSelectIcon(int position);
    }

}
