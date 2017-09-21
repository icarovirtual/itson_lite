package br.not.sitedoicaro.itson.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/** Provides functions to access the app's context. */
public class ContextUtil {

    /** Get the context from the instrumentation. */
    public static Context getContext() {
        return getContext(null);
    }

    /** Get the context from an activity. */
    public static Context getContext(@Nullable Activity activity) {
        Context context;
        if (activity == null) {
            context = getInstrumentation().getTargetContext();
        } else {
            context = activity.getApplicationContext();
        }
        return context;
    }
}
