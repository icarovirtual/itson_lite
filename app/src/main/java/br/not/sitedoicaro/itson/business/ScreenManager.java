package br.not.sitedoicaro.itson.business;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

/** Provides functions to manipulate the screen timeout. */
public class ScreenManager {

    private static final String TAG = ScreenManager.class.getName();

    /** Default if we read an invalid screen timeout. */
    private static final float DEFAULT_SCREEN_TIMEOUT = 0.5f;

    /** Gets the current timeout (in minutes) of the screen. */
    public static float getScreenOffTimeout(ContentResolver contentResolver) {
        try {
            return (Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT) / 60.0f) / 1000;
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "getScreenOffTimeout: ", e);
        }
        return DEFAULT_SCREEN_TIMEOUT;
    }

    /** Sets the timeout of the screen in minutes. */
    public static void setScreenOffTimeout(ContentResolver contentResolver, float delayMinutes) {
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, (int) (delayMinutes * 60 * 1000));
    }
}
