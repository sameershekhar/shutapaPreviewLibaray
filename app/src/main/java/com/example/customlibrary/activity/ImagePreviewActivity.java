package com.example.customlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.customlibrary.R;
import com.example.customlibrary.adapter.ImagePreviewAdapter;
import com.example.customlibrary.adapter.ImagePreviewBottomIconAdapter;
import com.example.customlibrary.entity.ChosenMediaFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImagePreviewActivity extends AppCompatActivity implements
        ImagePreviewAdapter.AdapterCallback, ImagePreviewBottomIconAdapter.ImageIconInterface {


    private final String  TAG="ImagePreviewActivity";
    private static final int CAMERA_REQUEST = 1888;
    private ImageView ivImageGlide;
    private String uri;
    private String mimeType;
    private int last_position=0;
    private List<ChosenMediaFile> images;
    private List<ChosenMediaFile> videos;

    private ChosenMediaFile image;
    private FloatingActionButton ok;
    private ImageView moreImage;
    private LinearLayout layout,layout_icon;
    private Toolbar topToolBar;
    private ActionBar actionBar;
    private RecyclerView preview_gallry_recyclerView;
    private ImagePreviewAdapter imagePreviewAdapter;
    private int current_position=0;
    private String pickerPath;

    private RecyclerView preview_icon_recyclerView;
    private ImagePreviewBottomIconAdapter imagePreviewBottomIconAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        //ivImageGlide=findViewById(R.id.ivImageGlide);
         topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        topToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
            public void onClick(View view) {
                finish();
            }
        });
        intiView();


    }

    private void intiView() {

        preview_gallry_recyclerView=findViewById(R.id.preview_gallry_recyclerView);
      //  preview_icon_recyclerView=findViewById(R.id.preview_icon_recyclerView);


        ok=findViewById(R.id.ok);
        moreImage=findViewById(R.id.moreImage);
        moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        images= getIntent().getParcelableArrayListExtra("data");
        if(images.size()>0){
            displayImage(images);
            onRotateGallaryImage(0);
            //DisplayIcon(images);
        }
    }

    private void displayImage( List<ChosenMediaFile> images) {

        this.images=images;
        imagePreviewAdapter=new ImagePreviewAdapter(this,images,this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false);
        preview_gallry_recyclerView = (RecyclerView) findViewById(R.id.preview_gallry_recyclerView);
        preview_gallry_recyclerView.setLayoutManager(linearLayoutManager);
        preview_gallry_recyclerView.setItemAnimator(new DefaultItemAnimator());
        preview_gallry_recyclerView.setAdapter(imagePreviewAdapter);
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(preview_gallry_recyclerView);
        preview_gallry_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    last_position=current_position;
                    current_position = linearLayoutManager.findFirstVisibleItemPosition();
                    onRotateGallaryImage(current_position);

                }
            }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);


        }
    });

        imagePreviewBottomIconAdapter=new ImagePreviewBottomIconAdapter(this,images,this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false);
        preview_icon_recyclerView = (RecyclerView) findViewById(R.id.preview_icon_recyclerView);
        preview_icon_recyclerView.setLayoutManager(linearLayoutManager1);
        preview_icon_recyclerView.setItemAnimator(new DefaultItemAnimator());
        preview_icon_recyclerView.setAdapter(imagePreviewBottomIconAdapter);


        imagePreviewAdapter.notifyDataSetChanged();
        imagePreviewBottomIconAdapter.notifyDataSetChanged();
    }

    public void DisplayMoreImages(List<ChosenMediaFile> images){

        this.images.addAll(images);

        imagePreviewAdapter.notifyDataSetChanged();
        imagePreviewBottomIconAdapter.notifyDataSetChanged();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.delete:
                deleteSelectedImage();
                break;
            case R.id.camara:
                takePicture();
                break;
            default:
                return true;


        }


        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedImage() {
        if(images.size()==1){
            return;
        }
        else if(images!=null){
            images.remove(current_position);
            current_position=current_position-1;
            if(current_position<0){
                current_position=0;
            }
            images.get(current_position).setIsSelected(1);
            imagePreviewAdapter.notifyDataSetChanged();
            imagePreviewBottomIconAdapter.notifyDataSetChanged();
            preview_gallry_recyclerView.getLayoutManager().scrollToPosition(current_position);
            preview_icon_recyclerView.getLayoutManager().scrollToPosition(current_position);

        }


    }


    public void takePicture() {
        try {
            takePictureWithCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void takePictureWithCamera() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pickerPath = storageDir.getAbsolutePath() + "/" + imageFileName;


        File file = new File(pickerPath);


        //Uri outputFileUri = Uri.fromFile(file);
        Log.d(TAG, "takePictureWithCamera: "+ pickerPath);

        Uri outputFileUri = FileProvider.getUriForFile(this, "com.example.customlibrary.utils.Fileprovider" ,file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            File file=new File(pickerPath);
            if(file.exists()){
                ChosenMediaFile chosenMediaFile=new ChosenMediaFile();
                chosenMediaFile.setUri(Uri.fromFile(file).toString());
                chosenMediaFile.setMimeType("jpg");

                images.add(chosenMediaFile);
                imagePreviewAdapter.notifyDataSetChanged();
                imagePreviewBottomIconAdapter.notifyDataSetChanged();

               // Log.d("TAG", "onActivityResult: "+Uri.fromFile(file));
            }

        }
    }


    @Override
    public void onImageSelecte(int position) {

        onRotateGallaryImage(position);
    }

    public void onRotateGallaryImage(int position){
//        for (ChosenMediaFile image : images) {
//            image.setIsSelected(0);
//        }

        if(last_position==images.size()){
            last_position=last_position-1;
        }
        images.get(last_position).setIsSelected(0);
        images.get(position).setIsSelected(1);
        imagePreviewBottomIconAdapter.notifyItemChanged(position);
        imagePreviewBottomIconAdapter.notifyDataSetChanged();
        preview_icon_recyclerView.getLayoutManager().scrollToPosition(position);

        //Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageSelectIcon(int position) {

        preview_gallry_recyclerView.getLayoutManager().smoothScrollToPosition(preview_gallry_recyclerView, new RecyclerView.State(),position);
        //Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_LONG).show();
    }
}
