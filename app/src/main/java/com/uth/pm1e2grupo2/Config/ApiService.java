package com.uth.pm1e2grupo2.Config;

import com.uth.pm1e2grupo2.models.Contactos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    // Método para obtener todos los contactos
    @GET("contactos")
    Call<List<Contactos>> getContactos();

    // Método para obtener un contacto específico por su ID
    @GET("contactos/{id}")
    Call<Contactos> getContacto(@Path("id") int id);
}
