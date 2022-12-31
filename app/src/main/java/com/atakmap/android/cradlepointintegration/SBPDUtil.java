package com.atakmap.android.cradlepointintegration;

import static com.atakmap.android.maps.MapView.getMapView;

import android.os.SystemClock;

import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.Marker;
import com.atakmap.android.user.PlacePointTool;
import com.atakmap.coremap.log.Log;
import com.atakmap.coremap.maps.coords.GeoPoint;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class Cradlepoint {
    String routerId;
    String routerName;
    String routerState;
    String lat;
    String lng;

    public Cradlepoint(String routerId, String routerName, String routerState, String lat, String lng) {
        this.routerId = routerId;
        this.routerName = routerName;
        this.routerState = routerState;
        this.lat = lat;
        this.lng = lng;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterState() {
        return routerState;
    }

    public void setRouterState(String routerState) {
        this.routerState = routerState;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}

public class SBPDUtil {
    public static final String TAG = SBPDUtil.class.getSimpleName();

    public static String code5GroupCookie = null;

    public static HashMap<String, Cradlepoint> cradlepointTrackerList = new HashMap<>();

    public SBPDUtil(String code5GroupCookie) {
        this.code5GroupCookie = code5GroupCookie;
    }

    public static String getCode5GroupCookie() {
        return code5GroupCookie;
    }

    public static void setCode5GroupCookie(String cookie) {
       code5GroupCookie = cookie;
    }

    public static boolean isGoodCookie() {
        if (code5GroupCookie == null) {
            Log.d(TAG, "BAD COOKIE ABORT");
            return false;
        }
        if (code5GroupCookie.contains("sessionid")) {
            try {
                String jsonUrl = "https://geoview.cradlepointecm.com/api/v1/current_locations?parentAccount=55523";
                DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                f.setNamespaceAware(false);
                f.setValidating(false);
                URL obj = new URL(jsonUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(true);
                urlConnection.setRequestProperty("authority", "geoview.cradlepointecm.com");
                urlConnection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                urlConnection.setRequestProperty("accept-encoding", "gzip, deflate, br");
                urlConnection.setRequestProperty("accept-language", "en-US,en;q=0.9");
                urlConnection.setRequestProperty("cache-control", "max-age=0");
                urlConnection.setRequestProperty("cookie", code5GroupCookie);
                urlConnection.setRequestProperty("dnt", "1");
                urlConnection.setRequestProperty("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Microsoft Edge\";v=\"103\", \"Chromium\";v=\"103\"");
                urlConnection.setRequestProperty("sec-ch-ua-platform", "Windows");
                urlConnection.setRequestProperty("sec-fetch-dest", "document");
                urlConnection.setRequestProperty("sec-fetch-mode", "navigate");
                urlConnection.setRequestProperty("sec-fetch-site", "none");
                urlConnection.setRequestProperty("sec-fetch-user", "?1");
                urlConnection.setRequestProperty("upgrade-insecure-requests:", "1");
                urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.114 Safari/537.36 Edg/103.0.1264.62");
                urlConnection.connect();
                Log.d(TAG, "GOOD COOKIE RESPONSE CODE" + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
                if (urlConnection.getResponseCode() == 200) {
                    return true;
                }
            } catch (Exception e) {
                Log.e(TAG, "error", e);
            }
        }
        return false;
    }

    public static void updateTrackers() {
        try {
            String jsonUrl = "https://geoview.cradlepointecm.com/api/v1/current_locations?parentAccount=55523";
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setNamespaceAware(false);
            f.setValidating(false);
            URL obj = new URL(jsonUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(true);
            urlConnection.setRequestProperty("authority", "geoview.cradlepointecm.com");
            urlConnection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            urlConnection.setRequestProperty("accept-encoding", "gzip, deflate, br");
            urlConnection.setRequestProperty("accept-language", "en-US,en;q=0.9");
            urlConnection.setRequestProperty("cache-control", "max-age=0");
            urlConnection.setRequestProperty("cookie", code5GroupCookie);
            urlConnection.setRequestProperty("dnt", "1");
            urlConnection.setRequestProperty("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Microsoft Edge\";v=\"103\", \"Chromium\";v=\"103\"");
            urlConnection.setRequestProperty("sec-ch-ua-platform", "Windows");
            urlConnection.setRequestProperty("sec-fetch-dest", "document");
            urlConnection.setRequestProperty("sec-fetch-mode", "navigate");
            urlConnection.setRequestProperty("sec-fetch-site", "none");
            urlConnection.setRequestProperty("sec-fetch-user", "?1");
            urlConnection.setRequestProperty("upgrade-insecure-requests:", "1");
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.114 Safari/537.36 Edg/103.0.1264.62");
            urlConnection.connect();
            StringBuilder result = new StringBuilder();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            GZIPInputStream gis = new GZIPInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            JSONObject cradlepointData = new JSONObject(result.toString());
            JSONArray arr = cradlepointData.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject cradlepointRouter = arr.getJSONObject(i).getJSONObject("attributes");
                String routerId = cradlepointRouter.getString("router_id");
                String routerName = cradlepointRouter.getString("router_name");
                String routerState = cradlepointRouter.getString("router_state");
                String lat = cradlepointRouter.getString("latitude");
                String lng = cradlepointRouter.getString("longitude");
                Log.d(TAG, routerId + " " + routerName + " " + routerState + " " + lat + " " + lng + "\n");
                Cradlepoint newCradlepoint = new Cradlepoint(routerId, routerName, routerState, lat, lng);
                if (newCradlepoint.routerState.equals("online"))
                    cradlepointTrackerList.put(newCradlepoint.routerId, newCradlepoint);
            }
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public static void plotTrackers() {
        if (cradlepointTrackerList.isEmpty())
            return;
        for (Map.Entry<String, Cradlepoint> entry : cradlepointTrackerList.entrySet()) {
            String key = entry.getKey();
            Cradlepoint gpsTracker = entry.getValue();

            double lat = Double.NaN, lon = Double.NaN;
            lat = Double.parseDouble(gpsTracker.lat);
            lon = Double.parseDouble(gpsTracker.lng);
            if (!Double.isNaN(lat) && !Double.isNaN(lon)) {
                final MapItem mi = getMapView().getMapItem(gpsTracker.routerId);
                if (mi != null) {
                    if (mi instanceof Marker) {
                        Marker marker = (Marker) mi;

                        GeoPoint newPoint = new GeoPoint(lat, lon);
                        GeoPoint lastPoint = ((Marker) mi).getPoint();
                        long currTime = SystemClock.elapsedRealtime();

                        double dist = lastPoint.distanceTo(newPoint);
                        double dir = lastPoint.bearingTo(newPoint);

                        double delta = currTime -
                                mi.getMetaLong("tracker.lastUpdateTime", 0);

                        double speed = dist / (delta / 1000f);

                        marker.setTrack(dir, speed);

                        marker.setPoint(newPoint);
                        mi.setMetaLong("tracker.lastUpdateTime",
                                SystemClock.elapsedRealtime());
                        //marker.setSummary("Battery: " + gpsTracker.batteryPercent + " | Nearest Address: " + gpsTracker.closestAddress);
                    }
                } else {
                    PlacePointTool.MarkerCreator mc = new PlacePointTool.MarkerCreator(
                            new GeoPoint(lat, lon));
                    mc.setUid(gpsTracker.routerId);
                    mc.setCallsign(gpsTracker.routerName);
                    mc.setType("a-f-G-I-G");
                    mc.showCotDetails(false);
                    mc.setNeverPersist(true);
                    Marker m = mc.placePoint();
                    // don't forget to turn on the arrow so that we know where the ISS is going
                    m.setStyle(Marker.STYLE_ROTATE_HEADING_MASK);
                    m.setMetaBoolean("movable", false);
                    m.setMetaString("how", "m-g");
                }
            }
        }
    }
}
