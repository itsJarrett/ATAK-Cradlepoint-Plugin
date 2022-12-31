
package com.atakmap.android.cradlepointintegration;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDownMapComponent;

import com.atakmap.coremap.log.Log;
import com.atakmap.android.cradlepointintegration.plugin.R;

public class PluginTemplateMapComponent extends DropDownMapComponent {

    private static final String TAG = "PluginTemplateMapComponent";

    private Context pluginContext;

    private PluginTemplateDropDownReceiver ddr;
    private BrowserDropDownReceiver wvdropDown;

    public void onCreate(final Context context, Intent intent,
            final MapView view) {

        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, view);
        pluginContext = context;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ddr = new PluginTemplateDropDownReceiver(
                view, context);

        Log.d(TAG, "registering the plugin filter");
        DocumentedIntentFilter ddFilter = new DocumentedIntentFilter();
        ddFilter.addAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
        registerDropDownReceiver(ddr, ddFilter);

        this.wvdropDown = new BrowserDropDownReceiver(view, context);
        Log.d(TAG, "registering the c5 login");
        DocumentedIntentFilter wvFilter = new DocumentedIntentFilter();
        wvFilter.addAction(BrowserDropDownReceiver.SHOW_WEBVIEW,
                "C5 Login");
        this.registerDropDownReceiver(this.wvdropDown, wvFilter);
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        super.onDestroyImpl(context, view);
    }

}
