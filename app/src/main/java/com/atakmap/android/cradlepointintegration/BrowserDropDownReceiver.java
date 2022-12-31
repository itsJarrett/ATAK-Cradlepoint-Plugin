
package com.atakmap.android.cradlepointintegration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownManager;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.cradlepointintegration.plugin.R;
import com.atakmap.android.util.NotificationUtil;
import com.atakmap.coremap.log.Log;

import java.util.Timer;

public class BrowserDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = BrowserDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_WEBVIEW = "sbpd.cradlepoint.webview";
    public static final String HIDE_WEBVIEW = "sbpd.cradlepoint.webview.hide";
    private final WebView htmlViewer;
    private final Context pluginContext;
    final Context appContext;

    private final LinearLayout ll;

    private Timer plotTrackerTimer = null;

    /**************************** CONSTRUCTOR *****************************/

    public BrowserDropDownReceiver(final MapView mapView,
                                   final Context context) {
        super(mapView);
        this.pluginContext = context;
        this.appContext = mapView.getContext();

        LayoutInflater inflater = LayoutInflater.from(pluginContext);
        ll = (LinearLayout) inflater.inflate(R.layout.c5_browser, null);

        // must be created using the application context otherwise this will fail
        this.htmlViewer = new WebView(mapView.getContext());
        this.htmlViewer.setVerticalScrollBarEnabled(true);
        this.htmlViewer.setHorizontalScrollBarEnabled(true);

        WebSettings webSettings = this.htmlViewer.getSettings();

        // do not enable per security guidelines
        //webSettings.setAllowFileAccessFromFileURLs(true);
        //webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.htmlViewer.setWebChromeClient(new ChromeClient());

        // cause subsequent calls to loadData not to fail - without this
        // the web view would remain inconsistent on subsequent concurrent opens
        this.htmlViewer.loadUrl("about:blank");
        this.htmlViewer.setWebViewClient(new Client());

        htmlViewer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        ll.addView(htmlViewer);

    }

    public class Client extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "started retrieving: " + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverride: " + url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "ended retrieving: " + url);
            if (url.contains("https://accounts.cradlepointecm.com") || url.contains("https://www.cradlepointecm.com")) {
                String cookie = CookieManager.getInstance().getCookie(url);
                if (cookie == null)
                    return;
                SBPDUtil.setCode5GroupCookie(cookie);
                Log.d(TAG, "GRABBED CRADLEPOINT COOKIE: " + SBPDUtil.getCode5GroupCookie());
                if (SBPDUtil.isGoodCookie()) {
                    DropDownManager.getInstance().hidePane();
                    ll.removeAllViews();
                    NotificationUtil.getInstance().postNotification(com.atakmap.app.R.drawable.phone, "Cradlepoint NCM Authenticated", "SBPD Cradlepoint NCM Authenticated, re-open plugin to load trackers.", "SBPD Cradlepoint NCM Authenticated, re-open plugin to load trackers.");
                }
            }
            super.onPageFinished(view, url);
        }
    }

    private static class ChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d(TAG, consoleMessage.message() + " -- From line "
                    + consoleMessage.lineNumber() + " of "
                    + consoleMessage.sourceId());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "loading progress: " + newProgress);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null && action.equals(SHOW_WEBVIEW)) {
            showDropDown(ll, HALF_WIDTH, FULL_HEIGHT,
                    FULL_WIDTH, HALF_HEIGHT, false, this);
            this.htmlViewer.loadUrl("about:blank");
            this.htmlViewer.loadUrl("https://accounts.cradlepointecm.com/#/login");
        }
        if (action != null && action.equals(HIDE_WEBVIEW)) {
            ll.removeAllViews();
        }
    }

    @Override
    public void disposeImpl() {
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