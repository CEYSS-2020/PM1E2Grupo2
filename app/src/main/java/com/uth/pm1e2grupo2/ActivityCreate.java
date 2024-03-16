package com.uth.pm1e2grupo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;

public class ActivityCreate extends AppCompatActivity {


    Button btn_grabar, btn_guardar_contacto, btn_contactos_guardados;
    EditText txt_nombre, txt_telefono, txt_latitud, txt_longitud;
    SurfaceView surfaceView_video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        btn_grabar = (Button) findViewById(R.id.btn_grabar);
        btn_guardar_contacto = (Button) findViewById(R.id.btn_guardar_contacto);


    }
}