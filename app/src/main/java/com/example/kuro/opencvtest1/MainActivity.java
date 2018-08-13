package com.example.kuro.opencvtest1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "Mytag";
    Mat mRGBa,imgGray,imgCanny;
    ImageView show_image;

    BaseLoaderCallback mloadercallback=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
        show_image = (ImageView) findViewById(R.id.show_image);
        Button cap_button = (Button) findViewById(R.id.capture_button);

        cap_button.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1888);
                        break;
                    default:
                        break;
                }
                    return true;

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1888 && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Mat mat = new Mat();
            Mat edge = new Mat();
            Bitmap bmp32 = mphoto.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, mat);
           /* mRGBa = new Mat(mphoto.getHeight(), mphoto.getWidth(),CvType.CV_8UC4);
            imgGray = new Mat(mphoto.getHeight(), mphoto.getWidth(),CvType.CV_8UC1);
            imgCanny = new Mat(mphoto.getHeight(), mphoto.getWidth(),CvType.CV_8UC1);   */
            mRGBa = new Mat(mat.size(),CvType.CV_8UC4);
            imgGray = new Mat(mat.size(),CvType.CV_8UC1);
            imgCanny = new Mat(mat.size(),CvType.CV_8UC1);
            Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_RGB2GRAY);
           /* Imgproc.cvtColor(imgGray, mat, Imgproc.COLOR_GRAY2BGR);
            //To compate with default android standard (4 channel), so we need to change from 1 channel to 4 channel before we display
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2BGRA);*/
           // Imgproc.Canny(imgGray, mat, 50, 150);
            Imgproc.Canny(imgGray, imgCanny, 50, 150);

            Utils.matToBitmap(imgCanny, bmp32);
            // mRGBa = bmp32;
            show_image.setImageBitmap(bmp32);
            //Imgproc finalimage = Imgproc.cvtColor(bmp32, imgGray, Imgproc.COLOR_YUV420sp2GRAY);

            mat.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


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
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        return null;
    }
}
