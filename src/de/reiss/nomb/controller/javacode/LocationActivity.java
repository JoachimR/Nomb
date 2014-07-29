package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import de.reiss.nomb.G;
import de.reiss.nomb.R;
import de.reiss.nomb.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener, GpsStatus.NmeaListener
        , GpsStatus.Listener {


    Activity mActivity;

    LocationClient mLocationClient;


    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;


    // write toggle values only in buttonclick listeners
    // in order to keep it transparent!
    private static boolean isNmeaListenerAdded;
    private static boolean isProximtyListenerAdded;
    private static boolean isGpsStatusListenerAdded;
    private static boolean isLocationClientConnected;

    private long lastTimeGpsStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_location);
        mActivity = this;

        initButtonClickListeners();

        lastTimeGpsStatus = System.currentTimeMillis();

        /**
         * https://developer.android.com/training/location/receive-location-updates.html
         */
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);


    }

    private void initButtonClickListeners() {
        isGpsStatusListenerAdded = false;
        isNmeaListenerAdded = false;
        isProximtyListenerAdded = false;
        isLocationClientConnected = false;


        Button btn_readlocation = (Button) findViewById(R.id.btn_readlocation);
        btn_readlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showResultInDialog(mActivity, readLocation());
            }
        });


        final Button btn_gpsstatus_toggle = (Button)
                findViewById(R.id.btn_gpsstatus_toggle);
        btn_gpsstatus_toggle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isGpsStatusListenerAdded) {
                            toggleGpsStatusListener();
                            isGpsStatusListenerAdded = true;
                            btn_gpsstatus_toggle.setText(
                                    R.string.btn_gpsstatus_remove);
                        } else {
                            toggleGpsStatusListener();
                            isGpsStatusListenerAdded = false;
                            btn_gpsstatus_toggle.setText(
                                    R.string.btn_gpsstatus_add);
                        }
                    }
                }
        );

        final Button btn_nmealistener_toggle = (Button)
                findViewById(R.id.btn_nmealistener_toggle);
        btn_nmealistener_toggle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isNmeaListenerAdded) {
                            toggleNmeaListener();
                            isNmeaListenerAdded = true;
                            btn_nmealistener_toggle.setText(
                                    R.string.btn_nmealistener_remove);
                        } else {
                            toggleNmeaListener();
                            isNmeaListenerAdded = false;
                            btn_nmealistener_toggle.setText(
                                    R.string.btn_nmealistener_add);
                        }
                    }
                }
        );

        final Button btn_proximity_toggle = (Button)
                findViewById(R.id.btn_proximity_toggle);
        btn_proximity_toggle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isProximtyListenerAdded) {
                            toggleProximityListener();
                            isProximtyListenerAdded = true;
                            btn_proximity_toggle.setText(
                                    R.string.btn_proximity_remove);
                        } else {
                            toggleProximityListener();
                            isProximtyListenerAdded = false;
                            btn_proximity_toggle.setText(
                                    R.string.btn_proximity_add);
                        }
                    }
                }
        );


        final Button btn_googleplay_connect_toggle = (Button)
                findViewById(R.id.btn_googleplay_connect_toggle);
        btn_googleplay_connect_toggle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isLocationClientConnected) {
                            toggleGoogePlayLocationService();
                            isLocationClientConnected = true;
                            btn_googleplay_connect_toggle.setText(
                                    R.string.btn_googleplay_disconnect);
                        } else {
                            toggleGoogePlayLocationService();
                            isLocationClientConnected = false;
                            btn_googleplay_connect_toggle.setText(
                                    R.string.btn_googleplay_connect);
                        }
                    }
                });

        Button btn_googleplay_addgeofence = (Button)
                findViewById(R.id.btn_googleplay_addgeofence);
        btn_googleplay_addgeofence.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.showResultInDialog(mActivity, addGeofence());
                    }
                });

        Button btn_googleplay_removegeofence = (Button)
                findViewById(R.id.btn_googleplay_removegeofence);
        btn_googleplay_removegeofence.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.showResultInDialog(mActivity, removeGeofence());
                    }
                });


    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected with LocationClient",
                Toast.LENGTH_SHORT).show();
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected from LocationClient." +
                " Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Error connecting " +
                "Googe Play Services.",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGpsStatusChanged(int event) {
        if ((System.currentTimeMillis() - lastTimeGpsStatus) < 5000) {
            return;
        }
        lastTimeGpsStatus = System.currentTimeMillis();


        final LocationManager locationManager =
                (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(mActivity, "LocationManager is null!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                GpsStatus status = locationManager.getGpsStatus(null);
                if (status != null) {
                    Toast.makeText(mActivity, "Got GPS Status: '"
                            + status.toString() + "'", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "Could not get GPS Status",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Toast.makeText(mActivity, "GPS: First Fix/ Refix",
                        Toast.LENGTH_SHORT).show();
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                Toast.makeText(mActivity, "GPS: Started",
                        Toast.LENGTH_SHORT).show();
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                Toast.makeText(mActivity, "GPS: Stopped",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(mActivity, "GPS: unknown event '"
                        + event + "'",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNmeaReceived(long timestamp, String nmea) {
//        Toast.makeText(mActivity, "Reveived nmea: '"
//                + nmea + "'", Toast.LENGTH_SHORT).show();
        Log.i(G.TAG, "Reveived nmea: '" + nmea + "'");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onStop() {
        disconnectLocationClient();
        super.onStop();
    }

    private String readLocation() {
        StringBuilder sb = new StringBuilder();

        String loc = null;
        String location_context = Context.LOCATION_SERVICE;
        LocationManager locationManager =
                (LocationManager) mActivity.getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider,
                    1000, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                }

                @Override
                public void onStatusChanged(String provider,
                                            int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                loc = "" + latitude + " / " + longitude;
            }
        }
        sb.append("Location:");
        sb.append("\n");
        sb.append(loc);
        sb.append("\n");
        return sb.toString();
    }

    private void toggleGpsStatusListener() {
        String msg;
        LocationManager locationManager =
                (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (!isGpsStatusListenerAdded) {
                locationManager.addGpsStatusListener(this);
                msg = "GPS Status Listener added.";
            } else {
                locationManager.removeGpsStatusListener(this);
                msg = "GPS Status Listener removed.";
            }
        } else {
            msg = "LocationManager is null!";
        }
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private void toggleNmeaListener() {
        String msg;
        LocationManager locationManager =
                (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (!isNmeaListenerAdded) {
                locationManager.addNmeaListener(this);
                msg = "Nmea Listener added.";
            } else {
                locationManager.removeNmeaListener(this);
                msg = "Nmea Listener removed.";
            }
        } else {
            msg = "LocationManager is null!";
        }
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private void toggleProximityListener() {
        String msg;
        LocationManager locationManager =
                (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        final int requestCodeNeverExpire = -1;

        if (locationManager != null) {
            if (!isProximtyListenerAdded) {
                Intent intent = new Intent(G.ACTION_PROXIMITY_ALERT);
                final float radius = 3000;
                PendingIntent pendingIntent = PendingIntent.
                        getBroadcast(mActivity, -1, intent, 0);
                locationManager.addProximityAlert(
                        UniMarburg.latitude, UniMarburg.longitude,
                        radius, requestCodeNeverExpire, pendingIntent);
                msg = "Added Proximity Listener for '" +
                        UniMarburg.id + "'.";
            } else {
                Intent intent = new Intent(G.ACTION_PROXIMITY_ALERT);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        mActivity, requestCodeNeverExpire, intent, 0);
                locationManager.removeProximityAlert(pendingIntent);
                msg = "Removed Proximity Listener";
            }

        } else {
            msg = "LocationManager is null!";
        }
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private String addGeofence() {
        if (mLocationClient == null || !mLocationClient.isConnected()) {
            return "LocationClient not connected.";
        }

        final StringBuffer sb = new StringBuffer();

        sb.append("Add Geofence:" + "\n\n");
        try {
            final ArrayList<Geofence> list = new ArrayList<Geofence>();

            Geofence.Builder builder = new Geofence.Builder();
            builder.setRequestId(UniMarburg.id);
            builder.setCircularRegion(UniMarburg.latitude, UniMarburg.longitude, 50);
            builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
            builder.setExpirationDuration(Geofence.NEVER_EXPIRE);

            list.add(builder.build());

            Intent intent = new Intent(getBaseContext(),
                    ReceiveTransitionsIntentService.class);
            PendingIntent pendingIntent = PendingIntent.getService(
                    getBaseContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mLocationClient.addGeofences(list, pendingIntent,
                    new LocationClient.OnAddGeofencesResultListener() {
                        public void onAddGeofencesResult(
                                int statusCode, String[] geofenceRequestIds) {

                            Log.d(G.TAG, "onAddGeofencesResult" + "\n");
                            Log.d(G.TAG, "geofenceRequestIds:" + "\n");

                            sb.append("onAddGeofencesResult" + "\n");
                            sb.append("geofenceRequestIds:" + "\n");
                            for (String geofenceRequestId : geofenceRequestIds) {
                                Log.d(G.TAG, geofenceRequestId + "\n");
                                sb.append(geofenceRequestId + "\n");
                            }
                        }
                    });
        } catch (Exception e) {
            sb.append("not successfull" + "\n\n");
            return sb.toString();

        }
        sb.append("successfull" + "\n\n");

        return sb.toString();
    }


    private String removeGeofence() {
        if (mLocationClient == null || !mLocationClient.isConnected()) {
            return "LocationClient not connected.";
        }

        final StringBuilder sb = new StringBuilder();

        sb.append("Remove Geofence:" + "\n\n");
        try {
            ArrayList<String> toRemoveList = new ArrayList<String>();
            toRemoveList.add(UniMarburg.id);
            mLocationClient.removeGeofences(toRemoveList,
                    new LocationClient.OnRemoveGeofencesResultListener() {
                        @Override
                        public void onRemoveGeofencesByRequestIdsResult(
                                int i, String[] strings) {
                            sb.append("onRemoveGeofences" +
                                    "ByRequestIdsResult, id:" + i + "\n");
                        }

                        @Override
                        public void onRemoveGeofencesByPendingIntentResult(
                                int i, PendingIntent pendingIntent) {
                            Log.d(G.TAG, "onRemoveGeofences" +
                                    "ByPendingIntentResult id:" + i + "\n" + "\n");
                            sb.append("onRemoveGeofences" +
                                    "ByPendingIntentResult id:" + i + "\n");
                        }
                    });

        } catch (Exception e) {
            sb.append("not successfull" + "\n\n");
            return sb.toString();

        }
        sb.append("successfull" + "\n\n");

        return sb.toString();
    }


    private void toggleGoogePlayLocationService() {
        if (!isLocationClientConnected) {
            Toast.makeText(this, "Connecting to LocationClient " +
                    "of Google Play Service...",
                    Toast.LENGTH_SHORT).show();

            mLocationClient = new LocationClient(this, this, this);
            mLocationClient.connect(); // see result in onConnected()


        } else {
            Toast.makeText(this, "Disconnecting LocationClient " +
                    "of Google Play Service...",
                    Toast.LENGTH_SHORT).show();

            disconnectLocationClient();
        }
    }

    private void disconnectLocationClient() {
        // Disconnecting the client invalidates it.
        if (mLocationClient != null) {
            // If the client is connected
            if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
                mLocationClient.removeLocationUpdates(this);
            }
            /*
             * After disconnect() is called, the client is
             * considered "dead".
             */
            mLocationClient.disconnect();
        }
    }


    private class ReceiveTransitionsIntentService extends IntentService {
        /**
         * Sets an identifier for the service
         */
        public ReceiveTransitionsIntentService() {
            super("ReceiveTransitionsIntentService");
        }

        /**
         * Handles incoming intents
         *
         * @param intent The Intent sent by Location Services. This Intent is provided
         *               to Location Services (inside a PendingIntent) when you call
         *               addGeofences()
         */
        @Override
        protected void onHandleIntent(Intent intent) {
            // First check for errors
            if (LocationClient.hasError(intent)) {
                // Get the error code with a static method
                int errorCode = LocationClient.getErrorCode(intent);
                // Log the error
                final String errorMsg = "Location Services error: "
                        + Integer.toString(errorCode);

                Log.e(G.TAG, errorMsg);
                Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();


                return;
            }

           /*
            * If there's no error, get the transition type and the IDs of the
            * geofence or geofences that triggered the transition
            */

            // Get the type of transition (entry or exit)
            int transitionType = LocationClient.getGeofenceTransition(intent);
            // Test that a valid transition was reported
            if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
                    || (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
                List<Geofence> triggerList = LocationClient.
                        getTriggeringGeofences(intent);

                String[] triggerIds = new String[triggerList.size()];

                for (int i = 0; i < triggerIds.length; i++) {
                    // Store the Id of each geofence
                    triggerIds[i] = triggerList.get(i).getRequestId();
                    final String msg = "Location Services " +
                            "triggerId: " + triggerIds[i];
                    Log.d(G.TAG, msg);
                    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private static class SimpleGeofence {
        public String id;
        public double longitude;
        public double latitude;
    }

    private static SimpleGeofence UniMarburg;

    static {
        UniMarburg = new SimpleGeofence();
        UniMarburg.id = "UniMarburgFB12";
        UniMarburg.latitude = 50.809745;
        UniMarburg.longitude = 8.810992;
        // https://maps.google.de/maps?f=q&source=s_q&hl=de&geocode=&q=Hans-Meerwein-Stra%C3%9Fe,+Marburg+fachbereich+mathematik&aq=&sll=51.175806,10.454119&sspn=11.758745,19.753418&vpsrc=6&ie=UTF8&hq=fachbereich+mathematik&hnear=Hans-Meerwein-Stra%C3%9Fe,+35043+Marburg&ll=50.809731,8.811035&spn=0.011566,0.01929&t=m&z=16&iwloc=A&cid=5561057568559404013
    }

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
}