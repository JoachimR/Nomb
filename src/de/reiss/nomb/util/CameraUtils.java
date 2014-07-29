package de.reiss.nomb.util;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import de.reiss.nomb.G;

public class CameraUtils {


    public static void setCameraDisplayOrientationFront(
            Activity activity, Camera camera) {
        setCameraDisplayOrientation(activity,
                Camera.CameraInfo.CAMERA_FACING_FRONT, camera);
    }

    public static void setCameraDisplayOrientationBack(
            Activity activity, Camera camera) {
        setCameraDisplayOrientation(activity,
                Camera.CameraInfo.CAMERA_FACING_BACK, camera);
    }

    public static Camera openFrontFacingCamera() {
        return openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    public static Camera openBackFacingCamera() {
        return openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private static void setCameraDisplayOrientation(Activity activity,
                                                    int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * http://stackoverflow.com/a/4767832
     */
    private static Camera openCamera(int facing) {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == facing) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Log.e(G.TAG, "Camera failed to open: " + e.getMessage());
                }
            }
        }

        return cam;
    }
}
