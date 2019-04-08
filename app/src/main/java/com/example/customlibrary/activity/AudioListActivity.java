package com.example.customlibrary.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.customlibrary.R;
import com.example.customlibrary.adapter.AudioListAdaptor;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.ArrayList;
import java.util.List;

public class AudioListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final int INTERNAL_STORAGE=1;
    private final int EXTERNAL_STORAGE=2;

    private Toolbar toolbar;
    private RecyclerView songlist;
    private ArrayList<ChosenMediaFile> songs;
    private List<ChosenMediaFile> SelectedAudiosFiles=new ArrayList<>();
    private AudioListAdaptor audioListAdaptor;
    private Button ok,cancelBtn;

    List<ChosenMediaFile> finalSongList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_activyt);
        songlist=findViewById(R.id.songlist);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("All audios");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        songlist.setLayoutManager(layoutManager);
        songlist.addItemDecoration(new DividerItemDecoration(songlist.getContext(), DividerItemDecoration.VERTICAL));

        getLoaderManager().initLoader(EXTERNAL_STORAGE,null, this);
        //getLoaderManager().initLoader(INTERNAL_STORAGE,null, this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
        };




        String[] extension={"%mp4","%m4a","%mp3","%wav"};
        //String[] extension1=new String[]{"%wav"};

//        if(id== INTERNAL_STORAGE){
//            return new CursorLoader(this, MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
//                    projection,
//                    MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ? OR "
//                            +MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ? OR "
//                            +MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ? OR "
//                            +MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ?",
//                    extension,
//                    null);
//        }
         if(id==EXTERNAL_STORAGE){
//            Toast.makeText(this,"External storage",Toast.LENGTH_LONG).show();
            return new CursorLoader(this, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
//                    MediaStore.Audio.Media.IS_MUSIC + " != 0",
                    MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ? OR "
                            +MediaStore.Audio.Media.DISPLAY_NAME + " LIKE ?",
                    extension,
                    null);
        }
        return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

          if(loader.getId()==EXTERNAL_STORAGE) {

              finalSongList.addAll(GetDataFromCursor(cursor));

            }

           // getLoaderManager().initLoader(INTERNAL_STORAGE,null, this);


//       else if(loader.getId()==INTERNAL_STORAGE){
//
//
//                Log.d("SonglistFilePath", "onLoadFinished: "+ " "+song_name +" "+song_id+" "+
//                        fullpath+" "+album_name+" "+album_id+" "+artist_id+" "+duration+" "+mimeType+" "+is_music);
//
//            }
//            finalSongList.addAll(internalSongList);
//
//


        if(finalSongList!=null){
            audioListAdaptor = new AudioListAdaptor(this, finalSongList, new AudioListAdaptor.OnItemClickListner() {
                @Override
                public void OnItemChecked(ChosenMediaFile chosenAudio) {
                    SelectedAudiosFiles.add(chosenAudio);
                    toolbar.setTitle(SelectedAudiosFiles.size()+" selected");

                }

                @Override
                public void OnItemUnChecked(ChosenMediaFile chosenAudio) {
                    SelectedAudiosFiles.remove(chosenAudio);
                    if(SelectedAudiosFiles.size()==0){
                        toolbar.setTitle("All audios");
                    }else {
                        toolbar.setTitle(SelectedAudiosFiles.size()+" selected");
                    }


                    //Toast.makeText(getApplicationContext(),"item UnSelected"+SelectedAudiosFiles.size(),Toast.LENGTH_LONG).show();
                }
            });
            songlist.setItemViewCacheSize(finalSongList.size());
            songlist.setAdapter(audioListAdaptor);

            ok=findViewById(R.id.okBtn);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SelectedAudiosFiles.size()==0){
                        Toast.makeText(AudioListActivity.this,"first select audio",Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent=new Intent(getApplicationContext(),AudioPreviewActivty.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("data1", (ArrayList<? extends Parcelable>) SelectedAudiosFiles);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    //startActivity(intent);

                }
            });
            cancelBtn=findViewById(R.id.cancelBtn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<ChosenMediaFile> GetDataFromCursor(Cursor cursor) {
        List<ChosenMediaFile> audioList = new ArrayList<>();
        while(cursor.moveToNext()){

            String displayName = cursor
                    .getString(0);
            int _id = cursor.getInt(1);

            String uri = cursor.getString(2);


            //fullsongpath.add(fullpath);
//
//            int width = cursor.getInt(3);
//            //int album_id = cursor.getInt(4);
//
//            int  height = cursor.getInt(4);
            String mimeType=cursor.getString(3);
            Long duration=cursor.getLong(4);
            String album=cursor.getString(5);

            Log.d("SonglistFilePath", "onLoadFinished: "+mimeType+" "+album);


            ChosenMediaFile chosenSong=new ChosenMediaFile();
            chosenSong.setFileName(displayName);
            chosenSong.setUri(uri);
            chosenSong.setExtType(getFileExtension(displayName));
            chosenSong.setMimeType(mimeType);
            chosenSong.setWidth(0);
            chosenSong.setHeight(0);
            chosenSong.setDuration(duration);
            chosenSong.setAlbum(album);
            audioList.add(chosenSong);
        }
        return audioList;
    }


    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf);
    }

}
