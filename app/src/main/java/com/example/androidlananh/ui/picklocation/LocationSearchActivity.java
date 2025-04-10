package com.example.androidlananh.ui.picklocation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


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

// Add these imports
import com.example.androidlananh.adapter.CustomSearchAdapter;
import com.example.androidlananh.databinding.ActivityLocationSearchBinding;
import com.example.androidlananh.model.Location;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.uppost.UpPostPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class LocationSearchActivity extends BaseActivity<LocationSearchPresenter> implements LocationSearchView {
    private GoogleMap mMap;
    private ActivityLocationSearchBinding binding;
    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private CustomSearchAdapter searchAdapter;  // Change this line
    private Location location;
    Handler searchHandler = new Handler(Looper.getMainLooper());
    Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            String query = binding.searchEditText.getText().toString();
            if (query.length() >= 3) {
                presenter.searchLocations(query);
            }
        }
    };

    @NonNull
    @Override
    protected LocationSearchPresenter createPresenter() {
        return new LocationSearchPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationSearchBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        checkLocationPermission();

        setupSearchAdapter();
        setupOnClick();

    }

    private void setupOnClick() {
        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        binding.Confirm.setOnClickListener(v -> {
            if (location != null) {
                if (location.getAddress().isEmpty()) {
                    showError("Không thể chọn địa chỉ, Vui lòng đổi địa chỉ");
                }
                else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("search_result", location); // Now using Serializable
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
            else{
                showError("Không thể chọn địa chỉ, Vui lòng đổi địa chỉ");
            }

        });
        binding.searchEditText.setOnItemClickListener((parent, view, position, id) -> {
            Location result = (Location) parent.getItemAtPosition(position);
            GeoPoint point = new GeoPoint(result.getLatitude(), result.getLongitude());
            showLocationPoint(point);
            binding.searchEditText.dismissDropDown();
        });
    }

    private void checkLocationPermission() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(binding.map.getId());
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                Toast.makeText(this, "Location permission is required to use the map", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    private void setupSearchAdapter() {
        searchAdapter = new CustomSearchAdapter(this, android.R.layout.simple_dropdown_item_1line);
        binding.searchEditText.setAdapter(searchAdapter);
        binding.searchEditText.setThreshold(1);

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAdapter.getFilter().filter(s);
                searchHandler.removeCallbacks(searchRunnable);
                searchHandler.postDelayed(searchRunnable, 1000); // 1 second delay
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(() -> {
            LatLng center = mMap.getCameraPosition().target;
            showLocationPoint(new GeoPoint(center.latitude, center.longitude));
            presenter.showLocationAddress(new GeoPoint(center.latitude, center.longitude));
        });
        setupMap();
    }

    private void setupMap() {
        LatLng hanoi = new LatLng(21.0285, 105.8542);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoi, 15));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (locationManager != null) {
                android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
            }
        }
    }

    private void showLocationPoint(GeoPoint point) {
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void searchLocationSuccess(List<Location> LocationList) {
        searchAdapter.setItems(LocationList);
        if (!LocationList.isEmpty()) {
            binding.searchEditText.showDropDown();
        }
    }

    @Override
    public void showLocationAddress(Location Location) {
        this.location = Location;
        String address=Location.getAddress();
        if(address.isEmpty()){
            address="Không xác định được địa chỉ";
        }
        binding.tvAddress.setText(address);

    }

    @Override
    public void updateMapCameraView(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}