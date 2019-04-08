package com.example.customlibrary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioListAdaptor extends RecyclerView.Adapter<AudioListAdaptor.ViewHolder> {


    private Context context;
    private List<ChosenMediaFile> songs;
    private OnItemClickListner onItemClickListner;


    public AudioListAdaptor(Context context, List<ChosenMediaFile> songs, OnItemClickListner onItemClickListner) {
        this.context = context;
        this.songs = songs;
        this.onItemClickListner=onItemClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item,parent,false);
        return new AudioListAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.songname.setText(songs.get(position).getFileName());
        holder.folderName.setText(songs.get(position).getAlbum());
        holder.songDuratition.setText(getFormatedDuration(songs.get(position).getDuration()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    onItemClickListner.OnItemChecked(songs.get(position));
                }else {
                    onItemClickListner.OnItemUnChecked(songs.get(position));
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songname;
        TextView folderName;
        TextView songDuratition;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songname=itemView.findViewById(R.id.songname);
            songDuratition=itemView.findViewById(R.id.songDuratition);
            checkBox=itemView.findViewById(R.id.selected_song);
            folderName=itemView.findViewById(R.id.songFolder);
        }
    }

     public interface OnItemClickListner{
         void OnItemChecked(ChosenMediaFile chosenAudio);
         void OnItemUnChecked(ChosenMediaFile chosenAudio);
    }

    private String getFormatedDuration(long duration) {
       return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }
}
