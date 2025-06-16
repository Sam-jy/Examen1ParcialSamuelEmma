package com.example.pm2examencuentas1092.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2examencuentas1092.R;
import com.example.pm2examencuentas1092.config.SQLiteConexion;
import com.example.pm2examencuentas1092.model.Contacto;

import java.util.List;

public class ListaContactosActivity extends AppCompatActivity {
    private SQLiteConexion conexion;
    private List<Contacto> lista;
    private ContactoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        conexion = new SQLiteConexion(this);
        ListView lv = findViewById(R.id.lvContactos);

        cargarLista(lv);
    }

    private void cargarLista(ListView lv) {
        lista  = conexion.getAllContactos();
        adapter = new ContactoAdapter(this, lista);
        lv.setAdapter(adapter);
    }

    // Invocado desde el adapter al pulsar el btnOpciones
    public void showOpciones(Contacto c) {
        String[] items = {"Eliminar", "Compartir"};
        new AlertDialog.Builder(this)
                .setTitle(c.getNombre())
                .setItems(items, (dlg, which) -> {
                    if (which == 0) {
                        conexion.deleteContacto(c.getId());
                        cargarLista(findViewById(R.id.lvContactos));
                    } else {
                        compartir(c);
                    }
                })
                .show();
    }

    private void compartir(Contacto c) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        String texto = "Nombre: " + c.getNombre() +
                "\nTeléfono: " + c.getTelefono() +
                "\nPaís: " + c.getPais() +
                "\nNota: " + c.getNota();
        i.putExtra(Intent.EXTRA_TEXT, texto);
        if (c.getFotoUri() != null && !c.getFotoUri().isEmpty()) {
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(c.getFotoUri()));
            i.setType("image/*");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(Intent.createChooser(i, "Compartir contacto"));
    }
}

