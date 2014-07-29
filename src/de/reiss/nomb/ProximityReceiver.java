package de.reiss.nomb;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * This is the Receiver for the Brodcast sent, here our app will be notified if the User is
 * in the region specified by our proximity alert.You will have to register the Receiver
 * with the same Intent you broadcasted in the previous Java file
 *
 * @Author: Adnan A M
 * http://myandroidtuts.blogspot.de/2012/10/proximity-alerts.html
 */
public class ProximityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING; // key for determining user is leaving or entering

        boolean state = arg1.getBooleanExtra(key, false); // whether the user is entering or leaving

        if (state) {
            // Call the Notification Service or anything else that you would like to do here
            Toast.makeText(arg0, "...Good to know that you are in my Area...", Toast.LENGTH_LONG).show();
        } else {
            //Other custom Notification
            Toast.makeText(arg0, "...Good to know that you were in my Area..." +
                    "Come back again !!", Toast.LENGTH_LONG).show();
        }
    }
}