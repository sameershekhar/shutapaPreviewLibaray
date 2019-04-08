package com.example.customlibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.List;

public class ImagePreviewGridAdaptor extends RecyclerView.Adapter<ImagePreviewGridAdaptor.ViewHolder> {
    private Context context;
    private List<ChosenMediaFile> chosenImageList;
    private OnItemClickListner onItemClickListner;



    public ImagePreviewGridAdaptor(Context context, List<ChosenMediaFile> chosenImageList,OnItemClickListner onItemClickListner) {
        this.context = context;
        this.chosenImageList = chosenImageList;
        this.onItemClickListner=onItemClickListner;
    }



        @NonNull
        @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_preview_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final boolean[] isChecked = {false};

        Glide.with(context)
                .load(chosenImageList.get(position).getUri())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.mediapicker_image_item_thumbnail);



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChecked[0]){
                    onItemClickListner.OnItemChecked(chosenImageList.get(position));
                    holder.picked_image_frame.setVisibility(View.VISIBLE);
                    holder.icon.setVisibility(View.VISIBLE);
//                    holder.selectOff.setVisibility(View.VISIBLE);
//                    holder.selectOn.setVisibility(View.VISIBLE);
//                    holder.selectOverlay.setVisibility(View.VISIBLE);
                    isChecked[0] =true;

                }else {
                    onItemClickListner.OnItemUnChecked(chosenImageList.get(position));
                    holder.picked_image_frame.setVisibility(View.GONE);
                    holder.icon.setVisibility(View.GONE);
//                    holder.selectOff.setVisibility(View.VISIBLE);
//                    holder.selectOn.setVisibility(View.VISIBLE);
//                    holder.selectOverlay.setVisibility(View.VISIBLE);
                    isChecked[0] =false;
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return chosenImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView   mediapicker_image_item_thumbnail,icon;
        private View      view;
        private FrameLayout picked_image_frame;
//        private View      selectOn;
//        private View      selectOff;
//        private View      selectOverlay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            mediapicker_image_item_thumbnail =itemView.findViewById(R.id.imageView);
            picked_image_frame =itemView.findViewById(R.id.picked_image_frame);
            icon=itemView.findViewById(R.id.icon);

//            selectOn                         = itemView.findViewById(R.id.mediapicker_select_on);
//            selectOff                        = itemView.findViewById(R.id.mediapicker_select_off);
//            selectOverlay                    = itemView.findViewById(R.id.mediapicker_select_overlay);
        }
    }

    public interface OnItemClickListner{
        void OnItemChecked(ChosenMediaFile chosenImage);
        void OnItemUnChecked(ChosenMediaFile chosenImage);
    }
}
