package com.uth.pm1e2grupo2.rests;

import com.google.gson.JsonObject;
import com.uth.pm1e2grupo2.callbacks.CallbackContactos;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "Data-Agent: PM1E2Grupo2";
    String ContentType = "Content-Type: application/json; charset=UTF-8";

    @Headers({CACHE, AGENT})
    @GET("api/allContactos")
    Call<CallbackContactos> getContactos(
            @Query("api-key") String api_key
    );

    ///Demas apis

    @Headers({CACHE, AGENT, ContentType})
    @POST("api/addContacto")
    Call<Void> agregarContacto(
            @Query("api-key") String apiKey,
            @Body JsonObject body
    );

    @Headers({CACHE, AGENT, ContentType})
    @POST("api/updateContacto")
    Call<Void> actualizarContacto(
            @Query("api-key") String apiKey,
            @Body JsonObject body
    );

    @Headers({CACHE, AGENT})
    @DELETE("api/deleteContacto")
    Call<Void> eliminarContacto(
            @Query("id") int id, @Query("api-key") String apiKey
    );

}
