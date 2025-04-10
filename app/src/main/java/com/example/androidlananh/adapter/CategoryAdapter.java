package com.example.androidlananh.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidlananh.databinding.ItemCategoryBinding;
import com.example.androidlananh.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private CategoryAdapterView view;
    private ArrayList<Category> originalList=new ArrayList<>();
    private ArrayList<Category> filteredList=new ArrayList<>();
    public CategoryAdapter(CategoryAdapterView view) {
        this.view=view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(view.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = filteredList.get(position);
        holder.binding.tvNameCategory.setText(category.getTitle());
        holder.binding.getRoot().setOnClickListener(v -> {
            view.onClickCategory(category);
        });
        Glide.with(view.getContext()).load(category.getImage()).into(holder.binding.imvCategory);
    }

    @Override
    public int getItemCount() {

        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;


        public ViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void filterList(String searchKey) {
        if(searchKey.isEmpty()){
            filteredList=new ArrayList<>(originalList);
            return;
        }
        ArrayList<Category> filterList = new ArrayList<>();
        for (Category category : originalList) {
            if (category.getTitle().toLowerCase().contains(searchKey.toLowerCase())) {
                filterList.add(category);
            }
        }
        filteredList.clear();
        filteredList.addAll(filterList);
        notifyDataSetChanged();
    }



    public void  loadAllCategory(ArrayList<Category> newList) {
        originalList.clear();
        originalList.addAll(newList);
        filteredList.clear();
        filteredList.addAll(newList);
        notifyDataSetChanged();
    }


}
