package br.not.sitedoicaro.itson.ui.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static br.not.sitedoicaro.itson.util.ContextUtil.getContext;
import static br.not.sitedoicaro.itson.util.SharedPreferencesUtil.clearSharedPreferences;
import static br.not.sitedoicaro.itson.util.SharedPreferencesUtil.getSharedPreferences;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.not.sitedoicaro.itson.R;
import br.not.sitedoicaro.itson.util.PermissionUtil;

@RunWith(AndroidJUnit4.class)
public class NeedsPermissionInstrumentedTest {

    /**
     * The test depends on running this activity first.
     * <p>
     * Parameters indicate that the activity must be launched manually using
     * {@link ActivityTestRule#launchActivity(android.content.Intent)}.
     */
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    /** In the app's first run, a dialog explaining the permission requirement is displayed and the first time flag is set. */
    @Test
    public void firstTimeShowsDialog() throws UiObjectNotFoundException {
        Context context = getContext(mainActivityActivityTestRule.getActivity());
        clearSharedPreferences(context);
        mainActivityActivityTestRule.launchActivity(null);

        // Due to test execution order, other tests might have enabled this
        // settings previously, so it must be disabled to simulate first run
        PermissionUtil.disableModifySystemSettings(mainActivityActivityTestRule.getActivity());

        onView(withText(R.string.permission_dialog_button)).check(matches(isDisplayed()));
        assertFalse(
                getSharedPreferences(context, MainActivity.class.getName())
                        .getBoolean("PREF_NEEDS_PERMISSION_FIRST_TIME", true)
        );
    }

    /**
     * While the permission is not granted, the views are related to the
     * permission request.
     */
    @Test
    public void needsPermissionState() {
        Context context = getContext(mainActivityActivityTestRule.getActivity());
        clearSharedPreferences(context);
        // Set the first time flag so the dialog doesn't show
        getSharedPreferences(context, MainActivity.class.getName())
                .edit()
                .putBoolean("PREF_NEEDS_PERMISSION_FIRST_TIME", false)
                .commit();
        mainActivityActivityTestRule.launchActivity(null);

        onView(withId(R.id.layout_problem)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_all_good)).check(matches(not(isDisplayed())));
    }
}
