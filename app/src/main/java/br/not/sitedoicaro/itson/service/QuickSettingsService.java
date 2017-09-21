package br.not.sitedoicaro.itson.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

import br.not.sitedoicaro.itson.R;
import br.not.sitedoicaro.itson.business.ScreenManager;
import br.not.sitedoicaro.itson.ui.activity.MainActivity;

/** Controls interactions with the quick settings tile. */
@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickSettingsService extends TileService {

    // region Constants
    private static final String TAG = QuickSettingsService.class.getName();
    /** Keep the screen on for one hour when the quick setting is active. */
    private static final int DELAY_ONE_HOUR = 60;
    // region Preferences
    /** Store if the quick setting is on or off. */
    private static final String PREF_QUICK_SETTING_STATE = "PREF_QUICK_SETTING_STATE";
    /** Stores the timestamp of the last time the tile was activated. */
    private static final String PREF_LAST_TILE_ON_TIMESTAMP = "PREF_LAST_TILE_ON_TIMESTAMP";
    /** Store the system's original screen timeout to restore when the quick setting is deactivated. */
    private static final String PREF_ORIGINAL_SCREEN_TIMEOUT = "PREF_ORIGINAL_SCREEN_TIMEOUT";
    // endregion
    // endregion

    /** Used to persist the tile state changes. */
    private SharedPreferences sharedPreferences;

    /** Create the shared preferences instance. */
    @Override
    public void onCreate() {
        super.onCreate();
        this.sharedPreferences = getApplicationContext().getSharedPreferences(TAG, MODE_PRIVATE);
    }

    /** Register an analytics hit for the tile added. */
    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    /** Restore original screen timeout if tile is removed and register an analytics hit for the tile removed. */
    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        persistTileState(false);
        processTileActionInactive();
    }

    /** Disable the tile if the permission is not granted or the tile is not active (e.g., first addition). */
    @Override
    public void onStartListening() {
        super.onStartListening();
        if (!Settings.System.canWrite(getApplicationContext()) || !isTileActive()) {
            persistTileState(false);
            setTileUiInactive();
        }
    }

    /** Handle screen timeout changes when the quick setting is activated or deactivated. */
    @Override
    public void onClick() {
        super.onClick();
        Context context = getApplicationContext();
        // Check if we have the necessary permissions
        if (!Settings.System.canWrite(context)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityAndCollapse(intent);
        } else {
            if (isTileActive()) {
                // Tile is getting deactivated
                persistTileState(false);
                processTileActionInactive();
                setTileUiInactive();
                processClickAnalytics(true);
            } else {
                // Tile is getting activated
                persistTileState(true);
                processTileActionActive();
                setTileUiActive();
                processClickAnalytics(false);
            }
        }
    }

    /** Register the click and calculate the time the user spent with the quick setting active. */
    private void processClickAnalytics(boolean isDeactivating) {
        if (!isDeactivating) {
            // If the use is activating, save the initial time
            sharedPreferences.edit().putLong(PREF_LAST_TILE_ON_TIMESTAMP, System.currentTimeMillis()).commit();
        }
    }

    /** Persist the tile state. */
    private void persistTileState(boolean isTileActive) {
        sharedPreferences.edit().putBoolean(PREF_QUICK_SETTING_STATE, isTileActive).commit();
    }

    /** Check if our last saved state is active. */
    private boolean isTileActive() {
        return sharedPreferences.getBoolean(PREF_QUICK_SETTING_STATE, false);
    }

    /**
     * Save the previous screen timeout, set our screen timeout.
     * <p>
     * We set to a very high minutes value because the system automatically adjusts it to the highest allowed.
     * e.g.: we set it to 1 hour but the system can handle at most 30 minutes so that's the value set.
     */
    private void processTileActionActive() {
        sharedPreferences.edit().putFloat(PREF_ORIGINAL_SCREEN_TIMEOUT, ScreenManager.getScreenOffTimeout(getContentResolver())).apply();
        ScreenManager.setScreenOffTimeout(getContentResolver(), DELAY_ONE_HOUR);
    }

    /** Load the previous screen timeout . */
    private void processTileActionInactive() {
        ScreenManager.setScreenOffTimeout(getContentResolver(), sharedPreferences.getFloat(PREF_ORIGINAL_SCREEN_TIMEOUT, ScreenManager.getScreenOffTimeout(getContentResolver())));
    }

    /** Changes the tile UI to make it inactive. */
    private void setTileUiInactive() {
        Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.vc_quick_setting_off));
        tile.setLabel(getString(R.string.quick_setting_label_off));
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    /** Changes the tile UI to make it active. */
    private void setTileUiActive() {
        Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.vc_quick_setting_on));
        tile.setLabel(getString(R.string.quick_setting_label_on));
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
    }
}
