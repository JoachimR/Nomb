package de.reiss.nomb.controller.nativecode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import de.reiss.nomb.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class RunCommandActivity extends Activity {


    public native String fetchdiskinfo();

    public native String fetchnetstatinfo();

    public native String fetchprocessinfo();

    public native String getsystemproperty();


    static {
        final String library = "de_reiss_nomb_controller_nativecode_RunCommandActivity";
        try {
            System.loadLibrary(library);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_runcommand);
        mActivity = this;
        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        initButton(R.id.btn_fetchdiskinfo, "fetchdiskinfo", new Callable<String>() {
            public String call() {
                return fetchdiskinfo();
            }
        });

        initButton(R.id.btn_fetchnetstatinfo, "fetchnetstatinfo", new Callable<String>() {
            public String call() {
                return fetchnetstatinfo();
            }
        });

        initButton(R.id.btn_fetchprocessinfo, "fetchprocessinfo", new Callable<String>() {
            public String call() {
                return fetchprocessinfo();
            }
        });

        initButton(R.id.btn_fetchsystemproperties, "getsystemproperty", new Callable<String>() {
            public String call() {
                return getsystemproperty();
            }
        });

    }

    private void initButton(final int viewid, final String methodName, final Callable<String> method) {
        Button b = (Button) findViewById(viewid);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoMethodInBackgroundTask().execute(methodName, method);
            }
        });
    }


    private class DoMethodInBackgroundTask extends
            AsyncTask<Object, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            dialog = new ProgressDialog(mActivity);
            dialog.setMessage(getString(R.string.dlg_loading));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {


            StringBuilder sb = new StringBuilder();
            try {
                String methodName = (String) params[0];
                Callable<String> method = (Callable<String>) params[1];

                Object result = method.call();
                String res = "" + result;

                final String outputfile = "/data/data/de.reiss.nomb"
                        + "/" + methodName + ".out.txt";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                String currentDateAndTime = sdf.format(new Date());

                sb.append("===== " + currentDateAndTime + " =====\n\n\n");

                if (res.equals("worked")) {
                    sb.append("Executing command seemed to work. " +
                            "Opening file where result was piped in...");
                    sb.append("\n\n");
                    sb.append(readFromFile(outputfile));

                } else {
                    sb.append(G.NOTHING_FOUND + "\n\n");
                    sb.append("Executing command did not work.");
                }

                // remove outputfile so that old result is not shown any longer in new test
                removeFile(outputfile);

            } catch (Exception e) {
                e.printStackTrace();
                sb.append(e.getLocalizedMessage());
            } catch (Throwable t) {
                t.printStackTrace();
                sb.append(t.getLocalizedMessage());
            }
            return sb.toString();


        }


        @Override
        protected void onPostExecute(String s) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            Utils.showResultInDialog(mActivity, s);
            super.onPostExecute(s);
        }
    }

    private boolean removeFile(String pathAndFileName) {
        File file = new File(pathAndFileName);
        return file.delete();
    }


    private String readFromFile(String pathAndFileName) {
        StringBuffer sb = new StringBuffer();

        final String success = String.format("Reading file %s worked!", pathAndFileName);
        final String fail = String.format("Reading file %s did not work!", pathAndFileName);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathAndFileName));
            String line;


            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            if(TextUtils.isEmpty(text)) {
                text.append(G.NOTHING_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(G.NOTHING_FOUND);
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }

        sb.append(success);
        sb.append("\nContent of file:\n\n\n\n" + text);
        return sb.toString();

    }


}