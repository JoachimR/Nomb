package de.reiss.nomb.controller.nativecode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.R;

import de.reiss.nomb.util.Utils;
import opensl_example.opensl_example;


public class RecordAudioActivity extends Activity {

    Activity mActivity;

    Thread recordAudioNativeThread;

    private boolean isNativeRecordRunning;
    private Button toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativecode_recordaudio);
        mActivity = this;
        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        isNativeRecordRunning = false;

        toggle = (Button) findViewById(R.id.btn_recordaudionative);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNativeRecordRunning) {
                    if (!stopRecordingNative()) {
                        Utils.showResultInDialog(mActivity, "Native Recording did not work!");
                    } else {
                        Utils.showResultInDialog(mActivity, "Native Recording worked!");
                    }
                    isNativeRecordRunning = false;
                    toggle.setText(R.string.btn_startrecordingnative);
                } else {
                    startRecordingNative();
                    isNativeRecordRunning = true;
                    toggle.setText(R.string.btn_stoprecordingnative);
                }
            }
        });
    }

    private void startRecordingNative() {
        recordAudioNativeThread = new Thread() {
            public void run() {
                setPriority(Thread.MAX_PRIORITY);
                opensl_example.start_process();
            }
        };
        recordAudioNativeThread.start();
    }

    private boolean stopRecordingNative() {
        boolean success = true;
        opensl_example.stop_process();
        try {
            recordAudioNativeThread.join();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        recordAudioNativeThread = null;

        return success;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isNativeRecordRunning) {
            stopRecordingNative();
            isNativeRecordRunning = false;
            toggle.setText(R.string.btn_startrecordingnative);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isNativeRecordRunning) {
            stopRecordingNative();
            isNativeRecordRunning = false;
            toggle.setText(R.string.btn_startrecordingnative);
        }
    }
}