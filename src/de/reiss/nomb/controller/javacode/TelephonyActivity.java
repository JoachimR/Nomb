package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.R;

import de.reiss.nomb.util.DoMethodInBackgroundAndDisplayResultTask;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;


public class TelephonyActivity extends Activity {


    private Activity mActivity;

    private TelephonyManager telephonyManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_telephony);
        mActivity = this;
        initButtonClickListeners(this);

        telephonyManager =
                (TelephonyManager) mActivity.
                        getSystemService(Context.TELEPHONY_SERVICE);

    }

    private void initButtonClickListeners(Activity activity) {


        Button btn_getdeviceid = (Button) findViewById(R.id.btn_getdeviceid);
        btn_getdeviceid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getDeviceId();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button getline1number = (Button) findViewById(R.id.getline1number);
        getline1number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getLine1Number();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getsimserialnumber = (Button) findViewById(R.id.btn_getsimserialnumber);
        btn_getsimserialnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getSimSerialNumber();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getandroidid = (Button) findViewById(R.id.btn_getandroidid);
        btn_getandroidid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getANDROID_ID();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getallcellinfo = (Button) findViewById(R.id.btn_getallcellinfo);
        btn_getallcellinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getAllCellInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getcelllocation = (Button) findViewById(R.id.btn_getcelllocation);
        btn_getcelllocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getCellLocation();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getvoicemailmumber = (Button) findViewById(R.id.btn_getvoicemailmumber);
        btn_getvoicemailmumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getVoiceMailNumber();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getneighboringcellinfo = (Button) findViewById(R.id.btn_getneighboringcellinfo);
        btn_getneighboringcellinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getNeighboringCellInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getnetworkcountryiso = (Button) findViewById(R.id.btn_getnetworkcountryiso);
        btn_getnetworkcountryiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getNetworkCountryIso();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getnetworkoperator = (Button) findViewById(R.id.btn_getnetworkoperator);
        btn_getnetworkoperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getNetworkOperator();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getnetworktype = (Button) findViewById(R.id.btn_getnetworktype);
        btn_getnetworktype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getNetworkType();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getsimcountryiso = (Button) findViewById(R.id.btn_getsimcountryiso);
        btn_getsimcountryiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getSimCountryIso();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getsimoperatorname = (Button) findViewById(R.id.btn_getsimoperatorname);
        btn_getsimoperatorname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getSimOperatorName();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getphonetype = (Button) findViewById(R.id.btn_getphonetype);
        btn_getphonetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getPhoneType();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_getsubscriberid = (Button) findViewById(R.id.btn_getsubscriberid);
        btn_getsubscriberid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getSubscriberId();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_appwidgetinfo = (Button) findViewById(R.id.btn_appwidgetinfo);
        btn_appwidgetinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Callable callable = new Callable<String>() {
                    public String call() {
                        return getInstalledAppWidgetsInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

        Button btn_bluetoothinfo = (Button) findViewById(R.id.btn_bluetoothinfo);
        btn_bluetoothinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getBluetoothInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });


        Button btn_getinputdeviceinfo = (Button)
                findViewById(R.id.btn_getinputdeviceinfo);
        btn_getinputdeviceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return getInputDeviceInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

    }

    private String getInputDeviceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Get Input Device Info:" + "\n\n");

        for (int deviceId : InputDevice.getDeviceIds()) {
            InputDevice inputDevice = InputDevice.getDevice(deviceId);

            sb.append("Input Device: name: '" +
                    inputDevice.getName() + "'"

                    //API 16:
                    + ", descriptor: '"
                    + inputDevice.getDescriptor() + "'"

                    + "\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }


    private String getInstalledAppWidgetsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Installed AppWidgets:" + "\n\n");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        List<AppWidgetProviderInfo> appWidgetProviderInfos =
                appWidgetManager.getInstalledProviders();
        for (AppWidgetProviderInfo info : appWidgetProviderInfos) {
            sb.append(info.toString() + "\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }

    private String getBluetoothInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bluetooh Infos:" + "\n\n");

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            sb.append("Bluetooh not supported");
        } else {
            sb.append("BluetoothAdapter Address: : " + bluetoothAdapter.getAddress() + "\n");


            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            if (bondedDevices.size() < 1) {
                sb.append("no bonded devices...");
            } else {
                for (BluetoothDevice device : bondedDevices) {
                    sb.append("bonded device: : " + device.getName() + "\n");
                    sb.append("bonded device address: : " + device.getAddress() + "\n");

                }
            }
        }
        sb.append("\n\n");
        return sb.toString();
    }


    private String getDeviceId() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getDeviceId");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }

    private String getLine1Number() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getLine1Number();
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getLine1Number");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }

    private String getSimSerialNumber() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getSimSerialNumber();
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getSimSerialNumber");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getANDROID_ID() {
        StringBuilder sb = new StringBuilder();

        String androidId = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID);

        sb.append(androidId);
        return sb.toString();
    }


    private String getAllCellInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            if (telephonyManager.getAllCellInfo() != null) {
                for (CellInfo cellInfo : telephonyManager.getAllCellInfo()) {
                    sb.append(cellInfo.toString() + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getCellLocation() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getCellLocation() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getCellLocation");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getVoiceMailNumber() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getVoiceMailNumber() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getVoiceMailNumber");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }

    private String getNeighboringCellInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            if (telephonyManager.getNeighboringCellInfo() != null) {
                for (NeighboringCellInfo cellInfo : telephonyManager.getNeighboringCellInfo()) {
                    sb.append(cellInfo.toString() + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getNetworkCountryIso() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getNetworkCountryIso() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getNetworkCountryIso");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getNetworkOperator() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getNetworkOperator() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getNetworkOperator");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getNetworkType() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getNetworkType() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getNetworkType");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getSimCountryIso() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getSimCountryIso() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getSimCountryIso");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getSimOperatorName() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getSimOperatorName() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getSimOperatorName");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getPhoneType() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getPhoneType() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getPhoneType");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String getSubscriberId() {
        StringBuilder sb = new StringBuilder();
        try {
            String result = telephonyManager.getSubscriberId() + "\n";
            if (TextUtils.isEmpty(result)) {
                result = useReflection("getSubscriberId");
            }
            sb.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.toString());
        } catch (Error error) {
            error.printStackTrace();
            sb.append(error.toString());
        }
        return sb.toString();
    }


    private String useReflection(String methodname) {
        StringBuilder sb = new StringBuilder();
        TelephonyManager telephonyManager =
                (TelephonyManager) mActivity.
                        getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class TelephonyManagerClass = telephonyManager.getClass();
            Method method = TelephonyManagerClass.getMethod(methodname);
            String ret = (String) method.invoke(telephonyManager);
            sb.append(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}