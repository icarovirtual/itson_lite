package br.not.sitedoicaro.itson.ui.activity;

import android.content.Context;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.not.sitedoicaro.itson.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.not.sitedoicaro.itson.util.ContextUtil.getContext;
import static br.not.sitedoicaro.itson.util.SharedPreferencesUtil.clearSharedPreferences;
import static br.not.sitedoicaro.itson.util.SharedPreferencesUtil.getSharedPreferences;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NeedsPermissionInstrumentedTest {

    /**
     * The test depends on running this activity first.
     * <p>
     * Parameters indicate that the activity must be launched manually using {@link ActivityTestRule#launchActivity(android.content.Intent)}.
     */
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    /** In the app's first run, a dialog explaining the permission requirement is displayed and the first time flag is set. */
    @Test
    public void firstTimeShowsDialog() throws Exception {
        Context context = getContext(mainActivityActivityTestRule.getActivity());
        clearSharedPreferences(context);
        mainActivityActivityTestRule.launchActivity(null);

        String dialogButtonText = context.getString(R.string.permission_dialog_button).toUpperCase();

        UiObject dialogButton = UiDevice.getInstance(getInstrumentation()).findObject(new UiSelector().text(dialogButtonText));
        assertTrue(dialogButton.exists());
        assertFalse(getSharedPreferences(context, MainActivity.class.getName()).getBoolean("PREF_NEEDS_PERMISSION_FIRST_TIME", true));
    }

    /** While the permission is not granted, the views are related to the permission request. */
    @Test
    public void needsPermissionState() throws Exception {
        Context context = getContext(mainActivityActivityTestRule.getActivity());
        clearSharedPreferences(context);
        // Set the first time flag so the dialog doesn't show
        getSharedPreferences(context, MainActivity.class.getName()).edit().putBoolean("PREF_NEEDS_PERMISSION_FIRST_TIME", false).commit();
        mainActivityActivityTestRule.launchActivity(null);

        onView(withId(R.id.layout_problem)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_all_good)).check(matches(not(isDisplayed())));
    }
}
