package br.not.sitedoicaro.itson.ui.presenter;

import android.content.Intent;
import android.databinding.ObservableBoolean;

import br.not.sitedoicaro.android.ui.dialog.DialogUtil;
import br.not.sitedoicaro.android.util.BrowserUtil;
import br.not.sitedoicaro.itson.R;
import br.not.sitedoicaro.itson.ui.activity.MainActivity;

/** Handles UI changes in the {@link MainActivity}. */
public class MainActivityPresenter {

    // region Fields
    /** Used to control showing the all good or problem views. */
    public final ObservableBoolean hasPermission = new ObservableBoolean();
    /** Interface with the activity. */
    private ActivityListener listener;
    // endregion

    /** Receive the activity interface and set the initial permission status to {@code true}. */
    public MainActivityPresenter(ActivityListener listener) {
        this.listener = listener;
        this.hasPermission.set(true);
    }

    /** Change the permission status. */
    public void setHasPermission(boolean hasPermission) {
        this.hasPermission.set(hasPermission);
    }

    /** Open a link with a video that shows how to add a quick setting tile. */
    public void onAllGoodClick() {
        final MainActivity activity = listener.provideActivity();
        final String videoUrl = activity.getString(R.string.main_all_good_button_url);
        BrowserUtil.checkSystemHasBrowser(videoUrl, activity.getPackageManager(), new BrowserUtil.OnHasBrowser() {
            @Override
            public void onBrowserSafe(Intent browserIntent) {
                activity.startActivity(browserIntent);
            }
        }, new BrowserUtil.OnNoBrowser() {
            @Override
            public void onNoBrowser() {
                DialogUtil.getBuilder(activity)
                        .setMessage(activity.getString(R.string.main_all_good_button_error, videoUrl))
                        .setPositiveButton(R.string.main_all_good_button_error_button, DialogUtil.DISMISS_LISTENER)
                        .show();
            }
        });
    }

    /** Shows again the dialog that explains the permission. */
    public void onProblemClick() {
        listener.provideExplainPermission();
    }

    /** Interface to communicate with this presenter's activity. */
    public interface ActivityListener {

        /** Get a reference to the activity. */
        MainActivity provideActivity();

        /** Make the activity show the permission explanation. */
        void provideExplainPermission();
    }
}
