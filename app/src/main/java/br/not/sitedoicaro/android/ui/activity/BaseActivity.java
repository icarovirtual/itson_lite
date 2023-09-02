package br.not.sitedoicaro.android.ui.activity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import br.not.sitedoicaro.itson.R;

/** Provides the structure and helps the setting up of an activity with a toolbar and coordinator layout. */
@SuppressWarnings("unused")
public class BaseActivity<T extends Application> extends AppCompatActivity {

    // region Constants
    /** Dynamically load id resources. */
    private static final String ID_RES = "id";
    /** Indicates an invalid id resource. */
    private static final int INVALID_ID = 0;

    /** Default id name for the coordinator layout. Change by overriding {@link BaseActivity#getCoordinatorLayoutIdName()}. */
    private static final String COORDINATOR_LAYOUT_ID_NAME = "coordinator";
    /** Default id name for the toolbar. Change by overriding {@link BaseActivity#getToolbarIdName()}. */
    private static final String TOOLBAR_ID_NAME = "toolbar";
    // endregion

    // region Fields
    /** Coordinator layout of the activity. */
    private CoordinatorLayout coordinatorLayout;
    /** Toolbar of the activity. */
    private Toolbar toolbar;
    /** Toolbar's title view of the activity. */
    private TextView titleView;
    /** Toolbar's subtitle view of the activity. */
    private TextView subtitleView;
    // endregion

    // region Lifecycle

    /** Automatically sets up the base views. */
    @Override
    protected void onStart() {
        super.onStart();
        // Prepare the views
        setupViews();
        // Set toolbar title
        String title = getTitle().toString();
        setToolbarTitle(title);
    }
    // endregion

    // region Initialization

    /** Injects the content view below the toolbar. */
    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        @SuppressLint("InflateParams") View fullLayout = getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout contentLayout = fullLayout.findViewById(R.id.material_content);
        getLayoutInflater().inflate(layoutResId, contentLayout, true);
        super.setContentView(fullLayout);
    }

    /** Injects the content view below the toolbar in an activity with data binding. */
    @Override
    public void setContentView(View view) {
        @SuppressLint("InflateParams") View fullLayout = getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout contentLayout = fullLayout.findViewById(R.id.material_content);
        contentLayout.addView(view);
        super.setContentView(fullLayout);
    }

    /** Setup an additional view that can cover the toolbar. */
    public void setOverlayView(@LayoutRes int layoutResId) {
        FrameLayout overlayRoot = findViewById(R.id.material_overlay);
        overlayRoot.setVisibility(View.VISIBLE);
        getLayoutInflater().inflate(layoutResId, overlayRoot, true);
    }

    /** Automatically initialize the toolbar and coordinator layout. */
    private void setupViews() {
        Context context = getApplicationContext();
        String packageName = getPackageName();
        // Setup the toolbar
        @SuppressLint("DiscouragedApi") int toolbarId = context.getResources().getIdentifier(getToolbarIdName(), ID_RES, packageName);
        if (toolbarId != INVALID_ID) {
            setToolbar(toolbarId);
        }
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        titleView = findViewById(R.id.toolbar_title);
        subtitleView = findViewById(R.id.toolbar_subtitle);
        // Setup the coordinator layout
        @SuppressLint("DiscouragedApi") int coordinatorLayoutId = context.getResources().getIdentifier(getCoordinatorLayoutIdName(), ID_RES, packageName);
        if (coordinatorLayoutId != INVALID_ID) {
            setCoordinatorLayout(coordinatorLayoutId);
        }
    }
    // endregion

    public T getCustomApplication() {
        //noinspection unchecked
        return (T) getApplication();
    }

    // region Coordinator layout

    /** Get the coordinator layout id resource name. */
    @NonNull
    protected String getCoordinatorLayoutIdName() {
        return COORDINATOR_LAYOUT_ID_NAME;
    }

    /** Set the coordinator layout from its id. */
    public final void setCoordinatorLayout(@IdRes int coordinatorId) {
        coordinatorLayout = findViewById(coordinatorId);
    }

    /** Get the coordinator layout. */
    public final CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }
    // endregion

    // region Toolbar

    /** Get the toolbar id resource name. */
    @NonNull
    protected String getToolbarIdName() {
        return TOOLBAR_ID_NAME;
    }

    /** Set the toolbar from its id. */
    public final void setToolbar(@IdRes int toolbarId) {
        toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar);
    }

    /** Get the toolbar. */
    public final Toolbar getToolbar() {
        return toolbar;
    }

    /** Set the toolbar title if it exists. */
    public final void setToolbarTitle(@Nullable String title) {
        if (this.titleView != null) {
            this.titleView.setText(title);
        }
    }

    /** Set the toolbar subtitle if it exists. */
    public final void setToolbarSubtitle(@Nullable String subtitle) {
        if (this.subtitleView != null) {
            this.subtitleView.setText(subtitle);
        }
    }

    /** Enables or hides the toolbar title. */
    public final void setShouldShowToolbarTitle(boolean shouldShow) {
        if (this.titleView != null) {
            titleView.setVisibility(!shouldShow ? View.GONE : View.VISIBLE);
        }
    }

    /** Enables or hides the toolbar subtitle. */
    public final void setShouldShowToolbarSubtitle(boolean shouldShow) {
        if (this.subtitleView != null) {
            subtitleView.setVisibility(!shouldShow ? View.GONE : View.VISIBLE);
        }
    }
    // endregion
}
