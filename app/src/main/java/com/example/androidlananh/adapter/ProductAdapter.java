package com.example.androidlananh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidlananh.R;
import com.example.androidlananh.databinding.ItemProductBinding;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ProductAdapterView view;
    private ArrayList<Product> originalList=new ArrayList<>();
    private ArrayList<Product> filteredList=new ArrayList<>();
    public ProductAdapter(ProductAdapterView view) {
        this.view=view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(view.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = filteredList.get(position);
//        holder.binding.imvProduct=
        holder.binding.tvNameProduct.setText(product.getName());
        holder.binding.getRoot().setOnClickListener(v -> {
            view.onClickProduct(product);
        });
        Glide.with(view.getContext()).load(product.getImage()).into(holder.binding.imvProduct);
    }

    @Override
    public int getItemCount() {

        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;


        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void filterList(String searchKey) {
        if(searchKey.isEmpty()){
            filteredList=new ArrayList<>(originalList);
            return;
        }
        ArrayList<Product> filterList = new ArrayList<>();
        for (Product product : originalList) {
            if (product.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                filterList.add(product);
            }
        }
        filteredList.clear();
        filteredList.addAll(filterList);
        notifyDataSetChanged();
    }



    public void  loadAllProduct(ArrayList<Product> newList) {
        originalList.clear();
        originalList.addAll(newList);
        filteredList.clear();
        filteredList.addAll(newList);
        notifyDataSetChanged();
    }


}
