package de.reiss.nomb.controller.nativecode;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.reiss.nomb.R;

import de.reiss.nomb.util.DoMethodInBackgroundAndDisplayResultTask;

import java.util.concurrent.Callable;


public class NetworkActivity extends Activity {


    Activity mActivity;


    public native String connecttoserver(String serverip, String port);


    static {
        final String library = "de_reiss_nomb_controller_nativecode_NetworkActivity";
        try {
            System.loadLibrary(library);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativecode_network);
        mActivity = this;
        initButtons();

    }

    private void initButtons() {

        Button btn_nativeconnect = (Button)
                findViewById(R.id.btn_socketconnectnative);
        btn_nativeconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return methodForSocketConnectNative();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);

            }
        });

    }

    private String methodForSocketConnectNative() {
        final String addr = getCurrentUsedIpAddress();
        final String port = getCurrentUsedPort();
        if (TextUtils.isEmpty(addr) || TextUtils.isEmpty(port)) {
            return "invalid server address: '" + addr + ":" + port + "'";
        }

        String result = "";
        try {
            result += connecttoserver(addr, port);
        } catch (Exception e) {
            e.printStackTrace();
            result += e.getLocalizedMessage();
        } catch (Throwable t) {
            t.printStackTrace();
            result += t.getLocalizedMessage();
        }
        return result;
    }


    private String getCurrentUsedIpAddress() {
        if (mActivity != null) {
            EditText e = (EditText) mActivity.findViewById(R.id.ed_serveripaddress);
            if (e != null) {
                return e.getText().toString();
            }
        }
        return "";
    }

    private String getCurrentUsedPort() {
        if (mActivity != null) {
            EditText e = (EditText) mActivity.findViewById(R.id.ed_serverport);
            if (e != null) {
                return e.getText().toString();
            }
        }
        return "";
    }


}