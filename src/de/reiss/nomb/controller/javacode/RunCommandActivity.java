package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.G;
import de.reiss.nomb.R;
import de.reiss.nomb.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class RunCommandActivity extends Activity {

    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_runcommand);
        mActivity = this;
        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        initButton(R.id.btn_fetchdiskinfo, new Callable<String>() {
            public String call() {
                return fetchDiskInfo();
            }
        });

        initButton(R.id.btn_fetchnetstatinfo, new Callable<String>() {
            public String call() {
                return fetchNetStatInfo();
            }
        });

        initButton(R.id.btn_fetchprocessinfo, new Callable<String>() {
            public String call() {
                return fetchProcessInfo();
            }
        });

        initButton(R.id.btn_fetchsystemproperties, new Callable<String>() {
            public String call() {
                return getSystemProperty();
            }
        });

    }

    private void initButton(final int viewid, final Callable<String> stringmethod) {
        Button b = (Button)
                findViewById(viewid);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoMethodInBackgroundTask().execute(stringmethod);
            }
        });
    }


    private class DoMethodInBackgroundTask extends
            AsyncTask<Callable<String>, Void, String> {

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
        protected String doInBackground(Callable<String>... params) {

            Callable<String> method = params[0];
            try {
                return method.call();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return G.NOTHING_FOUND + " \n\n Something went wrong";
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



    /*
     * http://jatinkotadiya.blogspot.de/2012/11/android-example.html
     */


    public String fetchDiskInfo() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {"/system/bin/df"};
            result = cmdexe.run(args, "/system/bin/");
            if (TextUtils.isEmpty(result.trim())) {
                result = G.NOTHING_FOUND;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public String fetchNetStatInfo() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {"/system/bin/netstat"};
            result = cmdexe.run(args, "/system/bin/");
            if (TextUtils.isEmpty(result.trim())) {
                result = G.NOTHING_FOUND;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public String fetchProcessInfo() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {"/system/bin/top", "-n", "1"};
            result = cmdexe.run(args, "/system/bin/");
            if (TextUtils.isEmpty(result.trim())) {
                result = G.NOTHING_FOUND;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }


    public String getSystemProperty() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(initProperty("java.vendor.url", "java.vendor.url"));
        buffer.append(initProperty("java.class.path", "java.class.path"));
        buffer.append(initProperty("user.home", "user.home"));
        buffer.append(initProperty("java.class.version", "java.class.version"));
        buffer.append(initProperty("os.version", "os.version"));
        buffer.append(initProperty("java.vendor", "java.vendor"));
        buffer.append(initProperty("user.dir", "user.dir"));
        buffer.append(initProperty("user.timezone", "user.timezone"));
        buffer.append(initProperty("path.separator", "path.separator"));
        buffer.append(initProperty(" os.name", " os.name"));
        buffer.append(initProperty("os.arch", "os.arch"));
        buffer.append(initProperty("line.separator", "line.separator"));
        buffer.append(initProperty("file.separator", "file.separator"));
        buffer.append(initProperty("user.name", "user.name"));
        buffer.append(initProperty("java.version", "java.version"));
        buffer.append(initProperty("java.home", "java.home"));
        buffer.append(initProperty("ro.serialno", "ro.serialno"));

        if (TextUtils.isEmpty(buffer.toString().trim())) {
            buffer.append(G.NOTHING_FOUND);
        }

        return buffer.toString();
    }

    private String initProperty(String description, String propertyStr) {
        String res = "";
        String prop = System.getProperty(propertyStr);
        if (!TextUtils.isEmpty(prop.trim())) {
            res = description + ":" + prop + "\n";
        }
        return res;
    }

    private class CMDExecute {

        public synchronized String run(String[] cmd, String workdirectory)
                throws IOException {
            String result = "";

            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                // set working directory
                if (workdirectory != null) {
                    builder.directory(new File(workdirectory));
                }
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
                    System.out.println(new String(re));
                    result = result + new String(re);
                }
                in.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

    }


}