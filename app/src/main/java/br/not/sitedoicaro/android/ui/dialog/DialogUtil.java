package br.not.sitedoicaro.android.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

/** Provides easier creation of frequently used dialogs. */
public class DialogUtil {

    /** Reusable button for a listener button that just closes the dialog. */
    public static final DialogInterface.OnClickListener DISMISS_LISTENER = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    /** Creates a default dialog builder. */
    public static AlertDialog.Builder getBuilder(Activity context) {
        return new AlertDialog.Builder(context);
    }
}
