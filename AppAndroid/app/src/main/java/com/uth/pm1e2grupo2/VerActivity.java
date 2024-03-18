package com.uth.pm1e2grupo2;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.uth.pm1e2grupo2.db.DbContactos;
import com.uth.pm1e2grupo2.models.Contactos;
import com.uth.pm1e2grupo2.models.Value;
import com.uth.pm1e2grupo2.rests.ApiInterface;
import com.uth.pm1e2grupo2.rests.RestAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerActivity extends AppCompatActivity {

    EditText txtPais, txtNombre, txtTelefono, txtNota;
    Button btnGuarda;
    Contactos contacto;
    int id = 0;
    public static String Codigo, Pais, VistaNombre, VistaFoto;
    private static final int REQUEST_CALL_PHONE = 1;
    String strLatitud, strLongitud, IDCodigo, IDCodigoc;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Detalle del Contacto");
        }

        View btnCompartir = findViewById(R.id.btnCompartir);
        View btnEliminar = findViewById(R.id.btnEliminar);
        View btnActualizar = findViewById(R.id.btnActualizar);
        View btnLlamar = findViewById(R.id.btnLlamar);
        View btnVerFoto = findViewById(R.id.btnVerFoto);

        View btnMapa = findViewById(R.id.btnMapa);
        btnVerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verFoto(VerActivity.this);
            }
        });


        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompartirContacto();
            }
        });

        txtPais = findViewById(R.id.txtPais);
        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtNota = findViewById(R.id.txtNota);
        btnGuarda = findViewById(R.id.btnGuarda);
        btnGuarda.setVisibility(View.INVISIBLE);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        Bundle extras2 = getIntent().getExtras();
        IDCodigo = extras2.getString("ID");

        final DbContactos dbContactos = new DbContactos(VerActivity.this);
        contacto = dbContactos.verContacto(Integer.parseInt(IDCodigo));

        if (contacto.getPais() == 1) {
            Pais = "Honduras";
            Codigo ="+504";
        }
        if (contacto.getPais() == 2) {
            Pais = "Guatelama";
            Codigo ="+502";
        }
        if (contacto.getPais() == 3) {
            Pais = "El Salvador";
            Codigo ="+503";
        }
        if (contacto.getPais() == 4) {
            Pais = "Nicaragua";
            Codigo ="+505";
        }
        if (contacto.getPais() == 5) {
            Pais = "Costa Rica";
            Codigo ="+506";
        }

        if(contacto != null){
            txtPais.setText(Pais);
            txtNombre.setText(contacto.getNombre());
            txtTelefono.setText(Codigo + " " + contacto.getTelefono());
            txtNota.setText(contacto.getNota());
            txtNombre.setInputType(InputType.TYPE_NULL);
            txtTelefono.setInputType(InputType.TYPE_NULL);
            txtNota.setInputType(InputType.TYPE_NULL);

            VistaNombre = contacto.getNombre();
            VistaFoto = contacto.getAvatar();

            IDCodigoc = String.valueOf(contacto.getCodc());

            strLatitud = contacto.getLatitud();
            strLongitud = contacto.getLongitud();
        }

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerActivity.this, EditarActivity.class);
                intent.putExtra("ID", IDCodigo);
                startActivity(intent);
            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerActivity.this);
                builder1.setTitle("Atencion");
                builder1.setMessage("¿Desea ir a la ubicacion de " + contacto.getNombre() + " ?");
                builder1.setPositiveButton("Ver Punto", (di, i) -> {
                    Intent intent = new Intent(VerActivity.this, MapActivity.class);
                    intent.putExtra("Latitud", strLatitud);
                    intent.putExtra("Longitud", strLongitud);
                    startActivity(intent);
                });
                builder1.setNegativeButton("Google Maps", (di, i) -> {
                    String destinoLat = strLatitud;
                    String destinoLng = strLongitud;

                    String uri = "http://maps.google.com/maps?saddr=Tu ubicación&daddr=" + destinoLat + "," + destinoLng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                });
                builder1.show();
            }
        });

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);
                builder.setTitle("Atencion");
                builder.setMessage("¿Desea Llamar a " + contacto.getNombre() + " ?");
                builder.setPositiveButton("Aceptar", (di, i) -> {
                    if (ContextCompat.checkSelfPermission(VerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(VerActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+Codigo + " " + contacto.getTelefono()));
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);                builder.setTitle("Atencion");
                builder.setMessage("¿Desea eliminar el contacto " + contacto.getNombre() + " ?");
                builder.setPositiveButton("Aceptar", (di, i) -> {
                    deleteContacto(Integer.parseInt(IDCodigoc));
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();

            }
        });
    }

    public void deleteContacto(int idCodigo) {

        progressDialog = new ProgressDialog(VerActivity.this);
        progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
        progressDialog.setMessage("Eliminando Contacto...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<Value> call = apiInterface.eliminarContacto(idCodigo);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                Value resp = response.body();
                if (resp != null) {
                    progressDialog.dismiss();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(VerActivity.this);
                    builder1.setTitle("Contactos");
                    builder1.setMessage(resp.message);
                    builder1.setPositiveButton(R.string.dialog_ok, (di, i) -> {
                        lista();
                    });
                    builder1.setCancelable(false);
                    builder1.show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Value> call, @NonNull Throwable t) {
                t.printStackTrace();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerActivity.this);
                builder1.setTitle("Contactos");
                builder1.setMessage(t.getMessage());
                builder1.setPositiveButton(R.string.dialog_ok, null);
                builder1.setCancelable(false);
                builder1.show();
            }
        });

    }

    public  static  void verFoto(Activity activity) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.dialog_foto, null);
        TextView txtFotoUser = view.findViewById(R.id.txtNombreUser);
        txtFotoUser.setText(VistaNombre);

        ImageView ImageFotoUser = view.findViewById(R.id.avatarUser);
        if (VistaFoto.equals("")) {
            ImageFotoUser.setImageResource(R.drawable.ic_user_account);
        } else {
            Glide.with(activity)
                    .load(VistaFoto)
                    .placeholder(R.drawable.ic_user_account)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(new RequestOptions().override(256, 256))
                    .centerCrop()
                    .into(ImageFotoUser);
        }

        final MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(activity);
        alert.setView(view);
        alert.setPositiveButton(R.string.dialog_option_ok, (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private void CompartirContacto() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_content) + "\n\n" + "Nombre: " + contacto.getNombre() + "\n" + "Telefono: " + Codigo + contacto.getTelefono());
        sendIntent.setType("text/plain");

        String title = "Compartir Contacto";
        Intent chooser = Intent.createChooser(sendIntent, title);

        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            Toast.makeText(this, "No se encontró una aplicación para compartir", Toast.LENGTH_SHORT).show();
        }
    }

    private void lista(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}