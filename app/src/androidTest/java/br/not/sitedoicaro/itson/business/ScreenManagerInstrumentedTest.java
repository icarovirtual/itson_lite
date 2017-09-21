package br.not.sitedoicaro.itson.business;

import android.content.ContentResolver;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.not.sitedoicaro.itson.ui.activity.MainActivity;

import static br.not.sitedoicaro.itson.util.ContextUtil.getContext;
import static br.not.sitedoicaro.itson.util.PermissionUtil.disableModifySystemSettings;
import static br.not.sitedoicaro.itson.util.PermissionUtil.enableModifySystemSettings;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ScreenManagerInstrumentedTest {

    // region Fields
    /** Saves the device's original screen off timeout to restore after a test if necessary. */
    private float originalScreenOffTimeout;
    /**
     * Indicates if it's necessary to restore the device's original screen off timeout after a test.
     * <p>
     * Set to {@code true} inside the test function so {@link ScreenManagerInstrumentedTest#restoreOriginalScreenOffTimeout()}
     * is executed and the value is restored.
     */
    private boolean shouldRestoreScreenOffTimeout;
    // endregion

    /** The test depends on running this activity first. */
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);

    // region Helper

    /** Save the device's screen off timeout to restore at the end of each test if necessary. */
    @Before
    public void saveOriginalScreenOffTimeout() {
        originalScreenOffTimeout = ScreenManager.getScreenOffTimeout(getContext().getContentResolver());
        shouldRestoreScreenOffTimeout = false;
    }

    /** Restore the device's screen off timeout to its original value. */
    @After
    public void restoreOriginalScreenOffTimeout() throws UiObjectNotFoundException {
        if (shouldRestoreScreenOffTimeout) {
            enableModifySystemSettings(mainActivityActivityTestRule.getActivity());
            ScreenManager.setScreenOffTimeout(getContext().getContentResolver(), originalScreenOffTimeout);
        }
    }
    // endregion

    /** Can retrieve the current screen off timeout. */
    @Test
    public void canGetScreenOffTimeout() {
        float screenOffTimeout = ScreenManager.getScreenOffTimeout(getContext().getContentResolver());
        assertThat(screenOffTimeout, greaterThan(0f));
    }

    /** Setting screen off timeout fails because the app lacks the permission to modify the system settings. */
    @Test(expected = SecurityException.class)
    public void cannotScreenOffTimeout() throws UiObjectNotFoundException {
        disableModifySystemSettings(mainActivityActivityTestRule.getActivity());

        ScreenManager.setScreenOffTimeout(getContext().getContentResolver(), 10);
    }

    /** Set the screen off timeout normally. */
    @Test
    public void canSetScreenOffTimeout() throws UiObjectNotFoundException {
        shouldRestoreScreenOffTimeout = true;
        enableModifySystemSettings(mainActivityActivityTestRule.getActivity());

        float newScreenOffTimeout = 5;
        ContentResolver contentResolver = getContext().getContentResolver();
        float originalScreenOffTimeout = ScreenManager.getScreenOffTimeout(contentResolver);
        // Guarantee that the device's current timeout is not the same we are testing to avoid false positives
        if (originalScreenOffTimeout == newScreenOffTimeout) {
            newScreenOffTimeout = 10;
        }

        ScreenManager.setScreenOffTimeout(contentResolver, newScreenOffTimeout);
        float setScreenOffTimeout = ScreenManager.getScreenOffTimeout(contentResolver);
        assertEquals(setScreenOffTimeout, newScreenOffTimeout, 0);
    }
}
