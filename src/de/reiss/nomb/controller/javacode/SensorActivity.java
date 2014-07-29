package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.G;
import de.reiss.nomb.R;
import de.reiss.nomb.util.Utils;

import java.util.List;


public class SensorActivity extends Activity implements SensorEventListener {


    Activity mActivity;


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_sensor);
        mActivity = this;


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        Button btn_getdefaultsensor = (Button)
                findViewById(R.id.btn_getdefaultsensor);
        btn_getdefaultsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder sb = new StringBuilder();
                sb.append("Looking for Sensor TYPE_ACCELEROMETER: \n\n\n");

                if (mSensorManager != null) {
                    Sensor acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                    if (acc != null) {
                        sb.append("Got Sensor: '" + acc.getName() + "'");
                    } else {
                        sb.append(G.NOTHING_FOUND + "\n\n" + "Could not get Sensor TYPE_ACCELEROMETER: ");
                    }
                } else {
                    sb.append(G.NOTHING_FOUND + "\n\n" + "Sensor Manager is null!");
                }
                Utils.showResultInDialog(mActivity, sb.toString());
            }
        });

        Button btn_getsensorlist = (Button)
                findViewById(R.id.btn_getsensorlist);
        btn_getsensorlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                sb.append("Sensor List:" + "\n\n\n\n");
                if (mSensorManager != null) {
                    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
                    if (sensors != null && !sensors.isEmpty()) {
                        for (Sensor s : sensors) {
                            sb.append("'" + s.getName() + "'" + "\n\n");
                        }
                    } else {
                        sb.append(G.NOTHING_FOUND + "\n\n" + "getSensorList is null or empty!");
                    }
                } else {
                    sb.append(G.NOTHING_FOUND + "\n\n" + "Sensor Manager is null!");
                }
                Utils.showResultInDialog(mActivity, sb.toString());
            }
        });


    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    }


}