package de.reiss.nomb.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import java.util.concurrent.Callable;

public class DoMethodInBackgroundAndDisplayResultTask extends AsyncTask<Callable<String>, Void, String> {

    ProgressDialog dialog;
    Activity mActivity;

    public DoMethodInBackgroundAndDisplayResultTask(Activity activity) {
        mActivity = activity;
    }


    @Override
    protected void onPreExecute() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(mActivity.getString(R.string.dlg_loading));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(Callable<String>... params) {

        Callable<String> method = params[0];
        try {
            return method.call();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, e.getMessage());
        }
        return "Something went wrong";
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