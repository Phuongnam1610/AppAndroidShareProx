package com.example.androidlananh.ui.picklocation;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.example.androidlananh.model.Location;
import com.example.androidlananh.ui.base.BasePresenter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationSearchPresenter extends BasePresenter<LocationSearchView> {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    protected LocationSearchPresenter(LocationSearchView view) {
        super(view);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void searchLocations(String query) {
        executor.execute(() -> {
            try {
                String encodedQuery = URLEncoder.encode(query, "UTF-8");
                URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + encodedQuery);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "AndroidApp");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray results = new JSONArray(response.toString());
                List<Location> Locations = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    Locations.add(new Location(
                            result.getString("display_name"),
                            result.getDouble("lat"),
                            result.getDouble("lon")
                    ));
                }

                handler.post(() -> {
                    try {
                        view.searchLocationSuccess(Locations);
                    } catch (Exception e) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showLocationAddress(GeoPoint point) {
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        view.updateMapCameraView(latLng);
        // Get address from coordinates
        executor.execute(() -> {
            try {
                URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=" +
                        point.getLatitude() + "&lon=" + point.getLongitude());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "AndroidApp");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject result = new JSONObject(response.toString());
                String address = result.getString("display_name");
                Location Location = new Location(address, point.getLatitude(), point.getLongitude());

                handler.post(() -> {
                    try {
                        view.showLocationAddress(Location);
                    } catch (Exception e) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        });
    }


}
