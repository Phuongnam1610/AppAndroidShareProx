package com.example.androidlananh.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidlananh.databinding.ItemProductBinding;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.ui.base.BaseView;
import com.example.androidlananh.utils.Constant;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ProductAdapterListener view;
    private ArrayList<Product> originalList = new ArrayList<>();
    private ArrayList<Product> filteredList = new ArrayList<>();

    public ProductAdapter(ProductAdapterListener view) {
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(((BaseView) view).getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Product product = filteredList.get(position);
//        holder.binding.imvProduct=
            holder.binding.tvNameProduct.setText(product.getName());
            holder.binding.getRoot().setOnClickListener(v -> {
                view.onClickProduct(product);
            });
            holder.binding.getRoot().setOnLongClickListener(v->{
                view.onLongClickProduct(product);
                return false;
            });
            if (product.getType().equals(Constant.TYPE_SHARE)) {
                holder.binding.imvProduct.setVisibility(VISIBLE);
                holder.binding.tvReason.setVisibility(GONE);
                Glide.with(((BaseView) view).getContext()).load(product.getImage()).into(holder.binding.imvProduct);

            } else {
                holder.binding.tvReason.setVisibility(VISIBLE);
                holder.binding.imvProduct.setVisibility(GONE);
                holder.binding.tvReason.setText(product.getReason());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {

        return filteredList.size();
    }

    public void deleteProduct(String id) {
        // Find position in original list
        int position = -1;
        for (int i = 0; i < originalList.size(); i++) {
            if (originalList.get(i).getId().equals(id)) {
                position = i;
                break;
            }
        }
        
        if (position != -1) {
            // Remove from original list
            originalList.remove(position);
            
            // Find and remove from filtered list
            for (int i = 0; i < filteredList.size(); i++) {
                if (filteredList.get(i).getId().equals(id)) {
                    filteredList.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;


        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void filterList(String searchKey) {
        if (searchKey.isEmpty()) {
            filteredList = new ArrayList<>(originalList);
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


    public void loadAllProduct(ArrayList<Product> newList) {
        originalList.clear();
        originalList.addAll(newList);
        filteredList.clear();
        filteredList.addAll(newList);
        notifyDataSetChanged();
    }


}
