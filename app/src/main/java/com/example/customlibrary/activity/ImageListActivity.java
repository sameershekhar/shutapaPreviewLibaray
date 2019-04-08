package com.example.customlibrary.activity;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.customlibrary.R;
import com.example.customlibrary.adapter.ImagePreviewGridAdaptor;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private final int INTERNAL_STORAGE=0;
  private final int EXTERNAL_STORAGE=1;
  private List<ChosenMediaFile> finalImageList=new ArrayList<>();
  private List<ChosenMediaFile> selectedImages=new ArrayList<>();
  private RecyclerView image_gird_recycler_view;
  private TextView ok;
  private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        image_gird_recycler_view=findViewById(R.id.image_grid_layout);
        ok=findViewById(R.id.image_ok);
        toolbar=findViewById(R.id.image_toolbar);
        toolbar.setTitle("All images");
        toolbar.setTitleTextColor(getResources().getColor(R.color.core_white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getLoaderManager().initLoader(INTERNAL_STORAGE,null,this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        String[] projection = {
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.MIME_TYPE,

        };



//
//        String[] extension={"%mp4","%m4a","%mp3","%wav"};
//        String[] extension1=new String[]{"%wav"};

        if(id== INTERNAL_STORAGE){
            return new CursorLoader(this, MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        }
        else if(id==EXTERNAL_STORAGE){
//            Toast.makeText(this,"External storage",Toast.LENGTH_LONG).show();
            return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
//                    MediaStore.Audio.Media.IS_MUSIC + " != 0",
                    null,
                    null,
                    null);
        }
        return null;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if(loader.getId()==INTERNAL_STORAGE){
            finalImageList.addAll(GetDataFromCursor(cursor));
            getLoaderManager().initLoader(EXTERNAL_STORAGE,null,this);
        } else if(loader.getId()==EXTERNAL_STORAGE){
            finalImageList.addAll(GetDataFromCursor(cursor));
        }
        if(finalImageList!=null){

            ImagePreviewGridAdaptor imagePreviewGridAdaptor=new ImagePreviewGridAdaptor(ImageListActivity.this, finalImageList, new ImagePreviewGridAdaptor.OnItemClickListner() {
                @Override
                public void OnItemChecked(ChosenMediaFile chosenImage) {
                    selectedImages.add(chosenImage);
                    toolbar.setTitle(selectedImages.size()+" images ");
                   // toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }

                @Override
                public void OnItemUnChecked(ChosenMediaFile chosenImage) {
                     selectedImages.remove(chosenImage);
                    toolbar.setTitle(selectedImages.size()+" images ");
                    //toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    if(selectedImages.size()==0){
                        //toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                        toolbar.setTitle("All images");

                    }
                }
            });

            image_gird_recycler_view.setAdapter(imagePreviewGridAdaptor);
            image_gird_recycler_view.setItemViewCacheSize(finalImageList.size());
            GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
           // AutoFitGridLayoutManager autoFitGridLayoutManager = new AutoFitGridLayoutManager(this, 150);
            image_gird_recycler_view.setLayoutManager(gridLayoutManager);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(selectedImages.size()>0){
                        Intent intent = new Intent(ImageListActivity.this, ImagePreviewActivity.class);
                        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) selectedImages);
                        startActivity(intent);
                    }else {
                        Toast.makeText(ImageListActivity.this,"Select images first",Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    private List<ChosenMediaFile> GetDataFromCursor(Cursor cursor) {
        List<ChosenMediaFile> internalImages=new ArrayList<>();
        //Set<ChosenImage> internalImages=new LinkedHashSet<>();
        while (cursor.moveToNext()){
            String fileName=cursor.getString(0);
            int id=cursor.getInt(1);
            String data=cursor.getString(2);
            int width=cursor.getInt(3);
            int height=cursor.getInt(4);

            String mime_type=cursor.getString(4);



            ChosenMediaFile chosenImage=new ChosenMediaFile();
            chosenImage.setFileName(fileName);
            chosenImage.setExtType(getFileExtension(fileName));
            chosenImage.setUri(data);
            chosenImage.setWidth(width);
            chosenImage.setHeight(height);
            chosenImage.setMimeType(mime_type);
            internalImages.add(chosenImage);


        }
        return internalImages;
    }



    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf);
    }

}
