package com.uth.pm1e2grupo2;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.uth.pm1e2grupo2.Config.ApiService;
import com.uth.pm1e2grupo2.models.Contactos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityDetalles extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtTelefono;
    private EditText txtLatitud;
    private EditText txtLongitud;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pm1e2grupo2.kadamy.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Inicializar los campos de texto
        txtNombre = findViewById(R.id.txt_nombre);
        txtTelefono = findViewById(R.id.txt_telefono);
        txtLatitud = findViewById(R.id.txt_latitud);
        txtLongitud = findViewById(R.id.txt_longitud);

        // Obtener el ID del contacto específico
        int idContacto = getIntent().getIntExtra("idContacto", 1);

        // Hacer una solicitud para obtener el contacto específico
        obtenerContactos(idContacto);
    }

    private void obtenerContactos(int idContacto) {
        Call<Contactos> call = apiService.getContacto(idContacto);
        call.enqueue(new Callback<Contactos>() {
            @Override
            public void onResponse(Call<Contactos> call, Response<Contactos> response) {
                if (response.isSuccessful()) {
                    Contactos contacto = response.body();
                    if (contacto != null) {
                        // Actualizar los campos de texto con la información del contacto
                        txtNombre.setText(contacto.getNombre());
                        txtTelefono.setText(contacto.getTelefono());
                        txtLatitud.setText(String.valueOf(contacto.getLatitud()));
                        txtLongitud.setText(String.valueOf(contacto.getLongitud()));
                    } else {
                        Toast.makeText(ActivityDetalles.this, "No se encontró el contacto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityDetalles.this, "Error al obtener el contacto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contactos> call, Throwable t) {
                Toast.makeText(ActivityDetalles.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
