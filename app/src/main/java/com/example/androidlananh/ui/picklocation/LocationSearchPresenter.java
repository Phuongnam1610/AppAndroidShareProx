package com.example.androidlananh.ui.picklocation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.androidlananh.model.Location;
import com.example.androidlananh.ui.base.BasePresenter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationSearchPresenter extends BasePresenter<LocationSearchView> {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final Handler handler = new Handler(Looper.getMainLooper());
    // Add tracking variables
    private Runnable currentSearchTask;
    private Runnable currentReverseGeocodingTask;

    protected LocationSearchPresenter(LocationSearchView view) {
        super(view);

    }



    public void searchLocations(String query) {
        // Cancel previous search if exists
        if (currentSearchTask != null) {
            handler.removeCallbacks(currentSearchTask);
        }

        Runnable searchTask = () -> {
            try {
                String encodedQuery = URLEncoder.encode(query, "UTF-8");
                URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + encodedQuery);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                Log.d("responseSearch",reader.toString());
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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("location",Locations.toString());
                            view.searchLocationSuccess(Locations);
                        } catch (Exception e) {

                        }                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                currentSearchTask = null;
            }
        };

        currentSearchTask = searchTask;
        executor.execute(searchTask);
    }

    public void showLocationAddress(GeoPoint point) {
        // Cancel previous reverse geocoding if exists
        if (currentReverseGeocodingTask != null) {
            handler.removeCallbacks(currentReverseGeocodingTask);
        }

        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        view.updateMapCameraView(latLng);

        Runnable reverseGeocodingTask = () -> {
            try {
                URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=" +
                        point.getLatitude() + "&lon=" + point.getLongitude());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.d("responseShow",reader.toString());

                StringBuilder response = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject result = new JSONObject(response.toString());
                String address = result.getString("display_name");
                Location Location = new Location(address, point.getLatitude(), point.getLongitude());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("address",address);
                            view.showLocationAddress(Location);
                        } catch (Exception e) {

                        }                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                currentReverseGeocodingTask = null;
            }
        };

        currentReverseGeocodingTask = reverseGeocodingTask;
        executor.execute(reverseGeocodingTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel ongoing tasks
        if (currentSearchTask != null) {
            handler.removeCallbacks(currentSearchTask);
            currentSearchTask = null;
        }
        if (currentReverseGeocodingTask != null) {
            handler.removeCallbacks(currentReverseGeocodingTask);
            currentReverseGeocodingTask = null;
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
