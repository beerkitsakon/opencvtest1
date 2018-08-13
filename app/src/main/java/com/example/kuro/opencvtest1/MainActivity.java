package com.example.kuro.opencvtest1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "Mytag";
    JavaCameraView javacam;
    Mat mRGBa;
    BaseLoaderCallback mloadercallback=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    javacam.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }

        javacam = (JavaCameraView)findViewById(R.id.opencv_cam);
        javacam.setVisibility(SurfaceView.VISIBLE);
        javacam.setCvCameraViewListener(this);

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javacam!= null)
            javacam.disableView();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javacam!= null)
            javacam.disableView();

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"Opencv not loaded");
            mloadercallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.d(TAG,"Opencv loaded successfully");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,mloadercallback);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBa = new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBa.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBa = inputFrame.rgba();
        return mRGBa;
    }
}
