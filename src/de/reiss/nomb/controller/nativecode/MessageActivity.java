package de.reiss.nomb.controller.nativecode;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.reiss.nomb.R;



public class MessageActivity extends Activity {

    Activity mActivity;


    private EditText mNumberToSend;
    private EditText mTextToSend;
    private String numberToSend;
    private String textToSend;


    public native String sendSmsViaAmCommand(String phonenumber, String messagebody);

    static {
        final String library = "de_reiss_nomb_controller_nativecode_MessageActivity";
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
        setContentView(R.layout.nativecode_message);
        mActivity = this;
        mNumberToSend = (EditText)
                findViewById(R.id.ed_numbertosend);
        mTextToSend = (EditText)
                findViewById(R.id.ed_texttosend);

        //debug sample
        mNumberToSend.setText("01511234567");
        mTextToSend.setText("some text to send");

        initButtonClickListeners(this);
    }


    private void initButtonClickListeners(Activity activity) {
        final Activity actv = activity;



        Button btn_amcommand = (Button)
                findViewById(R.id.btn_amcommand);
        btn_amcommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refreshPhoneAndMessage()) {
                    sendSmsViaAmCommand(numberToSend, textToSend);
                }
            }
        });



    }


    private boolean refreshPhoneAndMessage() {
        if (mNumberToSend == null || mTextToSend == null) {
            return false;
        }
        final String number = mNumberToSend.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(mActivity, mActivity.getString(R.string.enternumber),
                    Toast.LENGTH_SHORT);
            return false;
        }
        final String text = mTextToSend.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(mActivity, mActivity.getString(R.string.entertext),
                    Toast.LENGTH_SHORT);
            return false;
        }
        numberToSend = number;
        textToSend = text;
        return true;
    }




}