package br.not.sitedoicaro.android.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;

/** Provides utilities to validate the usage of intents for browsers. */
public class BrowserUtil {

    /** Check if the system as any web browser installed so we can redirect an intent. */
    public static void checkSystemHasBrowser(String url, PackageManager packageManager, OnHasBrowser onHasBrowser, @Nullable OnNoBrowser onNoBrowser) {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        if (browserIntent.resolveActivity(packageManager) != null) {
            onHasBrowser.onBrowserSafe(browserIntent);
        } else {
            if (onNoBrowser != null) {
                onNoBrowser.onNoBrowser();
            }
        }
    }

    /** Handle when we have an available browser. */
    public interface OnHasBrowser {

        /** Provides the intent to redirect to the URL in a browser. */
        void onBrowserSafe(Intent browserIntent);
    }

    /** Handle when no browser is installed. */
    public interface OnNoBrowser {

        /** Inform the user that no browser is available and to open the URL manually. */
        void onNoBrowser();
    }
}

