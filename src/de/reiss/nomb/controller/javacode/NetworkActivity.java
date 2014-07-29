package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.reiss.nomb.R;
import de.reiss.nomb.util.DoMethodInBackgroundAndDisplayResultTask;
import de.reiss.nomb.util.Utils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;


public class NetworkActivity extends Activity {


    Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_network);
        mActivity = this;
        initButtons();

    }


    private void initButtons() {

        final Button btn_socketconnect = (Button)
                findViewById(R.id.btn_socketconnect);
        btn_socketconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Callable callable = new Callable<String>() {
                    public String call() {
                        return methodForSocketConnect();
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });


        Button btn_getstate = (Button)
                findViewById(R.id.btn_getstate);
        btn_getstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) mActivity.
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                String msg = "";
                if (activeNetwork != null) {
                    final NetworkInfo.State s = activeNetwork.getState();
                    if (s != null) {
                        msg = "Network state: " + s.name();
                    } else {
                        msg = "Network state is null";
                    }

                } else {
                    msg = "NetworkInfo is null";
                }

                Utils.showResultInDialog(mActivity, msg);
            }
        });

        Button btn_isconnected = (Button)
                findViewById(R.id.btn_isconnected);
        btn_isconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) mActivity.
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                String msg;
                if (isConnected) {
                    msg = "Connected to network";
                } else {
                    msg = "Not Connected to network";
                }
                Utils.showResultInDialog(mActivity, msg);
            }
        });


        Button btn_getdetailedstate = (Button)
                findViewById(R.id.btn_getdetailedstate);
        btn_getdetailedstate.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) mActivity.
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                if (connectivityManager == null) {
                    String msg = "ConnectivityManager is null";
                    Utils.showResultInDialog(mActivity, msg);
                    return;
                }

                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if (activeNetwork == null) {
                    String msg = "NetworkInfo is null";
                    Utils.showResultInDialog(mActivity, msg);
                    return;
                }
                String msg = "Network state: " + activeNetwork.getDetailedState().name();
                Utils.showResultInDialog(mActivity, msg);
            }
        }

        );

        Button btn_getextrainfo = (Button)
                findViewById(R.id.btn_getextrainfo);
        btn_getextrainfo.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) mActivity.
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                if (connectivityManager == null) {
                    String msg = "ConnectivityManager is null";
                    Utils.showResultInDialog(mActivity, msg);
                    return;
                }

                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if (activeNetwork == null) {
                    String msg = "NetworkInfo is null";
                    Utils.showResultInDialog(mActivity, msg);
                    return;
                }


                String msg = "Network Extra Info: " + activeNetwork.getExtraInfo();
                Utils.showResultInDialog(mActivity, msg);
            }
        }

        );

        Button btn_iteratenetworkinterfaces = (Button)
                findViewById(R.id.btn_iteratenetworkinterfaces);
        btn_iteratenetworkinterfaces.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                Callable callable = new Callable<String>() {
                    public String call() {
                        return readAllNetworkInterfaces();
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);


            }
        }

        );

        Button btn_getwifiinfo = (Button)
                findViewById(R.id.btn_getwifiinfo);
        btn_getwifiinfo.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Utils.showResultInDialog(mActivity, readAllWifiInfos());
            }
        }

        );


    }


    private String readAllWifiInfos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reading all wifi infos:\n\n\n");

        // getConfiguredNetworks, getConnectionInfo, getDhcpInfo,
        // getScanResults, getWifiApConfiguration

        WifiManager wifiManager = (WifiManager) mActivity.
                getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            sb.append("WifiManager is null! Wifi enabled?");
            return sb.toString();
        }

        sb.append("getConfiguredNetworks:\n\n");
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        if (list != null) {
            for (WifiConfiguration wifiConfiguration : list) {
                sb.append(wifiConfiguration.toString() + "\n");
            }
        }
        sb.append("\n\n");

        sb.append("getConnectionInfo:\n\n");
        sb.append(wifiManager.getConnectionInfo());
        sb.append("\n\n");


        sb.append("getDhcpInfo:\n\n");
        sb.append(wifiManager.getDhcpInfo());
        sb.append("\n\n");

        sb.append("getScanResults:\n\n");
        List<ScanResult> scanResults = wifiManager.getScanResults();
        if (list != null) {
            for (ScanResult scanResult : scanResults) {
                sb.append(scanResult.toString() + "\n");
            }
        }
        sb.append("\n\n");

        sb.append("getWifiApConfiguration:\n\n");
        //  use reflection because method is tagged with @hide
        try {
            Class WifiManagerClass = wifiManager.getClass();
            Method method = WifiManagerClass.getMethod("getWifiApConfiguration");
            WifiConfiguration ret = (WifiConfiguration) method.invoke(wifiManager);
            sb.append(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("\n\n");

        return sb.toString();
    }

    private String readAllNetworkInterfaces() {
        StringBuilder sb = new StringBuilder();
        sb.append("Iterating over all network interfaces:\n\n\n");
        try {
            if (NetworkInterface.getNetworkInterfaces() == null || !NetworkInterface.getNetworkInterfaces().hasMoreElements()) {
                sb.append("getNetworkInterfaces() null or empty");
                return sb.toString();
            }
            for (Enumeration<NetworkInterface> en =
                         NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                sb.append("-------------------\n");
                NetworkInterface networkInterface = en.nextElement();

                String interfaceInfos = "";
                interfaceInfos += networkInterface.getName() + "\n";
                interfaceInfos += "HW Address: \n";
                byte[] hwadr = networkInterface.getHardwareAddress();
                if (hwadr != null) {
                    try {
                        String str = new String(hwadr, "UTF-8");
                        interfaceInfos += str + "\n";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                interfaceInfos += "\n\n";

                interfaceInfos += "Inet Addresses: \n";
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                if (inetAddresses != null) {
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        interfaceInfos += inetAddress.getHostAddress();
                        interfaceInfos += "\n";
                    }
                }
                interfaceInfos += "\n\n";

                sb.append("Interface: " + interfaceInfos);
                sb.append("\n");
                for (Enumeration<InetAddress> enumIPAddr =
                             networkInterface.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress iNetAddress = enumIPAddr.nextElement();
                    if (!iNetAddress.isLoopbackAddress()) {
                        String sLocalIP = iNetAddress.getHostAddress();
                        sb.append("IP: " + sLocalIP);
                        sb.append("\n");
                    }
                }
                sb.append("-------------------\n");
                sb.append("\n");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    private String methodForSocketConnect() {
//        Thread t = new Thread(new SendHelloToServerThread());
//        t.start();


        final String sendText = "hello from java";

        final String addr = getCurrentUsedIpAddress();
        final String port = getCurrentUsedPort();
        if (TextUtils.isEmpty(addr) || TextUtils.isEmpty(port)) {
            return "invalid server address: '" + addr + ":" + port + "'";
        }

        boolean worked = true;
        String received = "";
        try {
            InetAddress serverAddr = InetAddress.getByName(addr);
            Socket socket = new Socket(serverAddr, Integer.parseInt(port));
            if (socket.isConnected()) {

                PrintWriter out = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())), true);
                out.println(sendText);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                received = in.readLine();

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            worked = false;
        }

        String msg;
        if (worked) {
            final String result = received;


            msg = "Connecting to server " +
                    "and sending " +
                    sendText + " worked!\n" +
                    "Received:\n" +
                    "'" + result + "'";


        } else {
            msg = "Connecting to server " +
                    "and sending Hello did not work!";
        }
        return msg;
    }

    /**
     * Asynchronous execution of network stuff is necessary
     * http://stackoverflow.com/a/18165890/883083
     */
    public class SendHelloToServerThread implements Runnable {

        final String sendText = "hello from java";

        public void run() {
            final String addr = getCurrentUsedIpAddress();
            final String port = getCurrentUsedPort();
            if (TextUtils.isEmpty(addr) || TextUtils.isEmpty(port)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showResultInDialog(mActivity, "invalid server address: '" + addr + ":" + port + "'");
                    }
                });
                return;
            }

            boolean worked = true;
            String received = "";
            try {
                InetAddress serverAddr = InetAddress.getByName(addr);
                Socket socket = new Socket(serverAddr, Integer.parseInt(port));
                if (socket.isConnected()) {

                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    socket.getOutputStream())), true);
                    out.println(sendText);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    received = in.readLine();

                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                worked = false;
            }

            if (worked) {
                final String result = received;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showResultInDialog(mActivity, "Connecting to server " +
                                "and sending " +
                                sendText + " worked!\n" +
                                "Received:\n" +
                                "'" + result + "'");
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showResultInDialog(mActivity, "Connecting to server " +
                                "and sending Hello did not work!");
                    }
                });

            }
        }
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