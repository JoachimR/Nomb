package de.reiss.nomb;

import android.os.Environment;

/**
 * Global Values
 */
public class G {

    public static final String PICTURE_FILENAME = "photo.jpg";
    public static final String AUDIO_FILENAME = "audiorecord.3gp";
    public final static String TAG = "nomb";
    public final static String TESTFILE = "testfile.txt";

    public final static int RECORDAUDIOFINISHED = 23436598;


    public static final int HELLOSERVER_PORT = 12321;


    public final static String ACTION_PROXIMITY_ALERT = "de.reiss.nomb.proximityalert";


    public static final String NOTHING_FOUND = "xxxxx nothing found xxxxx";



    public enum FilePath {
        SYSTEM_INTERNAL_PATHANDFILE {
            public String toString() {
                return "Some System Internal File";
            }
        },

        SDCARD_PATHANDFILE {
            public String toString() {
                return "File on SdCard";
            }
        },

        OWN_APP_PATHANDFILE {
            public String toString() {
                return "File From Own App";
            }
        },

        OTHER_APP_PATHANDFILE {
            public String toString() {
                return "File From Another App";
            }
        };

        public String getValue() {
            if (this == SYSTEM_INTERNAL_PATHANDFILE) {
                return "/system/fonts/DroidSansMono.ttf";
            } else if (this == SDCARD_PATHANDFILE) {
                return Environment.getExternalStorageDirectory() + "/" + "sdcardfile.txt";
            } else if (this == OWN_APP_PATHANDFILE) {
                return "/data/data/de.reiss.nomb/" + "ownappfile.txt";
            } else if (this == OTHER_APP_PATHANDFILE) {
                return "/data/data/com.android.browser/databases/browser2.db";
            }
            return "";
        }
    }
}
