package br.not.sitedoicaro.itson.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;

import br.not.sitedoicaro.android.ui.activity.BaseActivity;
import br.not.sitedoicaro.android.ui.dialog.DialogUtil;
import br.not.sitedoicaro.android.util.WordUtil;
import br.not.sitedoicaro.itson.R;
import br.not.sitedoicaro.itson.application.OnApplication;
import br.not.sitedoicaro.itson.databinding.ActivityMainBinding;
import br.not.sitedoicaro.itson.ui.presenter.MainActivityPresenter;

/** Set the app's preferences. */
public class MainActivity extends BaseActivity<OnApplication> {

    // region Constants
    private static final String TAG = MainActivity.class.getName();
    // region Preferences
    /** Stores the flag that controls showing the permission explanation. */
    private static final String PREF_NEEDS_PERMISSION_FIRST_TIME = "PREF_NEEDS_PERMISSION_FIRST_TIME";
    /** Stores the flag that controls having the permission for the first time. */
    private static final String PREF_HAS_PERMISSION_FIRST_TIME = "PREF_HAS_PERMISSION_FIRST_TIME";
    // endregion
    // endregion

    // region Fields
    /** Handles layout changes and interactions. */
    private MainActivityPresenter presenter;
    /** Builder of the permission explanation dialog. */
    private AlertDialog.Builder dialogPermissionBuilder;
    /** Dialog with the permission explanation. */
    private AlertDialog dialogPermission;
    /** Snackbar with the permission request. */
    private Snackbar permissionSnackbar;
    // endregion

    /** Setup the layout. */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout setup
        View layoutView = getLayoutInflater().inflate(R.layout.activity_main, null, false);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.bind(layoutView);
        presenter = new MainActivityPresenter(new MainActivityPresenter.ActivityListener() {
            @Override
            public MainActivity provideActivity() {
                return MainActivity.this;
            }

            @Override
            public void provideExplainPermission() {
                MainActivity.this.explainPermission();
            }
        });
        activityMainBinding.setPresenter(presenter);
        setContentView(layoutView);
    }

    /** Check the permissions. Calling it here avoids infinite loops at {@link #onResume()}. */
    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }

    /** Set the analytics screen name. */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /** Checks and requests permissions and educates about their usage. */
    private void checkPermissions() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(TAG, MODE_PRIVATE);
        if (!Settings.System.canWrite(getApplicationContext())) {
            presenter.setHasPermission(false);
            // We manually show the permission explanation dialog the first time the app is opened
            boolean isNeedsPermissionFirstTime = sharedPreferences.getBoolean(PREF_NEEDS_PERMISSION_FIRST_TIME, true);
            if (isNeedsPermissionFirstTime) {
                sharedPreferences.edit().putBoolean(PREF_NEEDS_PERMISSION_FIRST_TIME, false).apply();
                explainPermission();
            } else {
                try {
                    PermissionInfo permissionInfo = getPackageManager().getPermissionInfo(Manifest.permission.WRITE_SETTINGS, PackageManager.GET_META_DATA);
                    CharSequence permissionLabel = permissionInfo.loadLabel(getPackageManager());
                    if (permissionSnackbar == null) {
                        permissionSnackbar = Snackbar.make(getCoordinatorLayout(), getString(R.string.permission_snackbar, WordUtil.titleCase(permissionLabel.toString())), Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.permission_snackbar_action, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        launchModifySystemSettings();
                                    }
                                });
                    }
                    if (!permissionSnackbar.isShown()) {
                        permissionSnackbar.show();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "checkPermissions: ", e);
                }
            }
        } else {
            boolean isHasPermissionFirstTime = sharedPreferences.getBoolean(PREF_HAS_PERMISSION_FIRST_TIME, true);
            if (isHasPermissionFirstTime) {
                sharedPreferences.edit().putBoolean(PREF_HAS_PERMISSION_FIRST_TIME, false).apply();
            }
            // Clear the snackbar in case we got the permission
            if (permissionSnackbar != null && permissionSnackbar.isShown()) {
                permissionSnackbar.dismiss();
            }
            presenter.setHasPermission(true);
        }
    }

    /** Show a dialog explaining the permission to write to the system settings. */
    private void explainPermission() {
        try {
            PermissionInfo permissionInfo = getPackageManager().getPermissionInfo(Manifest.permission.WRITE_SETTINGS, PackageManager.GET_META_DATA);
            CharSequence permissionLabel = permissionInfo.loadLabel(getPackageManager());
            if (dialogPermissionBuilder == null) {
                dialogPermissionBuilder = DialogUtil.getBuilder(MainActivity.this)
                        .setTitle(R.string.permission_dialog_title)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                checkPermissions();
                            }
                        })
                        .setMessage(getString(R.string.permission_dialog, getString(R.string.app_name), permissionLabel))
                        .setPositiveButton(R.string.permission_dialog_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                launchModifySystemSettings();
                            }
                        });
            }
            if (dialogPermission == null || !dialogPermission.isShowing()) {
                dialogPermission = dialogPermissionBuilder.show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "explainPermission: ", e);
        }
    }

    /** Launch the settings to enable modifying the system settings. */
    private void launchModifySystemSettings() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivityForResult(intent, RESULT_OK);
    }
}
