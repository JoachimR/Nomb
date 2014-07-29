package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import de.reiss.nomb.util.CameraUtils;
import de.reiss.nomb.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class TakePictureActivity extends Activity implements SurfaceHolder.Callback {

    private Activity mActivity;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;

    private FrameLayout mFrameLayout;

    private ImageView iv_result;


    private Timer loopTimer;


    private ProgressDialog mProgressDialog;

    private Button mBtnChangecam;
    private Button mBtnCapture;
    private Button mBtnCaptureLoopToggle;
    private Button mBtnPreview;
    private Button mBtnPreviewLoopToggle;


    private static boolean isPictureBeingTakenRightNow;
    private static boolean isLoopRunning;
    private static boolean isFrontCameraInUse;
    private static double camPreviewSizeChangeValue;
    private final static double maxWidth = 640;
    private final static double maxHeight = 480;
    private final static double minWidth = maxWidth * 0.01;
    private final static double minHeight = maxHeight * 0.01;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_takepicture);
        mActivity = this;

        isPictureBeingTakenRightNow = false;
        isLoopRunning = false;
        isFrontCameraInUse = false;
        camPreviewSizeChangeValue = 0.1;

        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(mActivity.getString(R.string.dlg_initcam));
        mProgressDialog.show();


        iv_result = (ImageView) findViewById(R.id.iv_result);
        initCameraStuff();
        initButtonClickListeners();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }


    }

    private void initCameraStuff() {
        mCamera = Camera.open();
        if (mCamera == null) {
            return;
        }
        mSurfaceView = new SurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mFrameLayout = (FrameLayout)
                findViewById(R.id.fl_camera_preview);
        mFrameLayout.addView(mSurfaceView);
    }

    private void initButtonClickListeners() {

        mBtnChangecam = (Button) findViewById(R.id.btn_changecam);
        mBtnChangecam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPictureBeingTakenRightNow || isLoopRunning) {
                    return;
                }
                try {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                    if (isFrontCameraInUse) {
                        mCamera = CameraUtils.openBackFacingCamera();
                        if (mCamera != null) {
                            isFrontCameraInUse = false;
                            mBtnChangecam.setText(
                                    getString(R.string.btn_changetofrontcam));
                            CameraUtils.setCameraDisplayOrientationBack(mActivity, mCamera);
                            mCamera.setPreviewDisplay(mSurfaceHolder);
                            mCamera.startPreview();
                        }
                    } else {
                        mCamera = CameraUtils.openFrontFacingCamera();
                        if (mCamera != null) {
                            isFrontCameraInUse = true;
                            mBtnChangecam.setText(
                                    getString(R.string.btn_changetobackcam));
                            CameraUtils.setCameraDisplayOrientationFront(mActivity, mCamera);
                            mCamera.setPreviewDisplay(mSurfaceHolder);
                            mCamera.startPreview();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(G.TAG, e.getMessage());
                }
            }
        });

        mBtnCapture = (Button) findViewById(R.id.btn_capture);
        mBtnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPictureBeingTakenRightNow) {
                    isPictureBeingTakenRightNow = true;
                    if (mCamera != null) {
                        mCamera.takePicture(null, null, photoCallback);
                    }
                }
            }
        });

        mBtnCaptureLoopToggle = (Button) findViewById(R.id.btn_captureloop_toogle);
        mBtnCaptureLoopToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoopRunning) {
                    isLoopRunning = false;
                    if (loopTimer != null) {
                        loopTimer.cancel();
                        loopTimer.purge();
                        loopTimer = null;
                    }
                    setButtonsToStatusNothingIsRunning();
                } else {
                    isLoopRunning = true;
                    loopTimer = new Timer();
                    loopTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (mCamera != null) {
                                if (!isPictureBeingTakenRightNow) {
                                    isPictureBeingTakenRightNow = true;
                                    if (mCamera != null) {
                                        mCamera.takePicture(null, null, photoCallback);
                                    }
                                }
                            }
                        }
                    }, 0, 5000);
                    setButtonsToStatusCaptureLoopIsRunning();
                }
            }
        });


        mBtnPreview = (Button) findViewById(R.id.btn_takepreview);
        mBtnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPictureBeingTakenRightNow) {
                    isPictureBeingTakenRightNow = true;
                    if (mCamera != null) {
                        mCamera.setOneShotPreviewCallback(previewCallback);
                    }
                }
            }
        });

        mBtnPreviewLoopToggle = (Button) findViewById(R.id.btn_takepreviewloop_toogle);
        mBtnPreviewLoopToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoopRunning) {
                    isLoopRunning = false;
                    if (loopTimer != null) {
                        loopTimer.cancel();
                        loopTimer.purge();
                        loopTimer = null;
                    }
                    setButtonsToStatusNothingIsRunning();
                } else {
                    isLoopRunning = true;
                    loopTimer = new Timer();
                    loopTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (mCamera != null) {
                                if (!isPictureBeingTakenRightNow) {
                                    isPictureBeingTakenRightNow = true;
                                    if (mCamera != null) {
                                        mCamera.setOneShotPreviewCallback(previewCallback);
                                    }
                                }
                            }
                        }
                    }, 0, 5000);
                    setButtonsToStatusPreviewLoopIsRunning();
                }
            }
        });


        Button btn_size_minus = (Button) findViewById(R.id.btn_camsize_minus);
        btn_size_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFrameLayout != null) {
                    ViewGroup.LayoutParams params = mFrameLayout.getLayoutParams();
                    camPreviewSizeChangeValue -= 0.01;
                    params.height = (int) (maxHeight * camPreviewSizeChangeValue);
                    if (params.height < minHeight) {
                        params.height = (int) minHeight;
                    }
                    params.width = (int) (maxWidth * camPreviewSizeChangeValue);
                    if (params.width < minWidth) {
                        params.width = (int) minWidth;
                    }
                    mFrameLayout.setLayoutParams(params);
                }
            }
        });
        Button btn_camsize_plus = (Button) findViewById(R.id.btn_camsize_plus);
        btn_camsize_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFrameLayout != null) {
                    ViewGroup.LayoutParams params = mFrameLayout.getLayoutParams();
                    camPreviewSizeChangeValue += 0.01;
                    params.height = (int) (maxHeight * camPreviewSizeChangeValue);
                    if (params.height > maxHeight) {
                        params.height = (int) maxHeight;
                    }
                    params.width = (int) (maxWidth * camPreviewSizeChangeValue);
                    if (params.width > maxWidth) {
                        params.width = (int) maxWidth;
                    }
                    mFrameLayout.setLayoutParams(params);
                }
            }
        });
    }

    private void setButtonsToStatusNothingIsRunning() {
        mBtnChangecam.setEnabled(true);
        mBtnCapture.setEnabled(true);
        mBtnCaptureLoopToggle.setEnabled(true);
        mBtnPreviewLoopToggle.setEnabled(true);

        mBtnCaptureLoopToggle.setText(
                getString(R.string.btn_captureloop_start));
        mBtnPreviewLoopToggle.setText(
                getString(R.string.btn_takepreviewloop_start));
    }

    private void setButtonsToStatusCaptureLoopIsRunning() {
        mBtnChangecam.setEnabled(false);
        mBtnCapture.setEnabled(false);
        mBtnCaptureLoopToggle.setEnabled(true);
        mBtnPreviewLoopToggle.setEnabled(false);

        mBtnCaptureLoopToggle.setText(
                getString(R.string.btn_captureloop_stop));
        mBtnPreviewLoopToggle.setText(
                getString(R.string.btn_takepreviewloop_start));
    }

    private void setButtonsToStatusPreviewLoopIsRunning() {
        mBtnChangecam.setEnabled(false);
        mBtnCapture.setEnabled(false);
        mBtnCaptureLoopToggle.setEnabled(false);
        mBtnPreviewLoopToggle.setEnabled(true);

        mBtnCaptureLoopToggle.setText(
                getString(R.string.btn_captureloop_start));
        mBtnPreviewLoopToggle.setText(
                getString(R.string.btn_takepreviewloop_stop));
    }

    private Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SavePhotoAndSetImageViewTask().execute(data);
        }
    };

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            new SavePhotoAndSetImageViewTask().execute(bytes);
        }
    };

    private class SavePhotoAndSetImageViewTask extends AsyncTask<byte[], Void, Boolean> {

        @Override
        protected Boolean doInBackground(byte[]... jpeg) {
            boolean worked = true;
            try {
                File photo = new File(Environment.getExternalStorageDirectory(),
                        G.PICTURE_FILENAME);
                if (photo.exists()) {
                    photo.delete();
                }
                FileOutputStream fos = new FileOutputStream(photo.getPath());
                fos.write(jpeg[0]);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, e.getMessage());
                worked = false;
            }
            return worked;
        }

        @Override
        protected void onPostExecute(Boolean worked) {
            if (worked) {
                Utils.showResultInDialog(mActivity,"Saved picture as "
                        + "'" + G.PICTURE_FILENAME + "'");
//                Toast.makeText(mActivity, "Saved picture as "
//                        + "'" + G.PICTURE_FILENAME + "'",
//                        Toast.LENGTH_SHORT).show();
                showLastTakenPicture();
            } else {
                Utils.showResultInDialog(mActivity,"Could not save picture..");
//                Toast.makeText(mActivity, "Could not save picture..",
//                        Toast.LENGTH_SHORT).show();
            }

            if (mCamera != null) {
                mCamera.startPreview();
            }
            isPictureBeingTakenRightNow = false;
            super.onPostExecute(worked);
        }
    }

    private void showLastTakenPicture() {
        try {
            File imageFile = new File(
                    Environment.getExternalStorageDirectory(),
                    G.PICTURE_FILENAME);
            FileInputStream fis = new FileInputStream(imageFile);
            Bitmap bitmap = BitmapFactory.decodeStream(fis, null, null);
            iv_result.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, e.getMessage());
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera != null) {
                isFrontCameraInUse = false;
                CameraUtils.setCameraDisplayOrientationBack(mActivity, mCamera);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(G.TAG, e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        super.onDestroy();
    }


}