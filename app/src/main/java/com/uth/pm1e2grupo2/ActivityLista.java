package com.uth.pm1e2grupo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.imageview.ShapeableImageView;

public class ActivityLista extends AppCompatActivity {


    SearchView buscar;
    ListView listacontactos;
    Button btnNuevoContacto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        buscar = (SearchView) findViewById(R.id.txtBuscar);
        listacontactos = (ListView) findViewById(R.id.listview);
        btnNuevoContacto = (Button) findViewById(R.id.btnNuevoContacto);

        btnNuevoContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactoNuevo();
            }
        });

    }

    private void ContactoNuevo() {

        Intent intent =  new Intent(this, ActivityCreate.class);
        startActivity(intent);
    }
}