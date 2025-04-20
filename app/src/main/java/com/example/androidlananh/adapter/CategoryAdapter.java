package com.example.androidlananh.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidlananh.databinding.ItemCategoryBinding;
import com.example.androidlananh.model.Category;
import com.example.androidlananh.ui.base.BaseView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private CategoryAdapterListener view;
    private ArrayList<Category> originalList=new ArrayList<>();
    public CategoryAdapter(CategoryAdapterListener view) {
        this.view=view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(((BaseView)view).getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = originalList.get(position);
        holder.binding.tvNameCategory.setText(category.getTitle());
        holder.binding.getRoot().setOnClickListener(v -> {
            view.onClickCategory(category);
        });
        Glide.with(((BaseView)view).getContext()).load(category.getImg()).into(holder.binding.imvCategory);
    }

    @Override
    public int getItemCount() {

        return originalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;


        public ViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



    public void  loadAllCategory(ArrayList<Category> newList) {
        originalList.clear();
        originalList.addAll(newList);

        notifyDataSetChanged();
    }


}
