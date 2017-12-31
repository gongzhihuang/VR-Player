package com.example.vrplayer;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import android.app.Activity;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

/**
 * Created by Administrator on 2017/6/1.
 */


public class PictureActivity extends AppCompatActivity {
    private static final String TAG = PictureActivity.class.getSimpleName();
    public static final int CHOSE_PHOTO = 1;

    private VrPanoramaView picture;
    private Button chosephoto;
    private Uri fileUri;
    private VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        chosephoto = (Button)findViewById(R.id.chose_photo);
        picture = (VrPanoramaView) findViewById(R.id.picture);
        picture.setTransitionViewEnabled(false);
        chosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PictureActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(PictureActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else {
                    openAlbum();//打开相册
                }
            }
        });
    }


    @Override
    protected void onPause() {
        picture.pauseRendering();
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        picture.resumeRendering();
    }
    @Override
    protected void onDestroy() {
        picture.shutdown();
        super.onDestroy();
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOSE_PHOTO);//打開相冊
    }
    //获取权限
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                else {
                    Toast.makeText(this,"no permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    //选择完图片后进入onActivityResult,
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case CHOSE_PHOTO:
                handleImageOnKitKat(data);
                break;
            default:
                break;
        }
    }
    private void handleImageOnKitKat(Intent data){//4.4以上版本处理图片,通过URI加载图片
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){//document类型的URI
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection= MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())){//content类型的URI
                imagePath = getImagePath(uri,null);
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())){//file类型的URI
                imagePath = uri.getPath();
            }
            picture.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);//橫屏雙眼
            picture.loadImageFromBitmap(BitmapFactory.decodeFile(imagePath),panoOptions);//加载图片
        }
    }
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor =getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path ;
    }
}


