package br.not.sitedoicaro.itson.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/** Provides functions to manipulate the app's shared preferences. */
public class SharedPreferencesUtil {

    /** Get a named shared preferences instance. */
    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /** Clear all shared preferences for the app. */
    public static void clearSharedPreferences(Context context) {
        File root = context.getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames =
                new File(root, "shared_prefs").list();

        if (sharedPreferencesFileNames == null) {
            return;
        }

        for (String fileName : sharedPreferencesFileNames) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    fileName.replace(".xml", ""),
                    Context.MODE_PRIVATE
            );
            sharedPreferences
                    .edit()
                    .clear()
                    .commit();
        }
    }
}
