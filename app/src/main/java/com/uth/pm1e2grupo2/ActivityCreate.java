package com.uth.pm1e2grupo2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.uth.pm1e2grupo2.rests.ApiInterface;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityCreate extends AppCompatActivity {
    private static final String api_key = "$2y$10$tdBQmpWMQaf7XpCMNFphAuQMbFfSktcCR/W32BMsIJ41vhTo6jxBi";
    private Uri videoUri;
    Button btnGrabarVideo, btn_guardar_contacto, btn_contactos_guardados;
    EditText txt_nombre, txt_telefono, txt_latitud, txt_longitud;

    VideoView videoView;
    static final int peticion_camara = 100;
    static final int peticion_video = 102;
    static final int peticion_seleccionar_video = 104;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        btnGrabarVideo = findViewById(R.id.boton_de_grabar);
        btn_guardar_contacto = findViewById(R.id.btn_guardar_contacto);
        btn_contactos_guardados = findViewById(R.id.btn_contactos_guardados);

        txt_nombre = findViewById(R.id.txt_nombre);
        txt_telefono = findViewById(R.id.txt_telefono);
        txt_latitud = findViewById(R.id.txt_latitud);
        txt_longitud = findViewById(R.id.txt_longitud);

        videoView = findViewById(R.id.VideoView1);

        btnGrabarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarSeleccionDeVideo();
            }
        });

        btn_guardar_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = txt_nombre.getText().toString();
                String telefono = txt_telefono.getText().toString();
                double latitud = Double.parseDouble(txt_latitud.getText().toString());
                double longitud = Double.parseDouble(txt_longitud.getText().toString());

                String pais = "Honduras"; //hay que hacer el spinner lo deje en Honduras como en la base
                String nota = "Demo"; //hay     que agregar el campo de noas en el xml
                String avatarURL = "https://pm1e2grupo2.kadamy.app/storage/app/avatar/avatar.png"; //estoe ra una prueba
                String videoURL = videoUri.toString();
                String ubicacion = latitud + ", " + longitud;

                ///FORMUALRIO JSON PARA CREAR CONTACTP
                JsonObject contacto = new JsonObject();
                contacto.addProperty("pais", pais);
                contacto.addProperty("nombre", nombre);
                contacto.addProperty("telefono", telefono);
                contacto.addProperty("nota", nota);
                contacto.addProperty("latitud", latitud);
                contacto.addProperty("longitud", longitud);
                contacto.addProperty("avatar", avatarURL);
                contacto.addProperty("video", videoURL);
                contacto.addProperty("ubicacion", ubicacion);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://pm1e2grupo2.kadamy.app")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                Call<Void> call = apiInterface.agregarContacto(api_key, contacto);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            showMessage("Contacto agregado exitosamente");
                        } else {
                            showMessage("Error al agregar contacto");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showMessage("Error en el llamado");
                    }
                });
            }
        });

        btn_contactos_guardados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaContactos();
            }
        });
    }


    //METODOS
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void ListaContactos() {
        Intent intent = new Intent(this, ActivityLista.class);
        startActivity(intent);
    }

    private void mostrarSeleccionDeVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar opción");
        builder.setMessage("¿Cómo desea seleccionar el video?");
        builder.setPositiveButton("Grabar nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Permisos();
            }
        });
        builder.setNegativeButton("Seleccionar de la galería", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                abrirAlmacenamiento();
            }
        });
        builder.show();
    }

    ////////////////////////////// METODOS PARA EL VIDEO ////////////////////////////////


    private void Permisos() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, peticion_camara);

        } else {
            tomarVideo();
        }
    }


    private void tomarVideo() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 2);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, (Uri) null); // Evitar que el video se guarde automáticamente en la galería
            startActivityForResult(intent, peticion_video);
        } else {
            Toast.makeText(this, "No se encontró una aplicación para manejar la grabación de video", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == peticion_camara) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarVideo();
            } else {
                Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == peticion_video && resultCode == RESULT_OK) {

            this.videoUri = data.getData();
            videoView.setVideoURI(this.videoUri);
            videoView.start();

        } else if (requestCode == peticion_seleccionar_video && resultCode == RESULT_OK) {
            Uri selectedVideoUri = data.getData();
            videoView.setVideoURI(selectedVideoUri);
            videoView.start();
        }
    }

    private void abrirAlmacenamiento() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        startActivityForResult(intent, peticion_seleccionar_video);
    }


}
