package com.uth.pm1e2grupo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uth.pm1e2grupo2.callbacks.CallbackNewContacto;
import com.uth.pm1e2grupo2.models.Contactos;
import com.uth.pm1e2grupo2.rests.ApiInterface;
import com.uth.pm1e2grupo2.rests.RestAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono, txtNota, lat, lon;
    Button btnGuarda;
    FloatingActionButton btnTakePhoto;
    public long idPais;
    ImageView avatar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    static final int PETICION_ACCESO_CAM = 100;
    TextView latitud, longitud, direccion;
    String strLatitud, strLongitud;
    ProgressDialog progressDialog;
    Contactos contactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Nuevo Contacto");
        }

        latitud = (TextView) findViewById(R.id.txtLatitud);
        longitud = (TextView) findViewById(R.id.txtLongitud);
        direccion = (TextView) findViewById(R.id.txtDireccion);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        Spinner spinnerPais = findViewById(R.id.ComboPais);
        String[] countries = getResources().getStringArray(R.array.country_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long selectedItemId = id;
                idPais = selectedItemId;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtNota = findViewById(R.id.txtNota);
        btnGuarda = findViewById(R.id.btnGuarda);
        btnGuarda.setText(R.string.btn_guarda);

        lat = findViewById(R.id.latitudTextView);
        lon = findViewById(R.id.longitudTextView);

        btnTakePhoto = findViewById(R.id.btn_change_image);
        avatar = (ImageView) findViewById(R.id.avatar);
        btnTakePhoto.setOnClickListener(view -> permisos());

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(idPais == 0 ){
                    ShowAlerta("Debe seleccionar un pais.");
                }
                else if(txtNombre.getText().toString().equals("") ){
                    ShowAlerta("Debe Registrar un Nombre.");
                }
                else if(txtTelefono.getText().toString().equals("") ){
                    ShowAlerta("Debe Registrar un numero de Telefono.");
                }
                else if(txtNota.getText().toString().equals("") ){
                    ShowAlerta("Debe Registrar una Nota.");
                }
                else{
                    strLatitud =  lat.getText().toString();
                    strLongitud = lon.getText().toString();

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NuevoActivity.this);
                    builder.setTitle("Registrar Contacto");
                    builder.setMessage("Desea registrar el contacto actual.?");
                    builder.setPositiveButton("Aceptar", (di, i) -> {
                        addContacto();
                    });
                    builder.setNegativeButton(R.string.dialog_cancel, null);
                    builder.show();

                }

            }
        });
    }

    public void addContacto() {

        progressDialog = new ProgressDialog(NuevoActivity.this);
        progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
        progressDialog.setMessage("Registrando Contacto...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        strLatitud =  lat.getText().toString();
        strLongitud = lon.getText().toString();

        String strNombre = txtNombre.getText().toString();
        String strTelefono = txtTelefono.getText().toString();
        String strNota = txtNota.getText().toString();
        String strVideo = "HolaMundoVideo";
        String strAvatar = ConvertImageBase64(currentPhotoPath);

        RequestBody avatar = RequestBody.create(MediaType.parse("multipart/form-data"), strAvatar);
        RequestBody pais = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idPais));
        RequestBody nombre = RequestBody.create(MediaType.parse("multipart/form-data"), strNombre);
        RequestBody telefono = RequestBody.create(MediaType.parse("multipart/form-data"), strTelefono);
        RequestBody nota = RequestBody.create(MediaType.parse("multipart/form-data"), strNota);
        RequestBody latitud = RequestBody.create(MediaType.parse("multipart/form-data"), strLatitud);
        RequestBody longitud = RequestBody.create(MediaType.parse("multipart/form-data"), strLongitud);
        RequestBody video = RequestBody.create(MediaType.parse("multipart/form-data"), strVideo);

        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<CallbackNewContacto> call = apiInterface.agregarContacto(avatar, pais, nombre, telefono, nota, latitud, longitud, video);
        call.enqueue(new Callback<CallbackNewContacto>() {
            @Override
            public void onResponse(Call<CallbackNewContacto> call, Response<CallbackNewContacto> response) {
                CallbackNewContacto resp = response.body();
                if (resp != null) {
                    contactos = resp.Contactos;
                    progressDialog.dismiss();

                    if (resp.status.equals("ok")){
                        Toast.makeText(NuevoActivity.this, "CONTACTO GUARDADO", Toast.LENGTH_LONG).show();
                        lista();
                    }else {
                        Toast.makeText(NuevoActivity.this, "ERROR AL GUARDAR CONTACTO", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackNewContacto> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("ErrorAddContacto", t.getMessage());
            }
        });

    }

    private String ConvertImageBase64(String path)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageArray, Base64.DEFAULT);
    }

    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED  &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PETICION_ACCESO_CAM);
        }else {
            patchTakePicture();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity2(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        latitud.setText("Localización agregada");
        direccion.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }

        if(requestCode == PETICION_ACCESO_CAM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                patchTakePicture();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso", Toast.LENGTH_LONG).show();
        }
    }

    public void setLocation2(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Clase Localizacion */
    public class Localizacion implements LocationListener {
        NuevoActivity mainActivity;
        public NuevoActivity getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity2(NuevoActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            String sLatitud = String.valueOf(loc.getLatitude());
            String sLongitud = String.valueOf(loc.getLongitude());
            latitud.setText("Latitud: " + sLatitud+ " - Longitud: " + sLongitud);
            lat.setText(sLatitud);
            lon.setText(sLongitud);

            strLatitud = sLatitud;
            strLongitud = sLongitud;
            setLocation2(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File foto = new File(currentPhotoPath);
            avatar.setImageURI(Uri.fromFile(foto));
            galleryAddImage();
        }
    }

    private  void galleryAddImage(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void patchTakePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.uth.pm1e2grupo2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void ShowAlerta(String mensaje) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Atencion");
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Aceptar", null);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void limpiar() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtNota.setText("");
    }
    private void lista(){
        limpiar();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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