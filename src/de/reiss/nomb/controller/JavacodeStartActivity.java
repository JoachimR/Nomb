package de.reiss.nomb.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.R;
import de.reiss.nomb.controller.javacode.MessageActivity;


public class JavacodeStartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_javacode);
        initButtonClickListeners(this);
    }

    private void initButtonClickListeners(Activity activity) {
        initButton(activity, R.id.btn_start_filehandling, de.reiss.nomb.controller.javacode.FileHandlingActivity.class);
        initButton(activity, R.id.btn_start_personalinfo, de.reiss.nomb.controller.javacode.TelephonyActivity.class);
        initButton(activity, R.id.btn_start_recordaudio, de.reiss.nomb.controller.javacode.RecordAudioActivity.class);
        initButton(activity, R.id.btn_start_takepicture, de.reiss.nomb.controller.javacode.TakePictureActivity.class);
        initButton(activity, R.id.btn_start_message, MessageActivity.class);
        initButton(activity, R.id.btn_start_location, de.reiss.nomb.controller.javacode.LocationActivity.class);
        initButton(activity, R.id.btn_start_nfc, de.reiss.nomb.controller.javacode.NfcActivity.class);
        initButton(activity, R.id.btn_start_network, de.reiss.nomb.controller.javacode.NetworkActivity.class);
        initButton(activity, R.id.btn_start_packagemanager, de.reiss.nomb.controller.javacode.PackageManagerInfoActivity.class);
        initButton(activity, R.id.btn_start_runcommand, de.reiss.nomb.controller.javacode.RunCommandActivity.class);
//        initButton(c, R.id.btn_start_runtime, de.reiss.nomb.controller.javacode.RuntimeActivity.class);
        initButton(activity, R.id.btn_start_sensor, de.reiss.nomb.controller.javacode.SensorActivity.class);
        initButton(activity, R.id.btn_start_webview, de.reiss.nomb.controller.javacode.WebViewActivity.class);
    }

    private void initButton(final Activity a, final int viewid,
                            final Class activityToStart) {
        Button b = (Button) findViewById(viewid);
        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(a, activityToStart);
                    a.startActivity(intent);
                }
            });
        }
    }

}