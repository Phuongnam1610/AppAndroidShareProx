package com.example.androidlananh.data;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageApiService {
    @Headers({
            "Accept: application/json",
            "Authorization: Basic private_HKeTt73Y960r9/bjpK78aRqF0sU="
    })
    @GET("v1/files/{id}/details")
    Call<ImageApiResponse> getFileUrl(@Path("id") String id);

    @Headers({
            "Authorization: Basic cHJpdmF0ZV9IS2VUdDczWTk2MHI5L2JqcEs3OGFScUYwc1U9Og=="
    })
    @POST("/api/v1/files/upload")
    @Multipart
    Call<ImageApiResponse> uploadFile(@Part MultipartBody.Part file, @Part("fileName")RequestBody fileName);
}
