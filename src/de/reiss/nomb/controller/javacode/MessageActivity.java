package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.reiss.nomb.G;
import de.reiss.nomb.R;
import de.reiss.nomb.util.DoMethodInBackgroundAndDisplayResultTask;
import de.reiss.nomb.util.Utils;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class MessageActivity extends Activity {

    Activity mActivity;

    private static final String SMS_SENT = "nomb SMS_SENT";
    private static final int DATA_SMS_SENT = 354654;
    private static final String SMS_DELIVERED = "nomb SMS_DELIVERED";

    private EditText ed_numbertosend;
    private EditText ed_mailtosend;
    private EditText ed_texttosend;
    private String numberToSend;
    private String mailToSend;
    private String textToSend;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_message);
        mActivity = this;
        ed_numbertosend = (EditText)
                findViewById(R.id.ed_numbertosend);
        ed_mailtosend = (EditText)
                findViewById(R.id.ed_mailtosend);
        ed_texttosend = (EditText)
                findViewById(R.id.ed_texttosend);

        //debug sample
        ed_numbertosend.setText("4915150734033");
        ed_mailtosend.setText("some@one.com");
        ed_texttosend.setText("some text to send");

        initButtonClickListeners(this);
        registerReceiver(smsSendResultReceiver, new IntentFilter(SMS_SENT));
    }


    private void initButtonClickListeners(Activity activity) {
        final Activity actv = activity;

        Button btn_sendsms = (Button)
                findViewById(R.id.btn_sendsms);
        btn_sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refreshAndVerifiyTarget(SEND_MODE_SMS)) {
                    sendSmsViaJava(numberToSend, textToSend);
                }
            }
        });


        Button btn_senddatasms = (Button)
                findViewById(R.id.btn_senddatasms);
        btn_senddatasms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refreshAndVerifiyTarget(SEND_MODE_DATASMS)) {
                    sendDataSms(numberToSend, textToSend);
                }
            }
        });


        Button btn_getmessagesfromicc = (Button)
                findViewById(R.id.btn_getmessagesfromicc);
        btn_getmessagesfromicc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showResultInDialog(mActivity, getSimCardMessages());
            }
        });


        Button btn_sendemail = (Button)
                findViewById(R.id.btn_sendemail);
        btn_sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (refreshAndVerifiyTarget(SEND_MODE_EMAIL)) {

                    // ACTION_SENDTO filters for email apps (discard bluetooth and others)
                    String uriText =
                            "mailto:" + mailToSend +
                                    "?subject=" + URLEncoder.encode(textToSend.substring(0, 1)) +
                                    "&body=" + URLEncoder.encode(textToSend);

                    Uri uri = Uri.parse(uriText);

                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                    sendIntent.setData(uri);
                    startActivity(Intent.createChooser(sendIntent, "Send email"));

                }


            }
        });


        Button btn_readaddressbookinfo = (Button)
                findViewById(R.id.btn_readaddressbookinfo);
        btn_readaddressbookinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Callable callable = new Callable<String>() {
                    public String call() {
                        return readAddressBookInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });


        Button btn_contentproviderinfo = (Button)
                findViewById(R.id.btn_contentproviderinfo);
        btn_contentproviderinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return readContentProvider();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });


        Button btn_readsmsinfo = (Button) findViewById(R.id.btn_readsmsinfo);
        btn_readsmsinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return readSmsInfo();
                    }
                };
                new DoMethodInBackgroundAndDisplayResultTask(mActivity).execute(callable);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String toast = null;
        switch (requestCode) {
            case DATA_SMS_SENT:
                switch (resultCode) {
                    case RESULT_OK:
                        toast = "Data SMS sent!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        toast = "Generic Failure";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        toast = "Radio Off";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        toast = "Null Pdu";
                        break;
                }
                break;
        }
        if (toast != null) {
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        }
    }

    public String getSimCardMessages() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMS messages: \n\n\n\n");

        ArrayList<SmsMessage> list = new ArrayList<SmsMessage>();


        // need to use reflection because
        // SmsManager.getAllMessagesFromIcc
        // is tagged with @hide in AOSP
        try {
            Class<?> smsMgrClass = SmsManager.getDefault().getClass();
            Method getMessages = smsMgrClass.getMethod("getAllMessagesFromIcc");

            // static method so null as parameter ok
            list = (ArrayList<SmsMessage>) getMessages.invoke(null);

            for (SmsMessage message : list) {
                sb.append(message.getDisplayMessageBody()
                        + "\nfrom "
                        + message.getDisplayOriginatingAddress()
                        + "\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static final int SEND_MODE_SMS = 123;
    private static final int SEND_MODE_DATASMS = 456;
    private static final int SEND_MODE_EMAIL = 789;

    private boolean refreshAndVerifiyTarget(int mode) {
        String target;
        switch (mode) {
            case SEND_MODE_SMS:
                if (!refreshAndVerifiyText()) {
                    return false;
                }
                if (ed_numbertosend == null) {
                    return false;
                }
                target = ed_numbertosend.getText().toString();
                if (TextUtils.isEmpty(target)) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.enternumber),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!TextUtils.isDigitsOnly(target)) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.numbersonly),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                numberToSend = target;
                return true;

            case SEND_MODE_DATASMS:
                if (!refreshAndVerifiyText()) {
                    return false;
                }
                if (ed_numbertosend == null) {
                    return false;
                }
                target = ed_numbertosend.getText().toString();
                if (TextUtils.isEmpty(target)) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.enternumber),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!TextUtils.isDigitsOnly(target)) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.numbersonly),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                numberToSend = target;
                return true;

            case SEND_MODE_EMAIL:
                if (!refreshAndVerifiyText()) {
                    return false;
                }
                if (ed_mailtosend == null) {
                    return false;
                }
                target = ed_mailtosend.getText().toString();
                if (TextUtils.isEmpty(target) || !Utils.isTextEmailAddress(target)) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.enteremailaddress),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }


                mailToSend = target;
                return true;

            default:

                break;
        }


        return false;


    }

    private boolean refreshAndVerifiyText() {

        if (ed_texttosend == null) {
            return false;
        }

        final String text = ed_texttosend.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(mActivity, mActivity.getString(R.string.entertext),
                    Toast.LENGTH_SHORT);
            return false;
        }
        textToSend = text;
        return true;
    }


    public void sendSmsViaJava(String phoneNumber, String message) {

        SmsManager manager = SmsManager.getDefault();

        PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        ArrayList<String> messageArrayList = manager.divideMessage(message);
        Log.d(G.TAG, "Amount of messages to be sent: " + messageArrayList.size());
        int counter = 0;
        for (String partOfMsg : messageArrayList) {
            counter++;
            Log.d(G.TAG, "Sending SMS #" + counter);
            manager.sendTextMessage(phoneNumber, null, partOfMsg, piSend, piDelivered);
        }
    }

    private void sendDataSms(String phoneNumber, String message) {

        short SMS_PORT = 8901;
        SmsManager smsManager = SmsManager.getDefault();
        if (smsManager == null) {
            Utils.showResultInDialog(mActivity,G.NOTHING_FOUND + "\n\n smsManager == null");
            return;
        }
        PendingIntent sent = mActivity.createPendingResult
                (DATA_SMS_SENT, new Intent(),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        smsManager.sendDataMessage(phoneNumber, null,
                SMS_PORT, message.getBytes(), sent, null);
    }


    private BroadcastReceiver smsSendResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(G.TAG, "Received SMS");
                String result = "Received SMS, result: ";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        result += "Message Received!";
                        break;
                    default:
                        result += "Error when receiving Message";
                        break;
                }
                Log.d(G.TAG, result);
                Utils.showResultInDialog(mActivity, result);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, e.getMessage());
                Utils.showResultInDialog(mActivity, "Error when receiving Message: " + e.getMessage());
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(smsSendResultReceiver);
        super.onDestroy();
    }


    private String readAddressBookInfo() {

        StringBuilder res = new StringBuilder();

        ContentResolver cr = mActivity.getContentResolver();

        StringBuilder sb = new StringBuilder();
        if (cr != null) {

            final int maxRows = 10;

            sb.append("phone" + "\n\n");
            Cursor phone = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                    null, null);
            sb.append(getCursorContent(phone, maxRows));

            sb.append("email" + "\n\n");
            Cursor email = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null,
                    null, null);
            sb.append(getCursorContent(email, maxRows));

            sb.append("data" + "\n\n");
            Cursor data = cr.query(
                    ContactsContract.Data.CONTENT_URI, null, null, null, null);
            sb.append(getCursorContent(data, maxRows));
        }


        return sb.toString();
    }

    private static String getCursorContent(Cursor c, int maxRows) {
        if (c == null) {
            return G.NOTHING_FOUND;
        }
        String allInfo = "";
        while (c.moveToNext() && c.getPosition() < maxRows) {
            try {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    String s = c.getString(i);
                    if (s != null) {
                        allInfo += "[" + "Index:" + i
                                + " | " + "Info: " + s
                                + "]\n";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(G.TAG, e.getMessage());
            }
        }
        c.close();
        if (TextUtils.isEmpty(allInfo)) {
            return G.NOTHING_FOUND;
        }
        return allInfo;
    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";


        return managedQuery(uri, projection, selection, selectionArgs,
                sortOrder);
    }

    private String readContentProvider() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reading Contact Infos via Content Provider:" + "\n\n");

        sb.append("\n");

        sb.append("Contacts: ");
        sb.append("\n");
        sb.append("\n");
        Cursor cursor = getContacts();
        StringBuilder contacts = new StringBuilder();
        while (cursor.moveToNext()) {
            String displayName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            contacts.append("Name: ");
            contacts.append(displayName);
            contacts.append("\n");


            contacts.append("numbers: ");
            contacts.append("\n");
            contacts.append("\n");
            ContentResolver cr = getContentResolver();
            Cursor cursorForName = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    "DISPLAY_NAME = '" + displayName + "'", null, null);
            if (cursorForName.moveToFirst()) {

                String contactId = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phones = cr.query(ContactsContract.CommonDataKinds.
                        Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                while (phones.moveToNext()) {
                    String number = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.append(number);
                    contacts.append("\n");
                }
            }
            contacts.append("\n\n");
        }
        if (contacts.length() == 0) {
            contacts.append(G.NOTHING_FOUND);
        }

        sb.append(contacts);
        sb.append("\n\n");
        return sb.toString();
    }


    private ArrayList<Sms> getAllSms() {
        ArrayList<Sms> allSms = new ArrayList<Sms>();
        Sms objSms;
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                allSms.add(objSms);
                c.moveToNext();
            }
        }
        c.close();
        return allSms;
    }


    private String readSmsInfo() {
        StringBuilder sb = new StringBuilder();

        ArrayList<Sms> allSms = getAllSms();
        for (Sms oneSms : allSms) {
            sb.append("SMS:" + "\n\n");
            sb.append(oneSms.toString());
        }
        String res = sb.toString();
        if (TextUtils.isEmpty(res.trim())) {
            res = G.NOTHING_FOUND;
        }

        return res;
    }


    private class Sms {
        private String _id;
        private String _address;
        private String _msg;
        private String _readState; //"0" for have not read sms and "1" for have read sms
        private String _time;
        private String _folderName;

        public String getId() {
            return _id;
        }

        public String getAddress() {
            return _address;
        }

        public String getMsg() {
            return _msg;
        }

        public String getReadState() {
            return _readState;
        }

        public String getTime() {
            return _time;
        }

        public String getFolderName() {
            return _folderName;
        }


        public void setId(String id) {
            _id = id;
        }

        public void setAddress(String address) {
            _address = address;
        }

        public void setMsg(String msg) {
            _msg = msg;
        }

        public void setReadState(String readState) {
            _readState = readState;
        }

        public void setTime(String time) {
            _time = time;
        }

        public void setFolderName(String folderName) {
            _folderName = folderName;
        }

        @Override
        public String toString() {

            String s = "";
            s += "id: " + this._id + "\n";
            s += "_address: " + this._address + "\n";
            s += "_msg: " + this._msg + "\n";
            s += "_readState: " + this._readState + "\n";
            s += "_time: " + this._time + "\n";
            s += "_folderName: " + this._folderName + "\n";

            return s;
        }
    }


}