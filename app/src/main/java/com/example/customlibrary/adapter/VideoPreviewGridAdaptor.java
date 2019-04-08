package com.example.customlibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoPreviewGridAdaptor extends RecyclerView.Adapter<VideoPreviewGridAdaptor.MyViewHolder> {

    private Context context;
    private List<ChosenMediaFile> chosenVideoList;
    private OnItemClickListner onItemClickListner;
    private Map<Integer,Boolean> validMediaFiles=new HashMap<>();


    public VideoPreviewGridAdaptor(Context context, List<ChosenMediaFile> chosenVideoList,OnItemClickListner onItemClickListner) {
        this.context = context;
        this.chosenVideoList = chosenVideoList;
        this.onItemClickListner=onItemClickListner;
    }

    @NonNull
    @Override
    public VideoPreviewGridAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_preview_grid_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPreviewGridAdaptor.MyViewHolder holder, int position) {
        final boolean[] isChecked = {false};
        Glide.with(context)
                .asBitmap()
                .load(chosenVideoList.get(position).getUri())
                .apply(new RequestOptions()
                        .centerCrop())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                       validMediaFiles.put(position,false);
                       return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChecked[0]){

                    if(validMediaFiles.containsKey(position)){
                        //Log.d("SonglistFilePath", "onLoadFinished: "+chosenVideoList.get(position).getMimeType());
                        Toast.makeText(context,"can't play this media file",Toast.LENGTH_LONG).show();
                    }else {
                        onItemClickListner.OnItemChecked(chosenVideoList.get(position));
                        holder.picked_image_frame.setVisibility(View.VISIBLE);
                        holder.icon.setVisibility(View.VISIBLE);
                        isChecked[0] =true;
                    }


                }else {
                    onItemClickListner.OnItemUnChecked(chosenVideoList.get(position));
                    holder.picked_image_frame.setVisibility(View.GONE);
                    holder.icon.setVisibility(View.GONE);
                    isChecked[0] =false;
                }

            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(validMediaFiles.containsKey(position)){
                    Toast.makeText(context,"can't play this media file",Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return chosenVideoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,icon;
        FrameLayout picked_image_frame;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            imageView=itemView.findViewById(R.id.imageView);
            picked_image_frame=itemView.findViewById(R.id.picked_image_frame);
            icon=itemView.findViewById(R.id.icon);

        }
    }

    public interface OnItemClickListner{
        void OnItemChecked(ChosenMediaFile chosenVideo);
        void OnItemUnChecked(ChosenMediaFile chosenVideo);
    }
}
