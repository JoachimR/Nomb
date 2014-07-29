package de.reiss.nomb.controller.nativecode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import de.reiss.nomb.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;


public class PackageManagerInfoActivity extends Activity {


    Activity mActivity;


    public native String getInstalledApplicationsViaPmCommand();

    static {
        final String library = "de_reiss_nomb_controller_nativecode_PackageManagerInfoActivity";
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
        setContentView(R.layout.nativecode_packagemanager);
        mActivity = this;
        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        Button btn_getinstalledpackages_ndk = (Button)
                findViewById(R.id.btn_getinstalledpackages_ndk);
        btn_getinstalledpackages_ndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetInstalledPackagesViaNdkTask().execute((Void) null);
            }
        });



    }


    private class GetInstalledPackagesViaNdkTask extends AsyncTask<Void, Void, String> {
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
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            try {

                sb.append("Trying to get list of packages via NDK " +
                        "using the command 'system(pm list packages)'..." + "\n\n\n");


                String nativeResult = getInstalledApplicationsViaPmCommand();
                if (nativeResult.equals("worked")) {
                    sb.append("Executing command seemed to work. " +
                            "Opening file where result was piped in...");
                    sb.append("\n\n");
                    sb.append(readFromFile(
                            Environment.getExternalStorageDirectory()
                                    + "/" + "packageslist.txt"));

                } else {
                    sb.append("Executing command did not work.");
                }

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
        protected void onPostExecute(final String s) {
            if (mActivity != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        Utils.showResultInDialog(mActivity, s);
                    }
                });
            }

            super.onPostExecute(s);
        }
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }

        sb.append(success);
        sb.append("\nContent of file:\n" + text);
        return sb.toString();

    }


}