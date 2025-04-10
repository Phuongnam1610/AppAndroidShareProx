package com.example.androidlananh.ui.picklocation;

import android.net.Uri;

import com.example.androidlananh.model.Location;
import com.example.androidlananh.ui.base.BaseView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface LocationSearchView extends BaseView, OnMapReadyCallback {
    void searchLocationSuccess(List<Location> LocationList);
    void showLocationAddress(Location Location);
    void updateMapCameraView(LatLng latLng);

}
