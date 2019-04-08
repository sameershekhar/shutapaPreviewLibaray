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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customlibrary.R;
import com.example.customlibrary.adapter.VideoPreviewGridAdaptor;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int INTERNAL_STORAGE=1;
    private final int EXTERNAL_STORAGE=2;
    private RecyclerView grirdRecyclerView;
    private List<ChosenMediaFile> selectedVideoList=new ArrayList<>();
    private List<ChosenMediaFile> finalVideoList=new ArrayList<>();
    private Toolbar toolbar;
    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        grirdRecyclerView=findViewById(R.id.video_grid_layout);
        toolbar=findViewById(R.id.toolbar);
        ok=findViewById(R.id.video_ok);
        toolbar.setTitle("All videos");
        toolbar.setTitleTextColor(getResources().getColor(R.color.core_white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getLoaderManager().initLoader(EXTERNAL_STORAGE,null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        String[] projection = {
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.MIME_TYPE,

        };



//videos file format, not supproted avi and flv,flack is audio format
        String[] extension={"%mp4","%webm","%3gp","%m4v","%mkv","%mov"};

         if(id==EXTERNAL_STORAGE){
//            Toast.makeText(this,"External storage",Toast.LENGTH_LONG).show();
            return new CursorLoader(this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ?",
                    extension,
                    null);
        }
        else if(id==INTERNAL_STORAGE){
//            Toast.makeText(this,"External storage",Toast.LENGTH_LONG).show();
            return new CursorLoader(this, MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                    projection,
                    MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Video.Media.DISPLAY_NAME + " LIKE ?",
                    extension,
                    null);
        }
        return null;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {


        if(loader.getId()==EXTERNAL_STORAGE){

//            Set<ChosenVideo> internalVideoList = GetDataFromCursor(cursor);
            finalVideoList.addAll(GetDataFromCursor(cursor));
            getLoaderManager().initLoader(INTERNAL_STORAGE,null,this);

    }
     else if(loader.getId()==INTERNAL_STORAGE){
            finalVideoList.addAll(GetDataFromCursor(cursor));
           // Toast.makeText(VideoListActivity.this,"Internal storage",Toast.LENGTH_LONG).show();
        }



        if(finalVideoList!=null){
           // Toast.makeText(VideoListActivity.this,finalVideoList.size(),Toast.LENGTH_LONG).show();
            VideoPreviewGridAdaptor videoPreviewGridAdaptor=new VideoPreviewGridAdaptor(this, finalVideoList, new VideoPreviewGridAdaptor.OnItemClickListner() {
                @Override
                public void OnItemChecked(ChosenMediaFile chosenVideo) {
                    selectedVideoList.add(chosenVideo);
                    toolbar.setTitle(selectedVideoList.size()+" videos ");
                }

                @Override
                public void OnItemUnChecked(ChosenMediaFile chosenVideo) {
                    selectedVideoList.remove(chosenVideo);
                    if(selectedVideoList.size()==0){
                        toolbar.setTitle("All videos ");
                    }else {
                        toolbar.setTitle(selectedVideoList.size()+" videos ");
                    }

                }
            });
            grirdRecyclerView.setAdapter(videoPreviewGridAdaptor);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
            //AutoFitGridLayoutManager autoFitGridLayoutManager = new AutoFitGridLayoutManager(this, 150);

            grirdRecyclerView.setLayoutManager(gridLayoutManager);
            grirdRecyclerView.setItemViewCacheSize(finalVideoList.size());
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedVideoList.size()>0){
                        Intent intent = new Intent(VideoListActivity.this, VideoPreviewActivty.class);
                        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) selectedVideoList);
                        startActivity(intent);
                    }else {
                        Toast.makeText(VideoListActivity.this,"Select videos first",Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }

    private List<ChosenMediaFile> GetDataFromCursor(Cursor cursor) {
        List<ChosenMediaFile> internalVideoList = new ArrayList<>();
        while(cursor.moveToNext()){

            String displayName = cursor
                    .getString(0);
            int _id = cursor.getInt(1);

            String uri = cursor.getString(2);


            //fullsongpath.add(fullpath);

            int width = cursor.getInt(3);
            //int album_id = cursor.getInt(4);

            int  height = cursor.getInt(4);
            String mimeType=cursor.getString(5);

//            Log.d("SonglistFilePath", "onLoadFinished: "+ " "+displayName +" "+uri+" "+
//                    width+" "+height+" "+mimeType);


            ChosenMediaFile chosenVideo=new ChosenMediaFile();
            chosenVideo.setFileName(displayName);
            chosenVideo.setUri(uri);
            chosenVideo.setExtType(getFileExtension(displayName));
            chosenVideo.setMimeType(mimeType);
            chosenVideo.setWidth(width);
            chosenVideo.setHeight(height);
            internalVideoList.add(chosenVideo);
        }
        return internalVideoList;
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf);
    }
}
