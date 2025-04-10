package com.example.androidlananh.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.androidlananh.R;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.ProductRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearbyProductsMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ProductRepository productRepository;
    private FusedLocationProviderClient fusedLocationClient;
    private CardView productInfoCard;
    private ImageView productImage;
    private TextView productName, productDescription, productDistance;
    private Map<Marker, Product> markerProductMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_products_map);

        productRepository = new ProductRepository();
        markerProductMap = new HashMap<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initializeViews();
        setupMap();
    }

    private void initializeViews() {
        productInfoCard = findViewById(R.id.productInfoCard);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productDistance = findViewById(R.id.productDistance);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocationAndLoadProducts();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        mMap.setOnMarkerClickListener(marker -> {
            Product product = markerProductMap.get(marker);
            if (product != null) {
                showProductInfo(product);
            }
            return true;
        });

        mMap.setOnMapClickListener(latLng -> productInfoCard.setVisibility(View.GONE));
    }

    private void getCurrentLocationAndLoadProducts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    loadNearbyProducts(location);
                }
            });
        }
    }

    private void loadNearbyProducts(Location location) {
        productRepository.getNearbyProducts(
                location.getLatitude(),
                location.getLongitude(),
                50, // 50km radius
                new ApiCallback<ArrayList<Product>>() {
                    @Override
                    public void onSuccess(ArrayList<Product> products) {
                        runOnUiThread(() -> {
                            for (Product product : products) {
                                addMarkerForProduct(product);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error
                    }
                });
    }

    private void addMarkerForProduct(Product product) {
        if (product.getLocation() != null ) {
            LatLng position = new LatLng(
                    product.getLocation().getLatitude(),
                    product.getLocation().getLongitude()
            );
            
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(product.getName()));
            
            if (marker != null) {
                markerProductMap.put(marker, product);
            }
        }
    }

    private void showProductInfo(Product product) {
        productInfoCard.setVisibility(View.VISIBLE);
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(this)
                    .load(product.getImage())
                    .into(productImage);
        }

        // Calculate and show distance
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null && product.getLocation() != null ) {
                    float[] results = new float[1];
                    Location.distanceBetween(
                            location.getLatitude(),
                            location.getLongitude(),
                            product.getLocation().getLatitude(),
                            product.getLocation().getLongitude(),
                            results
                    );
                    String distance = String.format("%.1f km", results[0] / 1000);
                    productDistance.setText(distance);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocationAndLoadProducts();
                }
            }
        }
    }
}