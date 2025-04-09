package com.example.androidlananh.data;

import android.util.Log;

import com.example.androidlananh.repository.ApiCallback;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageRepository {

    static void getUrlFile(String fileId, ApiCallback<String> apiCallback) {
        Call<ImageApiResponse> call = NetworkModule.imageApi.getFileUrl(fileId);
        call.enqueue(new Callback<ImageApiResponse>() {
            @Override
            public void onResponse(Call<ImageApiResponse> call, Response<ImageApiResponse> response) {
                if (response.isSuccessful()) {
                    apiCallback.onSuccess(response.body().getUrl());
                } else {
                    apiCallback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ImageApiResponse> call, Throwable t) {
                apiCallback.onError(t.getMessage());
            }
        });
    }

    public static void uploadUrlFile(File file,String fileName, ApiCallback<String> apiCallback)  {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody fileNameBody = RequestBody.create(
                MediaType.parse("text/plain"),fileName
        );
        Call<ImageApiResponse> call = NetworkModule.imageApi.uploadFile(body,fileNameBody);
        call.enqueue(new Callback<ImageApiResponse>() {
            @Override
            public void onResponse(Call<ImageApiResponse> call, Response<ImageApiResponse> response) {
                if (response.isSuccessful()) {
                    apiCallback.onSuccess(response.body().getUrl());
                } else {
                    Log.d("Error","Error: "+ response.toString());
                    apiCallback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ImageApiResponse> call, Throwable t) {
                Log.d("Error","Error: "+ t.toString());

                apiCallback.onError(t.getMessage());
            }
        });
    }
}
