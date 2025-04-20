package com.example.androidlananh.ui.uppost;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.L;
import com.example.androidlananh.databinding.ActivityUpPostBinding;
import com.example.androidlananh.model.Category;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.Location;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.picklocation.LocationSearchActivity;
import com.example.androidlananh.utils.Constant;
import com.example.androidlananh.utils.SessionManager;

import java.util.ArrayList;
import java.util.Collections;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;

public class UpPostActivity extends BaseActivity<UpPostPresenter> implements UpPostView {
    private ActivityUpPostBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> locationPickerLauncher;
    private Uri selectedImageUri;
    private Location location = new Location("", 0, 0);
    private String type = "";

    private String categoryId="";

    private void setupLocationPicker() {
        locationPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        location = (Location) result.getData().getSerializableExtra("search_result");
                        if(location!=null){
                            binding.tvAddress.setText(location.getAddress());
                        }
                    }
                }
        );
    }

    public void openLocationPicker() {
        Intent intent = new Intent(this,LocationSearchActivity.class);
        locationPickerLauncher.launch(intent);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            displayImageUri(selectedImageUri);
                            Log.d("imageuri", String.valueOf(selectedImageUri));
                        }

                    }
                }
        );
    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }


    @NonNull
    @Override
    protected UpPostPresenter createPresenter() {
        return new UpPostPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewBinding();
        setupEdgeToEdge();
        setupWindowInsets();
        setupImagePicker();
        setupLocationPicker();
        setupOnClick();
        setupTypePost();
        presenter.getAllCategory();

    }

    private void setupOnClick() {
        binding.btnPickAddress.setOnClickListener(v -> {
            openLocationPicker();
        });
        binding.btnPost.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString();
            String description = binding.edtDescription.getText().toString();
            int count = -1;
            try     {
                count = Integer.parseInt(binding.edtNumber.getText().toString());
            } catch (Exception e) {
            }
            String unit = binding.edtUnit.getText().toString();
            String reason=binding.edtReason.getContext().toString();
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setAuthorID(SessionManager.getInstance().getCurrentUser().getId());
            product.setType(type);
            product.setLocation(location);
            product.setCategoryId(categoryId);
            if (selectedImageUri != null) {
                product.setImage(String.valueOf(selectedImageUri));
            }
            product.setCount(count);
            product.setUnit(unit);
            product.setReason(reason);
            presenter.addProduct(product);
        });
        binding.btnShare.setOnClickListener(v -> {
            type = Constant.TYPE_SHARE;
            binding.btnShare.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            binding.btnReceive.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            binding.frameImage.setVisibility(VISIBLE);
            binding.frameReason.setVisibility(GONE);
        });
        binding.btnReceive.setOnClickListener(v -> {
            type = Constant.TYPE_RECEIVE;
            binding.btnReceive.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            binding.btnShare.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            binding.frameImage.setVisibility(GONE);
            binding.frameReason.setVisibility(VISIBLE);
        });
        binding.imvProduct.setOnClickListener(v -> {
            openImagePicker();
        });
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupTypePost() {
        type = getIntent().getStringExtra(Constant.TYPE_PASS_KEY);
        if (type != null) {
            if (type.equals(Constant.TYPE_RECEIVE)) {
                binding.btnReceive.performClick();
                binding.btnShare.setVisibility(GONE);
            } else if (type.equals(Constant.TYPE_SHARE)) {

                binding.btnShare.performClick();
                binding.btnReceive.setVisibility(GONE);
            }
        }
        else{
            type="";
        }

    }


    private void initializeViewBinding() {
        binding = ActivityUpPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupEdgeToEdge() {
        EdgeToEdge.enable(this);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void showLoading() {
        binding.animationView.setVisibility(VISIBLE);
        binding.btnPost.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        binding.animationView.setVisibility(GONE);
        binding.btnPost.setEnabled(true);

    }


    @Override
    public void addProductSuccess() {
        finish();
    }

    @Override
    public void displayImageUri(Uri uri) {
        binding.imvProduct.setImageURI(uri);
    }

    @Override
    public void getAllCategories(ArrayList<Category> categories) {

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(getItem(position).getTitle());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(getItem(position).getTitle());
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);
        int indexDefault = 0;

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equalsIgnoreCase("khongro")) {
                indexDefault = i;
                break;
            }
        }
        binding.spinnerCategory.setSelection(indexDefault);
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                categoryId= selectedCategory.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryId = "";
            }
        });
    }
}