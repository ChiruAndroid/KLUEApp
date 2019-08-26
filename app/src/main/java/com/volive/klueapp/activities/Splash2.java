package com.volive.klueapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.volive.klueapp.R;
import com.volive.klueapp.utils.FullScreenVideoView;

import java.util.ArrayList;
import java.util.List;

import static com.volive.klueapp.fragments.MyAccount.MY_REQUEST_CODE;


public class Splash2 extends AppCompatActivity {

    static String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_SMS};
    private static int MULTIPLE_PERMISSIONS = 10;

    FullScreenVideoView video;
    String video_url = "http://yummyapp.net/public/uploads/79b433238992bda1825b51b18fc3bd04.mp4";
    TextView textView;
    ImageView imageView;
    ProgressDialog progressDialog;

    public static boolean checkPermissions(Context context) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen2);
        initializeUI();
        permissionsValues();
//        video.setZOrderOnTop(true); //to "hide" before setting up your video

    }

    private void initializeUI() {
        imageView = findViewById(R.id.image1);
        video = findViewById(R.id.fullScreenVideoView);
        textView = (TextView) findViewById(R.id.text);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash2.this, OnBoardingScreen.class);
                startActivity(intent);
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }

    private void permissionsValues() {
        if (ActivityCompat.checkSelfPermission(Splash2.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Splash2.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Splash2.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Splash2.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Splash2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Splash2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_SMS},
                    MY_REQUEST_CODE);
        }
    }

    private void playVideo() {
        progressDialog = new ProgressDialog(Splash2.this);
        progressDialog.setMessage(getString(R.string.buferring_video_plz_wait));
        progressDialog.show();
        video.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);
        video.start();
        imageView.setVisibility(View.GONE);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                mp.setLooping(true);
                progressDialog.dismiss();
//                        video.setZOrderOnTop(true); //after, in your videoView.setOnPrepare
            }
        });
    }
}