package com.example.pm2examencuentas1092.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import com.example.pm2examencuentas1092.model.Contacto;

public class SQLiteConexion extends SQLiteOpenHelper {
    private static final String DB_NAME    = "ContactosDB";
    private static final int    DB_VERSION = 1;

    public SQLiteConexion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Contacto (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "pais TEXT NOT NULL, " +
                        "nombre TEXT NOT NULL, " +
                        "telefono TEXT NOT NULL, " +
                        "nota TEXT, " +
                        "fotoUri TEXT" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contacto");
        onCreate(db);
    }

    public long insertContacto(Contacto c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pais",     c.getPais());
        cv.put("nombre",   c.getNombre());
        cv.put("telefono", c.getTelefono());
        cv.put("nota",     c.getNota());
        cv.put("fotoUri",  c.getFotoUri());
        long id = db.insert("Contacto", null, cv);
        db.close();
        return id;
    }

    // 1. Leer todos los registros
    public List<Contacto> getAllContactos() {
        List<Contacto> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Contacto", null);
        if (cursor.moveToFirst()) {
            do {
                Contacto c = new Contacto();
                c.setId(cursor.getInt(cursor.getColumnIndex("id")));
                c.setPais(cursor.getString(cursor.getColumnIndex("pais")));
                c.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                c.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
                c.setNota(cursor.getString(cursor.getColumnIndex("nota")));
                c.setFotoUri(cursor.getString(cursor.getColumnIndex("fotoUri")));
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 2. Eliminar un contacto por ID
    public int deleteContacto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("Contacto", "id = ?", new String[]{ String.valueOf(id) });
        db.close();
        return rows;
    }

}
