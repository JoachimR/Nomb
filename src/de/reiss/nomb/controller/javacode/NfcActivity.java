package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.R;

import de.reiss.nomb.util.Utils;


public class NfcActivity extends Activity {


    Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_nfc);
        mActivity = this;
        initButtonClickListeners();
    }

    private void initButtonClickListeners() {


        Button btn_getnfcadapter = (Button)
                findViewById(R.id.btn_getnfcadapter);
        btn_getnfcadapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showResultInDialog(mActivity, getNfcAdapter());
            }
        });


    }

    private String getNfcAdapter() {
        StringBuilder sb = new StringBuilder();
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        if (manager == null) {
            sb.append("NfcManager is null!");
        } else {
            NfcAdapter adapter = manager.getDefaultAdapter();
            if (adapter != null && adapter.isEnabled()) {
                sb.append("Found NFC Adapter");
            } else {
                sb.append("Could not find/enable NFC Adapter");
            }
        }
        return sb.toString();
    }


}