package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.*;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.G;
import de.reiss.nomb.R;
import de.reiss.nomb.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class PackageManagerInfoActivity extends Activity {



    Activity mActivity;


    public native String getInstalledApplicationsViaPmCommand();

    static {
        final String library = "de_reiss_nomb_controller_PackageManagerInfoActivity";
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
        setContentView(R.layout.javacode_packagemanager);
        mActivity = this;
        initButtonClickListeners();
    }


    private void initButtonClickListeners() {

        Button btn_getinstalledapps = (Button)
                findViewById(R.id.btn_getinstalledapps);
        btn_getinstalledapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetInstalledApplicationsTask task = new GetInstalledApplicationsTask();
                task.execute((Void) null);
            }
        });
        Button btn_getinstalledpackages = (Button)
                findViewById(R.id.btn_getinstalledpackages);
        btn_getinstalledpackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetInstalledPackagesTask().execute((Void) null);
            }
        });


        Button btn_getrunningapps = (Button)
                findViewById(R.id.btn_getrunningapps);
        btn_getrunningapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRunningApplicationsTask task =
                        new GetRunningApplicationsTask();
                task.execute((Void) null);
            }
        });

        Button btn_getpackagesholdingpermissions = (Button)
                findViewById(R.id.btn_getpackagesholdingpermissions);
        btn_getpackagesholdingpermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> permissions = new ArrayList<String>();
                permissions.add("android.permission.INTERNET");
                permissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
                new GetPackagesHoldingPermissionsTask().execute(permissions);
            }
        });


        Button btn_querybroadcastreceivers = (Button)
                findViewById(R.id.btn_querybroadcastreceivers);
        btn_querybroadcastreceivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QueryBroadcastReceiversTask().execute("android.intent.action.BOOT_COMPLETED");

            }
        });
        Button btn_querycontentproviders = (Button)
                findViewById(R.id.btn_querycontentproviders);
        btn_querycontentproviders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QueryContentProvidersTask().execute((Void) null);
            }
        });



    }



    private class QueryContentProvidersTask extends
            AsyncTask<Void, Void, String> {
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
                sb.append("List of Content Providers:" + "\n\n\n");

                List<ProviderInfo> providers = getPackageManager()
                        .queryContentProviders(null, 0, 0);
                if (providers == null || providers.size() < 1) {
                    sb.append(G.NOTHING_FOUND);
                } else {
                    for (ProviderInfo info : providers) {
                        sb.append(info.packageName + ": " + info.authority + "\n\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, e.getMessage());
                return "Something went wrong";
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


    private class QueryBroadcastReceiversTask extends
            AsyncTask<String, Void, String> {
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
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                String intentaction = params[0];

                PackageManager packageManager = getPackageManager();
                final Intent intent = new Intent(intentaction);

                List<String> startupApps = new ArrayList<String>();
                final List<ResolveInfo> activities =
                        packageManager.queryBroadcastReceivers(intent, 0);
                if (activities == null || activities.size() < 1) {
                    sb.append(G.NOTHING_FOUND);
                } else {
                    for (ResolveInfo resolveInfo : activities) {
                        ActivityInfo activityInfo = resolveInfo.activityInfo;
                        if (activityInfo != null)
                            startupApps.add(activityInfo.name);
                    }
                }

                sb.append("Startup apps:" + "\n\n\n");
                if (startupApps == null || startupApps.size() < 1) {
                    sb.append(G.NOTHING_FOUND);
                } else {
                    for (String startupApp : startupApps) {
                        sb.append(startupApp + " \n\n\n");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, e.getMessage());
                return "Something went wrong";
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

    private class GetPackagesHoldingPermissionsTask extends
            AsyncTask<ArrayList<String>, Void, String> {
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
        protected String doInBackground(ArrayList<String>... arrayLists) {
            StringBuilder sb = new StringBuilder();


            try {


                sb.append("List of Packages holding some " +
                        "sample permission set:" + "\n\n\n");


                ArrayList<String> permissionList = arrayLists[0];
                if (permissionList != null) {
                    sb.append("Permission set:" + " \n\n");

                    for (String permission : permissionList) {
                        sb.append(permission + " \n");
                    }

                    sb.append("\n\n\n\n");
                    sb.append("Packages holding that set:" + " \n\n");

                    String[] permissionArray = permissionList.toArray(
                            new String[permissionList.size()]);


                    int currentapiVersion = Build.VERSION.SDK_INT;
                    if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        PackageManager packageManager = getPackageManager();
                        ArrayList<PackageInfo> packageInfoList = (ArrayList<PackageInfo>)
                                packageManager.getPackagesHoldingPermissions(
                                        permissionArray, 0);

                        if (packageInfoList == null || packageInfoList.size() < 1) {
                            sb.append(G.NOTHING_FOUND);
                        } else {
                            if (packageInfoList != null && packageInfoList.size() > 0) {
                                for (PackageInfo info : packageInfoList) {
                                    sb.append(info.packageName + " \n\n");
                                }
                            }
                        }
                    } else {
                        sb.append("nothing found . Cannot call PackageManager.getPackagesHoldingPermissions" +
                                "on Android version <" + Build.VERSION_CODES.JELLY_BEAN_MR2
                                + ".\nThus doing nothing here.");
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, e.getMessage());
                sb.append("Something went wrong");
                sb.append(e.getLocalizedMessage());
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

    private class GetInstalledApplicationsTask extends AsyncTask<Void, Void, String> {
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
            sb.append("List of applications:" + "\n\n\n");
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> installedApplications =
                    packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            if (installedApplications == null || installedApplications.size() < 1) {
                sb.append(G.NOTHING_FOUND);
            } else {
                for (ApplicationInfo appInfo : installedApplications) {
                    sb.append("\"" + appInfo.loadLabel(packageManager) + "\""
                            + "\n" + appInfo.packageName + "\n\n");
                }
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


    private class GetInstalledPackagesTask extends AsyncTask<Void, Void, String> {
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
            sb.append("List of packages:" + "\n\n\n");
            final PackageManager packageManager = getPackageManager();
            List<PackageInfo> installedPackages =
                    packageManager.getInstalledPackages(0);
            if (installedPackages == null || installedPackages.size() < 1) {
                sb.append(G.NOTHING_FOUND);
            } else {
                for (PackageInfo packageInfo : installedPackages) {
                    sb.append(packageInfo.packageName + "\n");
                }
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


    private class GetRunningApplicationsTask extends AsyncTask<Void, Void, String> {
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
            sb.append("List of applications:" + "\n\n\n");
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo>
                    runningProcesses = manager.getRunningAppProcesses();
            if (runningProcesses == null || runningProcesses.size() < 1) {
                sb.append(G.NOTHING_FOUND);
            } else {
                for (ActivityManager.RunningAppProcessInfo process : runningProcesses) {
                    sb.append(process.processName + "\n\n");
                }
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


    private String readFromFileUsingJava(String pathAndFileName) {
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