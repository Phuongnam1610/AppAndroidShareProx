package com.example.androidlananh.model;

import java.io.Serializable;

import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {
    private String id="";
    private String image="";
    private String name="";
    private String description="";
    private String type="";
    private int count=-1;
    private String unit="";
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location Location) {
        this.location = Location;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String authorID;

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Product() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("image", image);
        map.put("name", name);
        map.put("description", description);
        map.put("type", type);
        map.put("count", count);
        map.put("unit", unit);
        map.put("authorID", authorID);
        if (location != null) {
            Map<String, Object> locationMap = location.toMap();
            map.put("location", locationMap);
        }
        return map;
    }

    public static Product fromDocument(DocumentSnapshot document) {
        if (document == null) return null;
        Product product = new Product();
        product.setId(document.getString("id"));
        product.setImage(document.getString("image"));
        product.setName(document.getString("name"));
        product.setDescription(document.getString("description"));
        product.setType(document.getString("type"));
        product.setCount(document.getLong("count") != null ? document.getLong("count").intValue() : -1);
        product.setUnit(document.getString("unit"));
        product.setAuthorID(document.getString("authorID"));
        
        // Get location data
        Map<String, Object> locationMap = (Map<String, Object>) document.get("location");
        if (locationMap != null) {
            Location location = Location.fromMap(locationMap);
            product.setLocation(location);
        }
        
        return product;
    }
}