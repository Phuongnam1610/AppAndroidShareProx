package com.example.androidlananh.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.androidlananh.model.Location;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CustomSearchAdapter extends ArrayAdapter<Location> implements Filterable {
    private List<Location> allItems;
    private List<Location> filteredItems;
    private Context context;

    public CustomSearchAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.allItems = new ArrayList<>();
        this.filteredItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public Location getItem(int position) {
        return filteredItems.get(position);
    }

    public void setItems(List<Location> items) {
        allItems = new ArrayList<>(items);
        filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    private String normalizeText(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.toLowerCase();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Location> filtered = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filtered.addAll(allItems);
                } else {
                    String normalizedConstraint = normalizeText(constraint.toString());
                    for (Location item : allItems) {
                        String normalizedItem = normalizeText(item.getAddress());
                        if (normalizedItem.contains(normalizedConstraint)) {
                            filtered.add(item);
                        }
                    }
                }

                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (List<Location>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}