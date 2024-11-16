package com.example.tp_retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("items")
    Call<List<Item>> getItems();

    @POST("items")
    Call<Item> addItem(@Body Item item);

    @PUT("items/{id}")
    Call<Item> updateItem(@Path("id") int id, @Body Item item);

    @DELETE("items/{id}")
    Call<Void> deleteItem(@Path("id") int id);
}
