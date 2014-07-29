package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.media.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import de.reiss.nomb.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class RecordAudioActivity extends Activity {

    Activity mActivity;

    Handler recordAudioResultHandler;


    private Button btn_start_recordaudio2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_recordaudio);
        mActivity = this;
        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        Button btn_recordaudio = (Button) findViewById(R.id.btn_start_recordaudio);
        btn_recordaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordAudio.perform(recordAudioResultHandler);
            }
        });
        recordAudioResultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == G.RECORDAUDIOFINISHED) {
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("result");
                    if (result != null) {
                        Utils.showResultInDialog(mActivity, result);
                    } else {
                        Utils.showResultInDialog(mActivity,
                                "Record audio result was null!");
                    }
                }
            }
        };


        btn_start_recordaudio2 = (Button) findViewById(R.id.btn_start_recordaudio2);
        btn_start_recordaudio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoopbackRunning) {
                    stopLoopback();
                    isLoopbackRunning = false;
                    btn_start_recordaudio2.setText(R.string.btn_recordaudiojava2_start);
                } else {
                    startLoopback();
                    isLoopbackRunning = true;
                    btn_start_recordaudio2.setText(R.string.btn_recordaudiojava2_stop);
                }
            }
        });


    }

    ////////////////////////////////////////////////////////////

    // first method with MediaRecorder.java

    private static class RecordAudio {

        public static void perform(Handler activityHandler) {

            final Handler uiHandler = activityHandler;
            try {
                final MediaRecorder mr = new MediaRecorder();
                mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);


                final File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, G.AUDIO_FILENAME);
                if (!file.exists()) {
                    file.createNewFile();
                }

                String fn = sdcard.getAbsolutePath() + "/" + G.AUDIO_FILENAME;
                mr.setOutputFile(fn);

                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mr.prepare();
                mr.start();

                Log.d(G.TAG, "start recording" + new Date().toLocaleString());


                // stop recording after 5 seconds
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mr.stop();
                        mr.release();
                        tellActivity(uiHandler, "Recording and storing audio worked!");
                        Log.d(G.TAG, "finished recording" + new Date().toLocaleString());
                    }
                }, 5000);


            } catch (IOException ioe) {
                ioe.printStackTrace();
                Log.e(G.TAG, " RecordAudio IOException: " + ioe.getMessage());
                tellActivity(uiHandler, "Creating file on sd card to " +
                        "store recorded audio did not work!" + "\n" + ioe.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, " RecordAudio Exception: " + e.getMessage());
                tellActivity(uiHandler, "Recording did not work!" + "\n" + e.toString());
            }
        }

        private static void tellActivity(final Handler uiHandler, final String result) {
            // Finished. Tell the Activity
            if (uiHandler != null) {
                Message msg = new Message();
                msg.what = G.RECORDAUDIOFINISHED;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                msg.setData(bundle);
                uiHandler.sendMessage(msg);
            }
        }
    }


    ////////////////////////////////////////////////////////////

    // second method with AudioRecord.java

    private boolean isLoopbackRunning;

    private class RThread extends Thread {
        private boolean keepRunning = true;


        private int freq = 8000;
        private AudioRecord audioRecord = null;
        private AudioTrack audioTrack = null;

        public void stopRunning() {
            keepRunning = false;
        }

        public void run() {

            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            final int bufferSize = AudioRecord.getMinBufferSize(freq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);


            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, freq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    MediaRecorder.AudioEncoder.AMR_NB, bufferSize);

            audioTrack = new AudioTrack(AudioManager.ROUTE_HEADSET, freq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    MediaRecorder.AudioEncoder.AMR_NB, bufferSize,
                    AudioTrack.MODE_STREAM);


            audioTrack.setPlaybackRate(freq);
            final byte[] buffer = new byte[bufferSize];
            audioRecord.startRecording();
            Log.i(G.TAG, "Audio Recording started");
            audioTrack.play();
            Log.i(G.TAG, "Audio Playing started");


            keepRunning = true;
            while (keepRunning) {
                try {
                    audioRecord.read(buffer, 0, bufferSize);
                    audioTrack.write(buffer, 0, buffer.length);

                } catch (Throwable t) {
                    Log.e("Error", "Read write failed");
                    t.printStackTrace();
                }
            }
            audioRecord.stop();
        }
    }

    private RThread rThread;

    private void startLoopback() {
        rThread = new RThread();
        rThread.start();
    }

    private boolean stopLoopback() {
        boolean success = true;
        try {
            rThread.stopRunning();
            rThread.join();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        rThread = null;

        return success;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isLoopbackRunning) {
            stopLoopback();
            isLoopbackRunning = false;
            btn_start_recordaudio2.setText(R.string.btn_recordaudiojava2_start);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isLoopbackRunning) {
            stopLoopback();
            isLoopbackRunning = false;
            btn_start_recordaudio2.setText(R.string.btn_recordaudiojava2_start);
        }
    }


}