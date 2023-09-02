package br.not.sitedoicaro.itson.ui.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static br.not.sitedoicaro.itson.util.ContextUtil.getContext;
import static br.not.sitedoicaro.itson.util.SharedPreferencesUtil.clearSharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.not.sitedoicaro.itson.R;
import br.not.sitedoicaro.itson.util.PermissionUtil;

@RunWith(AndroidJUnit4.class)
public class HasPermissionInstrumentedTest {

    /**
     * The test depends on running this activity first.
     * <p>
     * Parameters indicate that the activity must be launched manually using
     * {@link ActivityTestRule#launchActivity(android.content.Intent)}.
     */
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    /** With the permission granted, the views are related to normal state. */
    @Test
    public void hasPermissionState() throws UiObjectNotFoundException {
        clearSharedPreferences(getContext(mainActivityActivityTestRule.getActivity()));
        mainActivityActivityTestRule.launchActivity(null);

        PermissionUtil.enableModifySystemSettings(mainActivityActivityTestRule.getActivity());
        // Close the permission dialog
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        uiDevice.pressBack();

        onView(withId(R.id.layout_all_good)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_problem)).check(matches(not(isDisplayed())));
    }
}
