    package com.example.androidlananh.model;

    import com.firebase.geofire.GeoFireUtils;
    import com.firebase.geofire.GeoLocation;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.GeoPoint;

    import java.io.Serializable;
    import java.util.HashMap;
    import java.util.Map;

    public class Location implements Serializable {
        private static final long serialVersionUID = 1L;
        private String address="";
        private double latitude;
        private double longitude;


        public void setAddress(String address) {
            this.address = address;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }


        public Location(String address, double latitude, double longitude) {
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return address;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("address", address);
            String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));
            Map<String, Object> updates = new HashMap<>();
            updates.put("geohash", hash);
            updates.put("lat", latitude);
            updates.put("lng", longitude);
            map.put("coordinates", updates);
            return map;
        }

        public static Location fromDocument(DocumentSnapshot document) {
            String address = document.getString("address");
            GeoPoint geoPoint = document.getGeoPoint("point");
            if (geoPoint != null) {
                return new Location(address, geoPoint.getLatitude(), geoPoint.getLongitude());
            }
            return null;
        }
        public static Location fromMap(Map<String,Object> map) {
            String address = map.get("address").toString();
            Map<String,Object> coordinates=(Map<String,Object>) map.get("coordinates");
            return new Location(address,(double) coordinates.get("lat"), (double)coordinates.get("lng"));
        }
        public  Location(){

        }
    }