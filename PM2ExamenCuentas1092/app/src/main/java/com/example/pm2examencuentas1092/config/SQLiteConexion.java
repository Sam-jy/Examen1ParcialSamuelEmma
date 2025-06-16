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

    public List<Contacto> getAllContactos() {
        List<Contacto> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Contacto", null);
        if (cursor.moveToFirst()) {
            do {
                Contacto c = new Contacto();
                
                int idIndex = cursor.getColumnIndex("id");
            if (idIndex >= 0) c.setId(cursor.getInt(idIndex));
                
                int paisIndex = cursor.getColumnIndex("pais");
                if (paisIndex >= 0) c.setPais(cursor.getString(paisIndex));
                
                int nombreIndex = cursor.getColumnIndex("nombre");
                if (nombreIndex >= 0) c.setNombre(cursor.getString(nombreIndex));
                
                int telefonoIndex = cursor.getColumnIndex("telefono");
                if (telefonoIndex >= 0) c.setTelefono(cursor.getString(telefonoIndex));
                
                int notaIndex = cursor.getColumnIndex("nota");
                if (notaIndex >= 0) c.setNota(cursor.getString(notaIndex));
                
                int fotoUriIndex = cursor.getColumnIndex("fotoUri");
                if (fotoUriIndex >= 0) c.setFotoUri(cursor.getString(fotoUriIndex));
                
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int deleteContacto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("Contacto", "id = ?", new String[]{ String.valueOf(id) });
        db.close();
        return rows;
    }

    public int updateContacto(Contacto c) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pais",     c.getPais());
        cv.put("nombre",   c.getNombre());
        cv.put("telefono", c.getTelefono());
        cv.put("nota",     c.getNota());
        cv.put("fotoUri",  c.getFotoUri());
        int rows = db.update("Contacto", cv, "id = ?", new String[]{ String.valueOf(c.getId()) });
        db.close();
        return rows;
    }

}
