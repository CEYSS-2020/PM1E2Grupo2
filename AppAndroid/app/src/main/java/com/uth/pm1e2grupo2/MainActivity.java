package com.uth.pm1e2grupo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uth.pm1e2grupo2.adaptadores.ListaContactosAdapter;
import com.uth.pm1e2grupo2.callbacks.CallbackContactos;
import com.uth.pm1e2grupo2.db.DbContactos;
import com.uth.pm1e2grupo2.models.Contactos;
import com.uth.pm1e2grupo2.rests.ApiInterface;
import com.uth.pm1e2grupo2.rests.RestAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscar;
    RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;
    FloatingActionButton fabNuevo, fabAbout;
    ListaContactosAdapter adapter;
    private DbContactos dataSource;
    private Call<CallbackContactos> callbackCall = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean gpsEnabled = isGPSEnabled();
        if (gpsEnabled) {
            // El GPS está activo
        } else {
            dataSource = new DbContactos(MainActivity.this);
            dataSource.deleteAllContactos();

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("¡Atención!");
            dialog.setMessage("Porfavor active el GPS para obtener la coordenada actual, para continuar usando la App.");
            dialog.setPositiveButton(R.string.dialog_ok, (di, i) -> {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            });
            dialog.setCancelable(false);
            dialog.show();
        }

        requestContactosApi();

        txtBuscar = findViewById(R.id.txtBuscar);
        listaContactos = findViewById(R.id.listaContactos);
        fabNuevo = findViewById(R.id.favNuevo);
        fabAbout = findViewById(R.id.favAbout);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));
        listaArrayContactos = new ArrayList<>();

        DbContactos dbContactos = new DbContactos(MainActivity.this);
        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);


        fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoRegistro();
            }
        });

        fabAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutDialog(MainActivity.this);
            }
        });

        txtBuscar.setOnQueryTextListener(this);
    }
    private void recargarDatos() {
        requestContactosApi();

        DbContactos dbContactos = new DbContactos(MainActivity.this);
        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Datos recargados", Toast.LENGTH_SHORT).show();
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recargarDatos();
    }


    private void requestContactosApi() {
        ApiInterface apiInterface = RestAdapter.createAPI();
        callbackCall = apiInterface.getContacts();
        callbackCall.enqueue(new Callback<CallbackContactos>() {
            @Override
            public void onResponse(@NonNull Call<CallbackContactos> call, @NonNull Response<CallbackContactos> response) {
                CallbackContactos resp = response.body();
                if (resp != null && resp.status.equals("ok")) {

                    dataSource = new DbContactos(MainActivity.this);
                    dataSource.deleteAllContactos();

                    DbContactos dbContactos = new DbContactos(MainActivity.this);
                    dbContactos.insertContactosFromJsonString(resp.listacontactos);

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Error");
                    builder1.setMessage(resp.message);
                    builder1.setPositiveButton(R.string.dialog_ok, null);
                    builder1.setCancelable(false);
                    builder1.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackContactos> call, @NonNull Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Error");
                builder1.setMessage(t.toString());
                builder1.setPositiveButton(R.string.dialog_ok, null);
                builder1.setCancelable(false);
                builder1.show();
            }

        });
    }


    private void nuevoRegistro(){
        Intent intent = new Intent(this, NuevoActivity.class);
        startActivity(intent);
    }

    public  static  void showAboutDialog(Activity activity) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.dialog_about, null);
        TextView txtAppVersion = view.findViewById(R.id.txt_app_version);
        txtAppVersion.setText(activity.getString(R.string.msg_about_version) + " " + 1 + " (" + 1.0 + ")");
        final MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(activity);
        alert.setView(view);
        alert.setPositiveButton(R.string.dialog_option_ok, (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }
}