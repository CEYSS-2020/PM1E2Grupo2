package com.uth.pm1e2grupo2.rests;

import com.uth.pm1e2grupo2.callbacks.CallbackContactos;
import com.uth.pm1e2grupo2.callbacks.CallbackNewContacto;
import com.uth.pm1e2grupo2.models.Value;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "Data-Agent: PM1E2Grupo2";

    @Headers({CACHE, AGENT})
    @GET("api/allContactos")
    Call<CallbackContactos>  getContacts();

    @Multipart
    @POST("api/addContacto")
    Call<CallbackNewContacto> agregarContacto(
            @Part("avatar") RequestBody avatar,
            @Part("pais") RequestBody pais,
            @Part("nombre") RequestBody nombre,
            @Part("telefono") RequestBody telefono,
            @Part("nota") RequestBody nota,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part("video") RequestBody video
    );

    @Multipart
    @POST("api/updateContacto")
    Call<CallbackNewContacto> actualizarContacto(
            @Part("id") int id,
            @Part("avatar") RequestBody avatar,
            @Part("pais") RequestBody pais,
            @Part("nombre") RequestBody nombre,
            @Part("telefono") RequestBody telefono,
            @Part("nota") RequestBody nota,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part("video") RequestBody video
    );


    @FormUrlEncoded
    @POST("api/deleteContacto")
    Call<Value> eliminarContacto(
            @Field("id") int id
    );

}
