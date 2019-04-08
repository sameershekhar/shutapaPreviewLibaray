package com.example.customlibrary.activity;

import android.Manifest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.customlibrary.R;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static ListView lvResults;
    private LinearLayoutManager linearLayoutManager;
    private  RecyclerView chat_recyclerView;

    private List<ChosenMediaFile> chosenImageList=new ArrayList<>();
    private ImageButton btPickImageSingle,getmp3;
    private Button btPickImageMultiple;
    private Button btTakePicture;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMARA = 1;
    private int pickerType;
    private String videoPickerPath;
    private int videoPickerType;
    private Cursor cursor;

    public MainActivity() {

    }

    private String pickerPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("IMAGES"));

        setContentView(R.layout.activity_main);


        requestPermission();


       // lvResults = (ListView) findViewById(R.id.lvResults);
        btPickImageSingle = findViewById(R.id.pickerIcon);
        btPickImageSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);

            }
        });


    }





    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        ImageView gallaryIcon=popupView.findViewById(R.id.gallaryIcon);
        ImageView camaraIcon=popupView.findViewById(R.id.camaraIcon);
        ImageView videoIcon=popupView.findViewById(R.id.vidoeIcon);
        ImageView audioIcon=popupView.findViewById(R.id.audioIcon);

        gallaryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pickImageSingle();

                startActivity(new Intent(MainActivity.this,ImageListActivity.class));
                popupWindow.dismiss();
            }

        });

        camaraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takePicture();
                popupWindow.dismiss();
            }

        });

        videoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pickVideos();

                startActivity(new Intent(MainActivity.this,VideoListActivity.class));
                popupWindow.dismiss();
            }

        });
        audioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // pickAudios();
                ShowSelectedSongs();
                popupWindow.dismiss();
            }

        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }





    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CAMARA);
        }

    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // String receivedHexColor = intent.getStringExtra("BG_SELECT");



            List<ChosenMediaFile> chosenImages=intent.getParcelableArrayListExtra("images");
            chosenImageList.addAll(chosenImages);
            //chatAdapter.notifyItemInserted(chosenImageList.size());

//            Toast.makeText(getApplicationContext(),chosenImages.size()+"",Toast.LENGTH_LONG).show();
//            for (ChosenImage image : chosenImages) {
//
//            }



        }
    };



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        outState.putInt("video_picker_type", videoPickerType);
        outState.putString("video_picker_path", videoPickerPath);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }else if(savedInstanceState.containsKey("video_picker_type")){
                videoPickerType = savedInstanceState.getInt("video_picker_type");
                videoPickerPath = savedInstanceState.getString("video_picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //FireGallaryMediaPickerLibrary();
               // pickImageSingle();
            }
        }
//

            if(requestCode==MY_PERMISSIONS_REQUEST_CAMARA){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               // takePicture();
            }
        }

    }




    private void ShowSelectedSongs() {


        Intent intent = new Intent(this, AudioListActivity.class);
        //intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) songList);
        startActivity(intent);

    }

}
