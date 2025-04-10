package com.example.androidlananh.ui.dialoginputpass;

import static android.view.View.GONE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.androidlananh.R;
import com.example.androidlananh.databinding.DialogInputPasswordBinding;

public class DialogInputPassword extends DialogFragment {
    
    private DialogInputPasswordBinding binding;
    private PasswordListener passwordListener;

    public interface PasswordListener {
        void onPasswordEntered(String password);
        void onClose();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            passwordListener = (PasswordListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PasswordListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=DialogInputPasswordBinding.inflate(inflater);
        binding.btnConfirm.setOnClickListener(v->{
            binding.animationView.setVisibility(View.VISIBLE);
            passwordListener.onPasswordEntered(binding.edtPassword.getText().toString());

        });
        binding.btnClose.setOnClickListener(v->{
            passwordListener.onClose();
        });

        return binding.getRoot();
    }

    public void hideLoading(){
        binding.animationView.setVisibility(GONE);
    }

}
