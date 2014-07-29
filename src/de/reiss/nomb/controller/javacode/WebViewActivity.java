package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import de.reiss.nomb.R;

import de.reiss.nomb.util.DoMethodInBackgroundAndDisplayResultTask;
import de.reiss.nomb.util.Utils;

import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.Callable;


public class WebViewActivity extends Activity {

    private WebView mWebView;

    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_webview);
        mActivity = this;
        initButtonClickListeners();

        mWebView = (WebView) findViewById(R.id.wv_webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mWebView.loadUrl("http://www.android.com/");

    }


    private void initButtonClickListeners() {


        Button btn_changepage = (Button)
                findViewById(R.id.btn_changepage);
        btn_changepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mWebView.loadUrl("http://developer.android.com");
            }
        });


        Button btn_getwebsettings = (Button)
                findViewById(R.id.btn_getwebsettings);
        btn_getwebsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StringBuilder sb = new StringBuilder();


                WebSettings webSettings = mWebView.getSettings();

                try {
                    sb.append("getDefaultUserAgent:" + "\n");
                    sb.append(webSettings.getDefaultUserAgent(mActivity));
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error error) {
                    error.printStackTrace();
                }

                try {
                    sb.append("getUserAgentString:" + "\n");
                    sb.append(webSettings.getUserAgentString());
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error error) {
                    error.printStackTrace();
                }

                Utils.showResultInDialog(mActivity, sb.toString());

            }
        });


        Button btn_setuseragent = (Button)
                findViewById(R.id.btn_setuseragent);
        btn_setuseragent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                WebSettings webSettings = mWebView.getSettings();

                try {
                    sb.append("setUserAgentString:" + "\n");
                    webSettings.setUserAgentString("new user agent "+ new Random().nextInt(999));
                    sb.append("No Exception when trying to set user agent String");
                } catch (Exception e) {
                    e.printStackTrace();
                    sb.append("Setting did not work" + "\n");
                } catch (Error error) {
                    error.printStackTrace();
                    sb.append("Setting did not work" + "\n");
                }

                Utils.showResultInDialog(mActivity, sb.toString());
            }
        });


        Button btn_useinetaddress = (Button)
                findViewById(R.id.btn_useinetaddress);
        btn_useinetaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return useInetAddress();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });

    }


    private class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }





    private String useInetAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append("Use InetAddress:" + "\n\n");

        String url = "www.google.com";
        String netAddress = null;
        try {
            InetAddress addr = null;
            addr = InetAddress.getByName(url);
            if (addr != null) {
                netAddress = addr.getHostAddress();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (!TextUtils.isEmpty(netAddress)) {
            sb.append("InetAddress.getByName returned for " + url
                    + " the following: " + netAddress + "\n\n");
        } else {
            sb.append("InetAddress.getByName returned nothing for " + url + "\n\n");
        }


        netAddress = null;
        try {
            InetAddress addr = null;
            addr = InetAddress.getByAddress(
                    InetAddress.getByName("www.google.com").getAddress());
            if (addr != null) {
                netAddress = addr.getHostAddress();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (!TextUtils.isEmpty(netAddress)) {
            sb.append("InetAddress.getByAddress returned the following: "
                    + netAddress + "\n\n");
        } else {
            sb.append("InetAddress.getByAddress returned nothing for "
                    + url + "\n\n");
        }

        sb.append("\n\n");
        return sb.toString();
    }

}