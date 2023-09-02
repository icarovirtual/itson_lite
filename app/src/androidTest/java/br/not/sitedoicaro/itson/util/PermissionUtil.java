package br.not.sitedoicaro.itson.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import android.widget.Switch;

import static android.app.Activity.RESULT_OK;

/** Provides functions to manipulate advanced permissions.
 * <p>
 * e.g.: modifying system settings
 */
public class PermissionUtil {

    /** Launch the settings to enable modifying the system settings. */
    private static void launchModifySystemSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivityForResult(intent, RESULT_OK);
    }

    /** Enables the permission to modify system settings.
     * <p>
     * Manually opens settings and checks the switch.
     */
    public static void enableModifySystemSettings(Activity activity) throws UiObjectNotFoundException {
        launchModifySystemSettings(activity);
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject switchObject = uiDevice.findObject(new UiSelector().className(Switch.class));
        if (!switchObject.isChecked()) {
            switchObject.click();
        }
        uiDevice.pressBack();
    }

    /** Disables the permission to modify system settings.
     * <p>
     * Manually opens settings and un-checks the switch.
     */
    public static void disableModifySystemSettings(Activity activity) throws UiObjectNotFoundException {
        launchModifySystemSettings(activity);
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject switchObject = uiDevice.findObject(new UiSelector().className(Switch.class));
        if (switchObject.isChecked()) {
            switchObject.click();
        }
        uiDevice.pressBack();
    }
}
