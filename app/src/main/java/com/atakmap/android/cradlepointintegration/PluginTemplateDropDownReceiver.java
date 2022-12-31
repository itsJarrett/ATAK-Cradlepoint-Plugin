
package com.atakmap.android.cradlepointintegration;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.atakmap.android.ipc.AtakBroadcast;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.cradlepointintegration.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;

import com.atakmap.coremap.log.Log;

import java.util.Timer;
import java.util.TimerTask;

public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.cradlepointintegration.SHOW_PLUGIN";
    private final View templateView;
    private final Context pluginContext;

    private Timer plotTrackerTimer = null;

    /**************************** CONSTRUCTOR *****************************/

    public PluginTemplateDropDownReceiver(final MapView mapView,
            final Context context) {
        super(mapView);
        this.pluginContext = context;

        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context,
                R.layout.main_layout, null);

        final Button loginButton = templateView
                .findViewById(R.id.draw_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRetain(true);
                Intent webViewIntent = new Intent();
                webViewIntent.setAction(BrowserDropDownReceiver.SHOW_WEBVIEW);
                AtakBroadcast.getInstance().sendBroadcast(webViewIntent);
            }
        });

        final Button plotTrackersButton = templateView
                .findViewById(R.id.plotTrackersButton);
        plotTrackersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = plotTrackersButton.isSelected();
                if (plotTrackerTimer != null) {
                    plotTrackerTimer.cancel();
                    plotTrackerTimer.purge();
                    plotTrackerTimer = null;
                }
                plotTrackerTimer = new Timer();
                plotTrackersButton.setSelected(!b);
                if (!b) {
                    plotTrackerTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            SBPDUtil.updateTrackers();
                            SBPDUtil.plotTrackers();
                        }
                    }, 0, 10000);
                }
            }
        });

    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
        if (plotTrackerTimer != null) {
            plotTrackerTimer.cancel();
            plotTrackerTimer.purge();
            plotTrackerTimer = null;
        }
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PLUGIN)) {
            Log.d(TAG, "showing plugin drop down");
            final Button login_button = templateView.findViewById(R.id.draw_login_button);
            final Button plotTrackersButton = templateView.findViewById(R.id.plotTrackersButton);
            if (SBPDUtil.isGoodCookie()) {
                login_button.setVisibility(View.GONE);
                plotTrackersButton.setVisibility(View.VISIBLE);
            } else {
                plotTrackersButton.setVisibility(View.INVISIBLE);
                login_button.setVisibility(View.VISIBLE);
            }
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false, this);
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }

}
