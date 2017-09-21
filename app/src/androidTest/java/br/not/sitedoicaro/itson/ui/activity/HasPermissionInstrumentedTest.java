package br.not.sitedoicaro.itson.ui.activity;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.not.sitedoicaro.itson.R;
import br.not.sitedoicaro.itson.util.PermissionUtil;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.not.sitedoicaro.itson.util.ContextUtil.getContext;
import static br.not.sitedoicaro.itson.util.SharedPreferencesUtil.clearSharedPreferences;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HasPermissionInstrumentedTest {

    /**
     * The test depends on running this activity first.
     * <p>
     * Parameters indicate that the activity must be launched manually using {@link ActivityTestRule#launchActivity(android.content.Intent)}.
     */
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    /** With the permission granted, the views are related to normal state. */
    @Test
    public void hasPermissionState() throws UiObjectNotFoundException {
        clearSharedPreferences(getContext(mainActivityActivityTestRule.getActivity()));
        mainActivityActivityTestRule.launchActivity(null);
        PermissionUtil.enableModifySystemSettings(mainActivityActivityTestRule.getActivity());

        // Close the permission dialog
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        uiDevice.pressBack();

        onView(withId(R.id.layout_all_good)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_problem)).check(matches(not(isDisplayed())));
    }
}
