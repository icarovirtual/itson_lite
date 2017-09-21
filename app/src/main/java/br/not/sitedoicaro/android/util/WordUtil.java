package br.not.sitedoicaro.android.util;

/** Provides utilities to manipulate strings. */
public class WordUtil {

    /** Sets the first character of the text to upper case and the other characters to lower case. */
    public static String titleCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
