package com.uth.pm1e2grupo2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.uth.pm1e2grupo2.models.Contactos;

import java.util.ArrayList;

public class DbContactos extends DbHelper {

    Context context;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertContactosFromJsonString(String jsonString) {
        Gson gson = new Gson();
        Contactos[] contactosArray = gson.fromJson(jsonString, Contactos[].class);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (Contactos contacto : contactosArray) {
            ContentValues values = new ContentValues();
            values.put("codc", contacto.getCodc());
            values.put("pais", contacto.getPais());
            values.put("nombre", contacto.getNombre());
            values.put("telefono", contacto.getTelefono());
            values.put("nota", contacto.getNota());
            values.put("latitud", contacto.getLatitud());
            values.put("longitud", contacto.getLongitud());
            values.put("avatar", contacto.getAvatar());

            id = db.insert(TABLE_CONTACTOS, null, values);
        }
        db.close();
        return id;
    }


    public ArrayList<Contactos> mostrarContactos() {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto;
        Cursor cursorContactos;

        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " ORDER BY nombre ASC", null);

        if (cursorContactos.moveToFirst()) {
            do {
                contacto = new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setCodc(cursorContactos.getInt(1));
                contacto.setPais(cursorContactos.getInt(2));
                contacto.setNombre(cursorContactos.getString(3));
                contacto.setTelefono(cursorContactos.getString(4));
                contacto.setNota(cursorContactos.getString(5));
                contacto.setAvatar(cursorContactos.getString(6));
                contacto.setLatitud(cursorContactos.getString(7));
                contacto.setLongitud(cursorContactos.getString(8));
                listaContactos.add(contacto);
            } while (cursorContactos.moveToNext());
        }

        cursorContactos.close();

        return listaContactos;
    }

    public void deleteAllContactos() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONTACTOS);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_CONTACTOS + "'");

        //db.delete(TABLE_CONTACTOS, null, null);
    }
    public Contactos verContacto(int id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Contactos contacto = null;
        Cursor cursorContactos;

        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorContactos.moveToFirst()) {
            contacto = new Contactos();
            contacto.setId(cursorContactos.getInt(0));
            contacto.setCodc(cursorContactos.getInt(1));
            contacto.setPais(cursorContactos.getInt(2));
            contacto.setNombre(cursorContactos.getString(3));
            contacto.setTelefono(cursorContactos.getString(4));
            contacto.setNota(cursorContactos.getString(5));
            contacto.setAvatar(cursorContactos.getString(6));
            contacto.setLatitud(cursorContactos.getString(7));
            contacto.setLongitud(cursorContactos.getString(8));
        }

        cursorContactos.close();

        return contacto;
    }
}
