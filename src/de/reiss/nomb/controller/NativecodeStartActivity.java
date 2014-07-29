package de.reiss.nomb.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.R;
import de.reiss.nomb.controller.nativecode.MessageActivity;
import de.reiss.nomb.controller.nativecode.PackageManagerInfoActivity;


public class NativecodeStartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_nativecode);
        initButtonClickListeners(this);
    }

    private void initButtonClickListeners(Activity activity) {
        initButton(activity, R.id.btn_start_filehandling, de.reiss.nomb.controller.nativecode.FileHandlingActivity.class);
        initButton(activity, R.id.btn_start_recordaudio, de.reiss.nomb.controller.nativecode.RecordAudioActivity.class);
        initButton(activity, R.id.btn_start_message, de.reiss.nomb.controller.nativecode.MessageActivity.class);
        initButton(activity, R.id.btn_start_network, de.reiss.nomb.controller.nativecode.NetworkActivity.class);
        initButton(activity, R.id.btn_start_packagemanager, de.reiss.nomb.controller.nativecode.PackageManagerInfoActivity.class);
        initButton(activity, R.id.btn_start_runcommand, de.reiss.nomb.controller.nativecode.RunCommandActivity.class);
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